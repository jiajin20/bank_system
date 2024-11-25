package com.banksystem.application.web.vo;

import java.util.Date;

public class ResultVO<T> {
    private String code;
    private String message;
    private Boolean success;
    private T data;
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code=code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message=message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success=success;
    }
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data=data;
    }
   public ResultVO(){
   }
   public ResultVO(T data){
        this.setCode("200");
       this.setSuccess(Boolean.TRUE);
       this.setMessage(null);
       this.setData(data);
   }

}
