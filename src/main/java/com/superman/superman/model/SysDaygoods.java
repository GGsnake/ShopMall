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
	private String titile;
	//内容
	private String content;
	//头像
	private String image;
	//删除状态
	private Integer status;
	//创建时间
	private Date createtime;


}
