package com.superman.superman.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author heguoliang
 * @Description: (常见问题)
 * @date 2019-01-09 16:28:57
 */
public class SysJhProblem implements Serializable {

    //
    private Integer id;
    //详情文章
    private String detail;
    //标题
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
     * 设置：详情文章
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }
    /**
     * 获取：详情文章
     */
    public String getDetail() {
        return detail;
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
