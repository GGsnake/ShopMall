package com.superman.superman.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by liujupeng on 2018/12/15.
 */
@Setter
@Getter
@ToString
public class Collect {
    private Integer volume;
    private String  image;
    private Long promotion_rate;
    private Long coupon;
    private Integer src;
    private Long price;
    private Long coupon_price;
    private Long goodId;
    private String  title;
    public Boolean param(){
        if (image==null||src==null||price==null||goodId==null||title==null){
            return false;
        }
        return true;
    }
}
