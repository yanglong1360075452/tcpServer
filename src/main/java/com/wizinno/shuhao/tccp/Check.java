package com.wizinno.shuhao.tccp;

import com.wizinno.shuhao.tccp.model.DeviceDto;
import com.wizinno.shuhao.tccp.model.DeviceSocket;
import com.wizinno.shuhao.tccp.model.HeartPackage;
import com.wizinno.shuhao.tccp.model.OnlineDto;
import com.wizinno.shuhao.tccp.util.DoMysql;
import com.wizinno.shuhao.tccp.util.OnLine;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Check extends Thread {
    @Override
    public void run(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                System.out.println("-------设定要指定任务--------");
                List<HeartPackage> packages=TcpThread.getPackages();
                if(packages.size()>0){
                    for(HeartPackage heartPackage:packages){
                        DeviceDto deviceDto= DoMysql.queryDevice(Long.parseLong(heartPackage.getDeviceId()));
                        Integer cyclTime= deviceDto.getSamplingPeriod();
                        Long time=(new Date().getTime()-heartPackage.getAcceptTime().getTime())/1000;
                        if(time>(long)cyclTime){
                            List<DeviceSocket> sockets= TcpThread.getDeviceSockets();
                            String deviceId=deviceDto.getDeviceId().toString();
                            OnLine.changeOnline2(sockets,deviceId);
                            //先查询该设备在日志表中是否已经存在
                           OnlineDto onlineD= DoMysql.queryOnlineLong(Long.parseLong(deviceId));
                           if(onlineD.getId()==null){
                               //保存onlineLog表
                               OnlineDto onlineDto=new OnlineDto();
                               onlineDto.setDeviceId(Long.parseLong(deviceId));
                               onlineDto.setOffLineTime(new Date());
                               DoMysql.saveOnlineLog(onlineDto);
                           }

                        }
                    }
                }

            }
        }, 10000,10000);// 设定指定的时间time,此处为2000毫秒
    }



    }

