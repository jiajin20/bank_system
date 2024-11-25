package com.banksystem.application.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.banksystem.application.dao.UserInfoDao;
import com.banksystem.application.dao.UserTransactionLog;
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
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 用户更新接口
 */
@WebServlet(urlPatterns="/user/update")//子路由名称
public class UserUpdateServlet extends HttpServlet {
    public static final UserInfoDao userInfoDao=new UserInfoDao();
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
        //参数获取
      Long userId=userInfo.getId();
      String password=param.getString("password");
        String repassword=param.getString("repassword");
        String name=param.getString("name");
        String address=param.getString("address");
        String mobile=param.getString("mobile");
        //需要修改密码，两次密码一致检查
        if (repassword !="" && password!= "" && !Objects.equals(password,repassword)) {
            ResponseUtil.fail(response, ErrorCode.REPEAT_PWD_NOT_SAME);
            return;
        }
        //更新用户（如果要允许修改手机号，需要检查新手机号是否已经存在，若存在则不允许修改，不存在则可以修改
    //数据库获取目标id信息
        UserInfo dbUserInfo=userInfoDao.selectById(userId);
        if (Objects.isNull(dbUserInfo)){
          ResponseUtil.fail(response,ErrorCode.USER_NOT_EXIST);
          return;
        }
      //设置新消息
        dbUserInfo.setPassword(password);
        dbUserInfo.setName(name);
        dbUserInfo.setAddress(address);
        dbUserInfo.setMobile(mobile);
        //执行更新
        userInfoDao.updateOne(dbUserInfo);
        ResponseUtil.success(response,null);

    }
}