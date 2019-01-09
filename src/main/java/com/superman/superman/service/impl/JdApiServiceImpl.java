package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.ScoreDao;
import com.superman.superman.model.ScoreBean;
import com.superman.superman.req.JdSerachReq;
import com.superman.superman.service.JdApiService;
import com.superman.superman.utils.NetUtils;
import lombok.NonNull;
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
    @Autowired
    private ScoreDao scoreDao;

    @Value("${juanhuang.range}")
    private Integer RANGE;

    //    @Cacheable(value="signonCache",key="'petstore:signon:'+#username", unless="#result==null")
//    @Cacheable(value = "scoreBean", key = "#id")
    public ScoreBean queryJdOder(String id) {
        ScoreBean scoreBean = new ScoreBean();
        scoreBean.setUserId(Long.valueOf(id));
        scoreBean.setDataSrc(1);
        ScoreBean exit = scoreDao.isExit(scoreBean);
        return exit;
    }

    @Override
    public JSONObject convertJd(Long jdpid, Long goodId) {

        return null;
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

