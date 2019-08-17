package com.superman.superman.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by liujupeng on 2018/11/24.
 */
@Data
public class UserLog extends BaseBean {
    private Integer userId;
    private Integer operation;
    private String ip;
}
