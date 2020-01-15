package com.superman.superman.dto;

import com.superman.superman.model.enums.Platform;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@ApiModel
public class GoodsDetailReq {
    @ApiModelProperty("商品id")
    private String goodId;
    @ApiModelProperty("来源")
    private Platform platform;

    public String getGoodId() {
        return goodId;
    }

    public GoodsDetailReq setGoodId(String goodId) {
        this.goodId = goodId;
        return this;
    }

    public Platform getPlatform() {
        return platform;
    }

    public GoodsDetailReq setPlatform(Platform platform) {
        this.platform = platform;
        return this;
    }
}
