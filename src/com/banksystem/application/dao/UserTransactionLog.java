package com.banksystem.application.dao;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.banksystem.application.dao.db.ConvertUtil;
import com.banksystem.application.dao.db.Database;
import com.banksystem.application.dao.entity.UserAmount;
import com.banksystem.application.dao.entity.UserTransaction;
import com.banksystem.application.web.constant.ErrorCode;
import com.banksystem.application.web.util.ReIPAddress;
import com.mysql.jdbc.JDBC4PreparedStatement;

import java.sql.*;
import java.util.Objects;

public class UserTransactionLog {
    /**
     * 用户操作记录，增加新的一条
     */
    public Long addOne(UserTransaction addInfo){
       Long reId=0L;
        try{
            Connection conn=Database.conn();
            String sql="insert into user_transaction_log set parent_id =?,user_id=?,amount=?, "+
                    "transaction_type=?,transaction_before=?,transaction_after=? ,ip = ?, city = ?, region = ?";//设置sql语句的字符串
         //插入记录，需要返回插入数据的id时，此处要加Statement.RETURN_GENERATED_KEYS
            PreparedStatement ps=conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, Objects.isNull(addInfo.getParentId()) ?0L:addInfo.getParentId());
            ps.setLong(2,addInfo.getUserId());
            ps.setLong(3,addInfo.getAmount());
            ps.setInt(4,addInfo.getTransactionType());
            ps.setLong(5,addInfo.getTransactionBefore());
            ps.setLong(6,addInfo.getTransactionAfter());
            ps.setString(7, ReIPAddress.getIP());
            ps.setString(8, ReIPAddress.getCity(ReIPAddress.getIP()));
            ps.setString(9, ReIPAddress.getRegion(ReIPAddress.getIP()));
            int i=ps.executeUpdate();
            //获取插入数据的id
            ResultSet rs=ps.getGeneratedKeys();
            while (rs.next()){
              reId=rs.getLong(1);    }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return reId;//返回记录id
    }
    /**
     * 存款
     * @param orgUserAmount 用户账户信息
     * @param deltaAmount 金额，元
     * @return
     */
    public Long save(UserAmount orgUserAmount,Long deltaAmount){
        UserTransaction tranLog=new UserTransaction();
        tranLog.setUserId(orgUserAmount.getUserId());
        tranLog.setAmount(deltaAmount);
        tranLog.setTransactionType(ErrorCode.TRANSACTION_TYPE_IN);
        tranLog.setTransactionBefore(orgUserAmount.getAmount());
        tranLog.setTransactionAfter(orgUserAmount.getAmount() + (deltaAmount));
        return addOne(tranLog);//完成存钱后调用“addOne”记录日志
    }
    /**
     * 取款
     * @param orgUserAmount 用户账户信息
     * @param deltaAmount 金额，元
     * @return
     */
    public Long withDraw(UserAmount orgUserAmount,Long deltaAmount){
        UserTransaction tranLog=new UserTransaction();
        tranLog.setUserId(orgUserAmount.getUserId());
        tranLog.setAmount(deltaAmount);
        tranLog.setTransactionType(ErrorCode.TRANSACTION_TYPE_OUT);
        tranLog.setTransactionBefore(orgUserAmount.getAmount());
        tranLog.setTransactionAfter(orgUserAmount.getAmount() - (deltaAmount));
        return addOne(tranLog);//完成存钱后调用“addOne”记录日志
    }

    /**
     * 转账
     * @param orgUserAmount 源用户账户信息
     * @param destUserAmount 目标用户账户信息
     * @param deltaMoney 转账金额，单位：元
     * @return
     */
    public void transaction(UserAmount orgUserAmount,UserAmount destUserAmount,Long deltaMoney){
    //转出存款
        UserTransaction tranOutLog=new UserTransaction();
        tranOutLog.setUserId(orgUserAmount.getUserId());
        tranOutLog.setAmount(deltaMoney * -1);
        tranOutLog.setTransactionType(ErrorCode.TRANSACTION_TYPE_TRANSACTION_OUT);
        tranOutLog.setTransactionBefore(orgUserAmount.getAmount());
        tranOutLog.setTransactionAfter(orgUserAmount.getAmount() - deltaMoney);
        Long saveId=addOne(tranOutLog);
        //转入存款
        UserTransaction tranInLog=new UserTransaction();
        tranInLog.setParentId(saveId);
        tranInLog.setUserId(destUserAmount.getUserId());
        tranInLog.setAmount(deltaMoney);
        tranInLog.setTransactionType(ErrorCode.TRANSACTION_TYPE_TRANSACTION_IN);
        tranInLog.setTransactionBefore(destUserAmount.getAmount());
        tranInLog.setTransactionAfter(destUserAmount.getAmount() + deltaMoney);
        addOne(tranInLog);
    }
    /**
     * 转账
     *  根据用户id查询用户操作记录
     * @param userId
     * @return
     */
    public JSONArray selectByUserId(Long userId) {
        JSONArray rsList = new JSONArray();
        try {
            Connection conn = Database.conn();
            String sql = "SELECT ut.id, ut.parent_id, ut.user_id, ut.transaction_type, ut.amount, ut.create_time, ut.ip, ut.city, " +
                    "up.user_id AS from_user, uc.user_id AS to_user " +
                    "FROM user_transaction_log ut " +
                    "LEFT JOIN user_transaction_log up ON ut.parent_id = up.id " +
                    "LEFT JOIN user_transaction_log uc ON ut.id = uc.parent_id " +
                    "WHERE ut.user_id = ? " +
                    "ORDER BY ut.create_time DESC";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        JSONObject json = new JSONObject();
                        json.put("id", rs.getLong("id"));
                        json.put("parent_id", rs.getLong("parent_id"));
                        json.put("userId", rs.getLong("user_id"));
                        json.put("transactionType", rs.getInt("transaction_type"));
                        json.put("amount", rs.getLong("amount"));
                        json.put("fromUserId", rs.getObject("from_user", Long.class));
                        json.put("toUserId", rs.getObject("to_user", Long.class));
                        json.put("create_time", ConvertUtil.stringToInstant(rs.getString("create_time")));
                        json.put("IP", rs.getString("ip"));
                        json.put("city", rs.getString("city"));
                        rsList.add(json);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rsList;
    }

}

