package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.model.Userinfo;
import com.superman.superman.service.PddApiService;
import com.superman.superman.service.UserApiService;
import com.superman.superman.utils.EverySign;
import com.superman.superman.utils.EveryUtils;
import com.superman.superman.utils.HttpRequest;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by liujupeng on 2018/11/6.
 */

@Log
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
    @Autowired
    private UserApiService userApiService;
    @Autowired
    private UserinfoMapper userinfoMapper;

    //获得推广位订单明细
    @Override
    public String getBillList(String pid, Integer page, String pagesize) {
        String res = null;

        log.info("sssssssssssssssssss");
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
    public JSONObject convertPdd(@NonNull String pid, @NonNull Long goodId) {
        String res = null;
        JSONObject pddTemp = new JSONObject();

        StringBuilder str = new StringBuilder();
        str.append("[").append(goodId).append("]");
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
            log.warning(e.getMessage());
            return pddTemp;
        }
        JSONArray temp = JSONObject.parseObject(res).getJSONObject("goods_promotion_url_generate_response").getJSONArray("goods_promotion_url_list");
        if (temp == null || temp.size() == 0) {
            return pddTemp;
        }
        JSONObject tempJson = (JSONObject) temp.get(0);
        pddTemp.put("uland_url", tempJson.getString("mobile_short_url"));
        pddTemp.put("url", tempJson.getString("url"));
        pddTemp.put("tkLink", null);
        return pddTemp;
    }

    //查询拼多多商品详情
    @Override
    public JSONObject pddDetail(Long goodIdList) {
        Double rang = RANGE / 100d;
        String res = null;
        StringBuilder str = new StringBuilder();
        str.append("[" + goodIdList);
        str.append("]");
        String type = "pdd.ddk.goods.detail";
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        SortedMap<String, String> urlSign = new TreeMap<>();
        urlSign.put("client_id", KEY);
        urlSign.put("type", type);
        urlSign.put("timestamp", timestamp);
        urlSign.put("goods_id_list", str.toString());
        urlSign.put("data_type", "JSON");
        urlSign.put("sign", EverySign.pddSign(urlSign, SECRET));
        //        urlSign.put("access_token", ACCESS_TOKEN);
        try {
            res = HttpRequest.sendPost("https://gw-api.pinduoduo.com/api/router", urlSign);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject temp=new JSONObject();
        JSONArray var1=new JSONArray();
        if (JSONObject.parseObject(res).getJSONObject("error_response")!=null) {
            temp.put("list",var1);
            return temp;
        }
            temp = (JSONObject) JSONObject.parseObject(res).getJSONObject("goods_detail_response").getJSONArray("goods_details").get(0);
        if (temp == null) {
            temp.put("list",var1);
            return temp;
        }
        JSONArray goods_gallery_urls = temp.getJSONArray("goods_gallery_urls");
        temp.clear();
        temp.put("list",goods_gallery_urls);
        return temp;
    }

    /**
     * @param uid         用户id
     * @param pagesize
     * @param page
     * @param sort_type
     * @param with_coupon
     * @param keyword     商品类目ID
     * @param opt_id      商品标签类目ID
     * @return
     */
    @Override
    public JSONObject getPddGoodList(Long uid, Integer pagesize, Integer page, Integer sort_type, Boolean with_coupon, String keyword, Long opt_id, Integer merchant_type) {
        //平台给运营商的分成 可调
        Double rang = RANGE / 100d;
        String res = null;
        String type = "pdd.ddk.goods.search";
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        SortedMap<String, String> urlSign = new TreeMap<>();
        urlSign.put("client_id", KEY);
        urlSign.put("type", type);
        urlSign.put("timestamp", timestamp);
        urlSign.put("sort_type", sort_type.toString());
        urlSign.put("page", page.toString());
        urlSign.put("page_size", pagesize.toString());
        urlSign.put("with_coupon", with_coupon.toString());
        if (keyword != null && !keyword.equals("")) {
            urlSign.put("keyword", keyword);
        }
        if (opt_id != null&&!opt_id.equals(0)) {
            urlSign.put("opt_id", opt_id.toString());
        }
        urlSign.put("data_type", "JSON");
        urlSign.put("sign", EverySign.pddSign(urlSign, SECRET));
        try {
            res = HttpRequest.sendPost(PDD_URL, urlSign);
        } catch (IOException e) {
            log.warning("错误========" + e.toString());
            e.printStackTrace();
        }

        JSONObject data = new JSONObject();


        Userinfo ufo = userinfoMapper.selectByPrimaryKey(uid);
        if (ufo == null) {
            return null;
        }
        Integer roleId = ufo.getRoleId();
        JSONArray jsonArray = JSONObject.parseObject(res).getJSONObject("goods_search_response").getJSONArray("goods_list");
        Integer total_count = JSONObject.parseObject(res).getJSONObject("goods_search_response").getInteger("total_count");
        JSONArray dataArray = new JSONArray();
        if (roleId == 1) {
            Float bonus = 1f;
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject o = (JSONObject) jsonArray.get(i);
                //佣金比率 千分比
                Long promotion_rate = o.getLong("promotion_rate");
                //最低团购价 千分比
                Long min_group_price = o.getLong("min_group_price");
                //优惠卷金额 千分比
                Long coupon_discount = o.getLong("coupon_discount");
                //佣金计算
                Float after = Float.valueOf(min_group_price - coupon_discount);
                Float promoto = Float.valueOf(promotion_rate) / 1000;
                Float comssion = Float.valueOf(after * promoto);
                Integer rmb = (int) (comssion * rang);
                Float bondList = (rmb * bonus);
                JSONObject dataJson = new JSONObject();
                dataJson.put("imgUrl", o.getString("goods_image_url"));
                dataJson.put("volume", o.getInteger("sold_quantity"));
                dataJson.put("goodName", o.getString("goods_name"));
                dataJson.put("zk_money", coupon_discount);
                dataJson.put("price", min_group_price);
                dataJson.put("zk_price", min_group_price - coupon_discount);
                dataJson.put("goodId", o.getLong("goods_id"));
                dataJson.put("istmall", "false");
                dataJson.put("agent", bondList);
                dataArray.add(dataJson);
            }
            data.put("data", dataArray);
            data.put("count", total_count);
            return data;
        }
        if (roleId == 2) {
            Float score = Float.valueOf(ufo.getScore()) / 100;
            Float bonus = EveryUtils.getCommission(score);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject o = (JSONObject) jsonArray.get(i);
                //佣金比率 千分比
                Long promotion_rate = o.getLong("promotion_rate");
                //最低团购价 千分比
                Long min_group_price = o.getLong("min_group_price");
                //优惠卷金额 千分比
                Long coupon_discount = o.getLong("coupon_discount");
                //佣金计算
                Float after = Float.valueOf(min_group_price - coupon_discount);
                Float promoto = Float.valueOf(promotion_rate) / 1000;
                Float comssion = Float.valueOf(after * promoto);
                Integer rmb = (int) (comssion * rang);
                Float bondList = (rmb * bonus);
                JSONObject dataJson = new JSONObject();
                dataJson.put("imgUrl", o.getString("goods_image_url"));
                dataJson.put("volume", o.getInteger("sold_quantity"));
                dataJson.put("goodName", o.getString("goods_name"));
                dataJson.put("zk_money", coupon_discount);
                dataJson.put("price", min_group_price);
                dataJson.put("zk_price", min_group_price - coupon_discount);
                dataJson.put("goodId", o.getLong("goods_id"));
                dataJson.put("istmall", "false");
                dataJson.put("agent", bondList);
                dataArray.add(dataJson);
            }
            data.put("data", dataArray);
            data.put("count", total_count);
            return data;

        }
        if (roleId == 3) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject o = (JSONObject) jsonArray.get(i);
                //佣金比率 千分比
                Long promotion_rate = o.getLong("promotion_rate");
                //最低团购价 千分比
                Long min_group_price = o.getLong("min_group_price");
                //优惠卷金额 千分比
                Long coupon_discount = o.getLong("coupon_discount");
                //佣金计算
                JSONObject dataJson = new JSONObject();
                dataJson.put("imgUrl", o.getString("goods_image_url"));
                dataJson.put("volume", o.getInteger("sold_quantity"));
                dataJson.put("goodName", o.getString("goods_name"));
                dataJson.put("zk_money", coupon_discount);
                dataJson.put("price", min_group_price);
                dataJson.put("zk_price", min_group_price - coupon_discount);
                dataJson.put("goodId", o.getLong("goods_id"));
                dataJson.put("istmall", "false");
                dataJson.put("agent", 0);
                dataArray.add(dataJson);
            }
            data.put("data", dataArray);
            data.put("count", total_count);

            return data;

        }
        return null;

    }


}
