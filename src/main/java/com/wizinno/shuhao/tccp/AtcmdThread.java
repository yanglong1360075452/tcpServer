package com.wizinno.shuhao.tccp;

import com.wizinno.shuhao.tccp.model.DeviceSocket;
import com.wizinno.shuhao.tccp.model.HeartPackage;
import com.wizinno.shuhao.tccp.model.SendCmd;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AtcmdThread extends Thread{
    private Socket socket;
    public static List<SendCmd> sendCmds=new CopyOnWriteArrayList<SendCmd>();
    public static  List<SendCmd> getSendCmds(){
        return  sendCmds;
    }
    public AtcmdThread(Socket socket) {
        this.socket = socket;
    }

    public void run(){
        InputStream is=null;
        OutputStream os=null;
        PrintWriter pw=null;

        OutputStream os2=null;
        PrintWriter pw2=null;
        try {
            //获取输入流，并读取客户端信息
            is = socket.getInputStream();
//          测试方式；
            while (true){
                byte[] bufs = new byte[1024];
                int lens = is.read(bufs);
                String text=new String(bufs,0,lens,"GBK");
                String []str=text.split("#");
                String deviceId=str[0];
                String cmd=str[1]+"\r\n";
                if(text.indexOf("AT")!=-1){ //AT命令
                    List<DeviceSocket> sockets= TcpThread.getDeviceSockets();

                    //AT+sendInterval
                    String[]str1= cmd.split("=");
                    String left=str1[0];
                    String right=str1[1];
                    System.out.println("left"+left);
                    System.out.println("right"+right);
                    //将用户发送的命令保存在sendCmds集合中
                    SendCmd sendCmd=new SendCmd();
                    sendCmd.setDeviceId(deviceId);
                    sendCmd.setSocket(socket);
                    sendCmds.add(sendCmd);
                    for(DeviceSocket socket:sockets){
                        if(socket.getDeviceId().equals(deviceId)){
                            System.out.println("tcp的socket的端口"+socket.getSocket().getLocalPort());
                            os2 = socket.getSocket().getOutputStream();
                            pw2 = new PrintWriter(os2);
                            pw2.write(cmd);
                            pw2.flush();
                        }
                    }

                }


            }


        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("app客户端关闭");
//            e.printStackTrace();
        } finally{
            //关闭资源
            try {
//                System.out.print("关闭");
                if(pw!=null)
                    pw.close();
                if(os!=null)
                    os.close();
                if(is!=null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
