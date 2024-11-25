package com.banksystem.application.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.banksystem.application.dao.SystemLogDao;
import com.banksystem.application.dao.UserInfoDao;
import com.banksystem.application.dao.entity.AdminInfo;
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

@WebServlet(urlPatterns = "/admin/histroy")
public class AdminLoginHistorySerlet extends HttpServlet {
    public static final SystemLogDao systemLogDao = new SystemLogDao();
    public static final UserInfoDao userInfoDao = new UserInfoDao();
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AdminInfo adminInfo = RequestUtil.checkAdminLogin(request);
        if (Objects.isNull(adminInfo)) {
            ResponseUtil.fail(response, ErrorCode.ADMIN_NOT_LOGIN);
            return;
        }
        JSONObject param = RequestUtil.getParam(request);
        String user = param.getString("user");
        if (Objects.equals(user,null)){
            JSONArray userJsonArr = systemLogDao.adminallloginHistory();
            ResponseUtil.success(response,userJsonArr);
        }else {
            JSONArray userJsonArr = systemLogDao.userloginHistory(String.valueOf(user));
            ResponseUtil.success(response,userJsonArr);
        }
    }
}
