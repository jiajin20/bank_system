package com.banksystem.application.dao.entity;

import java.time.Instant;

/*
* @Author: Iuxinghao
* @CreateTime: 2023/7/14 23:44
* @Description:
*/
public class AdminInfo {
    private Long id;//数据库 ID
    private String password;//密码
    private String nickname;//别名
    private String name;//姓名
    private String mobile;//手机号
    private Integer state;//状态，0禁用，1启用，2冻结
    private Boolean deleted;//是否删除，1是，2否
    private Long createBy;//创建者id，1系统
    private Long updateBy;//更新者id，1系统
    private Instant createTime;//创建时间
    private Instant updateTime;//更新时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id=id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password=password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname=nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile=mobile;
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
