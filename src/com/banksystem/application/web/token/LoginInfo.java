package com.banksystem.application.web.token;

import com.banksystem.application.dao.entity.AdminInfo;
import com.banksystem.application.dao.entity.UserInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LoginInfo {
    /**
     * 管理员登录信息
     * <mobile:AdminInfo>
     */
    public static Map<String, AdminInfo> ADMIN_LOGIN_MAP=new HashMap<>();
    /**
     * 用户登录信息
     * <mobile:AdminInfo>
     */
    public static Map<String, UserInfo> USER_LOGIN_MAP=new HashMap<>();
    public static String getRandomString(int length){
        String base="abcdefghijklmnopqrstuvwxyz0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for (int i=0;i<length;i++){
            int number =random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();//将StringBuffer对象转化成String对象，并返回这个随机生成的字符串。
    }
}
