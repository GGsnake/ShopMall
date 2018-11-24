package com.superman.superman.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by liujupeng on 2018/11/24.
 */
@ToString
@Setter
@Getter
public class UserLog {
    private Integer userId;
    private Integer operation;
    private String ip;
    private Date createTime;
}
