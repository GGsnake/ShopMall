package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.model.ScoreBean;
import com.superman.superman.model.Userinfo;
import com.superman.superman.req.JdSerachReq;
import com.superman.superman.service.JdApiService;
import com.superman.superman.utils.NetUtils;
import jd.union.open.goods.query.request.GoodsReq;
import jd.union.open.goods.query.request.UnionOpenGoodsQueryRequest;
import jd.union.open.goods.query.response.*;
import jd.union.open.promotion.bysubunionid.get.request.UnionOpenPromotionBysubunionidGetRequest;
import jd.union.open.promotion.bysubunionid.get.response.UnionOpenPromotionBysubunionidGetResponse;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liujupeng on 2018/11/14.
 */
@Log
@Service("jdApiService")
//@Cacheable
public class JdApiServiceImpl implements JdApiService {
    private static final String URL = "http://jdapi.apptimes.cn/";
    private static final String QQAPPKEY = "8k8lhumd";
    private static final String APID = "mm_261060037_253650051_";

    @Autowired
    RestTemplate restTemplate;
    @Value("${domain.jdimageurl}")
    private String jdimageurl;
    @Value("${domain.jdsecret}")
    private String jdsecret;
    @Value("${domain.jdkey}")
    private String jdkey;
    @Value("${domain.jdUrl}")
    private String jdurl;
    @Value("${domain.jduid}")
    private String jduid;
    @Autowired
    private UserinfoMapper userinfoMapper;

    @Value("${juanhuang.range}")
    private Integer RANGE;


