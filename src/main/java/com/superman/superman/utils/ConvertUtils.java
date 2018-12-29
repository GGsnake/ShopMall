package com.superman.superman.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.req.OderPdd;

import java.util.ArrayList;
import java.util.List;

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


    }
