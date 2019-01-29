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
        dataJson.put("zk_price", Double.valueOf(dataObj.getZkFinalPrice())*100);
        dataJson.put("price", Double.valueOf(dataObj.getReservePrice())*100);
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
        long rate1 = new BigDecimal(rate).longValue();
        Double var3 = range / 100d;
        Double var4 = rate1 / 1000d;
        Double var5 = var0.doubleValue() * var4;
        Double var7 = var5 * var3;
        return new BigDecimal(var7).setScale(2, BigDecimal.ROUND_DOWN);
    }


}
