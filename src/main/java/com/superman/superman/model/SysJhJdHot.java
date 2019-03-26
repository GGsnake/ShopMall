package com.superman.superman.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author heguoliang
 * @Description: TODO(淘宝采集表)
 * @date 2019-02-26 15:57:13
 */
@Setter
@Getter
@ToString
public class SysJhJdHot implements Serializable  {
	
	//
	private Integer id;
	//商品图片
	private String picturl;
	//店铺名
	private String shoptitle;
	//标题
	private String title;
	//佣金比例
	private BigDecimal commissionrate;
	//优惠卷金额
	private Integer coupon;
	//推客的预估佣金额
	private BigDecimal zkfinalprice;
	//销量
	private Integer volume;
	//商品id
	private Long numiid;
	//删除状态
	private Integer status;
	//优惠卷权重
	private Integer orderCoupon;
	//优惠卷权重
	private Integer istamll;


	//佣金权重
	private Integer orderCommiss;

    //佣金权重
	private Integer comssion;
	//销量权重
	private Integer orderVolume;
	//创建时间
	private Date createtime;

	private String jdurl;

	private String link;


}
