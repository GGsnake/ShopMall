package com.superman.superman.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by liujupeng on 2018/12/25.
 */
@Setter
@Getter
@ToString
public class JdSerachReq {

    private String keyword;
    private ArrayList skuidlist;
    private Integer priceto;
    private Integer pricefrom;
    private Integer cid3;
    private Integer pagesize ;
    private Integer page ;
}
