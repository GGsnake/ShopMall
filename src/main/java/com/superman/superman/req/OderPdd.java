package com.superman.superman.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by liujupeng on 2018/12/19.
 */
@Setter
@Getter
@ToString
public class OderPdd {
    private Long goods_id;
    private Long promotion_amount;
    private String goods_name;
    private Long order_create_time;
    private String goods_thumbnail_url;
    private String order_sn;
    private String p_id;

}
