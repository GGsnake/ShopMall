package com.superman.superman.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by liujupeng on 2018/11/14.
 积分表
 */
@Data
public class ScoreBean  extends BaseBean implements Serializable {
    private Long userId;
    private Long score;
    private Integer dataSrc;
    private Integer dataId;
    private String dataRemarks;
    private Integer scoreType;
    private String day;
}
