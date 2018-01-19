package com.wizinno.shuhao.tccp;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class Test {
    public static void main(String[] args) throws UnsupportedEncodingException {
        int a=1005;
        String b="023";
        String c=String.valueOf(a)+b;//一般的字符串
        String d= str2HexStr(c);//16进制的字符串
        //逆向转换
        String e= hex2bin(d);//普通的字符串
        e=new String(e.getBytes("GBK"),"GBK");
        byte[]bytes=e.getBytes();
        byte[]aa={bytes[0],bytes[1]};
        byte[]bb={bytes[2],bytes[3]};
        String aaa=aa.toString();
        String bbb=bb.toString();



    }
    public static String str2HexStr(String str)
    {

        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;

        for (int i = 0; i < bs.length; i++)
        {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            sb.append(' ');
        }
        return sb.toString().trim();
    }
    public static String hex2bin(String hex) {
        String digital = "0123456789ABCDEF";
        char[] hex2char = hex.toCharArray();
        byte[] bytes = new byte[hex.length() / 2];
        int temp;
        for (int i = 0; i < bytes.length; i++) {
            temp = digital.indexOf(hex2char[2 * i]) * 16;
            temp += digital.indexOf(hex2char[2 * i + 1]);
            bytes[i] = (byte) (temp & 0xff);
        }
        return new String(bytes);
    }
}






