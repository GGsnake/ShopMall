package com.superman.superman.dto;

import com.superman.superman.model.enums.Platform;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

@ApiModel
public class GoodsSearchResponse {

    private List<GoodDeatil> goodDeatilList;

    private Integer pageSize;

    private Integer pageNo;

    @ApiModelProperty("来源")
    private Platform platform;

    public static class GoodDeatil {
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

        public GoodDeatil setGoodName(String goodName) {
            this.goodName = goodName;
            return this;
        }

        public String getSort() {
            return sort;
        }

        public GoodDeatil setSort(String sort) {
            this.sort = sort;
            return this;
        }

        public Integer getHasCoupon() {
            return hasCoupon;
        }

        public GoodDeatil setHasCoupon(Integer hasCoupon) {
            this.hasCoupon = hasCoupon;
            return this;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public GoodDeatil setPrice(BigDecimal price) {
            this.price = price;
            return this;
        }
    }

    public List<GoodDeatil> getGoodDeatilList() {
        return goodDeatilList;
    }

    public GoodsSearchResponse setGoodDeatilList(List<GoodDeatil> goodDeatilList) {
        this.goodDeatilList = goodDeatilList;
        return this;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public GoodsSearchResponse setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
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
