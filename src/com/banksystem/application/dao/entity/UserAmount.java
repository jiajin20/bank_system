package com.banksystem.application.dao.entity;

import java.time.Instant;

public class UserAmount {
    private Long id;
    private Long userId;//用户id
    private Long amount;//开始只额：”分“。因为要确保数据精确
    private Long balance;//余额
    private Integer state;//状态：0禁用，1正常
    private Boolean deleted;//是否删除，1是，0否
    private Long createBy;//创建者id,1系统
    private Long updateBy;//更新者id，1系统
    private Instant createTime;//创建时间
    private Instant updateTime;//更新时间
    private Long loanmoney;
    private Long loanmonth;
    //私有转共有

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id=id;
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

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance=balance;
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

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getLoanmoney() {
        return loanmoney;
    }

    public void setLoanmoney(Long loanmoney) {
        this.loanmoney = loanmoney;
    }

    public Long getLoanmonth() {
        return loanmonth;
    }

    public void setLoanmonth(Long loanmonth) {
        this.loanmonth = loanmonth;
    }
}
