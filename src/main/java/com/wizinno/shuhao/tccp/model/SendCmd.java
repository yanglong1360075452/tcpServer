package com.wizinno.shuhao.tccp.model;

import java.net.Socket;

public class SendCmd {
    private String deviceId;//发送的命令
    private Socket socket;//发送者的socket

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
