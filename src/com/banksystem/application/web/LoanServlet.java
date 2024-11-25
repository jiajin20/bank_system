package com.banksystem.application.web;

import com.alibaba.fastjson.JSONObject;
import com.banksystem.application.dao.UserAmountDao;
import com.banksystem.application.dao.UserInfoDao;
import com.banksystem.application.dao.entity.UserInfo;
import com.banksystem.application.web.constant.ErrorCode;
import com.banksystem.application.web.util.RequestUtil;
import com.banksystem.application.web.util.ResponseUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@WebServlet(urlPatterns = "/user/loan")
public class LoanServlet extends HttpServlet {
    private final UserInfoDao userInfoDao = new UserInfoDao();
    private final UserAmountDao userAmountDao = new UserAmountDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserInfo userInfo = RequestUtil.checkUserLogin(request);
        if (Objects.isNull(userInfo)) {
            ResponseUtil.fail(response, ErrorCode.NOT_LOGIN);
            return;
        }

        JSONObject param = RequestUtil.getParam(request);
        Long loanAmount = param.getLong("loanmoney");
        Long loanTerm = param.getLong("loanmonth");

        if (loanAmount == null || loanAmount <= 0) {
            ResponseUtil.fail(response, ErrorCode.INVALID_LOAN_AMOUNT);
            return;
        }
        if (loanTerm == null || loanTerm <= 0) {
            ResponseUtil.fail(response, ErrorCode.INVALID_LOAN_TERM);
            return;
        }

        try {
            userAmountDao.addLoanAmount(userInfo.getId(), loanAmount, loanTerm);
            ResponseUtil.success(response, null);
        } catch (Exception e) {
            ResponseUtil.fail(response, ErrorCode.INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }
}
