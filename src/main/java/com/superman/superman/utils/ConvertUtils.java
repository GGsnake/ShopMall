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

    /**
     * 京东    订单状态      15.待付款,16.已付款,17.已完成,18.已结算
     * 淘宝    订单状态      3：订单结算，12：订单付款， 13：订单失效，14：订单成功
     * 拼多多   订单状态      -1 未支付; 0-已支付；1-已成团；2-确认收货；3-审核成功；4-审核失败（不可提现）
     *                      5 -已经结算；8-非多多进宝商品（无佣金订单）
     *
     * @param devId  0 淘宝 1 天猫 2 京东 3 拼多多
     * @param status 0 全部 1 已付款 2 已结算
     * @return
     */
    public static List getStatus(Integer devId, Integer status) {
        List<Integer> statusList = new ArrayList<>();
        switch (status) {
            case 0:
                if (devId == 0) {
                    statusList.add(3);
                    statusList.add(12);
                    statusList.add(13);
                    statusList.add(14);
                }
                if (devId == 1) {
                    statusList.add(3);
                    statusList.add(12);
                    statusList.add(13);
                    statusList.add(14);
                }
                if (devId == 2) {
                    statusList.add(15);
                    statusList.add(16);
                    statusList.add(17);
                    statusList.add(18);
                }
                if (devId == 3) {
                    statusList.add(0);
                    statusList.add(1);
                    statusList.add(2);
                    statusList.add(3);
                    statusList.add(4);
                    statusList.add(5);
                }
                return statusList;
            case 1:
                //淘宝已付款
                if (devId == 0) {
                    statusList.add(12);
                    statusList.add(14);
                }
                //天猫已付款
                if (devId == 1) {
                    statusList.add(12);
                    statusList.add(14);
                }
                //京东已付款
                if (devId == 2) {
                    statusList.add(17);
                    statusList.add(16);
                }
                //拼多多已付款
                if (devId == 3) {
                    statusList.add(0);
                    statusList.add(1);
                    statusList.add(2);
                    statusList.add(3);
                }
                return statusList;
            case 2:
                //已结算
                if (devId == 0) {
                    statusList.add(3);
                }
                if (devId == 1) {
                    statusList.add(3);
                }
                if (devId == 2) {
                    statusList.add(18);
                }
                if (devId == 3) {
                    statusList.add(5);
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

    public static JSONObject convertOder(OderPdd oderPdd) {
        if (oderPdd == null) {
            return null;
        }
        JSONObject var = new JSONObject();
        var.put("img", oderPdd.getGoods_thumbnail_url());
        var.put("oderId", oderPdd.getOrder_sn());
        var.put("title", oderPdd.getGoods_name());
        var.put("time", oderPdd.getOrder_create_time());
        return var;
    }

//
//
//    public static JSONObject convertPddSearchForSdk(PddDdkGoodsSearchResponse.GoodsListItem o) {
//        JSONObject dataJson = new JSONObject();
//        if (o == null) {
//            return null;
//        }
//        dataJson.put("commissionRate", o.getPromotionRate());
//        dataJson.put("imgUrl", o.getGoodsImageUrl());
//        dataJson.put("volume", o.getSoldQuantity());
//        dataJson.put("goodName", o.getGoodsName());
//        dataJson.put("goodId", o.getGoodsId());
//        dataJson.put("istmall", "false");
//        return dataJson;
//    }


}
