package com.wizinno.shuhao.tccp.model;

import java.util.Date;

public class OnlineDto {
    private Long id;
    private Long deviceId;
    private Date offLineTime;
    private Date onLineTime;

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

    public Date getOffLineTime() {
        return offLineTime;
    }

    public void setOffLineTime(Date offLineTime) {
        this.offLineTime = offLineTime;
    }

    public Date getOnLineTime() {
        return onLineTime;
    }

    public void setOnLineTime(Date onLineTime) {
        this.onLineTime = onLineTime;
    }
}
