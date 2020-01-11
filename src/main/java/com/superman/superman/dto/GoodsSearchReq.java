package com.superman.superman.dto;

import com.superman.superman.model.enums.Platform;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ApiModel
public class GoodsSearchReq {
    @ApiModelProperty("关键词")
    private String keyword;
    @ApiModelProperty("排序")
    private String sort;
    @ApiModelProperty("是否优惠券")
    private String hasCoupon;
    @NotNull
    private Integer pageSize;
    @NotNull
    private Integer pageNo;
    @NotNull
    @NotBlank
    @ApiModelProperty("来源")
    private Platform platform;

    private String tbcat;

    private Integer cid;

    private Long opt;

    public String getTbcat() {
        return tbcat;
    }

    public GoodsSearchReq setTbcat(String tbcat) {
        this.tbcat = tbcat;
        return this;
    }

    public Integer getCid() {
        return cid;
    }

    public GoodsSearchReq setCid(Integer cid) {
        this.cid = cid;
        return this;
    }

    public Long getOpt() {
        return opt;
    }

    public GoodsSearchReq setOpt(Long opt) {
        this.opt = opt;
        return this;
    }

    public Platform getPlatform() {
        return platform;
    }

    public GoodsSearchReq setPlatform(Platform platform) {
        this.platform = platform;
        return this;
    }

    public String getKeyword() {
        return keyword;
    }

    public GoodsSearchReq setKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }

    public String getSort() {
        return sort;
    }

    public GoodsSearchReq setSort(String sort) {
        this.sort = sort;
        return this;
    }

    public String getHasCoupon() {
        return hasCoupon;
    }

    public GoodsSearchReq setHasCoupon(String hasCoupon) {
        this.hasCoupon = hasCoupon;
        return this;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public GoodsSearchReq setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public GoodsSearchReq setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
        return this;
    }
}
