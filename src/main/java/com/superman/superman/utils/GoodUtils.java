package com.superman.superman.utils;

import com.alibaba.fastjson.JSONObject;
import com.taobao.api.response.TbkDgMaterialOptionalResponse;

import java.io.File;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Iterator;

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
    public static JSONObject convertPdd(TbkDgMaterialOptionalResponse.MapData dataObj) {
        JSONObject dataJson = new JSONObject();
        dataJson.put("zk_price", dataObj.getZkFinalPrice());
        dataJson.put("price", dataObj.getReservePrice());
        dataJson.put("volume", dataObj.getVolume());
        dataJson.put("goodId", dataObj.getNumIid());
        dataJson.put("imgUrl", dataObj.getPictUrl());
        dataJson.put("goodName", dataObj.getTitle());
        return dataJson;
    }

    public static BigDecimal commissonAritTaobao(String zk, String rate, Double range) {
        BigDecimal var0 = new BigDecimal(zk);
        BigDecimal var1 = new BigDecimal(rate).divide(new BigDecimal(1000), 2, BigDecimal.ROUND_UNNECESSARY);
        BigDecimal var2 = var0.multiply(var1);
        Double var3 = range / 100;
        return var2.multiply(new BigDecimal(var3));
    }


}
