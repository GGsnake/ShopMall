package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.model.Role;
import com.superman.superman.service.PddApiService;
import com.superman.superman.service.UserApiService;
import com.superman.superman.utils.EverySign;
import com.superman.superman.utils.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by liujupeng on 2018/11/6.
 */
@Service("pddServiceApi")
public class PddApiServiceImpl implements PddApiService {
    @Value("${pdd_pro.pdd-key}")
    private String KEY;
    @Value("${pdd_pro.pdd-secret}")
    private String SECRET;
    @Value("${pdd_pro.pdd-access_token}")
    private String ACCESS_TOKEN;
    @Value("${pdd_pro.pdd-router-url}")
    private String PDD_URL;
    @Value("${juanhuang.range}")
    private Integer RANGE;
    private final static Logger logger = LoggerFactory.getLogger(PddApiServiceImpl.class);

    @Autowired
    private UserApiService userApiService;

    //获得推广位订单明细
    @Override
    public String getBillList(String pid, Integer page, String pagesize) {
        String res = null;

        String type = "pdd.ddk.oauth.app.new.bill.list.get";
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        SortedMap<String, String> urlSign = new TreeMap<>();
        urlSign.put("client_id", KEY);
        urlSign.put("type", type);
        urlSign.put("timestamp", timestamp);
        urlSign.put("page", String.valueOf(page));
        if (pid != null) {
            urlSign.put("pid", pid);
        }
        urlSign.put("page_size", String.valueOf(pagesize));
        urlSign.put("access_token", ACCESS_TOKEN);
        urlSign.put("data_type", "JSON");
        SortedMap<String, String> mapsign = new TreeMap<>();
        mapsign.put("sign", EverySign.pddSign(urlSign, SECRET));
        try {
            res = HttpRequest.sendPost(PDD_URL, mapsign);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    //生成PID推广位
    @Override
    public String newBillSingle(Integer number) {
        String res = null;
        String type = "pdd.ddk.goods.pid.generate";
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        SortedMap<String, String> urlSign = new TreeMap<>();
        urlSign.put("client_id", KEY);
        urlSign.put("type", type);
        urlSign.put("timestamp", timestamp);
        urlSign.put("number", String.valueOf(number));
        urlSign.put("data_type", "JSON");
        urlSign.put("sign", EverySign.pddSign(urlSign, SECRET));
        try {
            res = HttpRequest.sendPost(PDD_URL, urlSign);
        } catch (IOException e) {
            e.printStackTrace();

        }
        //获取pid
        JSONObject o = (JSONObject) JSONObject.parseObject(res).getJSONObject("p_id_generate_response").getJSONArray("p_id_list").get(0);
        String p_id = o.getString("p_id");
        return p_id;
    }

    //生成推广链接
    @Override
    public String newPromotion(String pid, Long[] goodIdList) {
        String res = null;

        StringBuilder str = new StringBuilder();
        str.append("[");
        for (Long samp : goodIdList) {
            str.append(samp);
        }
        str.append("]");
        String type = "pdd.ddk.goods.promotion.url.generate";
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        SortedMap<String, String> urlSign = new TreeMap<>();
        urlSign.put("client_id", KEY);
        urlSign.put("type", type);
        urlSign.put("timestamp", timestamp);
        urlSign.put("p_id", pid);
        urlSign.put("goods_id_list", str.toString());
        urlSign.put("data_type", "JSON");
        urlSign.put("sign", EverySign.pddSign(urlSign, SECRET));
        //        urlSign.put("access_token", ACCESS_TOKEN);
        try {
            res = HttpRequest.sendPost("https://gw-api.pinduoduo.com/api/router", urlSign);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    //TODO 查询参数有待扩展
    @Override
    public String getPddGoodList(Long uid) {
        Double rang = RANGE / 100d;
        String res = null;
        String type = "pdd.ddk.goods.search";
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        SortedMap<String, String> urlSign = new TreeMap<>();
        urlSign.put("client_id", KEY);
        urlSign.put("type", type);
        urlSign.put("timestamp", timestamp);
        urlSign.put("sort_type", "0");
        urlSign.put("page", String.valueOf(1));
        urlSign.put("page_size", String.valueOf(10));
        urlSign.put("with_coupon", "true");
        urlSign.put("data_type", "JSON");
        urlSign.put("sign", EverySign.pddSign(urlSign, SECRET));
        try {
            res = HttpRequest.sendPost("https://gw-api.pinduoduo.com/api/router", urlSign);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Role role = new Role();
        role.setUserId(uid);
        Role role1 = userApiService.queryR(role);
        Float score = Float.valueOf(role1.getScore()) / 100;
        Float bonus = null;
        //佣金为0
        if (role1.getScore() == 0) {
            bonus = 0f;
        }
        //佣金为100代表总代理 90%返佣
        if (role1.getScore() == 100) {
            bonus = 1f;
        }
        bonus =score.floatValue();

        JSONArray jsonArray = JSONObject.parseObject(res).getJSONObject("goods_search_response").getJSONArray("goods_list");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject o = (JSONObject) jsonArray.get(i);
            Long promotion_rate = o.getLong("promotion_rate");
            Long min_group_price = o.getLong("min_group_price");
            Long coupon_discount = o.getLong("coupon_discount");
            //佣金计算
            Float after = Float.valueOf(min_group_price - coupon_discount);
            Float promoto = Float.valueOf(promotion_rate) / 1000;
            Float comssion = Float.valueOf(after * promoto);
            Integer rmb = (int) (comssion * rang);
            Float bondList = (rmb * bonus);
            o.put("bond",bondList);
        }
        return jsonArray.toJSONString();

    }


}
