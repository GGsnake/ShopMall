package com.superman.superman.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ljp
 * @Description: TODO(提现申请表)
 * @date 2019-01-09 10:57:38
 */
@Data
public class ApplyCash extends BaseBean implements Serializable {
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
    //更新
    private String account;


    //更新
    private String name;


}
