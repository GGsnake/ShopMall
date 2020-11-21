package com.superman.superman.dto;

import com.superman.superman.model.enums.Platform;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

@ApiModel
public class GoodsSearchResponse {

    private List<GoodDetail> goodsDetailList;

    private Integer pageSize;

    private Integer pageNo;

    private Integer totalCount;

    @ApiModelProperty("来源")
    private Platform platform;


    public static class GoodDetail {
        @ApiModelProperty("商品名")
        private String goodName;
        @ApiModelProperty("排序")
        private String sort;
        @ApiModelProperty("是否优惠券")
        private Integer hasCoupon;
        @ApiModelProperty("售价")
        private BigDecimal price;

        public String getGoodName() {
            return goodName;
        }

        public GoodDetail setGoodName(String goodName) {
            this.goodName = goodName;
            return this;
        }

        public String getSort() {
            return sort;
        }

        public GoodDetail setSort(String sort) {
            this.sort = sort;
            return this;
        }

        public Integer getHasCoupon() {
            return hasCoupon;
        }

        public GoodDetail setHasCoupon(Integer hasCoupon) {
            this.hasCoupon = hasCoupon;
            return this;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public GoodDetail setPrice(BigDecimal price) {
            this.price = price;
            return this;
        }
    }

    public List<GoodDetail> getGoodsDetailList() {
        return goodsDetailList;
    }

    public GoodsSearchResponse setGoodsDetailList(List<GoodDetail> goodsDetailList) {
        this.goodsDetailList = goodsDetailList;
        return this;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public GoodsSearchResponse setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public GoodsSearchResponse setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
        return this;
    }
    public Integer getPageNo() {
        return pageNo;
    }

    public GoodsSearchResponse setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
        return this;
    }

    public Platform getPlatform() {
        return platform;
    }

    public GoodsSearchResponse setPlatform(Platform platform) {
        this.platform = platform;
        return this;
    }
}
