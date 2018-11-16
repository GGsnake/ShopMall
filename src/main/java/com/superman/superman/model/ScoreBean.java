package com.superman.superman.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by liujupeng on 2018/11/14.

 */
@Setter
@Getter
@ToString
public class ScoreBean  extends BaseBean implements Serializable {
    private Long userId;
    private Long score;
    private Integer dataSrc;
    private Integer dataId;
    private String dataRemarks;
    private Integer scoreType;
}
