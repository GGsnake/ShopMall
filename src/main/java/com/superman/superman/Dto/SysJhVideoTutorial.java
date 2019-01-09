package com.superman.superman.Dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author heguoliang
 * @Description: TODO(视频教程)
 * @date 2019-01-09 16:28:58
 */
public class SysJhVideoTutorial implements Serializable {

    //
    private Integer id;
    //视频URL地址
    private String video;
    //视频标题
    private String title;
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
     * 设置：视频URL地址
     */
    public void setVideo(String video) {
        this.video = video;
    }
    /**
     * 获取：视频URL地址
     */
    public String getVideo() {
        return video;
    }
    /**
     * 设置：视频标题
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * 获取：视频标题
     */
    public String getTitle() {
        return title;
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
