package com.superman.superman.req;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by liujupeng on 2018/12/20.
 */
@Setter
@Getter
public class TaoBaoSerachBean {
    private Integer page_size;
    private Integer page_no;
    private Boolean is_tmall;
    private String sort;
    private String cat;
    private String q;
    private String material_id;
    private Boolean has_coupon;
}
