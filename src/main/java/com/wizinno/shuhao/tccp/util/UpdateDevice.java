package com.wizinno.shuhao.tccp.util;

import com.wizinno.shuhao.tccp.TcpThread;
import com.wizinno.shuhao.tccp.model.DeviceDto;
import com.wizinno.shuhao.tccp.model.HeartPackage;

import java.util.Date;
import java.util.List;

public class UpdateDevice {
    public static void updateOnLine(){
        //查询设备表
        List<DeviceDto> deviceDtos= DoMysql.queryAllDevice();
        List<HeartPackage> packages= TcpThread.getPackages();
        if(packages.size()>0){
            for(HeartPackage heartPackage:packages){
                for(DeviceDto deviceDto:deviceDtos){
                    if(Long.parseLong(heartPackage.getDeviceId())==(deviceDto.getDeviceId())){
                        //查询设备id的心跳包周期
                        DeviceDto dto=  DoMysql.queryDevice(deviceDto.getDeviceId());
                        int cycleTime=dto.getSamplingPeriod();
                        //判断该这设备是否在线
                        Long time=(new Date().getTime()-heartPackage.getAcceptTime().getTime())/1000;
                        System.out.println("###############################");
                        System.out.println("发送间隔时间"+time);
                        System.out.println("发送周期"+cycleTime);
                        if(time>(long)cycleTime){
                            //设备离线，更新数据库
                            deviceDto.setIsOnline(0);
                            deviceDto.setOnlineTtime(new Date());
                            DoMysql.updateDevice(deviceDto);
                        }else{
                            deviceDto.setIsOnline(1);
                            deviceDto.setOnlineTtime(new Date());
                            DoMysql.updateDevice(deviceDto);
                        }
                    }
                }
            }
        }
    }

}
