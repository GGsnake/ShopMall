package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.model.Userinfo;
import com.superman.superman.service.JdApiService;
import com.superman.superman.utils.net.NetUtils;
import jd.union.open.goods.query.request.GoodsReq;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liujupeng on 2018/11/14.
 */
@Log
@Service("jdApiService")
public class JdApiServiceImpl implements JdApiService {
    @Autowired
    private UserinfoMapper userinfoMapper;
    @Autowired
    private  RestTemplate restTemplate;
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
    @Value("${juanhuang.range}")
    private Integer range;
    @Value("${miao.apkey}")
    private String apkey;
    private static final String URL = "https://api.open.21ds.cn/jd_api_v1/";
    /**
     * 京东生成推广URL
     * @param jdpid
     * @param materialId
     * @return
     */
    public JSONObject convertJd(String jdpid, String materialId) {
        String jdurl = URL + "getitemcpsurl?";
        Map<String, String> urlSign = new HashMap<>();
        urlSign.put("apkey", apkey);
        urlSign.put("unionId", jduid);
        urlSign.put("materialId", materialId);
        urlSign.put("positionId", jdpid);
        String linkStringByGet = null;
        try {
            linkStringByGet = NetUtils.createLinkStringByGet(urlSign);
            String res = restTemplate.getForObject(jdurl + linkStringByGet, String.class);
            JSONObject resJson = JSON.parseObject(res);
            if (resJson.getInteger("code") == 200) {
                JSONObject var1 = resJson.getJSONObject("data");
                JSONObject data = new JSONObject();
                data.put("uland_url", var1.getString("shortURL"));
                data.put("tkLink", null);
                data.put("url", null);
                return data;
            }
            else {
                return new JSONObject();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 京东搜索引擎
     * @param goodsReq
     * @param uid
     * @return
     */
    @Override
    public JSONObject serachGoodsAllJd(GoodsReq goodsReq, Long uid) {
        Userinfo usr = userinfoMapper.selectByPrimaryKey(uid);
        if (usr == null) {
            return null;
        }
        String jdurl = URL + "getjdunionitems?";
        Integer roleId = usr.getRoleId();
        Double score = Double.valueOf(usr.getScore());
        Map<String, String> urlSign = new HashMap<>();
        urlSign.put("apkey", apkey);
        if (goodsReq.getCid3() != null) {
            urlSign.put("cid3", goodsReq.getCid3().toString());
        }
        if (goodsReq.getPageSize() != null) {
            urlSign.put("pageSize", String.valueOf(goodsReq.getPageSize()));
        }
        if (goodsReq.getPageIndex() != null) {
            urlSign.put("pageIndex", goodsReq.getPageIndex().toString());
        }
        if (goodsReq.getSortName() != null) {
            urlSign.put("sortName", String.valueOf(goodsReq.getSortName()));
        }
        if (goodsReq.getSort() != null) {
            urlSign.put("sort", goodsReq.getSort());
        }
        if (goodsReq.getIsCoupon() != null) {
            urlSign.put("isCoupon", goodsReq.getIsCoupon().toString());
        }
        if (goodsReq.getKeyword() != null) {
            urlSign.put("keyword", goodsReq.getKeyword());
        } else {
            urlSign.put("keyword", "女装");
        }
        String linkStringByGet = null;
        try {
            linkStringByGet = NetUtils.createLinkStringByGet(urlSign);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JSONObject temp = new JSONObject();
        JSONArray dataArray = new JSONArray();
        String res = restTemplate.getForObject(jdurl + linkStringByGet, String.class);
        Integer totalCount = JSON.parseObject(res).getJSONObject("data").getInteger("totalCount");
        if (totalCount == null || totalCount == 0) {
            temp.put("data", dataArray);
            temp.put("count", totalCount);
            return temp;
        }
        dataArray = JSON.parseObject(res).getJSONObject("data").getJSONArray("lists");
        JSONArray templist = new JSONArray();
        for (int i = 0; i < dataArray.size(); i++) {
            JSONObject jdJson = (JSONObject) dataArray.get(i);
            JSONObject jdData = new JSONObject();
            //单价
            Double price = jdJson.getJSONObject("priceInfo").getDouble("price");
            //佣金金额
            Double commissionA = jdJson.getJSONObject("commissionInfo").getDouble("commission");
            //佣金比例
            Double comsA = jdJson.getJSONObject("commissionInfo").getDouble("commissionShare");
            //图片URL
            JSONObject img = (JSONObject) jdJson.getJSONObject("imageInfo").getJSONArray("imageList").get(0);
            BigDecimal coms = new BigDecimal(comsA);
            BigDecimal commission = new BigDecimal(commissionA);
            BigDecimal priceall = new BigDecimal(price);
            //优惠卷信息
            JSONArray coupon = jdJson.getJSONObject("couponInfo").getJSONArray("couponList");
            //判断是否有优惠卷
            if (coupon != null && coupon.size() != 0) {
                JSONObject couponList = (JSONObject) coupon.get(0);
                BigDecimal discount = new BigDecimal(couponList.getInteger("discount"));
                BigDecimal subtract = priceall.subtract(discount);
                jdData.put("zk_price", subtract.doubleValue() * 100);
                jdData.put("zk_money", discount.doubleValue() * 100);
            } else {
                jdData.put("zk_price", priceall.doubleValue() * 100);
                jdData.put("zk_money", 0);
            }
            if (roleId == 1) {
                Double var9 = (100 * commission.doubleValue()) * range / 100;
                BigDecimal var5 = new BigDecimal(var9);
                jdData.put("agent", var5.intValue());
            }
            if (roleId == 2) {
                BigDecimal var5 = new BigDecimal(((100 * commission.doubleValue()) * range / 100));
                Double var3 = score / 100;
                BigDecimal var6 = new BigDecimal(var3);
                var5.multiply(var6);
                jdData.put("agent", var5.intValue());
            }
            if (roleId == 3) {
                jdData.put("agent", 0);
            }
            jdData.put("shopName",  jdJson.getJSONObject("shopInfo").getString("shopName"));
            jdData.put("commissionRate", coms.intValue() * 10);
            jdData.put("volume", jdJson.getInteger("inOrderCount30Days"));
            jdData.put("goodId", jdJson.getLong("skuId"));
            jdData.put("goodName", jdJson.getString("skuName"));
            jdData.put("imgUrl", img.getString("url"));
            jdData.put("istmall", "false");
            jdData.put("price", price * 100);
            jdData.put("jdurl", "http://"+jdJson.getString("materialUrl"));
            templist.add(jdData);
        }
        temp.put("data", templist);
        temp.put("count", totalCount);
        return temp;
    }
    /** 京东商品详情
     * 商品ID
     * @param goodId
     * @return
     */
    @Override
    public JSONObject jdDetail(@NonNull Long goodId) {
        JSONObject data = new JSONObject();
//        String jdurl = URL + "gettgiteminfo?";
//        Map<String, String> urlSign = new HashMap<>();
//        urlSign.put("apkey", apkey);
//        urlSign.put("skuids", goodId.toString());
//        String link = null;
        String jdurl1 = URL + "getitemdesc?";
        Map<String, String> urlSign1 = new HashMap<>();
        urlSign1.put("apkey", apkey);
        urlSign1.put("skuid", goodId.toString());
        String linkStringByGet1 = null;
        try {
            linkStringByGet1 = NetUtils.createLinkStringByGet(urlSign1);
//            link = NetUtils.createLinkStringByGet(urlSign);
            String res1 = restTemplate.getForObject(jdurl1 + linkStringByGet1, String.class);
//            String res = restTemplate.getForObject(jdurl + link, String.class);

            if (JSON.parseObject(res1).getInteger("code") == 200 ) {
                JSONArray list = JSON.parseObject(res1).getJSONArray("data");
//                JSONObject object = (JSONObject) JSON.parseObject(res).getJSONArray("data").get(0);
                JSONArray jsonArray = new JSONArray();
                list.forEach(img -> {
                    String url = (String) img;
                    jsonArray.add("http:" + url);
                });
                data.put("list", jsonArray);
//                data.put("url", object.getString("materialUrl"));
                return data;
            }

        } catch (UnsupportedEncodingException e) {
            log.warning("查询京东商品详情失败");
            e.printStackTrace();
        }
        return null;
    }



}

