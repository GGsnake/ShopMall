package com.superman.superman.service;

import com.alibaba.fastjson.JSONObject;
import com.pdd.pop.sdk.http.api.request.PddDdkGoodsSearchRequest;
import com.superman.superman.req.PddSerachBean;
import com.taobao.api.request.TbkDgMaterialOptionalRequest;

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
     * 拼多多通用商品搜索
     * @param uid  用户id
     * @param pagesize
     * @param page

     * @return
     */
    JSONObject getPddGoodList(Long uid, Integer pagesize, Integer page, PddSerachBean pddSerachBean);
    /**
     * 拼多多搜索引擎
     * @param request
     * @param uid
     * @return
     */
    JSONObject serachGoodsAll(PddDdkGoodsSearchRequest request, Long uid);
}
