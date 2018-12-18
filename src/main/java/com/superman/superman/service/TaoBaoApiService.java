package com.superman.superman.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;


/**
 * Created by liujupeng on 2018/12/4.
 */
public interface TaoBaoApiService {
    JSONObject serachGoods(Long uid,String Keywords, String cat, Boolean isTmall,  Boolean HasCoupon, Long page_no, Long page_size, String sort, String itemloc);
    JSONObject indexGoodList(Integer type,Long page_no,Long page_size);

    /**
     * 生成淘口令推广链接
     * @param pid
     * @param good_id
     * @return
     */
    JSONObject convertTaobao(Long pid,Long good_id);
    /**
     * 统计订单

     * @return
     */
    Long countWaitTb(List list);
}
