package com.superman.superman.dto;

import lombok.Data;

import java.util.Date;
@Data
public class CollectBeanDto  {
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
    private Double agent;
}
