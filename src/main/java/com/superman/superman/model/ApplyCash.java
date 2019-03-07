package com.superman.superman.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ljp
 * @Description: TODO(提现申请表)
 * @date 2019-01-09 10:57:38
 */
public class ApplyCash implements Serializable {
    private Integer id;
    //用户id
    private Integer userid;
    //提现金额
    private Long money;
    //处理状态 0处理中 1提现完成 2提现失败
    private Integer audit;

    public Integer getRoleid() {
        return roleid;
    }

    public void setRoleid(Integer roleid) {
        this.roleid = roleid;
    }

    private Integer roleid;
    //删除状态
    private Integer status;
    //创建时间
    private Date createtime;
    //更新
    private Date updatetime;
    //更新
    private String account;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //更新
    private String name;

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
     * 设置：用户id
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    /**
     * 获取：用户id
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * 设置：提现金额
     */
    public void setMoney(Long money) {
        this.money = money;
    }

    /**
     * 获取：提现金额
     */
    public Long getMoney() {
        return money;
    }

    /**
     * 设置：处理状态 0处理中 1提现完成 2提现失败
     */
    public void setAudit(Integer audit) {
        this.audit = audit;
    }

    /**
     * 获取：处理状态 0处理中 1提现完成 2提现失败
     */
    public Integer getAudit() {
        return audit;
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

    /**
     * 设置：更新
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    /**
     * 获取：更新
     */
    public Date getUpdatetime() {
        return updatetime;
    }

}
