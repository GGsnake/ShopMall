package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.model.Userinfo;
import com.superman.superman.service.TaoBaoApiService;
import com.superman.superman.utils.HttpRequest;
import com.superman.superman.utils.NetUtils;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.NTbkItem;
import com.taobao.api.request.TbkDgItemCouponGetRequest;
import com.taobao.api.request.TbkDgMaterialOptionalRequest;
import com.taobao.api.request.TbkItemGetRequest;
import com.taobao.api.response.TbkDgItemCouponGetResponse;
import com.taobao.api.response.TbkDgMaterialOptionalResponse;
import com.taobao.api.response.TbkItemGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by liujupeng on 2018/12/4.
 */
@Service("taoBaoApiService")
public class TaoBaoApiServiceImpl implements TaoBaoApiService {
    final String URL = "http://tkapi.apptimes.cn/";
    final String TAOBAOURL = "http://gw.api.taobao.com/router/rest";
    final String APPKEY = "25377219";
    final String SECRET = "4c3e6b9e6484ce2982bca63d524564c1";
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    UserinfoMapper userinfoMapper;

    @Override
    public JSONObject serachGoods(Long uid, String Keywords, String cat, Boolean isTmall, Boolean HasCoupon, Long page_no, Long page_size, String sort, String itemloc) {
        Userinfo ufo = userinfoMapper.selectByPrimaryKey(uid);
        if (ufo == null) {
            return null;
        }
        TaobaoClient client = new DefaultTaobaoClient(TAOBAOURL, APPKEY, SECRET);
        TbkDgMaterialOptionalRequest req = new TbkDgMaterialOptionalRequest();

        req.setPageSize(page_size);
        req.setPageNo(page_no);
        req.setIsTmall(isTmall);
        req.setHasCoupon(HasCoupon);
        req.setSort(sort);
        req.setAdzoneId(71784050073l);
        req.setQ(Keywords);
        JSONObject data = new JSONObject();
        TbkDgMaterialOptionalResponse rsp = null;
        try {
            rsp = client.execute(req);
            JSONArray dataArray = new JSONArray();
            List<TbkDgMaterialOptionalResponse.MapData> resultList = rsp.getResultList();
            if (resultList == null || resultList.size() == 0) {
                return data;
            }
            Long count = rsp.getTotalResults();
            if (ufo.getRoleId() == 1) {
                for (int i = 0; i < resultList.size(); i++) {
                    TbkDgMaterialOptionalResponse.MapData dataObj = resultList.get(i);
                    JSONObject dataJson = new JSONObject();
                    String coupon_info1 = dataObj.getCouponInfo();
                    //查找指定字符第一次出现的位置
                    int star = coupon_info1.indexOf(20943);//参数为字符的ascii码
                    String coupon_info = coupon_info1.substring(star + 1, coupon_info1.length() - 1);
                    String commissionRate = dataObj.getCommissionRate();
                    dataJson.put("zk_price", dataObj.getZkFinalPrice());
                    dataJson.put("price", dataObj.getReservePrice());
                    dataJson.put("volume", dataObj.getVolume());
                    dataJson.put("goodId", dataObj.getNumIid());
                    dataJson.put("imgUrl", dataObj.getPictUrl());
                    dataJson.put("goodName", dataObj.getTitle());
                    dataJson.put("istmall", isTmall.toString());
                    dataJson.put("zk_money", Integer.parseInt(coupon_info));
                    dataJson.put("agent", 111l);
                    dataArray.add(dataJson);
                }
                data.put("data", dataArray);

                data.put("count", count);
            }
            if (ufo.getRoleId() == 2) {
                for (int i = 0; i < resultList.size(); i++) {
                    TbkDgMaterialOptionalResponse.MapData dataObj = resultList.get(i);
                    JSONObject dataJson = new JSONObject();
                    String coupon_info1 = dataObj.getCouponInfo();
                    //查找指定字符第一次出现的位置
                    int star = coupon_info1.indexOf(20943);//参数为字符的ascii码
                    String coupon_info = coupon_info1.substring(star + 1, coupon_info1.length() - 1);
                    String commissionRate = dataObj.getCommissionRate();
                    dataJson.put("zk_price", dataObj.getZkFinalPrice());
                    dataJson.put("price", dataObj.getReservePrice());
                    dataJson.put("volume", dataObj.getVolume());
                    dataJson.put("goodId", dataObj.getNumIid());
                    dataJson.put("imgUrl", dataObj.getPictUrl());
                    dataJson.put("goodName", dataObj.getTitle());
                    dataJson.put("istmall", isTmall.toString());
                    dataJson.put("zk_money", Integer.parseInt(coupon_info));
                    dataJson.put("agent", 111l);
                    dataArray.add(dataJson);
                }
                data.put("data", dataArray);

                data.put("count", count);
            }
            for (int i = 0; i < resultList.size(); i++) {
                TbkDgMaterialOptionalResponse.MapData dataObj = resultList.get(i);
                JSONObject dataJson = new JSONObject();
                String coupon_info1 = dataObj.getCouponInfo();
                //查找指定字符第一次出现的位置
                int star = coupon_info1.indexOf(20943);//参数为字符的ascii码
                String coupon_info = coupon_info1.substring(star + 1, coupon_info1.length() - 1);
                String commissionRate = dataObj.getCommissionRate();
                dataJson.put("zk_price", dataObj.getZkFinalPrice());
                dataJson.put("price", dataObj.getReservePrice());
                dataJson.put("volume", dataObj.getVolume());
                dataJson.put("goodId", dataObj.getNumIid());
                dataJson.put("imgUrl", dataObj.getPictUrl());
                dataJson.put("goodName", dataObj.getTitle());
                dataJson.put("istmall", isTmall.toString());
                dataJson.put("zk_money", Integer.parseInt(coupon_info));
                dataJson.put("agent", 0l);
                dataArray.add(dataJson);
            }
            data.put("data", dataArray);

            data.put("count", count);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return data;
    }

    public JSONObject indexGoodList(Integer type,Long page_no,Long page_size) {
        JSONObject data=new JSONObject();
        JSONArray dataArray = new JSONArray();
        TaobaoClient client = new DefaultTaobaoClient(TAOBAOURL, APPKEY, SECRET);
        TbkDgMaterialOptionalRequest req = new TbkDgMaterialOptionalRequest();
        req.setAdzoneId(71784050073L);
        if (type==1){
            req.setQ("9.9元包邮");
        }
        if (type==2){
            req.setQ("9.9元包邮");
        }
        if (type==3){
            req.setQ("9.9元包邮");
        }if (type==4){
            req.setQ("9.9元包邮");
        }


        req.setPageNo(page_no);
        req.setPageSize(page_size);
        try {
            TbkDgMaterialOptionalResponse response = client.execute(req);
            List<TbkDgMaterialOptionalResponse.MapData> results = response.getResultList();

            Long totalResults = response.getTotalResults();
            for (int i = 0; i < results.size(); i++) {
                TbkDgMaterialOptionalResponse.MapData dataObj = results.get(i);
                JSONObject dataJson = new JSONObject();
                String coupon_info1 = dataObj.getCouponInfo();
                //查找指定字符第一次出现的位置
                int star = coupon_info1.indexOf(20943);//参数为字符的ascii码
                String coupon_info = coupon_info1.substring(star + 1, coupon_info1.length() - 1);
                String commissionRate = dataObj.getCommissionRate();
                dataJson.put("zk_price", dataObj.getZkFinalPrice());
                dataJson.put("price", dataObj.getReservePrice());
                dataJson.put("volume", dataObj.getVolume());
                dataJson.put("goodId", dataObj.getNumIid());
                dataJson.put("imgUrl", dataObj.getPictUrl());
                dataJson.put("goodName", dataObj.getTitle());
                dataJson.put("istmall", null);
                dataJson.put("zk_money", Integer.parseInt(coupon_info));
                dataJson.put("agent", 0l);
                dataArray.add(dataJson);
            }
            data.put("data", dataArray);

            data.put("count", totalResults);

        } catch (ApiException e) {
            e.printStackTrace();
        }

        return data;
    }
}