    @Override
    public JSONObject convertJd(Long jdpid, Long goodId) {
        String jdurl = URL + "wxlink?";
        JSONObject data;
        Map<String, String> urlSign = new HashMap<>();
        urlSign.put("positionid",jdpid.toString());
        urlSign.put("materialids",goodId.toString());
        urlSign.put("unionid",jduid);
        String linkStringByGet = null;
        try {
            linkStringByGet = NetUtils.createLinkStringByGet(urlSign);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String res = restTemplate.getForObject(jdurl + linkStringByGet, String.class);
        data = (JSONObject) JSON.parseObject(res).getJSONArray("data").get(0);
//
//        UnionOpenPromotionBysubunionidGetRequest request = new UnionOpenPromotionBysubunionidGetRequest();
//        jd.union.open.promotion.bysubunionid.get.request.PromotionCodeReq promotionCodeReq = new jd.union.open.promotion.bysubunionid.get.request.PromotionCodeReq();
//        promotionCodeReq.setMaterialId("https://item.jd.com/23484023378.html");
//        promotionCodeReq.setSubUnionId("1000054104");
//
//        request.setPromotionCodeReq(promotionCodeReq);
//        try {
//            UnionOpenPromotionBysubunionidGetResponse response = client.execute(request);
//
//        } catch (JdException e) {
//            e.printStackTrace();
//        }

        return data;
    }

    @Override
    public JSONObject serachGoodsAll(JdSerachReq jdSerachReq, Long uid) {
        String jdurl = URL + "conponitems?";
        JSONObject temp = new JSONObject();
        Map<String, String> urlSign = new HashMap<>();
        if (jdSerachReq.getKeyword() != null) {
            urlSign.put("keyword", jdSerachReq.getKeyword());
        }
        if (jdSerachReq.getPage() != null) {
            urlSign.put("page", String.valueOf(jdSerachReq.getPage()));

        }
        if (jdSerachReq.getPagesize() != null) {
            urlSign.put("pagesize", String.valueOf(jdSerachReq.getPagesize()));

        }
        if (jdSerachReq.getPriceto() != null) {
            urlSign.put("priceto", String.valueOf(jdSerachReq.getPriceto()));

        }
        if (jdSerachReq.getPricefrom() != null) {
            urlSign.put("pricefrom", String.valueOf(jdSerachReq.getPricefrom()));

        }
        if (jdSerachReq.getSkuidlist() != null) {
            Integer[] skuidlist = (Integer[]) jdSerachReq.getSkuidlist().toArray();
            urlSign.put("skuidlist", skuidlist.toString());

        }
        String linkStringByGet = null;
        try {
            linkStringByGet = NetUtils.createLinkStringByGet(urlSign);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String res = restTemplate.getForObject(jdurl + linkStringByGet, String.class);
        JSONArray var1 = JSON.parseObject(res).getJSONArray("data");
        Integer count = Integer.valueOf(JSON.parseObject(res).getString("total"));
        JSONArray templist = new JSONArray();


        for (Object var2 : var1) {
            JSONObject var4 = new JSONObject();
            JSONObject var3 = (JSONObject) var2;
            String imageurl = var3.getString("imageurl");
            String volume = var3.getString("inOrderCount");
            String goodId = var3.getString("skuId");
            String skuName = var3.getString("skuName");
            String price = var3.getString("wlPrice");
            String commissionShare = var3.getString("wlCommissionShare");

            BigDecimal coms = new BigDecimal(commissionShare);
            BigDecimal priceall = new BigDecimal(price);

            JSONArray coupon = var3.getJSONArray("couponList");
            var4.put("commissionRate", new BigDecimal(commissionShare).intValue() * 100);
            if (coupon != null && coupon.size() != 0) {
                JSONObject data = (JSONObject) coupon.get(0);
                BigDecimal discount = new BigDecimal(data.getString("discount"));
                BigDecimal var5 = new BigDecimal(price);
                BigDecimal subtract = var5.subtract(discount);
                var4.put("zk_price", subtract.doubleValue());
                var4.put("zk_money", discount.intValue());
                Double var9 = (subtract.doubleValue() * coms.intValue() / 100) * RANGE / 100;
                var4.put("agent", var9);

            } else {
                Double var9 = (priceall.doubleValue() * coms.intValue() / 100) * RANGE / 100;
                var4.put("agent", var9);
            }
            var4.put("volume", new BigDecimal(volume).intValue());
            var4.put("goodId", new BigDecimal(goodId).longValue());
            var4.put("goodName", skuName);
            var4.put("imgUrl", jdimageurl + imageurl);
            var4.put("istmall", "false");
            var4.put("price", priceall.doubleValue());
            templist.add(var4);
        }

        temp.put("data", templist);
        temp.put("count", count);

        return temp;
    }

    @Override
    public JSONObject serachGoodsAllJd(GoodsReq goodsReq, Long uid) {
        Userinfo usr = userinfoMapper.selectByPrimaryKey(uid);
        if (usr == null) {
            return null;
        }
        Integer roleId = usr.getRoleId();
        Double score = Double.valueOf(usr.getScore());
        String accessToken = "";
        JdClient client = new DefaultJdClient(jdurl, accessToken, jdkey, jdsecret);
        UnionOpenGoodsQueryRequest request = new UnionOpenGoodsQueryRequest();
        request.setGoodsReqDTO(goodsReq);
        JSONObject temp = new JSONObject();
        try {
            UnionOpenGoodsQueryResponse response = client.execute(request);
            if (response.getData() != null && response.getTotalCount() != 0) {
                GoodsResp[] data = response.getData();
                Long totalCount = response.getTotalCount();
                JSONArray templist = new JSONArray();
                for (GoodsResp var2 : data) {
                    JSONObject var4 = new JSONObject();
                    Double price = var2.getPriceInfo()[0].getPrice();
                    //佣金信息
                    CommissionInfo[] commissionInfo = var2.getCommissionInfo();
                    BigDecimal coms = new BigDecimal(commissionInfo[0].getCommissionShare());
                    BigDecimal commission = new BigDecimal(commissionInfo[0].getCommission());
                    BigDecimal priceall = new BigDecimal(price);
                    Coupon[] coupon = var2.getCouponInfo()[0].getCouponList();
                    var4.put("commissionRate", coms.intValue() * 10);
                    if (coupon != null && coupon.length != 0) {
                        Double couponList = coupon[0].getDiscount();
                        BigDecimal discount = new BigDecimal(couponList);
                        BigDecimal subtract = priceall.subtract(discount);
                        var4.put("zk_price", subtract.doubleValue()*100);
                        var4.put("zk_money", discount.doubleValue()*100);
//                        Double var9 = (subtract.doubleValue() * coms.intValue() / 100) * RANGE / 100;
//                        var4.put("agent", var9);

                    } else {
                        var4.put("zk_price", priceall.doubleValue()*100);
                        var4.put("zk_money", 0);
                    }
                    if (roleId == 1) {
                        Double var9 = (100*commission.doubleValue()) * RANGE / 100;
                        BigDecimal var5 = new BigDecimal(var9);
                        var4.put("agent", var5.intValue());
                    }
                    if (roleId == 2) {
                        BigDecimal var5 = new BigDecimal(( (100*commission.doubleValue()) * RANGE / 100));
                        Double var3 = score / 100;
                        BigDecimal var6 = new BigDecimal(var3);
                        var5.multiply(var6);
                        var4.put("agent", var5.intValue());
                    }
                    if (roleId == 3) {
                        var4.put("agent", 0);
                    }
                    var4.put("volume", var2.getInOrderCount30Days());
                    var4.put("goodId", var2.getSkuId());
                    var4.put("goodName", var2.getSkuName());
                    var4.put("imgUrl", var2.getImageInfo()[0].getImageList()[0].getUrl());
                    var4.put("istmall", "false");
                    var4.put("price", price*100);
                    templist.add(var4);
                }
                temp.put("data", templist);
                temp.put("count", totalCount);
                return temp;
            }
            return null;

        } catch (JdException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JSONObject jdDetail(@NonNull Long goodId) {
//        String jdurl = URL + "/iteminfo?";
//        JSONObject temp = new JSONObject();
//        Map<String, String> urlSign = new HashMap<>();
//        urlSign.put("skuids",goodId.toString());
//        String linkStringByGet = null;
//        try {
//            linkStringByGet = NetUtils.createLinkStringByGet(urlSign);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        String res = restTemplate.getForObject(jdurl + linkStringByGet, String.class);
//        JSONArray var1 = JSON.parseObject(res).getJSONArray("data");
//        JSONObject data= (JSONObject) var1.get(0);
//        data.ge
        return null;
    }

    //
//    @CachePut(value = "concurrenmapcache")
//    public long save() {
//        long timestamp = new Timestamp(System.currentTimeMillis()).getTime();
//        System.out.println("进行缓存：" + timestamp);
//        return timestamp;
//    }
//
//    public long getByCache() {
//        ScoreBean exit = scoreDao.isExit(scoreBean);
//        return exit;
//    }
//    @Override
    public String queryJdOder() {
        String SERVER_URL = "https://router.jd.com/api";
        String appKey = "your appkey";
        String appSecret = "your secret";
        String accessToken = "";


        return null;
    }


//    UnionSearchGoodsParamQueryRequest request=new UnionSearchGoodsParamQueryRequest();
//    UnionServiceQueryCommissionOrdersResponse
//
//            request.setPageIndex( 1 );
//            request.setPageSize( 10 );
//
//            try {
//
//        UnionSearchGoodsParamQueryResponse response=client.execute(request);
//        String queryResult = response.getQueryPromotionGoodsByParamResult();
//
//        JSONArray jsonObject = JSON.parseArray(queryResult);
//        for (int i = 0; i < jsonObject.size(); i++) {
//            JSONObject o = (JSONObject) jsonObject.get(i);
//            //佣金比率 千分比
//            Long promotion_rate = o.getLong("promotion_rate");
//            //最低团购价 千分比
//            Long min_group_price = o.getLong("min_group_price");
//            //优惠卷金额 千分比
//            Long coupon_discount = o.getLong("coupon_discount");
//            //佣金计算
//            Float after = Float.valueOf(min_group_price - coupon_discount);
//            Float promoto = Float.valueOf(promotion_rate) / 1000;
//            Float comssion = Float.valueOf(after * promoto);
//            Integer rmb = (int) (comssion * rang);
//            Float bondList = (rmb * bonus);
//            o.put("bond",bondList);
//        }
//
//        logger.info(response.getQueryPromotionGoodsByParamResult());
//
//    } catch (JdException e) {
//        e.printStackTrace();
//    }

}

