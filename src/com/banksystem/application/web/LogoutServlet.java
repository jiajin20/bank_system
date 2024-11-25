package com.banksystem.application.web;

import com.alibaba.fastjson.JSONObject;
import com.banksystem.application.dao.SystemLogDao;
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

@WebServlet(urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {
    public  static  final SystemLogDao systemLogDao = new SystemLogDao();
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cookie = request.getHeader("token");
        String ip = ReIPAddress.getIP();
        String city = ReIPAddress.getCity(ip);
        String region = ReIPAddress.getRegion(ip);
        AdminInfo adminInfo = LoginInfo.ADMIN_LOGIN_MAP.get(cookie);
        UserInfo userInfo = LoginInfo.USER_LOGIN_MAP.get(cookie);
        if (Objects.isNull(adminInfo) && Objects.isNull(userInfo)) {
            ResponseUtil.fail(response, ErrorCode.NOT_LOGIN);
            return;
        }
        if (Objects.nonNull(adminInfo)) {
            LoginInfo.ADMIN_LOGIN_MAP.remove(cookie);
            systemLogDao.addAdminLogout(adminInfo.getId(),ip,city,region);
        }
        if (Objects.nonNull(userInfo)) {
            LoginInfo.USER_LOGIN_MAP.remove(cookie);
            systemLogDao.addUserLogout(userInfo.getId(),ip,city,region);
        }
        Cookie myCookie = new Cookie("token", "");
        response.addCookie(myCookie);
        ResponseUtil.success(response, null);
    }
}
