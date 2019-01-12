package com.superman.superman.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author heguoliang
 * @Description: TODO(订单通知表)
 * @date 2019-01-11 16:28:45
 */
@Setter
@Getter
@ToString
public class SysJhAdviceOder implements Serializable {
	
	//
	private Integer id;
	//用户id
	private Integer userid;
	//订单编号
	private Long oderSn;
	//平台类型
	private Integer src;
	//平台名称
	private String src_Name;
	//订单标题
	private String name;
	//用户名
	private String userName;
	//推广位
	private String pid;
	//订单状态
	private Integer order_Status;
	//订单状态描述
	private String order_Status_Desc;
	//订单的创建时间
	private Date oder_Createtime;
	//创建时间
	private Date createtime;

}
