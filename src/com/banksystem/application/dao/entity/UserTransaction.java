package com.banksystem.application.dao.entity;

import java.time.Instant;

public class UserTransaction {
    private Long id;//用户id
    private Long parentId;
    private Long userId;//用户Id
    private Long amount;//金额 单位：
    private Integer transactionType;//操作类型，0取款，1存款，2转出，3转入
    private Long transactionBefore;//操作前余额，单位：分
    private  Long transactionAfter;//操作后余额，单位：分
    private Integer state;//状态：0禁用，1正常
    private Boolean deleted;//是否删除：1是，2否
    private Long createBy;//创建者id， 1系统
    private Long updateBy;//更新id ， 1系统
    private Instant createTime;//创建时间
    private Instant updateTime;//更新时间
    private String ip;
    private String city;
    private String region;


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id=id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId=parentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId=userId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount=amount;
    }

    public Integer getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Integer transactionType) {
        this.transactionType=transactionType;
    }

    public Long getTransactionBefore() {
        return transactionBefore;
    }

    public void setTransactionBefore(Long transactionBefore) {
        this.transactionBefore=transactionBefore;
    }

    public Long getTransactionAfter() {
        return transactionAfter;
    }

    public void setTransactionAfter(Long transactionAfter) {
        this.transactionAfter=transactionAfter;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state=state;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted=deleted;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy=createBy;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy=updateBy;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime=createTime;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime=updateTime;
    }
}
