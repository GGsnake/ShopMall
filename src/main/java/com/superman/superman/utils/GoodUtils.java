package com.superman.superman.utils;

import com.alibaba.fastjson.JSONObject;
import com.taobao.api.response.TbkDgMaterialOptionalResponse;

import java.io.File;

/**
 * Created by liujupeng on 2018/12/18.
 */
public class GoodUtils {
    public static JSONObject convertTaobao(TbkDgMaterialOptionalResponse.MapData dataObj) {
        JSONObject dataJson = new JSONObject();
        dataJson.put("zk_price", dataObj.getZkFinalPrice());
        dataJson.put("price", dataObj.getReservePrice());
        dataJson.put("volume", dataObj.getVolume());
        dataJson.put("goodId", dataObj.getNumIid());
        dataJson.put("imgUrl", dataObj.getPictUrl());
        dataJson.put("goodName", dataObj.getTitle());
        return dataJson;
    }

}
