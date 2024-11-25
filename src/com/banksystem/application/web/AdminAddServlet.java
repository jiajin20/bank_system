package com.banksystem.application.web;

import com.alibaba.fastjson.JSONObject;
import com.banksystem.application.dao.AdminInfoDao;
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

@WebServlet(urlPatterns="/admin/add")//配置子路由
public class AdminAddServlet extends HttpServlet {//继承HttpServlet实现网络接口
    public static final AdminInfoDao adminInfoDao=new AdminInfoDao();//管理员实例化

    //post请求对应的逻辑处理
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //参数获取
        JSONObject param=RequestUtil.getParam(request);
        String mobile=param.getString("mobile");//手机号
        String repassword=param.getString("repassword");//重复密码
        String password=param.getString("password");//密码
        String nickname=param.getString("nickname");//用户名
        String name=param.getString("name");//姓名
        //两次密码一致检查
        if (!Objects.equals(password, repassword)) {//如果不一致则返回异常错误提示信息
            ResponseUtil.fail(response, ErrorCode.REPEAT_PWD_NOT_SAME);
            return;
        }
        //检查是否有相同手机号管理员存在，存在则不允许添加
        AdminInfo adminInfo=adminInfoDao.selectByMobile(mobile);
        if (Objects.nonNull(adminInfo)) {
            ResponseUtil.fail(response, ErrorCode.ADMIN_MOBILE_SAME);
            return;
        }
        //封装实体属性值
        adminInfo=new AdminInfo();
        adminInfo.setPassword(password);//设置密码
        adminInfo.setNickname(nickname);//设置用户名
        adminInfo.setName(name);//设置姓名
        adminInfo.setMobile(mobile);//设置手机号
        adminInfoDao.addOne(adminInfo);//调用实例方法添加该管理员
        request.setAttribute("loginInfo", adminInfo);
        JSONObject adminInfoJson=new JSONObject();
        adminInfoJson.put("name", name);
        adminInfoJson.put("nickname", nickname);
        ResponseUtil.success(response, adminInfoJson);
    }

    //get请求对应的逻辑处理
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);//导向Post
    }
}

