package com.superman.superman.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class OptReq {
    private String keyword;
    private String cat;
    private String field;
    private String sort;
    private String startprice;
    private String endprice;
    private String itemid;
    private String itemtype;
    private String start_time;
    private String page;
    private String pagesize;
    private String end_time;
}
