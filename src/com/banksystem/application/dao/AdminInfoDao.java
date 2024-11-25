package com.banksystem.application.dao;

import com.banksystem.application.dao.db.ConvertUtil;
import com.banksystem.application.dao.db.Database;
import com.banksystem.application.dao.entity.AdminInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminInfoDao {
    public AdminInfo selectByMobile(String mobile){
        AdminInfo adminInfo=null;
        try{
            Connection conn=Database.conn();
            String sql="select id, password, nickname, name, mobile, create_time from admin_info where mobile= ? and state=1 and is_deleted=0";
            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setString(1,mobile);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                adminInfo=new AdminInfo();
                adminInfo.setId(rs.getLong("id"));
                adminInfo.setPassword(rs.getString("password"));
                adminInfo.setNickname(rs.getString("nickname"));
                adminInfo.setName(rs.getString("name"));
                adminInfo.setMobile(rs.getString("mobile"));
                adminInfo.setCreateTime(ConvertUtil.stringToInstant(rs.getString("create_time")));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return adminInfo;
    }
    public Long addOne(AdminInfo adminInfo){
        Long addId=0L;
        try{
            Connection conn=Database.conn();
            String sql="insert into admin_info set password=?,nickname=?,name=?,mobile=?";
            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setString(1, adminInfo.getPassword());
            ps.setString(2, adminInfo.getNickname());
            ps.setString(3, adminInfo.getName());
            ps.setString(4, adminInfo.getMobile());
            int i=ps.executeUpdate();
            if (i > 0) {
                AdminInfo add=selectByMobile(adminInfo.getMobile());
                addId=add.getId();
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return addId;
    }
}
