package com.banksystem.application.dao;

import com.banksystem.application.dao.db.ConvertUtil;
import com.banksystem.application.dao.db.Database;
import com.banksystem.application.dao.entity.UserAmount;

import java.sql.*;

public class UserAmountDao {
    //添加新用户
    public Long addOne(UserAmount userAmount){
        Long reId=0L;
        try{
            Connection conn=Database.conn();
            String sql="insert into user_amount set user_id =?,amount=?,balance=?";//设置sql语句的字符串
            PreparedStatement ps =conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1,userAmount.getUserId());
            ps.setLong(2,userAmount.getAmount());
            ps.setLong(3,userAmount.getBalance());
            int i=ps.executeUpdate();
            //获取插入数据的id
            ResultSet rs=ps.getGeneratedKeys();
            while (rs.next()){
                reId=rs.getLong(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return reId;
    }
    //根据用户id，查询账户信息
    public UserAmount selectByUserId(Long userId){
        UserAmount userAmount=null;
        try{
            Connection conn=Database.conn();
            String sql="select id,user_id,amount,balance,`state`,create_time from user_amount where user_id=?";//设置sql语句的字符串
            PreparedStatement ps =conn.prepareStatement(sql);
            ps.setLong(1,userId);
           ResultSet rs=ps.executeQuery();
            while (rs.next()){
              userAmount=new UserAmount();
              userAmount.setId(rs.getLong("id"));
                userAmount.setUserId(rs.getLong("user_id"));
                userAmount.setAmount(rs.getLong("amount"));
                userAmount.setBalance(rs.getLong("balance"));
                userAmount.setState(rs.getInt("state"));
                userAmount.setCreateTime(ConvertUtil.stringToInstant(rs.getString("create_time")));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return userAmount;
    }
    //全额修改
    public void addUserAmount(Long userId,Long delta){
        try{
            Connection conn=Database.conn();
            String sql="update user_amount set amount=amount + ?," +
                    "balance=balance + ? where user_id=?";
        PreparedStatement ps=conn.prepareStatement(sql);
        ps.setLong(1,delta);
            ps.setLong(2,delta);
            ps.setLong(3,userId);
            int i=ps.executeUpdate();
            if (i==1){
                System.out.println("更新用户余额成功");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void addLoanAmount(Long userId,Long loanmoney,Long loanmonth){
        try{
            Connection conn=Database.conn();
            String sql="update user_amount set amount=amount + ?*100, balance=balance + ?*100 ,loanmoney=loanmoney + ?*100 ,loanmonth=loanmonth +? where user_id=?";//设置sql语句的字符串
            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setLong(1,loanmoney);
            ps.setLong(2,loanmoney);
            ps.setLong(3,loanmoney);
            ps.setLong(4,loanmonth);
            ps.setLong(5,userId);
            int i=ps.executeUpdate();
            if (i==1){
                System.out.println("添加贷款成功");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
