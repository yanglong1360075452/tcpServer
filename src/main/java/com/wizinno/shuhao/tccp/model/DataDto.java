package com.wizinno.shuhao.tccp.model;

import java.sql.Timestamp;
import java.util.Date;

public class DataDto {
    private Long id;
    private Long deviceId;
    private String data;
    private Timestamp receiveTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getData() {
        return data;
    }


    public void setData(String data) {
        this.data = data;
    }

    public Timestamp getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Timestamp receiveTime) {
        this.receiveTime = receiveTime;
    }
}
