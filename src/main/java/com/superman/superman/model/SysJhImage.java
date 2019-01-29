package com.superman.superman.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by liujupeng on 2019/1/12.
 */
@Setter
@Getter
@ToString
public class SysJhImage {
    private Integer id;
    private Integer day;
    private String image;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}
