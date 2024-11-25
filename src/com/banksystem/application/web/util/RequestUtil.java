package com.banksystem.application.web.util;

import com.alibaba.fastjson.JSONObject;
import com.banksystem.application.dao.entity.AdminInfo;
import com.banksystem.application.dao.entity.UserInfo;
import com.banksystem.application.web.token.LoginInfo;
import javax.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class RequestUtil {
    public static JSONObject getParam(HttpServletRequest request) throws IOException{
        BufferedReader streamReader=new BufferedReader(new InputStreamReader(request.getInputStream (),
                StandardCharsets.UTF_8));
        StringBuilder responseStrBuilder=new StringBuilder();
        String inputStr;
        while ((inputStr=streamReader.readLine())!=null){
            responseStrBuilder.append(inputStr);
        }
        return JSONObject.parseObject(responseStrBuilder.toString(),JSONObject.class);
    }
    /**
     * 管理员登录
     */
    public static AdminInfo checkAdminLogin(HttpServletRequest request){
        String token=getTokenFromRequestHeader(request);
        return LoginInfo.ADMIN_LOGIN_MAP.get(token);
    }
    /**
     * 检查用户登录
     *@param request
     * @return
     */
    public static UserInfo checkUserLogin(HttpServletRequest request){
        String token=getTokenFromRequestHeader(request);
        UserInfo userInfo=LoginInfo.USER_LOGIN_MAP.get(token);
        return userInfo;
    }
    public static String getTokenFromRequestHeader(HttpServletRequest request){
        return request.getHeader("token");
    }
}
