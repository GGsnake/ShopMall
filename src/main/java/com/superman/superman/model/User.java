package com.superman.superman.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by liujupeng on 2018/11/5.
 */
@Setter
@Getter
@ToString
public class User extends BaseBean implements Serializable {
    private String userName;//	用户名称
    private String loginName;//		账号
    private String loginPwd;//	密码
    private String loginSecret;//	安全码
    private String userSex;//	性别
    private String userPhone;//	手机
    private String userStatus;//		账号状态
    private Integer userScore;//	用户积分
    private String 	userTotalScore;//		用户历史消费积分


    private Date lastTime;//	最后登录时间
    private Long userMoney;//		帐号余额
    private Long lockMoney;//	冻结金额
    private Long distributMoney;//	佣金
    private String payPwd;//		支付密码
    private String wxOpenId;//		wxOpenId
    private String openId;//	openId
}
