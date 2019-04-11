package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.annotation.FastCache;
import com.superman.superman.dao.SysJhTaobaoHotDao;
import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.manager.ConfigQueryManager;
import com.superman.superman.model.SysJhJdHot;
import com.superman.superman.model.Userinfo;
import com.superman.superman.service.JdApiService;
import com.superman.superman.utils.GoodUtils;
import com.superman.superman.utils.PageParam;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private SysJhTaobaoHotDao sysJhTaobaoHotDao;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${domain.jdimageurl}")
    private String jdimageurl;
    @Value("${domain.jduid}")
    private String jduid;
    @Value("${juanhuang.range}")
    private Integer range;

    private static final String URL = "https://api.open.21ds.cn/jd_api_v1/";
    @Autowired
    private ConfigQueryManager configQueryManager;

    public static String getURLEncoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 京东生成推广URL
     *
     * @param jdpid
     * @param materialId
     * @return
     */
    public JSONObject convertJd(String jdpid, String materialId, String coupon) {
        String apkey = configQueryManager.queryForKey("MiaoAppKey");
        String urlEncoderString = getURLEncoderString(materialId);
        String jdurl = URL + "getitemcpsurl?";
        Map<String, String> urlSign = new HashMap<>();
        urlSign.put("apkey", apkey);
        urlSign.put("unionId", jduid);
        urlSign.put("materialId", urlEncoderString);
        if (coupon != null) {
            urlSign.put("couponUrl", getURLEncoderString(coupon));
        }
        urlSign.put("positionId", jdpid);
        String reqUrl = null;
        try {
            reqUrl = jdurl + NetUtils.convertUrlParam(urlSign);
            String res = restTemplate.getForObject(reqUrl, String.class);
            JSONObject resJson = JSON.parseObject(res);
            if (resJson.getInteger("code") == 200) {
                JSONObject var1 = resJson.getJSONObject("data");
                JSONObject data = new JSONObject();
                data.put("uland_url", var1.getString("shortURL"));
                data.put("tkLink", null);
                data.put("url", null);
                return data;
            } else {
                return new JSONObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 京东搜索引擎
     *
     * @param goodsReq
     * @param uid
     * @return
     */
    @Override
    public JSONObject serachGoodsAllJd(GoodsReq goodsReq, Long uid) {
        Userinfo userInfo = userinfoMapper.selectByPrimaryKey(uid);
        if (userInfo == null) {
            return null;
        }
        String apkey = configQueryManager.queryForKey("MiaoAppKey");
        String jdurl = URL + "getjdunionitems?";
        Integer roleId = userInfo.getRoleId();
        Double score = Double.valueOf(userInfo.getScore());
        double myScore = 0d;
        if (score != 0) {
            myScore=score / 100;
        }
        Map<String, String> urlSign = new HashMap<>();
        urlSign.put("apkey", apkey);
        if (goodsReq.getCid3() != null) {
            urlSign.put("cid1", goodsReq.getCid3().toString());
        }
        if (goodsReq.getPageSize() != null) {
            urlSign.put("pageSize", String.valueOf(goodsReq.getPageSize()));
        }
        if (goodsReq.getPageIndex() != null) {
            urlSign.put("pageIndex", goodsReq.getPageIndex().toString());
        }
        if (goodsReq.getSortName() != null) {
            urlSign.put("sortName", "inOrderCount30Days");
        }
        if (goodsReq.getSort() != null) {
            urlSign.put("sort", goodsReq.getSort());
        }
        if (goodsReq.getKeyword() != null) {
            urlSign.put("keyword", goodsReq.getKeyword());
        }
        JSONArray dataArray = null;
        //构建请求
        String reqUrl = NetUtils.convertUrlParam(urlSign);
        JSONObject temp = new JSONObject();

        String getData = null;
        try {
            getData = restTemplate.getForObject(jdurl + reqUrl, String.class);
        } catch (Exception e) {
            log.warning("京东搜索引擎异常,发送URL请求到喵有券失败");
        }
        JSONObject allData = JSON.parseObject(getData).getJSONObject("data");
        Integer totalCount = allData.getInteger("totalCount");
        if (totalCount == null || totalCount == 0) {
            temp.put("data", new JSONArray());
            temp.put("count", 0);
            return temp;
        }
        dataArray = allData.getJSONArray("lists");
        JSONArray arrayData = new JSONArray(15);
        for (int i = 0, length = dataArray.size(); i < length; i++) {
            JSONObject jdJson = (JSONObject) dataArray.get(i);
            JSONObject jdData = new JSONObject();
            //单价2*
            Double price = jdJson.getJSONObject("priceInfo").getDouble("price");
            //佣金金额
            Double commissionA = jdJson.getJSONObject("commissionInfo").getDouble("commission");
            //佣金比例
            Double comsA = jdJson.getJSONObject("commissionInfo").getDouble("commissionShare");
            //图片URL
            JSONObject img = (JSONObject) jdJson.getJSONObject("imageInfo").getJSONArray("imageList").get(0);
            BigDecimal coms = new BigDecimal(comsA);
            //原价
            BigDecimal priceall = new BigDecimal(price);
            jdData.put("agent", 0);
            //优惠卷信息
            JSONArray coupon = jdJson.getJSONObject("couponInfo").getJSONArray("couponList");
            //判断是否有优惠卷
            if (coupon != null && coupon.size() != 0) {
                JSONObject couponList = (JSONObject) coupon.get(0);
                //券额
                BigDecimal discount = new BigDecimal(couponList.getInteger("discount"));
                //券后价
                BigDecimal couponPrice = priceall.subtract(discount);
                jdData.put("coupon", couponList.getString("link"));
                jdData.put("zk_price", couponPrice.doubleValue() * 100);
                jdData.put("zk_money", discount.doubleValue() * 100);
                Double var9 = couponPrice.doubleValue() * comsA;
                if (roleId == 1) {
                    BigDecimal var5 = new BigDecimal(var9);
                    jdData.put("agent", var5.setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
                }
                if (roleId == 2) {
                    double priceComs = var9 * myScore;
                    BigDecimal var5 = new BigDecimal(priceComs);
                    jdData.put("agent", var5.setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
                }
            } else {
                double commission = price * comsA;
                if (roleId == 1) {
                    jdData.put("agent", commission);
                }
                if (roleId == 2) {
                    BigDecimal var5 = new BigDecimal(commission * myScore);
                    jdData.put("agent", var5.setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
                }
                jdData.put("zk_price", price * 100);
                jdData.put("zk_money", 0);
            }

            jdData.put("shopName",
                    jdJson.getJSONObject("shopInfo").getString("shopName"));
            jdData.put("commissionRate", coms.intValue() * 10);
            jdData.put("volume", jdJson.getInteger("inOrderCount30Days"));
            jdData.put("goodId", jdJson.getLong("skuId"));
            jdData.put("goodName", jdJson.getString("skuName"));
            jdData.put("imgUrl", img.getString("url"));
            jdData.put("istmall", "false");
            jdData.put("price", price * 100);
            jdData.put("jdurl", "http://" + jdJson.getString("materialUrl"));
            arrayData.add(jdData);
        }
        temp.put("data", arrayData);
        temp.put("count", totalCount);
        return temp;
    }

    @Override
    public JSONObject goodLocal(PageParam pageParam, Long uid, Integer status) {
        JSONObject param = new JSONObject();
        param.put("start", pageParam.getStartRow());
        param.put("end", pageParam.getPageSize());
        Boolean isTmall = false;
        List<SysJhJdHot> sysJhTaobaoHots = new ArrayList<>(10);
        Integer count = 0;
        if (status == 1) {
            sysJhTaobaoHots = sysJhTaobaoHotDao.queryPageJd(param);
            count = sysJhTaobaoHotDao.countMaxJd();
        }
        JSONObject data = new JSONObject();

        Userinfo ufo = userinfoMapper.selectByPrimaryKey(uid);
        if (ufo == null) {
            return null;
        }
        Double score = Double.valueOf(ufo.getScore());
        if (sysJhTaobaoHots == null || sysJhTaobaoHots.size() == 0) {
            return data;
        }
        int len = sysJhTaobaoHots.size();
        JSONArray dataArray = new JSONArray();
        if (ufo.getRoleId() == 1) {
            for (int i = 0; i < len; i++) {
                SysJhJdHot dataObj = sysJhTaobaoHots.get(i);
                JSONObject dataJson = GoodUtils.convertLocalTaobao(dataObj);
                //查找指定字符第一次出现的位置
                dataJson.put("zk_money", dataObj.getCoupon() * 100);
                dataJson.put("hasCoupon", 1);
                dataJson.put("zk_price", dataObj.getZkfinalprice().doubleValue() * 100);
                dataJson.put("commissionRate", dataObj.getComssion() * 100);
                dataJson.put("coupon", dataObj.getLink());
//                BigDecimal agent = GoodUtils.commissonAritLocalTaobao(dataObj.getCommissionrate().doubleValue());
                dataJson.put("shopName", dataObj.getShoptitle());
                dataJson.put("istmall", isTmall);
                dataJson.put("agent", dataObj.getCommissionrate().doubleValue() * 100);
                dataJson.put("jdurl", dataObj.getJdurl());
                if (dataObj.getCoupon() != 0) {
                    dataJson.put("hasCoupon", 1);

                } else {
                    dataJson.put("hasCoupon", 0);

                }
                dataArray.add(dataJson);
            }
            data.put("data", dataArray);
            data.put("count", count);
            return data;

        }
        if (ufo.getRoleId() == 2) {
            for (int i = 0; i < len; i++) {

                SysJhJdHot dataObj = sysJhTaobaoHots.get(i);
                JSONObject dataJson = GoodUtils.convertLocalTaobao(dataObj);
                //查找指定字符第一次出现的位置
                dataJson.put("zk_money", dataObj.getCoupon() * 100);
                if (dataObj.getCoupon() != 0) {
                    dataJson.put("hasCoupon", 1);

                } else {
                    dataJson.put("hasCoupon", 0);

                }
                dataJson.put("coupon", dataObj.getLink());

                dataJson.put("zk_price", dataObj.getZkfinalprice().doubleValue() * 100);
                dataJson.put("commissionRate", dataObj.getComssion() * 100);
//                BigDecimal agent = GoodUtils.commissonAritLocalTaobao(dataObj.getCommissionrate().doubleValue());
                dataJson.put("shopName", dataObj.getShoptitle());
                dataJson.put("istmall", isTmall);
                dataJson.put("jdurl", dataObj.getJdurl());

                dataJson.put("agent", dataObj.getCommissionrate().doubleValue() * 100 * score / 100);
                dataArray.add(dataJson);
            }
            data.put("data", dataArray);
            data.put("count", count);
            return data;
        }
        for (int i = 0; i < len; i++) {

            SysJhJdHot dataObj = sysJhTaobaoHots.get(i);
            JSONObject dataJson = GoodUtils.convertLocalTaobao(dataObj);
            //查找指定字符第一次出现的位置
            dataJson.put("coupon", dataObj.getLink());
            dataJson.put("zk_money", dataObj.getCoupon() * 100);
            dataJson.put("hasCoupon", 1);
            dataJson.put("zk_price", dataObj.getZkfinalprice().doubleValue() * 100);
            dataJson.put("commissionRate", dataObj.getComssion() * 100);
            dataJson.put("shopName", dataObj.getShoptitle());
            dataJson.put("istmall", isTmall);
            dataJson.put("jdurl", dataObj.getJdurl());
            if (dataObj.getCoupon() != 0) {
                dataJson.put("hasCoupon", 1);

            } else {
                dataJson.put("hasCoupon", 0);

            }
            dataJson.put("agent", 0);
            dataArray.add(dataJson);
        }
        data.put("data", dataArray);
        data.put("count", count);
//        return
        return data;
    }

    @Override
    public JSONObject goodLocal(PageParam pageParam, Long uid, Integer status, Integer cid) {
        JSONObject param = new JSONObject();
        param.put("start", pageParam.getStartRow());
        param.put("end", pageParam.getPageSize());
        param.put("cid", cid);
        Boolean isTmall = false;
        List<SysJhJdHot> sysJhTaobaoHots = new ArrayList<>(10);
        Integer count = 0;
        if (status == 1) {
            sysJhTaobaoHots = sysJhTaobaoHotDao.queryPageJd(param);
            count = sysJhTaobaoHotDao.countMaxJdCid(cid);
        }
        JSONObject data = new JSONObject();

        Userinfo ufo = userinfoMapper.selectByPrimaryKey(uid);
        if (ufo == null) {
            return null;
        }
        Double score = Double.valueOf(ufo.getScore());
        if (sysJhTaobaoHots == null || sysJhTaobaoHots.size() == 0) {
            return data;
        }
        int len = sysJhTaobaoHots.size();
        JSONArray dataArray = new JSONArray();
        if (ufo.getRoleId() == 1) {
            for (int i = 0; i < len; i++) {
                SysJhJdHot dataObj = sysJhTaobaoHots.get(i);
                JSONObject dataJson = GoodUtils.convertLocalTaobao(dataObj);
                //查找指定字符第一次出现的位置
                dataJson.put("zk_money", dataObj.getCoupon() * 100);
                dataJson.put("hasCoupon", 1);
                dataJson.put("zk_price", dataObj.getZkfinalprice().doubleValue() * 100);
                dataJson.put("commissionRate", dataObj.getCommissionrate().doubleValue() * 100);
//                BigDecimal agent = GoodUtils.commissonAritLocalTaobao(dataObj.getCommissionrate().doubleValue());
                dataJson.put("shopName", dataObj.getShoptitle());
                dataJson.put("istmall", isTmall);
                dataJson.put("agent", dataObj.getComssion().doubleValue() * 100);
                dataJson.put("jdurl", dataObj.getJdurl());
                dataJson.put("coupon", dataObj.getLink());

                if (dataObj.getCoupon() != 0) {
                    dataJson.put("hasCoupon", 1);

                } else {
                    dataJson.put("hasCoupon", 0);

                }
                dataArray.add(dataJson);
            }
            data.put("data", dataArray);
            data.put("count", count);
            return data;

        }
        if (ufo.getRoleId() == 2) {
            for (int i = 0; i < len; i++) {

                SysJhJdHot dataObj = sysJhTaobaoHots.get(i);
                JSONObject dataJson = GoodUtils.convertLocalTaobao(dataObj);
                //查找指定字符第一次出现的位置
                dataJson.put("zk_money", dataObj.getCoupon() * 100);
                if (dataObj.getCoupon() != 0) {
                    dataJson.put("hasCoupon", 1);

                } else {
                    dataJson.put("hasCoupon", 0);

                }
                dataJson.put("coupon", dataObj.getLink());

                dataJson.put("zk_price", dataObj.getZkfinalprice().doubleValue() * 100);
                dataJson.put("commissionRate", dataObj.getCommissionrate().doubleValue() * 100);
//                BigDecimal agent = GoodUtils.commissonAritLocalTaobao(dataObj.getCommissionrate().doubleValue());
                dataJson.put("shopName", dataObj.getShoptitle());
                dataJson.put("istmall", isTmall);
                dataJson.put("jdurl", dataObj.getJdurl());

                dataJson.put("agent", dataObj.getComssion().doubleValue() * 100 * score / 100);
                dataArray.add(dataJson);
            }
            data.put("data", dataArray);
            data.put("count", count);
            return data;
        }
        for (int i = 0; i < len; i++) {
            SysJhJdHot dataObj = sysJhTaobaoHots.get(i);
            JSONObject dataJson = GoodUtils.convertLocalTaobao(dataObj);
            //查找指定字符第一次出现的位置
            dataJson.put("zk_money", dataObj.getCoupon() * 100);
            dataJson.put("hasCoupon", 1);
            dataJson.put("zk_price", dataObj.getZkfinalprice().doubleValue() * 100);
            dataJson.put("commissionRate", dataObj.getCommissionrate().doubleValue() * 100);
            dataJson.put("shopName", dataObj.getShoptitle());
            dataJson.put("istmall", isTmall);

            dataJson.put("jdurl", dataObj.getJdurl());
            if (dataObj.getCoupon() != 0) {
                dataJson.put("hasCoupon", 1);

            } else {
                dataJson.put("hasCoupon", 0);

            }
            dataJson.put("coupon", dataObj.getLink());

            dataJson.put("agent", 0);
            dataArray.add(dataJson);
        }
        data.put("data", dataArray);
        data.put("count", count);
        return data;
    }

    /**
     * 京东商品详情
     * 商品ID
     *
     * @param goodId
     * @return
     */
    @Override
    @FastCache(timeOut = 70)
    public JSONObject jdDetail(@NonNull Long goodId) {
        String apkey = configQueryManager.queryForKey("MiaoAppKey");
        String jdurl1 = URL + "getitemdesc?";

        JSONObject data = new JSONObject();

        Map<String, String> reqMap = new HashMap<>();
        reqMap.put("apkey", apkey);
        reqMap.put("skuid", goodId.toString());
        try {
            String reqUrl  = NetUtils.convertUrlParam(reqMap);
            String res1 = restTemplate.getForObject(jdurl1 + reqUrl, String.class);
            if (JSON.parseObject(res1).getInteger("code") == 200) {
                JSONArray list = JSON.parseObject(res1).getJSONArray("data");
                JSONArray jsonArray = new JSONArray();
                list.forEach(img -> {
                    String url = (String) img;
                    jsonArray.add("http:" + url);
                });
                data.put("list", jsonArray);
                return data;
            }

        } catch (Exception e) {
            log.warning("查询京东商品详情失败");
            e.printStackTrace();
        }
        return null;
    }


}

