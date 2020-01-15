package com.superman.superman.dto;

import com.superman.superman.model.enums.Platform;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

@ApiModel
public class GoodsDetailResponse {
    @ApiModelProperty("来源")
    private Platform platform;
    @ApiModelProperty("商品名")
    private String goodName;
    @ApiModelProperty("排序")
    private String sort;
    @ApiModelProperty("是否优惠券")
    private Integer hasCoupon;
    @ApiModelProperty("售价")
    private BigDecimal price;

    public Platform getPlatform() {
        return platform;
    }

    public GoodsDetailResponse setPlatform(Platform platform) {
        this.platform = platform;
        return this;
    }

    public String getGoodName() {
        return goodName;
    }

    public GoodsDetailResponse setGoodName(String goodName) {
        this.goodName = goodName;
        return this;
    }

    public String getSort() {
        return sort;
    }

    public GoodsDetailResponse setSort(String sort) {
        this.sort = sort;
        return this;
    }

    public Integer getHasCoupon() {
        return hasCoupon;
    }

    public GoodsDetailResponse setHasCoupon(Integer hasCoupon) {
        this.hasCoupon = hasCoupon;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public GoodsDetailResponse setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }
}
