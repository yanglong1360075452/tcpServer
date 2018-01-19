package com.wizinno.shuhao.tccp;

public class Enter {
    public static void main(String[] args) {
        Atcmd atcmd=new Atcmd();
        Tcp tcp=new Tcp();
        Check check=new Check();
        UpdateDevice updateDevice=new UpdateDevice();
        tcp.start();
        atcmd.start();
        check.start();
        updateDevice.start();

    }
}
