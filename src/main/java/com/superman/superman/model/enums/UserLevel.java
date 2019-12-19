package com.superman.superman.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum UserLevel {

    LOW(0, "普通用户");

    UserLevel(int code, String descp) {
        this.code = code;
        this.descp = descp;
    }

    @EnumValue
    private final int code;

    private final String descp;
}
