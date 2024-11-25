package com.banksystem.application.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.banksystem.application.dao.db.ConvertUtil;
import com.banksystem.application.dao.db.Database;
import com.banksystem.application.dao.entity.UserInfo;
import com.mysql.jdbc.JDBC4PreparedStatement;

import java.sql.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserInfoDao {
    /**
     * 根据用户信息新增用户
     *
     * @return
     */
    public Long addOne(UserInfo userInfo) {
        Long reId=0L;
        try {
            Connection conn=Database.conn();
            String sql="insert into user_info set card_type=?,card_no=?,password=?," +
                    "nickname=?,name=?,address=?,id_num=?,mobile=?";//设置sql语句的字符串
            //插入记录，需要返回插入数据的id时候，此处要加Statement.GENERATED_KEYS
            PreparedStatement ps=conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, userInfo.getCardType());
            ps.setString(2, userInfo.getCardNo());
            ps.setString(3, userInfo.getPassword());
            ps.setString(4, userInfo.getNickname());
            ps.setString(5, userInfo.getName());
            ps.setString(6, userInfo.getAddress());
            ps.setString(7, userInfo.getIdNum());
            ps.setString(8, userInfo.getMobile());
            int i=ps.executeUpdate();
            //获取插入数据的id
            ResultSet rs=ps.getGeneratedKeys();
            while (rs.next()) {
                reId=rs.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reId;
    }
    /**
     * 根据手机号查询用户
     * @return
     */
    public UserInfo selectByMobile(String mobile) {
        UserInfo userInfo=null;
        try {
            Connection conn=Database.conn();
            String sql="select id,card_type,card_no,password,nickname,name,address,id_num,mobile,"+
                    "create_time from user_info where mobile=? and `state`=1 and `is_deleted`=0";//设置sql语句的字符串
            //插入记录，需要返回插入数据的id时候，此处要加Statement.GENERATED_KEYS
            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setString(1, mobile);
            ResultSet rs=ps.executeQuery();
            //获取插入数据的id
            while (rs.next()) {
            userInfo=new UserInfo();
            userInfo.setId(rs.getLong("id"));
            userInfo.setCardType(rs.getString("card_type"));
            userInfo.setCardNo(rs.getString("card_no"));
            userInfo.setPassword(rs.getString("password"));
            userInfo.setNickname(rs.getString("nickname"));
            userInfo.setName(rs.getString("name"));
            userInfo.setAddress(rs.getString("address"));
            userInfo.setIdNum(rs.getString("id_num"));
            userInfo.setMobile(rs.getString("mobile"));
            userInfo.setUpdateTime(ConvertUtil.stringToInstant(rs.getString("create_time")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userInfo;
    }
    /**
     * 根据用户卡号，身份证查询
     * @param cardNo
     * @param idNum
     * @return
     */
    public UserInfo selectByCardNoAndIdBum(String cardNo,String idNum){
        UserInfo userInfo=null;
        try{
            Connection conn=Database.conn();
            String sql="select id,card_type,card_no,password,nickname,name,address,id_num,mobile,create_time from user_info "+
                    "where (card_no=? or id_num=?) and `state`=1 and `is_deleted`=0";//设置sql语句的字符串
            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setString(1,cardNo);
            ps.setString(2,idNum);
            ResultSet rs=ps.executeQuery();
while (rs.next()){
    userInfo=new UserInfo();
    userInfo.setId(rs.getLong("id"));
    userInfo.setCardType(rs.getString("card_type"));
    userInfo.setCardNo(rs.getString("card_no"));
    userInfo.setPassword(rs.getString("password"));
    userInfo.setNickname(rs.getString("nickname"));
    userInfo.setName(rs.getString("name"));
    userInfo.setAddress(rs.getString("address"));
    userInfo.setIdNum(rs.getString("id_num"));
    userInfo.setMobile(rs.getString("mobile"));
    userInfo.setCreateTime(ConvertUtil.stringToInstant(rs.getString("create_time")));
}
        }catch (SQLException e){
            e.printStackTrace();
        }
        return userInfo;
    }
    /**
     * 用户查询，连表查询余额
     * @param username
     * @param state
     * @return
     */
    public JSONArray selectAllWithBalance(String username, String state){
        JSONArray reList=new JSONArray();
        try{
            Connection conn=Database.conn();
            String sql="select u.id,u.card_type,u.card_no,u.password,u.nickname,u.name, "+
            "u.address,u.id_num,u.mobile,u.create_time,u.`state`,um.balance from "+
                    "user_info u left join user_amount um on u.id=um.user_id "+
                    "where 1=1";//设置sql语句的字符串
            //拼接sql 模糊查询姓名，用户名
            if (Objects.nonNull(username) && !Objects.equals("",username)){
                sql += " and ( u.name like '%" + username + "%' or u.nickname like '%"+ username + "%')";
            }
            //拼接sql 包含查询状态
            if (Objects.nonNull(state) && !Objects.equals("",state)){
                sql += " and u.state=?";
            }
            PreparedStatement ps=conn.prepareStatement(sql);
            if (Objects.nonNull(state) && !Objects.equals("",state)){
               ps.setInt(1,Integer.valueOf(state));
            }
            //查询执行的sql语句
          String exeSql=((JDBC4PreparedStatement)ps).asSql();
            ResultSet rs=ps.executeQuery();
            while (rs.next()){//循环读取并写入单个用户的账户的信息
              UserInfo  userInfo=new UserInfo();
                userInfo.setId(rs.getLong("id"));
                userInfo.setCardType(rs.getString("card_type"));
                userInfo.setCardNo(rs.getString("card_no"));
                userInfo.setPassword(rs.getString("password"));
                userInfo.setNickname(rs.getString("nickname"));
                userInfo.setName(rs.getString("name"));
                userInfo.setAddress(rs.getString("address"));
                userInfo.setIdNum(rs.getString("id_num"));
                userInfo.setMobile(rs.getString("mobile"));
                userInfo.setCreateTime(ConvertUtil.stringToInstant(rs.getString("create_time")));
                userInfo.setState(rs.getInt("state"));
                JSONObject json=(JSONObject) JSON.toJSON(userInfo);
                json.put("balance",rs.getBigDecimal("balance"));
                reList.add(json);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return reList;
    }
    /**
     * 冻结，解冻用户
     * @param userId
     * @param state 状态，0禁用，1启用，2冻结
     */
    public void freezeById(long userId,Integer state){
        try{
            Connection conn=Database.conn();
            String sql="update user_info set `state`=? where id=?";//设置sql语句的字符串
            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setInt(1,state);
            ps.setLong(2,userId);
            int i=ps.executeUpdate();
            if (i==1){
                System.out.println("更新用户成功");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    /**
     * 删除用户，这里是真实删除
     * @param userId
     */
    public void deleteById(long userId){
        try{
            Connection conn=Database.conn();
            String sql="delete from user_info where id=? and `is_deleted`=0";//设置sql语句的字符串
            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setLong(1,userId);
            int i=ps.executeUpdate();
            if (i==1){
                System.out.println("更新用户成功");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    /**
     * 根据用户id查询
     * @param userId
     * @return
     */
    public UserInfo selectById(long userId){
        UserInfo userInfo=null;
        try{
            Connection conn=Database.conn();
            String sql="select id,card_type,card_no,password,nickname,name,address, "+
                    "id_num,mobile,create_time from user_info where id=? and `is_deleted`=0";//设置sql语句的字符串
            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setLong(1,userId);
            ResultSet rs=ps.executeQuery();
           while (rs.next()){
              userInfo=new UserInfo();
               userInfo.setId(rs.getLong("id"));
               userInfo.setCardType(rs.getString("card_type"));
               userInfo.setCardNo(rs.getString("card_no"));
               userInfo.setPassword(rs.getString("password"));
               userInfo.setNickname(rs.getString("nickname"));
               userInfo.setName(rs.getString("name"));
               userInfo.setAddress(rs.getString("address"));
               userInfo.setIdNum(rs.getString("id_num"));
               userInfo.setMobile(rs.getString("mobile"));
               userInfo.setCreateTime(ConvertUtil.stringToInstant(rs.getString("create_time")));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return userInfo;
    }
    /**
     * 根据手机号查询单一用户
     * @param mobile
     * @return
     */
    public JSONArray selectSingleWithBalance(String mobile){
        JSONArray reList=new JSONArray();
        try{
            Connection conn=Database.conn();
            String sql="select u.id,u.card_type,u.card_no,u.password,u.nickname,u.name,u.address, "+
                    "u.id_num,u.mobile,u.create_time,u.`state`,um.balance from user_info u left join "+
                    "user_amount um on u.id=um.user_id where u.`mobile`=? and u.`state`=1";//设置sql语句的字符串
            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setString(1,mobile);
            ResultSet rs=ps.executeQuery();
            System.out.println(rs);
            while (rs.next()){
                UserInfo userInfo=new UserInfo();
                userInfo.setId(rs.getLong("id"));
                userInfo.setCardType(rs.getString("card_type"));
                userInfo.setCardNo(rs.getString("card_no"));
                userInfo.setNickname(rs.getString("nickname"));
                userInfo.setName(rs.getString("name"));
                userInfo.setAddress(rs.getString("address"));
                userInfo.setIdNum(rs.getString("id_num"));
                userInfo.setMobile(rs.getString("mobile"));
                userInfo.setCreateTime(ConvertUtil.stringToInstant(rs.getString("create_time")));
         userInfo.setState(rs.getInt("state"));
         JSONObject json=(JSONObject) JSON.toJSON(userInfo);
         json.put("balance",rs.getBigDecimal("balance"));
         reList.add(json);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return reList;
    }
    /**
     * 根据用户卡号查询
     * @param cardNo
     * @return
     */
    public UserInfo selectByCardNo(String cardNo){
        UserInfo userInfo=null;
        try{
            Connection conn=Database.conn();
            String sql="select id,card_type,card_no,password,nickname,name,address, "+
                    "id_num,mobile,create_time from user_info where card_no=? and `is_deleted`=0";//设置sql语句的字符串
            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setString(1,cardNo);
            ResultSet rs=ps.executeQuery();
            System.out.println(rs);
            while (rs.next()){
                     userInfo=new UserInfo();
                userInfo.setId(rs.getLong("id"));
                userInfo.setCardType(rs.getString("card_type"));
                userInfo.setCardNo(rs.getString("card_no"));
                userInfo.setPassword(rs.getString("password"));
                userInfo.setNickname(rs.getString("nickname"));
                userInfo.setName(rs.getString("name"));
                userInfo.setAddress(rs.getString("address"));
                userInfo.setIdNum(rs.getString("id_num"));
                userInfo.setMobile(rs.getString("mobile"));
                userInfo.setCreateTime(ConvertUtil.stringToInstant(rs.getString("create_time")));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return userInfo;
    }
    /**
     * 交易记录根据转入转出放id 查询用户账户信息
     */
    public Map<Long, UserInfo> selectByIds(Collection<String> ids) {
        Map<Long, UserInfo> reMap = new HashMap<>();
        try {
            Connection conn = Database.conn();
            StringBuilder placeholders = new StringBuilder();
            for (int i = 0; i < ids.size(); i++) {
                placeholders.append("?");
                if (i < ids.size() - 1) {
                    placeholders.append(",");
                }
            }
            String sql = "SELECT id, card_type, card_no, password, nickname, name, address, id_num, mobile, create_time, `state` FROM  user_info WHERE id IN ( "+ placeholders.toString() + ")";
            PreparedStatement ps = conn.prepareStatement(sql);

            int index = 1;
            for (String id : ids) {
                ps.setString(index++, id);
            }

            // 查看执行的SQL语句
            String exsql = ((com.mysql.jdbc.JDBC4PreparedStatement) ps).asSql();
            System.out.println(exsql);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UserInfo userInfo = new UserInfo();
                userInfo.setId(rs.getLong("id"));
                userInfo.setCardType(rs.getString("card_type"));
                userInfo.setCardNo(rs.getString("card_no"));
                userInfo.setNickname(rs.getString("nickname"));
                userInfo.setName(rs.getString("name"));
                userInfo.setAddress(rs.getString("address"));
                userInfo.setIdNum(rs.getString("id_num"));
                userInfo.setMobile(rs.getString("mobile"));
                userInfo.setCreateTime(ConvertUtil.stringToInstant(rs.getString("create_time")));
                userInfo.setState(rs.getInt("state"));
                reMap.put(userInfo.getId(), userInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reMap;
    }
    /**
     * 用户信息更新
     */
    public UserInfo updateOne(UserInfo userInfo){
        try{
            Connection conn=Database.conn();
            String sql="update user_info set card_type=?,card_no=?, "+
                    "password=?,nickname=?,name=?,address=?,"+
                    "mobile=? where id=? and `state` =1 and `is_deleted`=0";//设置sql语句的字符串
            PreparedStatement ps=conn.prepareStatement(sql);
                ps.setString(1,userInfo.getCardType());
                ps.setString(2,userInfo.getCardNo());
                ps.setString(3,userInfo.getPassword());
                ps.setString(4,userInfo.getNickname());
                ps.setString(5,userInfo.getName());
                ps.setString(6,userInfo.getAddress());
                ps.setString(7,userInfo.getMobile());
                ps.setLong(8,userInfo.getId());
                int i=ps.executeUpdate();
                if (i==1) {
                    System.out.println("更新用户成功");
                }
                   }catch (SQLException e){
            e.printStackTrace();
        }
        return userInfo;
    }
}
