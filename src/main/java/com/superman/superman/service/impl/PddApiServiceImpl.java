package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.request.PddDdkGoodsDetailRequest;
import com.pdd.pop.sdk.http.api.request.PddDdkGoodsSearchRequest;
import com.pdd.pop.sdk.http.api.response.PddDdkGoodsDetailResponse;
import com.pdd.pop.sdk.http.api.response.PddDdkGoodsSearchResponse;
import com.superman.superman.annotation.FastCache;
import com.superman.superman.dao.SysJhPddAllDao;
import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.manager.ConfigQueryManager;
import com.superman.superman.model.SysJhPddAll;
import com.superman.superman.model.Userinfo;
import com.superman.superman.service.OtherService;
import com.superman.superman.service.PddApiService;
import com.superman.superman.utils.ConvertUtils;
import com.superman.superman.utils.GoodUtils;
import com.superman.superman.utils.PageParam;
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
 * Created by snake on 2018/11/6.
 */

@Log
@Service("pddServiceApi")
public class PddApiServiceImpl implements PddApiService {

    @Value("${juanhuang.range}")
    private Integer RANGE;
    @Autowired
    private UserinfoMapper userinfoMapper;
    @Autowired
    private SysJhPddAllDao sysJhPddAllDao;
    @Autowired
    private ConfigQueryManager configQueryManager;
    /**
     *   生成推广链接
     */
    @Override
    public JSONObject convertPdd(@NonNull String pid, @NonNull Long goodId) {
        String KEY = configQueryManager.queryForKey("PDDKEY");
        String SECRET = configQueryManager.queryForKey("PDDSECRET");
        String PDD_URL = configQueryManager.queryForKey("PDDREQURL");

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
            log.warning("请求拼多多生成链接失败 信息="+e.getMessage());
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
    @FastCache(timeOut = 70)
    public JSONObject pddDetail(Long goodIdList) {
        String KEY = configQueryManager.queryForKey("PDDKEY");
        String SECRET = configQueryManager.queryForKey("PDDSECRET");

        PopHttpClient client = new PopHttpClient(KEY, SECRET);
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

    @Autowired
    private OtherService otherService;

    @Override
    public JSONObject serachGoodsAll(PddDdkGoodsSearchRequest request, Long uid) {
//        Userinfo ufo = userinfoMapper.selectByPrimaryKey(uid);
//        if (ufo == null) {
//            return null;
//        }
//        int roleId = ufo.getRoleId();
//        double score = ufo.getScore().doubleValue() / 100;
//
//
//        JSONArray dataArray = new JSONArray();
//        JSONObject data = new JSONObject();
//        PddDdkGoodsSearchResponse response = null;
//
//        try {
//            response = client.syncInvoke(request);
//            if (response == null) {
//                data.put("data", dataArray);
//                data.put("count", 0);
//                return data;
//            }
//            Integer totalCount = response.getGoodsSearchResponse().getTotalCount();
//            if (totalCount == 0) {
//                data.put("data", dataArray);
//                data.put("count", 0);
//                return data;
//            }
//            JSONObject dataJson;
//
//            List<PddDdkGoodsSearchResponse.GoodsListItem> goodsList = response.getGoodsSearchResponse().getGoodsList();
//            if (roleId == 1) {
//                for (int i = 0, len = goodsList.size(); i < len; i++) {
//                    PddDdkGoodsSearchResponse.GoodsListItem item = goodsList.get(i);
//                    //佣金比率
//                    Long promotion_rate = item.getPromotionRate();
//                    //最小团购价
//                    Long min_group_price = item.getMinGroupPrice();
//                    //优惠券额度
//                    Long coupon_discount = item.getCouponDiscount();
//                    //券后价
//                    Long after = (min_group_price - coupon_discount);
//                    //佣金
//                    Float comssion = Float.valueOf(after * promotion_rate / 1000);
//                    dataJson = ConvertUtils.convertPddSearchForSdk(item);
//                    dataJson.put("zk_money", coupon_discount);
//                    dataJson.put("price", min_group_price);
//                    dataJson.put("shopName", item.getMallName());
//                    dataJson.put("zk_price", after);
//                    dataJson.put("agent", comssion.intValue());
//                    dataArray.add(dataJson);
//                }
//            }
//            if (roleId == 2) {
//                for (int i = 0; i < goodsList.size(); i++) {
//                    PddDdkGoodsSearchResponse.GoodsListItem item = goodsList.get(i);
//                    //佣金比率
//                    Long promotion_rate = item.getPromotionRate();
//                    //最小团购价
//                    Long min_group_price = item.getMinGroupPrice();
//                    //优惠券额度
//                    Long coupon_discount = item.getCouponDiscount();
//                    //券后价
//                    Float after = Float.valueOf(min_group_price - coupon_discount);
//                    //佣金
//                    Float comssion = Float.valueOf(after * promotion_rate / 1000);
//                    Double agent = comssion * score;
//                    dataJson = ConvertUtils.convertPddSearchForSdk(item);
//                    dataJson.put("zk_money", coupon_discount);
//                    dataJson.put("price", min_group_price);
//                    dataJson.put("shopName", item.getMallName());
//                    dataJson.put("zk_price", after);
//                    dataJson.put("agent", agent.intValue());
//                    dataArray.add(dataJson);
//                }
//            }
//            if (roleId == 3) {
//                for (int i = 0; i < goodsList.size(); i++) {
//                    PddDdkGoodsSearchResponse.GoodsListItem item = goodsList.get(i);
//                    //最小团购价
//                    Long min_group_price = item.getMinGroupPrice();
//                    //优惠券额度
//                    Long coupon_discount = item.getCouponDiscount();
//                    //券后价
//                    Float after = Float.valueOf(min_group_price - coupon_discount);
//                    dataJson = ConvertUtils.convertPddSearchForSdk(item);
//                    dataJson.put("zk_money", coupon_discount);
//                    dataJson.put("price", min_group_price);
//                    dataJson.put("zk_price", after);
//                    dataJson.put("shopName", item.getMallName());
//                    dataJson.put("agent", 0);
//                    dataArray.add(dataJson);
//                }
//            }
//            data.put("data", dataArray);
//            data.put("count", totalCount);
//            return data;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return null;
    }

    @Override
    public JSONObject getLocalGoodsAll(PageParam pageParam, Long uid) {
        Userinfo ufo = userinfoMapper.selectByPrimaryKey(uid);
        if (ufo == null) {
            return null;
        }
        JSONObject param = new JSONObject();
        param.put("start", pageParam.getStartRow());
        param.put("end", pageParam.getPageSize());
        int count = sysJhPddAllDao.queryTotal();
        if (count == 0) {
            return null;
        }
        Double score = Double.valueOf(ufo.getScore());
        JSONArray dataArray = new JSONArray();
        JSONObject data = new JSONObject();

        List<SysJhPddAll> sysJhTaobaoHots = sysJhPddAllDao.queryPageJd(param);

        int len = sysJhTaobaoHots.size();
        if (ufo.getRoleId() == 1) {
            for (int i = 0; i < len; i++) {
                SysJhPddAll dataObj = sysJhTaobaoHots.get(i);
                JSONObject dataJson = GoodUtils.convertLocalTaobao(dataObj);
                //查找指定字符第一次出现的位置
                dataJson.put("zk_money", dataObj.getCoupon().intValue() * 100);
                dataJson.put("hasCoupon", 1);
                dataJson.put("zk_price", dataObj.getCouponprice().doubleValue() * 100);
                dataJson.put("commissionRate", dataObj.getCommissionrate().intValue());
//                BigDecimal agent = GoodUtils.commissonAritLocalTaobao(dataObj.getCommissionrate().doubleValue());
                dataJson.put("shopName", dataObj.getShoptitle());
                dataJson.put("istmall", 0);
                dataJson.put("agent", dataObj.getCommission().doubleValue() * 100);
                dataArray.add(dataJson);
            }
            data.put("data", dataArray);
            data.put("count", count);
            return data;

        }
        if (ufo.getRoleId() == 2) {
            for (int i = 0; i < len; i++) {
                SysJhPddAll dataObj = sysJhTaobaoHots.get(i);
                JSONObject dataJson = GoodUtils.convertLocalTaobao(dataObj);
                //查找指定字符第一次出现的位置
                dataJson.put("zk_money", dataObj.getCoupon().intValue() * 100);
                dataJson.put("hasCoupon", 1);
                dataJson.put("zk_price", dataObj.getCouponprice().doubleValue() * 100);
                dataJson.put("commissionRate", dataObj.getCommissionrate().intValue() * 100);
//                BigDecimal agent = GoodUtils.commissonAritLocalTaobao(dataObj.getCommissionrate().doubleValue());
                dataJson.put("shopName", dataObj.getShoptitle());
                dataJson.put("istmall", 0);
                dataJson.put("agent", dataObj.getCommission().doubleValue() * 100 * score / 100);
                dataArray.add(dataJson);
            }
            data.put("data", dataArray);
            data.put("count", count);
            return data;
        }
        for (int i = 0; i < len; i++) {
            SysJhPddAll dataObj = sysJhTaobaoHots.get(i);
            JSONObject dataJson = GoodUtils.convertLocalTaobao(dataObj);
            //查找指定字符第一次出现的位置
            dataJson.put("zk_money", dataObj.getCoupon().intValue() * 100);
            dataJson.put("hasCoupon", 1);
            dataJson.put("zk_price", dataObj.getCouponprice().doubleValue() * 100);
            dataJson.put("commissionRate", dataObj.getCommissionrate().intValue() * 100);
//                BigDecimal agent = GoodUtils.commissonAritLocalTaobao(dataObj.getCommissionrate().doubleValue());
            dataJson.put("shopName", dataObj.getShoptitle());
            dataJson.put("istmall", 0);
            dataJson.put("agent", 0);
            dataArray.add(dataJson);
        }
        data.put("data", dataArray);
        data.put("count", count);
//        return
        return data;
    }


}
