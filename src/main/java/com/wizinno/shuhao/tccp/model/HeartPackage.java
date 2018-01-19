package com.wizinno.shuhao.tccp.model;

import java.util.Date;

public class HeartPackage {
    private String deviceId;
    private String type;
    private String device_ver;
    private Integer sampling_period;
    private Integer sensor_count;
    private Integer driver_count;
    private Date acceptTime;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDevice_ver() {
        return device_ver;
    }

    public void setDevice_ver(String device_ver) {
        this.device_ver = device_ver;
    }

    public Integer getSampling_period() {
        return sampling_period;
    }

    public void setSampling_period(Integer sampling_period) {
        this.sampling_period = sampling_period;
    }

    public Integer getSensor_count() {
        return sensor_count;
    }

    public void setSensor_count(Integer sensor_count) {
        this.sensor_count = sensor_count;
    }

    public Integer getDriver_count() {
        return driver_count;
    }

    public void setDriver_count(Integer driver_count) {
        this.driver_count = driver_count;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Date getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(Date acceptTime) {
        this.acceptTime = acceptTime;
    }
}
