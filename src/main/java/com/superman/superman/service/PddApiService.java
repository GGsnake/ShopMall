package com.superman.superman.service;

import com.alibaba.fastjson.JSONObject;
import com.pdd.pop.sdk.http.api.request.PddDdkGoodsSearchRequest;
import com.superman.superman.utils.PageParam;

/**
 * Created by liujupeng on 2018/11/6.
 */
public interface PddApiService {


    /**
     *生成推广链接
     * @param goodId  需要推广的商品id列表
     * @param pid 推广位ID
     * @return
     */
    JSONObject convertPdd(String pid, Long goodId);

    /** 拼多多商品详情
     * 商品ID
     * @param goodIdList
     * @return
     */
     JSONObject pddDetail(Long goodIdList);

     /**
     * 拼多多搜索引擎
     * @param request
     * @param uid
     * @return
     */
    JSONObject serachGoodsAll(PddDdkGoodsSearchRequest request, Long uid);
     /**
     * 拼多多本地搜索引擎
     * @param pageParam
     * @param uid
     * @return
     */
    JSONObject getLocalGoodsAll(PageParam  pageParam, Long uid);
}
