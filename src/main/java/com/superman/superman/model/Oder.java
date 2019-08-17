package com.superman.superman.model;

import lombok.Data;

import java.util.Date;

/**
 * 拼多多订单表
 */
@Data
public class Oder {

    private Long id;


    private String orderSn;


    private Integer goodsId;

    private String goodsName;


    private String goodsThumbnailUrl;


    private Integer goodsQuantity;

    private Integer goodsPrice;


    private Integer orderAmount;


    private String pId;


    private Long promotionRate;
    private Long promotionAmount;
    private Integer orderStatus;

    private String orderStatusDesc;

    private Long orderCreateTime;

    private Long orderPayTime;
    private Long orderGroupSuccessTime;

    private Long orderVerifyTime;


    private Long orderModifyAt;

    private Date updatetime;


    private Byte status;


}