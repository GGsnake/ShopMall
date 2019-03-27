package com.superman.superman.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by liujupeng on 2019/1/8.
 */
@ToString
@Setter
@Getter
public class BannerGoods {
    //
    private Integer id;
    //商品id
    private Long goodId;

    private Integer status;
    //平台来源
    private Integer src;

    //创建时间
    private Date createtime;
}
