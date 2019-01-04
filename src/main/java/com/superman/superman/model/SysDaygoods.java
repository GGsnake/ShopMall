package com.superman.superman.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author heguoliang
 * @Description: TODO(每日爆款)
 * @date 2019-01-04 20:47:50
 */
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
	private String contentImages;
	//删除状态
	private Integer status;
	//创建时间
	private Date createtime;

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
	 * 设置：标题
	 */
	public void setTitile(Long titile) {
		this.titile = titile;
	}
	/**
	 * 获取：标题
	 */
	public Long getTitile() {
		return titile;
	}
	/**
	 * 设置：内容
	 */
	public void setContent(Long content) {
		this.content = content;
	}
	/**
	 * 获取：内容
	 */
	public Long getContent() {
		return content;
	}
	/**
	 * 设置：头像
	 */
	public void setImage(String image) {
		this.image = image;
	}
	/**
	 * 获取：头像
	 */
	public String getImage() {
		return image;
	}
	/**
	 * 设置：图片内容url数组
	 */
	public void setContentImages(String contentImages) {
		this.contentImages = contentImages;
	}
	/**
	 * 获取：图片内容url数组
	 */
	public String getContentImages() {
		return contentImages;
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

}
