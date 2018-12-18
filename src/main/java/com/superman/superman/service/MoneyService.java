package com.superman.superman.service;

import java.util.Map;

/**
 * Created by liujupeng on 2018/12/13.
 */
public interface MoneyService {
    /**
     * 查询未结算
     * @param uid
     * @return
     */
     Long queryWaitMoney(Long uid);

    /**
     * 查询已结算
     * @param uid
     * @return
     */
    Long queryFinishMoney(Long uid);


    /**
     * 查询单个已结算未结算（所有平台）
     * @return
     */
    Long queryAllFinishSimple(Map map,Integer flag);


    }
