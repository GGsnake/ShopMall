package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.request.PddDdkGoodsDetailRequest;
import com.pdd.pop.sdk.http.api.request.PddDdkGoodsSearchRequest;
import com.pdd.pop.sdk.http.api.response.PddDdkGoodsDetailResponse;
import com.pdd.pop.sdk.http.api.response.PddDdkGoodsSearchResponse;
import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.model.Userinfo;
import com.superman.superman.service.PddApiService;
import com.superman.superman.utils.ConvertUtils;
import com.superman.superman.utils.sign.EverySign;
import com.superman.superman.utils.net.HttpRequest;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
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
    private UserinfoMapper userinfoMapper;
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
        try {
            res = HttpRequest.sendPost(PDD_URL, urlSign);

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
        PopClient client = new PopHttpClient(KEY, SECRET);
        PddDdkGoodsDetailRequest request = new PddDdkGoodsDetailRequest();
        List gd = new ArrayList();
        gd.add(goodIdList);
        request.setGoodsIdList(gd);
        PddDdkGoodsDetailResponse response = null;
        try {
            response = client.syncInvoke(request);
            JSONObject temp = new JSONObject();
            JSONArray var1 = new JSONArray();
            if (response.getGoodsDetailResponse().getGoodsDetails().size() == 0) {
                temp.put("list", var1);
                return temp;
            }
            List<String> goodsGalleryUrls = response.getGoodsDetailResponse().getGoodsDetails().get(0).getGoodsGalleryUrls();
            if (goodsGalleryUrls == null) {
                temp.put("list", var1);
                return temp;
            }
            temp.clear();
            temp.put("list", goodsGalleryUrls);
            return temp;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    @Override
    public JSONObject serachGoodsAll(PddDdkGoodsSearchRequest request, Long uid) {
        Userinfo ufo = userinfoMapper.selectByPrimaryKey(uid);
        if (ufo == null) {
            return null;
        }
        PopClient client = new PopHttpClient(KEY, SECRET);
        Double score = Double.valueOf(ufo.getScore());
        Double scoreAfer = score / 100;
        Double rang = RANGE / 100d;
        PddDdkGoodsSearchResponse response = null;
        try {
            JSONArray dataArray = new JSONArray();
            JSONObject data = new JSONObject();
            Integer roleId = ufo.getRoleId();
            response = client.syncInvoke(request);
            if (response == null) {
                data.put("data", dataArray);
                data.put("count", 0);
                return data;
            }
            Integer totalCount = response.getGoodsSearchResponse().getTotalCount();
            if (totalCount == 0) {
                data.put("data", dataArray);
                data.put("count", 0);
                return data;
            }
            List<PddDdkGoodsSearchResponse.GoodsListItem> goodsList = response.getGoodsSearchResponse().getGoodsList();
            if (roleId == 1) {
                for (int i = 0; i < goodsList.size(); i++) {
                    PddDdkGoodsSearchResponse.GoodsListItem item = goodsList.get(i);
                    Long promotion_rate = item.getPromotionRate();
                    Long min_group_price = item.getMinGroupPrice();
                    Long coupon_discount = item.getCouponDiscount();
                    Float after = Float.valueOf(min_group_price - coupon_discount);
                    Float promoto = Float.valueOf(promotion_rate) / 1000;
                    Float comssion = Float.valueOf(after * promoto);
                    BigDecimal var1 = new BigDecimal(comssion);
                    BigDecimal var2 = new BigDecimal(rang);
                    BigDecimal rmb = var1.multiply(var2);
                    JSONObject dataJson = ConvertUtils.convertPddSearchForSdk(item);
                    dataJson.put("zk_money", coupon_discount);
                    dataJson.put("price", min_group_price);
                    dataJson.put("shopName", item.getMallName());
                    dataJson.put("zk_price", after);
                    dataJson.put("agent", rmb.setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
                    dataArray.add(dataJson);
                }
            }
            if (roleId == 2) {
                for (int i = 0; i < goodsList.size(); i++) {
                    PddDdkGoodsSearchResponse.GoodsListItem item = goodsList.get(i);
                    Long promotion_rate = item.getPromotionRate();
                    Long min_group_price = item.getMinGroupPrice();
                    Long coupon_discount = item.getCouponDiscount();
                    Float after = Float.valueOf(min_group_price - coupon_discount);
                    Float promoto = Float.valueOf(promotion_rate) / 1000;
                    Float comssion = Float.valueOf(after * promoto);
                    BigDecimal var1 = new BigDecimal(comssion);
                    BigDecimal var2 = new BigDecimal(rang);
                    BigDecimal var3 = new BigDecimal(scoreAfer);
                    BigDecimal rmb = var1.multiply(var2);
                    BigDecimal agent = rmb.multiply(var3);
                    JSONObject dataJson = ConvertUtils.convertPddSearchForSdk(item);
                    dataJson.put("zk_money", coupon_discount);
                    dataJson.put("price", min_group_price);
                    dataJson.put("shopName", item.getMallName());

                    dataJson.put("zk_price", after);
                    dataJson.put("agent", agent.setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
                    dataArray.add(dataJson);
                }
            }
            if (roleId == 3) {
                for (int i = 0; i < goodsList.size(); i++) {
                    PddDdkGoodsSearchResponse.GoodsListItem item = goodsList.get(i);
                    Long min_group_price = item.getMinGroupPrice();
                    Long coupon_discount = item.getCouponDiscount();
                    Float after = Float.valueOf(min_group_price - coupon_discount);
                    JSONObject dataJson = ConvertUtils.convertPddSearchForSdk(item);
                    dataJson.put("zk_money", coupon_discount);
                    dataJson.put("price", min_group_price);
                    dataJson.put("zk_price", after);
                    dataJson.put("shopName", item.getMallName());

                    dataJson.put("agent", 0);
                    dataArray.add(dataJson);
                }
            }
            data.put("data", dataArray);
            data.put("count", totalCount);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
