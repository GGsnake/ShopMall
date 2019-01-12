package com.superman.superman.service;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.model.ScoreBean;
import com.superman.superman.req.JdSerachReq;
import io.swagger.models.Model;
import jd.union.open.goods.query.request.GoodsReq;

/**
 * Created by liujupeng on 2018/11/14.
 */
public interface JdApiService {
    ScoreBean queryJdOder(String id);
    JSONObject convertJd(Long jdpid,Long goodId);
    JSONObject serachGoodsAll(JdSerachReq jdSerachReq, Long uid);

    JSONObject serachGoodsAllJd(GoodsReq goodsReq, Long uid);

    /** 京东商品详情
     * 商品ID
     * @param goodId
     * @return
     */
    JSONObject jdDetail(Long goodId);

}
