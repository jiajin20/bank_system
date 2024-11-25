package com.banksystem.application.dao.entity;

import java.time.Instant;

public class UserInfo {
    private Long id;//用户id
    private String cardType;//卡类型：1储蓄卡，2信用卡，3社保卡
    private String cardNo;//卡号
    private String password;//账户密码
    private String nickname;//用户名
    private String name;//姓名
    private String address;//住址
    private String idNum;//身份证号
    private String mobile;//手机号
    private Integer state;//卡状态：0禁用，1启用，2冻结
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

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType=cardType;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo=cardNo;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address=address;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum=idNum;
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
