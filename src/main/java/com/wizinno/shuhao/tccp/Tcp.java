package com.wizinno.shuhao.tccp;

import com.wizinno.shuhao.tccp.model.DeviceSocket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public  class Tcp  extends Thread{


    @Override
    public void run(){
        try {
            //1.创建一个服务器端Socket，即ServerSocket，指定绑定的端口，并监听此端口
            ServerSocket serverSocket=new ServerSocket(34567);
            Socket socket=null;
            //记录客户端的数量
            int count=0;
            System.out.println("***tcp服务器即将启动，等待客户端的连接***");
            //循环监听等待客户端的连接
            while(true){
                //调用accept()方法开始监听，等待客户端的连接
                socket=serverSocket.accept();
                //创建一个新的线程
                TcpThread tcpThread=new TcpThread(socket);
                //启动线程
                tcpThread.start();
                count++;//统计客户端的数量
                System.out.println("tcp客户端的数量："+count);
                InetAddress address=socket.getInetAddress();
                System.out.println("tcp当前客户端的IP："+address.getHostAddress());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
