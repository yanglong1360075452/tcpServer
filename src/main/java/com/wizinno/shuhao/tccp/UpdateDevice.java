package com.wizinno.shuhao.tccp;

import com.wizinno.shuhao.tccp.model.DeviceDto;
import com.wizinno.shuhao.tccp.util.DoMysql;

import java.util.Date;
import java.util.List;

public class UpdateDevice extends Thread {
    @Override
    public void run(){
        List<DeviceDto> deviceDtos= DoMysql.queryAllDevice();
                for(DeviceDto deviceDto:deviceDtos){
                    deviceDto.setIsOnline(0);
                    deviceDto.setOnlineTtime(new Date());
                    DoMysql.updateDevice(deviceDto);

                }
    }
}
