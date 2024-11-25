package com.banksystem.application.web.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.banksystem.application.web.constant.ErrorCode;
import com.banksystem.application.web.vo.ResultVO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponseUtil {
    //请求成功的响应体
    public static <T> void success(HttpServletResponse response, JSON jsonObject){
        PrintWriter out=null;
        try{
          response.setContentType("application/json;charset=utf-8");
            ResultVO<Object> resp=new ResultVO<>(jsonObject);
            //json返回
            out=response.getWriter();
            out.write(JSONObject.toJSONString(resp, SerializerFeature.WriteMapNullValue));//响应体写入json对象
            out.flush();
            out.close();
        }catch (IOException e){//遇到错误则抛出异常
            throw new RuntimeException(e);
        }
    }
    //请求失败的响应体
    public static void fail(HttpServletResponse response, ErrorCode errorCode){
        PrintWriter out=null;
        try{
            //封装错误信息
            response.setContentType("application/json;charset=utf-8");
            ResultVO<Object> resp=new ResultVO<>();
            resp.setSuccess(false);
            resp.setCode(errorCode.getCode());
            resp.setMessage(errorCode.getMessage());
            //json返回
            out=response.getWriter();
            out.write(JSONObject.toJSONString(resp));//响应体写入json对象
            out.flush();
            out.close();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
