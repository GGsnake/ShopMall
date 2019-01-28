package com.superman.superman.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by liujupeng on 2018/12/19.
 */
@Setter
@Getter
@ToString
public class InvCode {
    private Integer id;
    private Integer userId;
    private Date createTime;
}
