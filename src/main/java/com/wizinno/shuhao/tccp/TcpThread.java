package com.wizinno.shuhao.tccp;

import com.wizinno.shuhao.tccp.model.*;
import com.wizinno.shuhao.tccp.util.Crc16;
import com.wizinno.shuhao.tccp.util.DoMysql;
import com.wizinno.shuhao.tccp.util.OnLine;
import com.wizinno.shuhao.tccp.util.UpdateDevice;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TcpThread extends Thread  {
    private final String verify="6600";//
    private Socket socket;
   public static List<HeartPackage>packages=new CopyOnWriteArrayList<HeartPackage>();
   public static List<HeartPackage> getPackages(){
       return packages;
   }

    private static List<DeviceSocket> deviceSockets=new CopyOnWriteArrayList<DeviceSocket>();

    public static List<DeviceSocket> getDeviceSockets(){
        return deviceSockets;
    }
    public TcpThread(Socket socket) {
        this.socket = socket;

    }

    public void run(){
            InputStream is2=null;
            OutputStream os2=null;
            PrintWriter pw2=null;
            String devId=null;
            try {
                //获取输入流，并读取客户端信息
                while(true){
                    is2=socket.getInputStream();
                    byte[] bufs2 = new byte[1024];
                    int lens2 = is2.read(bufs2);
                    String text2=new String(bufs2,0,lens2,"GBK");
                    System.out.println("数据包的长度："+lens2);
                    //获取输出流，响应客户端的请求
                    os2 = socket.getOutputStream();
                    pw2 = new PrintWriter(os2);


                    if(text2==null){
                        pw2.write("error");
                    }else{
                        String[]str3=text2.split("\r\n");
                        for(int i=0;i<str3.length;i++){
                            String[]str2=new String[1024];
                            String packageType = null;
                            if(str3[i].indexOf("OK")==-1){
                                str2=str3[i].split(",");
                                 packageType=str2[1];
                            }
                            if(packageType!=null){
                                //判断是什么类型的包
                                if(packageType.equals("3")){  //模块数据包
                                    System.out.println("***********************************************");
                                    System.out.print("模块数据包："+str3[i]);
                                    String id=str2[0];
                                    devId=id;
                                    String data=str2[3];
                                    DataDto dataDto=new DataDto();
                                    dataDto.setDeviceId(Long.parseLong(id));
                                    dataDto.setReceiveTime(new Timestamp(new Date().getTime()));
                                    dataDto.setData(str3[i]);
                                    DoMysql.saveData(dataDto);
                                }else if(packageType.equals("2")){  //模块列表包
                                    String id=str2[0];
                                    devId=id;
                                    int number=Integer.parseInt(str2[3]);//模块序号
                                    String type=str2[1];
                                    String crc=str2[5];
                                    if(Crc16.getCrc(crc).equals(verify)){
                                        String response=id+","+type+","+"1,222\r\n";
                                        pw2.write(response);
                                        //将模块io 配置存储到设备表，并更新配置时间
                                        DeviceDto deviceDto= DoMysql.queryDevice(Long.parseLong(id));
                                        if(deviceDto.getDeviceId()!=null){
                                            if(deviceDto.getIoCfg()==null){
                                                deviceDto.setIoCfg(str3[i]);
                                                deviceDto.setIoCfgTime(new Date());
                                                DoMysql.updateDevice(deviceDto);
                                            }else{
                                                if(number!=0){
                                                    deviceDto.setIoCfg(deviceDto.getIoCfg()+";"+str3[i]);
                                                    deviceDto.setIoCfgTime(new Date());
                                                    DoMysql.updateDevice(deviceDto);

                                                }else{
                                                    deviceDto.setIoCfg(str3[i]);
                                                    deviceDto.setIoCfgTime(new Date());
                                                    DoMysql.updateDevice(deviceDto);
                                                }
                                            }


                                        }
                                    }else {
                                        String response=id+","+type+","+"0,222\r\n";
                                        pw2.write(response);
                                    }
                                }else if(packageType.equals("1")){  //心跳包
                                    //更新数据库
                                    System.out.println("心跳包"+str3[i]);
                                    System.out.println("心跳包的ip地址："+socket.getInetAddress().getHostAddress());
                                    System.out.println("心跳包的端口："+socket.getPort());
                                    String[] str=str3[i].split(",");
                                    String id=str[0];
                                    devId=id;
                                    String type=str[1];
                                    String deviceVer=str[2];
                                    int samplingPeriod=Integer.parseInt(str[3]);
                                    int sensorCount=Integer.parseInt(str[4]);
                                    int driverCount=Integer.parseInt(str[5]);
                                    String crc=str[6];
                                    //crc16校验
                                    String a=Crc16.getCrc(crc);
                                    if(a.equals(verify)){
                                        DeviceDto device=new DeviceDto();
                                        device.setDeviceId(Long.parseLong(id));
                                        device.setDeviceVer(deviceVer);
                                        device.setSensorCount(sensorCount);
                                        device.setDriverCount(driverCount);
                                        device.setSamplingPeriod(samplingPeriod);
                                        device.setIsOnline(1);
                                        device.setOnlineTtime(new Timestamp(new Date().getTime()));
                                        //将设备端的socket 信息保存进集合
                                        List<String> ids=new CopyOnWriteArrayList<String>();
                                        if(deviceSockets.size()==0){
                                            DeviceSocket deviceSocket=new DeviceSocket();
                                            Long did=Long.parseLong(id);
                                            deviceSocket.setDeviceId(did.toString());
                                            deviceSocket.setSocket(socket);
                                            deviceSockets.add(deviceSocket);
                                        }else{
                                            for(DeviceSocket deviceSocket:deviceSockets){
                                                ids.add(deviceSocket.getDeviceId());
                                            }
                                            Long did=Long.parseLong(id);
                                            if(!ids.contains(did.toString())){
                                                DeviceSocket deviceSocket=new DeviceSocket();
                                                deviceSocket.setDeviceId(did.toString());
                                                deviceSocket.setSocket(socket);
                                                deviceSockets.add(deviceSocket);
                                            }
                                        }

                                        //保存设备表
                                        //1.先查询该设备是否存在
                                        DeviceDto deviceDto= DoMysql.queryDevice(Long.parseLong(id));
                                        if(deviceDto.getDeviceId()==null){
                                            //保存设备表
                                            DoMysql.saveDevice(device);
                                        }else{
                                            //更新设备表
                                            device.setIoCfg(deviceDto.getIoCfg());
                                            device.setIoCfgTime(deviceDto.getIoCfgTime());
                                            device.setSinarioCfgTime(deviceDto.getSinarioCfgTime());
                                            device.setSinarioCfg(deviceDto.getSinarioCfg());
                                            device.setUserPhone(deviceDto.getUserPhone());
                                            device.setUserId(device.getUserId());
                                            DoMysql.updateDevice(device);
                                        }
                                        List<String>deviceIds=new ArrayList<String>();
                                        if(packages.size()>0){
                                            for(HeartPackage heartpackage:packages){
                                                deviceIds.add(heartpackage.getDeviceId());
                                            }
                                        }
                                        if(packages.size()==0){
                                            HeartPackage heartPackage=new HeartPackage();
                                            heartPackage.setDeviceId(id);
                                            heartPackage.setType(packageType);
                                            heartPackage.setDevice_ver(deviceVer);
                                            heartPackage.setSampling_period(samplingPeriod);
                                            heartPackage.setDriver_count(driverCount);
                                            heartPackage.setSensor_count(sensorCount);
                                            heartPackage.setAcceptTime(new Date());
                                            packages.add(heartPackage);
                                            String response=id+","+type+","+"1,222\r\n";
                                            pw2.write(response);
                                        }else {
                                            if(!deviceIds.contains(id)){
                                                HeartPackage heartPackage=new HeartPackage();
                                                heartPackage.setDeviceId(id);
                                                heartPackage.setType(packageType);
                                                heartPackage.setDevice_ver(deviceVer);
                                                heartPackage.setSampling_period(samplingPeriod);
                                                heartPackage.setDriver_count(driverCount);
                                                heartPackage.setSensor_count(sensorCount);
                                                heartPackage.setAcceptTime(new Date());
                                                packages.add(heartPackage);
                                                String response=id+","+type+","+"1,222\r\n";
                                                pw2.write(response);
                                            }else{
                                                //校验发过来的心跳包是否超过规定周期60s
                                                for(HeartPackage heartpackage:packages){
                                                    if(id.equals(heartpackage.getDeviceId())){
                                                        DeviceDto deviceDto2= DoMysql.queryDevice(Long.parseLong(id));
                                                        //如果当前时间-上一次接受时间>发送周期
                                                        int cycleTime= heartpackage.getSampling_period();
                                                        long time= (new Date().getTime()-heartpackage.getAcceptTime().getTime())/1000;
                                                        System.out.println("发送周期"+cycleTime);
                                                        System.out.println("间隔时间"+time);
                                                        if(time>(long)cycleTime){
                                                            //设备id 掉线
                                                            packages.remove(heartpackage);
                                                            deviceDto2.setIsOnline(0);
                                                            deviceDto2.setOnlineTtime(new Date());
                                                            DoMysql.updateDevice(deviceDto2);
                                                            String response=id+","+type+","+"0,222\r\n";
                                                            pw2.write(response);
                                                            OnlineDto onlineD= DoMysql.queryOnlineLong(Long.parseLong(devId));
                                                            if(onlineD.getId()==null){
                                                                //保存onlineLog表
                                                                OnlineDto onlineDto=new OnlineDto();
                                                                onlineDto.setDeviceId(Long.parseLong(devId));
                                                                onlineDto.setOffLineTime(new Date());
                                                                DoMysql.saveOnlineLog(onlineDto);
                                                            }


                                                        }else{
                                                            //查看发过来的心跳包是否有更新
                                                            if(!heartpackage.getSampling_period().equals(samplingPeriod)){
                                                                heartpackage.setSampling_period(samplingPeriod);
                                                            }if(!heartpackage.getDriver_count().equals(driverCount)){
                                                                heartpackage.setDriver_count(driverCount);
                                                            }if(!heartpackage.getSensor_count().equals(sensorCount)){
                                                                heartpackage.setSensor_count(sensorCount);
                                                            }if(!heartpackage.getDevice_ver().equals(deviceVer)){
                                                                heartpackage.setDevice_ver(deviceVer);
                                                            }
                                                            //更新改设备id的心跳包接受时间
                                                            deviceDto2.setIsOnline(1);
                                                            deviceDto2.setOnlineTtime(new Date());
                                                            DoMysql.updateDevice(deviceDto2);
                                                            heartpackage.setAcceptTime(new Date());
                                                            System.out.println("心跳包的个数"+packages.size());
                                                            String response=id+","+type+","+"1,222\r\n";
                                                            pw2.write(response);
                                                            //先查询到onlineLog表中的该设备
                                                          OnlineDto onlineDto= DoMysql.queryOnlineLong(Long.parseLong(devId));
                                                            //更新onlineLog表
                                                            if(onlineDto.getId()!=null){
                                                                onlineDto.setOnLineTime(new Date());
                                                                DoMysql.updateOnline(onlineDto);
                                                            }

                                                        }
                                                    }
                                                }
                                            }
                                        }

                                    }else{
                                        String response=id+","+type+","+"0,222\r\n";
                                        pw2.write(response);
                                    }

                                }
                            }
                            else if(text2.indexOf("OK")!=-1||text2.indexOf("ERR")!=-1){ //采集器回应app server的包
                                System.out.println("采集器回应内容："+text2);
                                List<SendCmd>sendCmds=AtcmdThread.getSendCmds();
                                    //返回的有数据
                                    List<SendCmd>sendCmds1= AtcmdThread.getSendCmds();
                                    for(SendCmd sendCmd:sendCmds1){
                                        for(DeviceSocket deviceSocket:deviceSockets){
                                            if(deviceSocket.getDeviceId().equals(sendCmd.getDeviceId())){
                                                //向该用户返回该设备的数据包
                                                System.out.println("AT客户端的socket信息"+sendCmd.getSocket().getInetAddress());
                                                OutputStream os = sendCmd.getSocket().getOutputStream();
                                                PrintWriter pw = new PrintWriter(os);
                                                pw.write(text2);
                                                pw.flush();
                                                sendCmd.getSocket().close();
                                                //删除客户端中的sokcet
                                                sendCmds1.remove(sendCmd);
                                            }
                                        }

                                    }

                            }
                        }
                        }


                    pw2.flush();//调用flush()方法将缓冲输出

                }


            } catch (IOException e) {
                List<HeartPackage>packages=getPackages();
                if(packages.size()>0){
                    for(HeartPackage heartPackage:packages){
                        if(devId.equals(heartPackage.getDeviceId())){
                        packages.remove(heartPackage);
                        break;
                        }
                    }
                }

                List<DeviceSocket> sockets= getDeviceSockets();
                OnLine.changeOnline(sockets,socket);
                //保存onlineLog表
                OnlineDto onlineDto=new OnlineDto();
                onlineDto.setDeviceId(Long.parseLong(devId));
                onlineDto.setOffLineTime(new Date());
                DoMysql.saveOnlineLog(onlineDto);
            } catch (Exception e) {
                List<HeartPackage>packages=getPackages();
                if(packages.size()>0){
                    for(HeartPackage heartPackage:packages){
                        if(devId.equals(heartPackage.getDeviceId())){
                            packages.remove(heartPackage);
                            break;
                        }
                    }
                }
                List<DeviceSocket> sockets= getDeviceSockets();
                OnLine.changeOnline(sockets,socket);
                //保存onlineLog表
                OnlineDto onlineDto=new OnlineDto();
                onlineDto.setDeviceId(Long.parseLong(devId));
                onlineDto.setOffLineTime(new Date());
                DoMysql.saveOnlineLog(onlineDto);

            } finally{
                //关闭资源
                try {
//                System.out.print("关闭");
                    if(pw2!=null)
                        pw2.close();
                    if(os2!=null)
                        os2.close();
                    if(is2!=null)
                        is2.close();
                    if(socket!=null)
                        socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


    }


}
