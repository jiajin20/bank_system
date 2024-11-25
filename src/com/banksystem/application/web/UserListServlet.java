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

/**
 * 用户列表接口
 */
@WebServlet(urlPatterns="/user/list")//子路由名称
public class UserListServlet extends HttpServlet{
    public static final UserInfoDao userInfoDao=new UserInfoDao();//实例用户模型
    @Override
    public void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
        //管理员登录检查
       AdminInfo adminInfo=RequestUtil.checkAdminLogin(request);
        if (Objects.isNull(adminInfo)){
            ResponseUtil.fail(response, ErrorCode.ADMIN_NOT_LOGIN);
            return;
        }
        //参数获取
        JSONObject param=RequestUtil.getParam(request);
        //非必填 nickname，name 模糊查询
        String username=param.getString("username");
        //非必填 状态，0禁用，1启用，2冻结-默认启用状态
        String state=param.getString("state");
        //用户余额-单位分-关联查询用户及对应的账户信息
    JSONArray userJsonArr=userInfoDao.selectAllWithBalance(username,state);
      ResponseUtil.success(response,userJsonArr);
    }
}
