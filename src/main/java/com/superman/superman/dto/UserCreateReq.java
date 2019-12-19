package com.superman.superman.dto;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

public class UserCreateReq {
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^(((13[0-9])|(14[579])|(15([0-3]|[5-9]))|(16[6])|(17[0135678])|(18[0-9])|(19[89]))\\d{8})$", message = "手机号格式错误")
    private String phone;
    @NotBlank(message = "验证码不能为空")
    private String authCode;

    public String getPhone() {
        return phone;
    }

    public UserCreateReq setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getAuthCode() {
        return authCode;
    }

    public UserCreateReq setAuthCode(String authCode) {
        this.authCode = authCode;
        return this;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public UserCreateReq setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
        return this;
    }

    private String inviteCode;
}
