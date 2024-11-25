package com.banksystem.application.dao.entity;

import java.time.Instant;

public class SystemLog {
    private Long id;
    private Integer type;//类型：0登出，1登入
    private Integer userType;//用户类型：0管理员，1用户
    private Long userId;//用户id
    private Integer state;//状态，0禁用，1正常
    private Boolean deleted;//逻辑删除：1是，2否
    private Long createBy;//创建者id， 1管理员
    private Long updateBy;//更新id ， 1管理员
    private Instant createTime;//创建时间
    private Instant updateTime;//更新时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id=id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type=type;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType=userType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId=userId;
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
