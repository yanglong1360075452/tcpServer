package com.wizinno.shuhao.tccp.util;

import com.wizinno.shuhao.tccp.model.DeviceDto;
import com.wizinno.shuhao.tccp.model.DeviceSocket;

import java.net.Socket;
import java.util.Date;
import java.util.List;

public class OnLine {
    public static void changeOnline(List<DeviceSocket> sockets, Socket socket){
        for(DeviceSocket deviceSocket:sockets){
            if(socket==deviceSocket.getSocket()){
                DeviceDto deviceDto= DoMysql.queryDevice(Long.parseLong(deviceSocket.getDeviceId()));
                deviceDto.setIsOnline(0);
                deviceDto.setOnlineTtime(new Date());
                DoMysql.updateDevice(deviceDto);
                sockets.remove(deviceSocket);
            }
        }
    }
    public static void changeOnline2(List<DeviceSocket> sockets,String deviceId){
        for(DeviceSocket deviceSocket:sockets){
            if(deviceId.equals(deviceSocket.getDeviceId())){
                DeviceDto deviceDto= DoMysql.queryDevice(Long.parseLong(deviceId));
                deviceDto.setIsOnline(0);
                deviceDto.setOnlineTtime(new Date());
                DoMysql.updateDevice(deviceDto);
                sockets.remove(deviceSocket);
            }
        }
    }
}
