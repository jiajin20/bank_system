package com.banksystem.application.web;

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

/**
 * 操作单个用户接口- 冻结/解冻/删除
 */
@WebServlet(urlPatterns = "/user/operation")
public class UserOperationServlet extends HttpServlet {
    public static final UserInfoDao userInfoDao = new UserInfoDao();
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AdminInfo adminInfo = RequestUtil.checkAdminLogin(request);
        if (Objects.isNull(adminInfo)) {
            ResponseUtil.fail(response, ErrorCode.ADMIN_NOT_LOGIN);
            return;
        }
        JSONObject param = RequestUtil.getParam(request);
        String operation = param.getString("operation");
        if (!Objects.equals("freeze", operation)
                && !Objects.equals("unfreeze", operation)
                && !Objects.equals("delete", operation)) {
            ResponseUtil.fail(response, ErrorCode.ADMIN_NOT_LOGIN);
            return;
        }
        Long UserId = param.getLong("userId");
        UserInfo userInfo = userInfoDao.selectById(UserId);
        if (Objects.isNull(userInfo)) {
            ResponseUtil.fail(response, ErrorCode.USER_NOT_EXIST);
            return;
        }
        if (Objects.equals("freeze", operation)) {
            userInfoDao.freezeById(userInfo.getId(), 2);
        }else if (Objects.equals("unfreeze", operation)) {
            userInfoDao.freezeById(userInfo.getId(), 1);
        }
        if (Objects.equals("delete", operation)) {
            userInfoDao.deleteById(userInfo.getId());
        }
        ResponseUtil.success(response, null);
    }
}

