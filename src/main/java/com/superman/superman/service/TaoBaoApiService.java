package com.superman.superman.service;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.req.OptReq;
import com.superman.superman.utils.PageParam;
import com.taobao.api.request.TbkDgMaterialOptionalRequest;
import com.taobao.api.request.TbkDgOptimusMaterialRequest;

import java.util.List;


/**
 * Created by liujupeng on 2018/12/4.
 */
public interface TaoBaoApiService {
    /**
     * 淘宝搜索引擎
     *
     * @param request
     * @param uid
     * @return
     */
    JSONObject serachGoodsAll(TbkDgMaterialOptionalRequest request, Long uid);

    /**
     * 本地淘宝搜索引擎加强版
     *
     * @return
     */
    JSONObject goodLocalSuperForOpt(JSONObject pageParam,Long uid, Integer status);


    /**
     * 查询淘宝商品单个的缩略图
     *
     * @param goodId
     * @return
     */
    String deatilGoodList(Long goodId);

    /**
     * 生成淘口令推广链接
     *
     * @param pid
     * @param good_id
     * @return
     */
    JSONObject convertTaobao(Long pid, Long good_id);


    /**
     * 解析淘口令
     *
     * @param tkl
     * @return
     */
    JSONObject convertTaobaoTkl(String tkl);


    /**
     * 查询淘宝的商品详情
     *
     * @return
     */
    JSONObject deatil(Long goodId);

}
