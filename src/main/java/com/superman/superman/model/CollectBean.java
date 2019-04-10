package com.superman.superman.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by liujupeng on 2018/11/20.
 */
@ToString
@Setter
@Getter
public class CollectBean  {
    private Integer id;
    private Long userId;
    private Long goodId;
    private String  title;
    private String  image;
    private Integer src;
    private Integer volume;
    private Double price;
    private Double coupon_price;
    private Long promotion_rate;
    private Long coupon;
    private Date createTime;
    private Date updateTime;

}
