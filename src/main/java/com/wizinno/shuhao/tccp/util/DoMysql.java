package com.wizinno.shuhao.tccp.util;
import com.wizinno.shuhao.tccp.model.DataDto;
import com.wizinno.shuhao.tccp.model.DeviceDto;
import com.wizinno.shuhao.tccp.model.OnlineDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DoMysql {
    public static Connection conn;
    public static PreparedStatement ps;
    public static ResultSet rs;

    public static Connection getConnection() {
        String url = "jdbc:mysql://localhost:3306/shuhao";
        String userName = "user_elvis";
        String password = "02160497730";
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("找不到驱动！");
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection(url, userName, password);
            if (conn != null) {
                System.out.println("connection successful");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            System.out.println("connection fail");
            e.printStackTrace();
        }
        return conn;
    }

    public static void saveData(DataDto dataDto) throws Exception {
        // 连接数据库，完成数据的录入操作
        String sql = "insert into data (device_id,receive_time,data) values (?,?,?)";
        try {
            PreparedStatement preStmt = getConnection().prepareStatement(sql);
            preStmt.setLong(1, dataDto.getDeviceId());
            preStmt.setTimestamp(2, new Timestamp(dataDto.getReceiveTime().getTime()));
            preStmt.setString(3, dataDto.getData());
            preStmt.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void saveDevice(DeviceDto deviceDto) throws Exception {
        // 连接数据库，完成数据的录入操作
        String sql = "insert into device (device_id,device_ver,sensor_count,driver_count,sampling_period" +
                ",is_online,online_time) values (?,?,?,?,?,?,?)";
        try {
            PreparedStatement preStmt = getConnection().prepareStatement(sql);
            preStmt.setLong(1, deviceDto.getDeviceId());
            preStmt.setString(2, deviceDto.getDeviceVer());
            preStmt.setInt(3, deviceDto.getSensorCount());
            preStmt.setInt(4, deviceDto.getDriverCount());
            preStmt.setInt(5, deviceDto.getSamplingPeriod());
            preStmt.setInt(6, deviceDto.getIsOnline());
            preStmt.setTimestamp(7, new Timestamp(deviceDto.getOnlineTtime().getTime()));
            preStmt.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void updateDevice(DeviceDto deviceDto) {
        PreparedStatement psql = null;
        ResultSet res;
        Connection con = getConnection();
        try {
            if(deviceDto.getUserId()!=null){
                psql = con.prepareStatement("update device set device_id=?,device_ver=?,sensor_count=?,driver_count=?," +
                        "io_cfg=?,sampling_period=?,io_cfg_time=?,is_online=?,online_time=?,sinario_cfg=?,sinario_cfg_time=?," +
                        "user_id=?,user_phone=? where device_id = ?");
                psql.setLong(1, deviceDto.getDeviceId());
                psql.setString(2, deviceDto.getDeviceVer());
                psql.setInt(3, deviceDto.getSensorCount());
                psql.setInt(4, deviceDto.getDriverCount());
                psql.setString(5,deviceDto.getIoCfg());
                psql.setInt(6, deviceDto.getSamplingPeriod());
                if(deviceDto.getIoCfgTime()==null){
                    psql.setTimestamp(7,null);
                }else{
                    psql.setTimestamp(7,new Timestamp(deviceDto.getIoCfgTime().getTime()));
                }

                psql.setInt(8, deviceDto.getIsOnline());
                if(deviceDto.getOnlineTtime()==null){
                    psql.setTimestamp(9,null);
                }else{
                    psql.setTimestamp(9,new Timestamp(deviceDto.getOnlineTtime().getTime()));
                }

                psql.setString(10,deviceDto.getSinarioCfg());
                if(deviceDto.getSinarioCfgTime()==null){
                    psql.setTimestamp(11,null);
                }else{
                    psql.setTimestamp(11,new Timestamp(deviceDto.getSinarioCfgTime().getTime()));
                }
                psql.setLong(12,deviceDto.getUserId());
                psql.setString(13,deviceDto.getUserPhone());
                psql.setLong(14,deviceDto.getDeviceId());
                psql.executeUpdate();



            }else{
                psql = con.prepareStatement("update device set device_id=?,device_ver=?,sensor_count=?,driver_count=?," +
                        "io_cfg=?,sampling_period=?,io_cfg_time=?,is_online=?,online_time=?,sinario_cfg=?,sinario_cfg_time=?," +
                        "user_phone=? where device_id = ?");
                psql.setLong(1, deviceDto.getDeviceId());
                psql.setString(2, deviceDto.getDeviceVer());
                psql.setInt(3, deviceDto.getSensorCount());
                psql.setInt(4, deviceDto.getDriverCount());
                psql.setString(5,deviceDto.getIoCfg());
                psql.setInt(6, deviceDto.getSamplingPeriod());
                if(deviceDto.getIoCfgTime()==null){
                    psql.setTimestamp(7,null);
                }else{
                    psql.setTimestamp(7,new Timestamp(deviceDto.getIoCfgTime().getTime()));
                }

                psql.setInt(8, deviceDto.getIsOnline());
                if(deviceDto.getOnlineTtime()==null){
                    psql.setTimestamp(9,null);
                }else{
                    psql.setTimestamp(9,new Timestamp(deviceDto.getOnlineTtime().getTime()));
                }

                psql.setString(10,deviceDto.getSinarioCfg());
                if(deviceDto.getSinarioCfgTime()==null){
                    psql.setTimestamp(11,null);
                }else{
                    psql.setTimestamp(11,new Timestamp(deviceDto.getSinarioCfgTime().getTime()));
                }
                psql.setString(12,deviceDto.getUserPhone());
                psql.setLong(13,deviceDto.getDeviceId());
                psql.executeUpdate();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static DeviceDto queryDevice(Long deviceId) {
        Connection con = getConnection();
        String sql = "select * from device where device_id=?";
        PreparedStatement psql;
        DeviceDto deviceDto=new DeviceDto();
        try {
            psql = con.prepareStatement(sql);
            psql.setLong(1, deviceId);
            ResultSet rs = psql.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            while (rs.next()) {
            for(int i=1;i<=col;i++){
                switch(i){
                    case 1 :
                        deviceDto.setDeviceId(rs.getLong(1));
                        break; //optional
                    case 2 :
                        deviceDto.setDeviceVer(rs.getString(2));
                        break;
                    case 3 :
                       deviceDto.setSensorCount(rs.getInt(3));
                        break;
                    case 4 :
                       deviceDto.setDriverCount(rs.getInt(4));
                        break;
                    case 5 :
                      deviceDto.setIoCfg(rs.getString(5));
                        break;
                    case 6:
                        deviceDto.setSamplingPeriod(rs.getInt(6));
                        break;
                    case 7:
                        deviceDto.setIoCfgTime(rs.getTimestamp(7));
                        break;
                    case 8:
                        deviceDto.setIsOnline(rs.getInt(8));
                        break;
                    case 9:
                        deviceDto.setOnlineTtime(rs.getTimestamp(9));
                        break;
                    case 10:
                        deviceDto.setSinarioCfg(rs.getString(10));
                        break;
                    case 11:
                        deviceDto.setSinarioCfgTime(rs.getTimestamp(11));
                        break;
                    case 12:
                        deviceDto.setUserId(rs.getLong(12));
                    case 13:
                        deviceDto.setUserPhone(rs.getString(13));
                    default : //Optional
                        //Statements
                }



           }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deviceDto;
    }
    public static List<DeviceDto> queryAllDevice(){
        Connection con = getConnection();
        String sql = "select * from device";
        PreparedStatement psql;
        DeviceDto deviceDto;
        List<DeviceDto>deviceDtos=new ArrayList<DeviceDto>();
        try {
            psql = con.prepareStatement(sql);
            ResultSet rs = psql.executeQuery();
            while (rs.next()) {
                deviceDto=new DeviceDto();
                deviceDto.setDeviceId(rs.getLong("device_id"));
                deviceDto.setDeviceVer(rs.getString("device_ver"));
                deviceDto.setSensorCount(rs.getInt("sensor_count"));
                deviceDto.setDriverCount(rs.getInt("driver_count"));
                deviceDto.setIoCfg(rs.getString("io_cfg"));
                deviceDto.setSamplingPeriod(rs.getInt("sampling_period"));
                deviceDto.setIoCfgTime(rs.getTimestamp("io_cfg_time"));
                deviceDto.setIsOnline(rs.getInt("is_online"));
                deviceDto.setOnlineTtime(rs.getTimestamp("online_time"));
                deviceDto.setSinarioCfg(rs.getString("sinario_cfg"));
                deviceDto.setSinarioCfgTime(rs.getTimestamp("sinario_cfg_time"));
                deviceDto.setUserId(rs.getLong("user_id"));
                deviceDto.setUserPhone(rs.getString("user_phone"));
                deviceDtos.add(deviceDto);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deviceDtos;
    }

    public static void saveOnlineLog(OnlineDto onlineDto){
        String sql = "insert into online_log (deviceId,off_line_time,on_line_time) values (?,?,?)";
        try {
            PreparedStatement preStmt = getConnection().prepareStatement(sql);
            preStmt.setLong(1, onlineDto.getDeviceId());
            if(onlineDto.getOffLineTime()==null){
                preStmt.setTimestamp(2, null);
            }else{
                preStmt.setTimestamp(2, new Timestamp(onlineDto.getOffLineTime().getTime()));
            }
           if(onlineDto.getOnLineTime()==null){
               preStmt.setTimestamp(3, null);
           }else{
               preStmt.setTimestamp(3, new Timestamp(onlineDto.getOnLineTime().getTime()));
           }

            preStmt.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void updateOnline(OnlineDto onlineDto) {
        PreparedStatement psql = null;
        ResultSet res;
        Connection con = getConnection();
        try {
            if(onlineDto.getId()!=null){
                psql = con.prepareStatement("update online_log set id=?,deviceId=?,off_line_time=?,on_line_time=? where id = ?");
                psql.setLong(1, onlineDto.getId());
                psql.setLong(2, onlineDto.getDeviceId());
                psql.setTimestamp(3,new Timestamp(onlineDto.getOffLineTime().getTime()));
                psql.setTimestamp(4,new Timestamp(onlineDto.getOnLineTime().getTime()));
                psql.setLong(5, onlineDto.getId());
                psql.executeUpdate();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public static OnlineDto queryOnlineLong(Long deviceId) {
        Connection con = getConnection();
        String sql = "select * from online_log where deviceId=? AND off_line_time IS not NULL AND on_line_time is NULL ";
        PreparedStatement psql;
        OnlineDto onlineDto=new OnlineDto();
        try {
            psql = con.prepareStatement(sql);
            psql.setLong(1, deviceId);
            ResultSet rs = psql.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                for(int i=1;i<=col;i++){
                    switch(i){
                        case 1 :
                            onlineDto.setId(rs.getLong(1));
                            break; //optional
                        case 2 :
                            onlineDto.setDeviceId(rs.getLong(2));
                            break;
                        case 3 :
                           onlineDto.setOffLineTime(rs.getTime(3));
                            break;
                        case 4 :
                            onlineDto.setOnLineTime(rs.getTime(4));
                        default : //Optional
                            //Statements
                    }
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return onlineDto;
    }
}
