package com.superman.superman.service;

import com.alibaba.fastjson.JSONObject;
import jd.union.open.goods.query.request.GoodsReq;

/**
 * Created by liujupeng on 2018/11/14.
 */
public interface JdApiService {
    /**
     * 京东生成推广URL
     * @param pid
     * @param materialId
     * @return
     */
    JSONObject convertJd(String pid, String materialId);


    /**
     * 京东搜索引擎
     * @param goodsReq
     * @param uid
     * @return
     */
    JSONObject serachGoodsAllJd(GoodsReq goodsReq, Long uid);

    /** 京东商品详情
     * 商品ID
     * @param goodId
     * @return
     */
    JSONObject jdDetail(Long goodId);

}
