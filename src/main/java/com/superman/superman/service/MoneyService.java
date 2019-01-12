package com.superman.superman.service;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.model.Userinfo;

import java.util.Map;

/**
 * Created by liujupeng on 2018/12/13.
 */
public interface MoneyService {
    /**
     * 获取已结算待结算
     *
     * @param uid
     * @param status 0未结算  1已结算
     * @return
     */
    Long queryCashMoney(Long uid, Integer status, Userinfo user);

    /**
     * 查询我的收益
     *
     * @param uid
     * @return
     */
    JSONObject queryMyIncome(Long uid);



}
