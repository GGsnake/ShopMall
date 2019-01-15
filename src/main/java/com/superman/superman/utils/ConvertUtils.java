package com.superman.superman.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pdd.pop.sdk.http.api.response.PddDdkGoodsSearchResponse;
import com.superman.superman.req.OderPdd;

import java.util.ArrayList;
import java.util.List;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.o;

/**
 * Created by liujupeng on 2018/12/19.
 */
public class ConvertUtils {
    public static List getStatus(Integer devId, Integer status) {
        List<Integer> statusList = new ArrayList<>();
        switch (status) {
            case 0:
                if (devId == 0) {
                    statusList.add(3);
                    statusList.add(12);
                    statusList.add(14);
                }
                if (devId == 1) {

                    statusList.add(1);
                    statusList.add(2);
                    statusList.add(3);
                    statusList.add(5);
                }
                if (devId == 2) {

                }
                return statusList;
            case 1:
                if (devId == 0) {
                    statusList.add(1);
                    statusList.add(2);
                    statusList.add(3);

                }
                if (devId == 1) {
                    statusList.add(12);
                    statusList.add(14);

                }
                if (devId == 2) {

                }
                return statusList;
            case 2:
                if (devId == 0) {
                    statusList.add(5);
                }
                if (devId == 1) {
                    statusList.add(3);

                }
                if (devId == 2) {

                }
            case 3:
                if (devId == 0) {

                }
                if (devId == 1) {

                }
                if (devId == 2) {

                }
                return statusList;

        }

        return statusList;
    }

    public static JSONObject convertOder(OderPdd  oderPdd) {
        if (oderPdd==null){
            return null;
        }
        JSONObject var=new JSONObject();
        var.put("img", oderPdd.getGoods_thumbnail_url());
        var.put("oderId", oderPdd.getOrder_sn());
        var.put("title", oderPdd.getGoods_name());
        var.put("time", oderPdd.getOrder_create_time());
        return var;
    }

    public static JSONObject convertPddSearch(JSONObject o) {
        JSONObject dataJson=new JSONObject();
        if (o==null){
            return null;
        }
        dataJson.put("commissionRate", o.getString("promotion_rate"));
        dataJson.put("imgUrl", o.getString("goods_image_url"));
        dataJson.put("volume", o.getInteger("sold_quantity"));
        dataJson.put("goodName", o.getString("goods_name"));
        dataJson.put("goodId", o.getLong("goods_id"));
        dataJson.put("istmall", "false");

        return dataJson;
    }
    public static JSONObject convertPddSearchForSdk(PddDdkGoodsSearchResponse.GoodsListItem o) {
        JSONObject dataJson=new JSONObject();
        if (o==null){
            return null;
        }
        dataJson.put("commissionRate", o.getPromotionRate());
        dataJson.put("imgUrl", o.getGoodsImageUrl());
        dataJson.put("volume", o.getSoldQuantity());
        dataJson.put("goodName", o.getGoodsName());
        dataJson.put("goodId", o.getGoodsId());
        dataJson.put("istmall", "false");
        return dataJson;
    }


    }
