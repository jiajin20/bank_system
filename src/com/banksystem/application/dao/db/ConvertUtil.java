package com.banksystem.application.dao.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class ConvertUtil {
    //定义了一个公开的，静态的，不可修改的字符串集里TIME_FORMAT,表示的是日期格式，具体格式为年-月-日 时：分：秒
    public static final String TIME_FORMAT="yyyy-MM-dd HH:mm:ss";
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT=new SimpleDateFormat(TIME_FORMAT);
    public static Instant stringToInstant(String timeString){
        //如果解析过程中出现异常（即timeString不符合预期的格式），则会捕获ParseException异常
        //并将其封装为一个运行时异常（RuntimeException）抛出
        //如果解析成功，它会将Date对象转换成Instant对象并返回
        Date date=null;
        try{
            date=SIMPLE_DATE_FORMAT.parse(timeString);
        }catch (ParseException e){
            throw new RuntimeException(e);
        }
       return date.toInstant();
    }
}
