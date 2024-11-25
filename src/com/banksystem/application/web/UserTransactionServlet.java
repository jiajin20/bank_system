package com.banksystem.application.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.banksystem.application.dao.UserInfoDao;
import com.banksystem.application.dao.UserTransactionLog;
import com.banksystem.application.dao.entity.UserInfo;
import com.banksystem.application.dao.entity.UserTransaction;
import com.banksystem.application.web.constant.ErrorCode;
import com.banksystem.application.web.util.RequestUtil;
import com.banksystem.application.web.util.ResponseUtil;
import com.mysql.jdbc.util.ResultSetUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 用户交易记录
 */
@WebServlet(urlPatterns="/user/transaction")//子路由名称
public class UserTransactionServlet extends HttpServlet {
    public static final UserTransactionLog userTransactionLogDao=new UserTransactionLog();//记录实例化
    public static final UserInfoDao userInfoDao=new UserInfoDao();//用户实例化
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //用户登录检查
        UserInfo userInfo=RequestUtil.checkUserLogin(request);
        if (Objects.isNull(userInfo)){
            ResponseUtil.fail(response, ErrorCode.NOT_LOGIN);
            return;
        }
        JSONArray tranLogs=userTransactionLogDao.selectByUserId(userInfo.getId());//查询登录用户的交易记录
        Set<String> userIds=new HashSet<>();
        for (Object tranLog:tranLogs){
            JSONObject json=(JSONObject) JSON.toJSON(tranLog);
            Long fromUserId=json.getLong("fromUserId");
            Long toUserId=json.getLong("toUserId");
            userIds.add(Objects.nonNull(fromUserId) ? fromUserId.toString() : "0");//如果用户id不存在则用“0”填充
            userIds.add(Objects.nonNull(toUserId) ? toUserId.toString() : "0");
        }
        //获取关联用户数据{userId：userInfor}
        Map<Long,UserInfo> relationUserMap=userInfoDao.selectByIds(userIds);//将关联的用户信息查出
        //填充关联用户信息
        for (Object tranLog:tranLogs){
            JSONObject json=(JSONObject) JSON.toJSON(tranLog);
            //汇款方
            Long fromUserId=json.getLong("fromUserId");
            if (Objects.nonNull(fromUserId) && relationUserMap.containsKey(fromUserId)){
                UserInfo fromUser=relationUserMap.get(fromUserId);//获取用户信息
                json.put("fromUserCardNo",fromUser.getCardNo());//填入汇款方账户账号
            }
            //收款方
            Long toUserId=json.getLong("toUserId");
            if (Objects.nonNull(toUserId) && relationUserMap.containsKey(toUserId)){
                UserInfo toUser=relationUserMap.get(toUserId);//获取用户信息
                json.put("toUserCardNo",toUser.getCardNo());//填入汇款方账户账号
            }
        }
        ResponseUtil.success(response,tranLogs);
    }
}
