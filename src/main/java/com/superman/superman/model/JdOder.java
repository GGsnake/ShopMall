package com.superman.superman.model;

import lombok.*;

import java.util.Date;
/**
 * 京东订单表
 */
@Data
public class JdOder{
    private Integer id;
    private String positionId;
    private Double actualCosPrice;
    private Double actualFee;
    private Long commissionRate;
    private Double estimateCosPrice;
    private Double estimateFee;
    private Double finalRate;
    private Double price;
    private Long skuId;
    private String skuName;
    private Long orderId;
    private Long payMonth;
    private Long finishTime;
    private Long orderTime;
    private Integer validCode;
    private Date createTime;
    private Date updateTime;
    private Integer settle;
}
