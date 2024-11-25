package com.banksystem.application.web;

import com.alibaba.fastjson.JSONObject;
import com.banksystem.application.dao.UserAmountDao;
import com.banksystem.application.dao.UserInfoDao;
import com.banksystem.application.dao.UserTransactionLog;
import com.banksystem.application.dao.entity.UserAmount;
import com.banksystem.application.dao.entity.UserInfo;
import com.banksystem.application.web.constant.ErrorCode;
import com.banksystem.application.web.util.RequestUtil;
import com.banksystem.application.web.util.ResponseUtil;
import com.sun.org.apache.regexp.internal.RE;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 用户存款/取款/转账
 */
@WebServlet(urlPatterns="/user/money")//子路由
public class UserMoneyServlet extends HttpServlet{
    public static final UserInfoDao userInfoDao=new UserInfoDao();
    public static final UserAmountDao userAmountDao=new UserAmountDao();
    public static final UserTransactionLog userTransactionLogDao=new UserTransactionLog();
    @Override
    public void doPost(HttpServletRequest request,HttpServletResponse response) throws SecurityException, IOException{
        UserInfo userInfo=RequestUtil.checkUserLogin(request);
        if (Objects.isNull(userInfo)){
            ResponseUtil.fail(response, ErrorCode.NOT_LOGIN);
            return;
        }
        JSONObject param=RequestUtil.getParam(request);
        Integer transactionType=param.getInteger("type");
        String cardNo=param.getString("cardNo");
        Long absAmount=param.getLong("amount");
        if (absAmount < 0) {
            ResponseUtil.fail(response,ErrorCode.USER_AMOUNT_UNDER_ZERO);
            return;
        }
        UserAmount orgUserAmount=userAmountDao.selectByUserId(userInfo.getId());
        if (transactionType.equals(ErrorCode.TRANSACTION_TYPE_IN)){
            if (orgUserAmount.getAmount().compareTo(absAmount) < 0){
                ResponseUtil.fail(response,ErrorCode.USER_AMOUNT_INSUFFICIENT);
                return;
            }
            userAmountDao.addUserAmount(userInfo.getId(),absAmount * -1);
            userTransactionLogDao.withDraw(orgUserAmount,absAmount);
        }
        if (transactionType.equals(ErrorCode.TRANSACTION_TYPE_OUT)){
            userAmountDao.addUserAmount(userInfo.getId(),absAmount);
            userTransactionLogDao.save(orgUserAmount,absAmount);
        }
        if (transactionType.equals(ErrorCode.TRANSACTION_TYPE_TRANSACTION_OUT)){
            if (orgUserAmount.getAmount().compareTo(absAmount)<0){
                ResponseUtil.fail(response,ErrorCode.USER_AMOUNT_INSUFFICIENT);
                return;
            }
            if (Objects.isNull(cardNo) || Objects.equals("",cardNo)){
                ResponseUtil.fail(response,ErrorCode.TRANSACTION_OUT_HAS_NO_CARND);
                return;
            }
            UserInfo destUserInfo=userInfoDao.selectByCardNo(cardNo);
            UserAmount destUserAmount=userAmountDao.selectByUserId(destUserInfo.getId());
            if (Objects.isNull(destUserAmount)){
                ResponseUtil.fail(response,ErrorCode.TRANSACTION_OUT_SUCH_USER);
                return;
            }
            userAmountDao.addUserAmount(userInfo.getId(),absAmount * -1);
            userAmountDao.addUserAmount(destUserInfo.getId(),absAmount);
            userTransactionLogDao.transaction(orgUserAmount,destUserAmount,absAmount);
        }
        ResponseUtil.success(response,null);
    }
}
