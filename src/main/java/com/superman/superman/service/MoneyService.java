package com.superman.superman.service;

import com.superman.superman.model.Userinfo;

import java.util.Map;

/**
 * Created by liujupeng on 2018/12/13.
 */
public interface MoneyService {
    /**
     * 查询订单金额
     *
     * @param uid
     * @param status 0未结算  1已结算
     * @return
     */
    Long queryCashMoney(Long uid, Integer status, Userinfo user);



}
