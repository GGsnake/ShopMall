package com.superman.superman.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by liujupeng on 2018/11/5.
 */
public class User extends BaseBean implements Serializable {
    private String userName;//	用户名称
    private String loginName;//		账号
    private String loginPwd;//	密码
    private String loginSecret;//	安全码
    private String userSex;//	性别
    private String userPhone;//	手机
    private String userStatus;//		账号状态
    private String userScore;//	用户积分
    private String 	userTotalScore;//		用户历史消费积分

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", loginName='" + loginName + '\'' +
                ", loginPwd='" + loginPwd + '\'' +
                ", loginSecret='" + loginSecret + '\'' +
                ", userSex='" + userSex + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userStatus='" + userStatus + '\'' +
                ", userScore='" + userScore + '\'' +
                ", userTotalScore='" + userTotalScore + '\'' +
                ", lastTime=" + lastTime +
                ", userMoney=" + userMoney +
                ", lockMoney=" + lockMoney +
                ", distributMoney=" + distributMoney +
                ", payPwd='" + payPwd + '\'' +
                ", wxOpenId='" + wxOpenId + '\'' +
                ", openId='" + openId + '\'' +
                '}';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public String getLoginSecret() {
        return loginSecret;
    }

    public void setLoginSecret(String loginSecret) {
        this.loginSecret = loginSecret;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserScore() {
        return userScore;
    }

    public void setUserScore(String userScore) {
        this.userScore = userScore;
    }

    public String getUserTotalScore() {
        return userTotalScore;
    }

    public void setUserTotalScore(String userTotalScore) {
        this.userTotalScore = userTotalScore;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public Long getUserMoney() {
        return userMoney;
    }

    public void setUserMoney(Long userMoney) {
        this.userMoney = userMoney;
    }

    public Long getLockMoney() {
        return lockMoney;
    }

    public void setLockMoney(Long lockMoney) {
        this.lockMoney = lockMoney;
    }

    public Long getDistributMoney() {
        return distributMoney;
    }

    public void setDistributMoney(Long distributMoney) {
        this.distributMoney = distributMoney;
    }

    public String getPayPwd() {
        return payPwd;
    }

    public void setPayPwd(String payPwd) {
        this.payPwd = payPwd;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    private Date lastTime;//	最后登录时间
    private Long userMoney;//		帐号余额
    private Long lockMoney;//	冻结金额
    private Long distributMoney;//	佣金
    private String payPwd;//		支付密码
    private String wxOpenId;//		wxOpenId
    private String openId;//	openId
}
