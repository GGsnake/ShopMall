package com.superman.superman.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by liujupeng on 2019/1/8.
 */
@ToString
@Setter
@Getter
public class SysJhAdviceDev {
    //
    private Integer id;
    //标题
    private String titile;
    //内容
    private String content;
    //头像
    private String image;
    //图片URL
    private String contentImage;
    //创建时间
    private Date createtime;
    //修改时间

    
    private Date updatetime;
}
