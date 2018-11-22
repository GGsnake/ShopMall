package com.superman.superman.model;

/**
 * @Auther: le
 * @Date: 2018/8/17 15:39
 * @Description:
 */
//Token的Model类，可以增加字段提高安全性，例如时间戳、url签名
public class TokenModel {
    private String userId;

    private String token;

    public TokenModel(String userId, String token){
        this.userId = userId;
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
