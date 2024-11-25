package com.banksystem.application.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.banksystem.application.dao.UserInfoDao;
import com.banksystem.application.dao.entity.AdminInfo;
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
@WebServlet(urlPatterns="/user/info")//子路由名称
public class UserInfoServlet extends HttpServlet {
    public static final UserInfoDao userInfoDao=new UserInfoDao();//实例用户模型
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //用户登录检查
        UserInfo userInfo=RequestUtil.checkUserLogin(request);
        if (Objects.isNull(userInfo)){
            ResponseUtil.fail(response, ErrorCode.NOT_LOGIN);
            return;
        }
        //参数获取
        JSONObject param=RequestUtil.getParam(request);
        //非必填 nickname，name 模糊查询
        String mobile=param.getString("mobile");//获取用户手机号
        //用户余额-单位分
        JSONArray userJsonArr=userInfoDao.selectSingleWithBalance(mobile);//传入手机号，调用查询方法
        ResponseUtil.success(response,userJsonArr);
    }
}
