package com.superman.superman.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author heguoliang
 * @Description: TODO(淘宝采集表)
 * @date 2019-03-08 12:12:22
 */
public class SysJhTaobaoAll implements Serializable {
	
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
	//佣金
	private BigDecimal commission;
	//优惠卷金额
	private Integer coupon;
	//折后价
	private BigDecimal zkfinalprice;
	//券后价
	private BigDecimal couponprice;
	//销量
	private Integer volume;
	//商品id
	private Long numiid;
	//删除状态
	private Integer status;
	//1 上百券 2 聚划算 3 9.9包邮 4生活家居
	private Integer opt;
	//淘宝类目
	private Integer cat;
	//优惠卷权重
	private Integer orderCoupon;
	//佣金权重
	private Integer orderCommiss;
	//销量权重
	private Integer orderVolume;
	//创建时间
	private Date createtime;
	//
	private Integer istamll;

	/**
	 * 设置：
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：商品图片
	 */
	public void setPicturl(String picturl) {
		this.picturl = picturl;
	}
	/**
	 * 获取：商品图片
	 */
	public String getPicturl() {
		return picturl;
	}
	/**
	 * 设置：店铺名
	 */
	public void setShoptitle(String shoptitle) {
		this.shoptitle = shoptitle;
	}
	/**
	 * 获取：店铺名
	 */
	public String getShoptitle() {
		return shoptitle;
	}
	/**
	 * 设置：标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * 获取：标题
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * 设置：佣金比例
	 */
	public void setCommissionrate(BigDecimal commissionrate) {
		this.commissionrate = commissionrate;
	}
	/**
	 * 获取：佣金比例
	 */
	public BigDecimal getCommissionrate() {
		return commissionrate;
	}
	/**
	 * 设置：佣金
	 */
	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}
	/**
	 * 获取：佣金
	 */
	public BigDecimal getCommission() {
		return commission;
	}
	/**
	 * 设置：优惠卷金额
	 */
	public void setCoupon(Integer coupon) {
		this.coupon = coupon;
	}
	/**
	 * 获取：优惠卷金额
	 */
	public Integer getCoupon() {
		return coupon;
	}
	/**
	 * 设置：折后价
	 */
	public void setZkfinalprice(BigDecimal zkfinalprice) {
		this.zkfinalprice = zkfinalprice;
	}
	/**
	 * 获取：折后价
	 */
	public BigDecimal getZkfinalprice() {
		return zkfinalprice;
	}
	/**
	 * 设置：券后价
	 */
	public void setCouponprice(BigDecimal couponprice) {
		this.couponprice = couponprice;
	}
	/**
	 * 获取：券后价
	 */
	public BigDecimal getCouponprice() {
		return couponprice;
	}
	/**
	 * 设置：销量
	 */
	public void setVolume(Integer volume) {
		this.volume = volume;
	}
	/**
	 * 获取：销量
	 */
	public Integer getVolume() {
		return volume;
	}
	/**
	 * 设置：商品id
	 */
	public void setNumiid(Long numiid) {
		this.numiid = numiid;
	}
	/**
	 * 获取：商品id
	 */
	public Long getNumiid() {
		return numiid;
	}
	/**
	 * 设置：删除状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：删除状态
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：1 上百券 2 聚划算 3 9.9包邮 4生活家居
	 */
	public void setOpt(Integer opt) {
		this.opt = opt;
	}
	/**
	 * 获取：1 上百券 2 聚划算 3 9.9包邮 4生活家居
	 */
	public Integer getOpt() {
		return opt;
	}
	/**
	 * 设置：淘宝类目
	 */
	public void setCat(Integer cat) {
		this.cat = cat;
	}
	/**
	 * 获取：淘宝类目
	 */
	public Integer getCat() {
		return cat;
	}
	/**
	 * 设置：优惠卷权重
	 */
	public void setOrderCoupon(Integer orderCoupon) {
		this.orderCoupon = orderCoupon;
	}
	/**
	 * 获取：优惠卷权重
	 */
	public Integer getOrderCoupon() {
		return orderCoupon;
	}
	/**
	 * 设置：佣金权重
	 */
	public void setOrderCommiss(Integer orderCommiss) {
		this.orderCommiss = orderCommiss;
	}
	/**
	 * 获取：佣金权重
	 */
	public Integer getOrderCommiss() {
		return orderCommiss;
	}
	/**
	 * 设置：销量权重
	 */
	public void setOrderVolume(Integer orderVolume) {
		this.orderVolume = orderVolume;
	}
	/**
	 * 获取：销量权重
	 */
	public Integer getOrderVolume() {
		return orderVolume;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreatetime() {
		return createtime;
	}
	/**
	 * 设置：
	 */
	public void setIstamll(Integer istamll) {
		this.istamll = istamll;
	}
	/**
	 * 获取：
	 */
	public Integer getIstamll() {
		return istamll;
	}

}
