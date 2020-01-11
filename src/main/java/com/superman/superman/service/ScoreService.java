package com.superman.superman.service;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.model.ScoreBean;

/**
 * Created by snake on 2018/11/14.
 */
public interface ScoreService {
    /**
     * 查询当天积分
     * @param scoreBean
     * @return
     */
    Boolean isExitSign(ScoreBean scoreBean);
    /**
     * 查询当天积分
     * @param
     * @return
     */
    JSONObject myScore(Integer  uid);

    /**
     * 查询浏览商品次数
     * @param uid
     * @return
     */
    Long countLooks(Long uid);

    /**
     *查询浏览分享次数
     * @param uid
     * @return
     */
    Boolean isShare(Long uid);


}
