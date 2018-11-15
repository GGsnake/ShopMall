package com.superman.superman.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by liujupeng on 2018/11/6.
 */
public interface PddApiService {

    /**
     * 获得推广位订单明细
     * @param pid 拼多多推广位ID
     * @param page
     * @param pagesize
     * @return
     */
    String getBillList(String pid, Integer page, String pagesize);

    /**
     * 生成拼多多推广位
     * @param number 需要生成的pid推广位数量
     * @return
     */
    String newBillSingle(Integer number);

    /**
     *生成推广链接
     * @param pid 推广位ID
     * @param goodIdList  需要推广的商品id列表
     * @return
     */
    String newPromotion(String pid,Long[] goodIdList);

    /** 拼多多商品详情
     * 商品ID
     * @param goodIdList
     * @return
     */
     JSONObject pddDetail(Long goodIdList, String uid);

    /**
     * 拼多多通用商品搜索
     * @param uid  用户id
     * @param pagesize
     * @param page
     * @param sort_type
     * @param with_coupon
     * @param keyword
     * @param opt_id
     * @return
     */
    JSONArray getPddGoodList(Long uid, Integer pagesize, Integer page, Integer sort_type, Boolean with_coupon, String keyword, Long opt_id,Integer merchant_type);

}
