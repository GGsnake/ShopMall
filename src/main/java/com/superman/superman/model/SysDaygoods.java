package com.superman.superman.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author heguoliang
 * @Description: TODO(每日爆款)
 * @date 2019-01-04 20:47:50
 */
@Setter
@ToString
@Getter
public class SysDaygoods implements Serializable {
	
	//
	private Integer id;
	//标题
	private Long titile;
	//内容
	private Long content;
	//头像
	private String image;
	//图片内容url数组
	private String content_Images;
	//删除状态
	private Integer status;
	//创建时间
	private Date createtime;


}
