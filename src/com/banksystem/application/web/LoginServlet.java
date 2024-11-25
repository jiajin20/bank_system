package com.banksystem.application.web;

import com.alibaba.fastjson.JSONObject;
import com.banksystem.application.dao.AdminInfoDao;
import com.banksystem.application.dao.SystemLogDao;
import com.banksystem.application.dao.UserInfoDao;
import com.banksystem.application.dao.entity.AdminInfo;
import com.banksystem.application.dao.entity.UserInfo;
import com.banksystem.application.web.constant.ErrorCode;
import com.banksystem.application.web.token.LoginInfo;
import com.banksystem.application.web.util.ReIPAddress;
import com.banksystem.application.web.util.RequestUtil;
import com.banksystem.application.web.util.ResponseUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    public static final AdminInfoDao adminInfoDao = new AdminInfoDao();
    public static final UserInfoDao userInfoDao = new UserInfoDao();
    public static final SystemLogDao systemLogDao = new SystemLogDao();
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject param = RequestUtil.getParam(request);
        String mobile = param.getString("mobile");
        String password = param.getString("password");
        String loginType = param.getString("loginType");
        String ip = ReIPAddress.getIP();
        String city = ReIPAddress.getCity(ip);
        String region = ReIPAddress.getRegion(ip);
        if (Objects.isNull(loginType) || Objects.equals(loginType, "")) {
            ResponseUtil.fail(response, ErrorCode.LOGIN_TYPE_ERROR);
            return;
        }
        String randomCookie = LoginInfo.getRandomString(12);
        JSONObject loginResponse = new JSONObject();
        if (Objects.equals(loginType, "admin")) {
            AdminInfo adminInfo = adminInfoDao.selectByMobile(mobile);
            if (Objects.isNull(adminInfo)) {
                ResponseUtil.fail(response, ErrorCode.ADMIN_NOT_EXIST);
                return;
            }
            if (!Objects.equals(adminInfo.getPassword(), password)) {
                ResponseUtil.fail(response, ErrorCode.PWD_ERR);
                return;
            }
            systemLogDao.addAdminLogin(adminInfo.getId(),ip,city,region);
            LoginInfo.ADMIN_LOGIN_MAP.put(randomCookie, adminInfo);
            Cookie myCookie = new Cookie("myCookie", randomCookie);
            response.addCookie(myCookie);
            loginResponse.put("token", randomCookie);
            loginResponse.put("IP", ip);
            loginResponse.put("city", city);
            loginResponse.put("region", region);
            loginResponse.put("loginType","admin");
            ResponseUtil.success(response, loginResponse);
        }
        if (Objects.equals(loginType, "user")) {
            UserInfo userInfo = userInfoDao.selectByMobile(mobile);
            if (Objects.isNull(userInfo)) {
                ResponseUtil.fail(response, ErrorCode.ADMIN_NOT_EXIST);
                return;
            }
            if (!Objects.equals(userInfo.getPassword(), password)) {
                ResponseUtil.fail(response, ErrorCode.PWD_ERR);
                return;
            }
            systemLogDao.addUserLogin(userInfo.getId(),ip,city,region);
            LoginInfo.USER_LOGIN_MAP.put(randomCookie, userInfo);
            Cookie myCookie = new Cookie("myCookie", randomCookie);
            response.addCookie(myCookie);
            loginResponse.put("token", randomCookie);
            loginResponse.put("IP", ip);
            loginResponse.put("city", city);
            loginResponse.put("region", region);
            loginResponse.put("loginType","user");
            loginResponse.put("mobile",userInfo.getMobile());
            ResponseUtil.success(response, loginResponse);
        }
        ResponseUtil.fail(response, ErrorCode.USER_NOT_EXIST);
    }
}
