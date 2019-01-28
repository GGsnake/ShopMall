package com.superman.superman.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author heguoliang
 * @Description: TODO(通知表)
 * @date 2018-12-22 18:05:59
 */
public class SysAdvice implements Serializable {
	
	//
	private Integer id;
	//标题
	private Integer title;
	//内容
	private Long content;
	//头像
	private String img;
	//来
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
	public void setTitle(Integer title) {
		this.title = title;
	}
	/**
	 * 获取：标题
	 */
	public Integer getTitle() {
		return title;
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
	public void setImg(String img) {
		this.img = img;
	}
	/**
	 * 获取：头像
	 */
	public String getImg() {
		return img;
	}
	/**
	 * 设置：来
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：来
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
