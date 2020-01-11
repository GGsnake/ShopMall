package com.superman.superman.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by snake on 2019/1/8.
 */
@Data
public class BannerGoods extends BaseBean {
    //商品id
    private Long goodId;
    //平台来源
    private Integer src;

    private String imgUrl;

}
