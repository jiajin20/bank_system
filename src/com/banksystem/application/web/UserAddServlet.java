package com.banksystem.application.web;

import com.alibaba.fastjson.JSONObject;
import com.banksystem.application.dao.UserAmountDao;
import com.banksystem.application.dao.UserInfoDao;
import com.banksystem.application.dao.entity.AdminInfo;
import com.banksystem.application.dao.entity.UserAmount;
import com.banksystem.application.dao.entity.UserInfo;
import com.banksystem.application.web.constant.ErrorCode;
import com.banksystem.application.web.util.RequestUtil;
import com.banksystem.application.web.util.ResponseUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@WebServlet(urlPatterns="/user/add")
public class UserAddServlet extends HttpServlet {
    public static final UserInfoDao userInfoDao=new UserInfoDao();//创建用户实例
    public static final UserAmountDao userAmountDao=new UserAmountDao();//创建账户实例
    @Override
    public void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
        //管理员登陆检查
        AdminInfo adminInfo=RequestUtil.checkAdminLogin(request);
        if (Objects.isNull(adminInfo)){
            ResponseUtil.fail(response, ErrorCode.ADMIN_NOT_LOGIN);
            return;
        }
        //参数获取
        JSONObject param=RequestUtil.getParam(request);
        String cardType=param.getString("cardType");
        String cardNo=param.getString("cardNo");
        String password=param.getString("password");
        String nickname=param.getString("nickname");
        String name=param.getString("name");
        String address=param.getString("address");
        String idNum=param.getString("idNum");
        String mobile=param.getString("mobile");
        Integer state=param.getInteger("state");
        //检查是否有相同手机号用户存在，存在则不允许添加
        UserInfo userInfo=userInfoDao.selectByMobile(mobile);
        if (Objects.nonNull(userInfo)){
            ResponseUtil.fail(response,ErrorCode.USER_MOBILE_SAME);
            return;
        }
        //检查是否有相同卡号或身份证号用户存在，存在则不允许添加
        userInfo=userInfoDao.selectByCardNoAndIdBum(cardNo,idNum);
        if (Objects.nonNull(userInfo)){
            ResponseUtil.fail(response,ErrorCode.USER_CARDNO_OR_IDNUM_SAME);
            return;
        }
        //封装实体属性值
        userInfo=new UserInfo();
        userInfo.setCardType(cardType);
        userInfo.setCardNo(cardNo);
        userInfo.setPassword(password);
        userInfo.setNickname(nickname);
        userInfo.setName(name);
        userInfo.setAddress(address);
        userInfo.setIdNum(idNum);
        userInfo.setMobile(mobile);
        userInfo.setState(state);
     //储存用户信息
        Long addId=userInfoDao.addOne(userInfo);

        //初始化用户余额信息
        UserAmount userAmount=new UserAmount();
        userAmount.setUserId(addId);
        userAmount.setAmount(0L);
        userAmount.setBalance(0L);
        userAmountDao.addOne(userAmount);
        ResponseUtil.success(response,null);
    }
}
