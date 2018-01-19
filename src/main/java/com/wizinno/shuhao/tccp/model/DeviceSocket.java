package com.wizinno.shuhao.tccp.model;

import java.net.Socket;

public class DeviceSocket {
    private String deviceId;

    private Socket  socket;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
