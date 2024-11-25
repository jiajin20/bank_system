package com.banksystem.application.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.banksystem.application.dao.db.Database;
import com.banksystem.application.dao.entity.adminloginHistory;
import com.banksystem.application.dao.entity.userloginHistory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class SystemLogDao {
    public void addAdminLogin(long adminId,String ip,String city,String region){
        addOne(1,0,adminId,ip,city,region);
    }
    /*
     * 管理员登出信息
     * */
    public void addAdminLogout(long adminId,String ip,String city,String region){
        addOne(0,0,adminId,ip,city,region);
    }
    /*
     * 用户登入信息
     * */
    public void addUserLogin(long userId,String ip,String city,String region){
        addOne(1,1,userId,ip,city,region);
    }
    /*
     * 用户登出记录
     * */
    public void addUserLogout(long userId,String ip,String city,String region){
        addOne(0,1,userId,ip,city,region);
    }
    public void addOne(Integer type,Integer userType,long id,String ip,String city,String region){
        try {
            Connection conn = Database.conn();
            String sql = "insert into system_log set `type` = ? , user_type = ? , user_id = ?, ip = ?, city = ?, region = ?";//设置sql
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, type);
            ps.setInt(2, userType);
            ps.setLong(3, id);
            ps.setString(4, ip);
            ps.setString(5, city);
            ps.setString(6, region);
            int i = ps.executeUpdate();
            if (i > 0){
                System.out.println("登陆日志记录成功");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public JSONArray userloginHistory(String id){
        JSONArray reList = new JSONArray();
        try {
            Connection conn = Database.conn();
            String sql = "select create_time,ip,city,region from system_log where user_id = ? and type = 1";//设置sql
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                userloginHistory loginhistory = new userloginHistory();
                loginhistory.setCreateTime(rs.getString("create_time"));
                loginhistory.setIp(rs.getString("ip"));
                loginhistory.setCity(rs.getString("city"));
                loginhistory.setRegion(rs.getString("region"));
                JSONObject json = (JSONObject) JSON.toJSON(loginhistory);
//                json.put("balance", rs.getBigDecimal("balance"));
                reList.add(json);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return reList;
    }
    public JSONArray adminallloginHistory(){
        JSONArray reList = new JSONArray();
        try {
            Connection conn = Database.conn();
            String sql = "select create_time,ip,city,region,user_id from system_log where type = 1";//设置sql
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                adminloginHistory loginhistory = new adminloginHistory();
                loginhistory.setCreateTime(rs.getString("create_time"));
                loginhistory.setIp(rs.getString("ip"));
                loginhistory.setCity(rs.getString("city"));
                loginhistory.setRegion(rs.getString("region"));
                loginhistory.setUserid(rs.getString("user_id"));
                JSONObject json = (JSONObject) JSON.toJSON(loginhistory);
//                json.put("balance", rs.getBigDecimal("balance"));
                reList.add(json);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return reList;
    }
}
