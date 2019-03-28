package com.superman.superman.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by liujupeng on 2019/1/12.
 */
@Setter
@ToString
@Getter
public class SysFriendDto implements Serializable {
    private Integer id;
    //标题
    private String titile;
    //内容
    private String content;
    //头像
    private String image;
    //头像
    private String goodIds;
    //图片内容url数组
    private List<String> content_Images;
    //删除状态
    private Integer status;
    //创建时间
    private Date createtime;


}
