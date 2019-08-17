package com.superman.superman.model;

import lombok.Data;

import java.util.Date;

/**
 * Created by liujupeng on 2018/11/5.
 */
@Data
public class BaseBean {
    public Integer id;
    public Date createTime;
    public Date updateTime;
    public Byte status;
}
