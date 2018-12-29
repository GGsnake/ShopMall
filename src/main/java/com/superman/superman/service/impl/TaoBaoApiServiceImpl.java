package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.TboderMapper;
import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.model.Userinfo;
import com.superman.superman.service.TaoBaoApiService;
import com.superman.superman.utils.GoodUtils;
import com.superman.superman.utils.NetUtils;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.TbkDgMaterialOptionalRequest;
import com.taobao.api.request.TbkItemInfoGetRequest;
import com.taobao.api.response.TbkDgMaterialOptionalResponse;
import com.taobao.api.response.TbkItemInfoGetResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by liujupeng on 2018/12/4.
 */
@Service("taoBaoApiService")
public class TaoBaoApiServiceImpl implements TaoBaoApiService {
    private static final String QQAPPKEY = "8k8lhumd";
    private static final String APID = "mm_261060037_253650051_";
    final String URL = "http://tkapi.apptimes.cn/";
    final String TAOBAOURL = "http://gw.api.taobao.com/router/rest";
    final String APPKEY = "25377219";
    final String SECRET = "4c3e6b9e6484ce2982bca63d524564c1";

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    UserinfoMapper userinfoMapper;
    @Autowired
    TboderMapper tboderMapper;
    @Value("${juanhuang.range}")
    Double rangeaa;


    @Override
    public JSONObject serachGoodsAll(TbkDgMaterialOptionalRequest request, Long uid) {
        Userinfo ufo = userinfoMapper.selectByPrimaryKey(uid);
        if (ufo == null) {
            return null;
        }
        request.setAdzoneId(71784050073l);
        Double score = Double.valueOf(ufo.getScore());
        TaobaoClient client = new DefaultTaobaoClient(TAOBAOURL, APPKEY, SECRET);
        JSONObject data = new JSONObject();
        TbkDgMaterialOptionalResponse rsp = null;
        try {
            rsp = client.execute(request);
            JSONArray dataArray = new JSONArray();
            List<TbkDgMaterialOptionalResponse.MapData> resultList = rsp.getResultList();
            if (resultList == null || resultList.size() == 0) {
                return data;
            }
            Boolean isTmall = request.getIsTmall();
            Long count = rsp.getTotalResults();
            if (ufo.getRoleId() == 1) {
                for (int i = 0; i < resultList.size(); i++) {
                    TbkDgMaterialOptionalResponse.MapData dataObj = resultList.get(i);
                    String coupon_info1 = dataObj.getCouponInfo();
                    String coupon_info = null;
                    JSONObject dataJson = GoodUtils.convertTaobao(dataObj);
                    //查找指定字符第一次出现的位置
                    if (coupon_info1 != null && !coupon_info1.equals("")) {
                        int star = coupon_info1.indexOf(20943);//参数为字符的ascii码
                        coupon_info = coupon_info1.substring(star + 1, coupon_info1.length() - 1);
                        dataJson.put("zk_money", Integer.parseInt(coupon_info));
                    } else {
                        dataJson.put("zk_money", 0);
                    }
                    Long commissionRate = Long.valueOf(dataObj.getCommissionRate());
                    dataJson.put("commissionRate", commissionRate);
                    BigDecimal agent = GoodUtils.commissonAritTaobao(dataObj.getZkFinalPrice(), dataObj.getCommissionRate(), rangeaa);
                    dataJson.put("istmall", isTmall);
                    dataJson.put("agent", agent.setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
                    dataArray.add(dataJson);
                }
                data.put("data", dataArray);
                data.put("count", count);
                return data;
            }
            if (ufo.getRoleId() == 2) {
                for (int i = 0; i < resultList.size(); i++) {
                    TbkDgMaterialOptionalResponse.MapData dataObj = resultList.get(i);
                    String coupon_info1 = dataObj.getCouponInfo();
                    JSONObject dataJson = GoodUtils.convertTaobao(dataObj);
                    String coupon_info = null;
                    //查找指定字符第一次出现的位置
                    if (coupon_info1 != null && !coupon_info1.equals("")) {
                        int star = coupon_info1.indexOf(20943);//参数为字符的ascii码
                        coupon_info = coupon_info1.substring(star + 1, coupon_info1.length() - 1);
                        dataJson.put("zk_money", Integer.parseInt(coupon_info));
                    } else {
                        dataJson.put("zk_money", 0);
                    }
                    Long commissionRate = Long.valueOf(dataObj.getCommissionRate());
                    Double var3 = score / 100;
                    BigDecimal var4 = GoodUtils.commissonAritTaobao(dataObj.getZkFinalPrice(), dataObj.getCommissionRate(), rangeaa);
                    BigDecimal agent = var4.multiply(new BigDecimal(var3));
                    dataJson.put("istmall", isTmall);
                    dataJson.put("agent", agent.setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
                    dataJson.put("commissionRate", commissionRate);
                    dataArray.add(dataJson);
                }
                data.put("data", dataArray);
                data.put("count", count);
                return data;
            }
            for (int i = 0; i < resultList.size(); i++) {
                TbkDgMaterialOptionalResponse.MapData dataObj = resultList.get(i);
                //查找指定字符第一次出现的位置
                JSONObject dataJson = GoodUtils.convertTaobao(dataObj);
                String coupon_info1 = dataObj.getCouponInfo();
                String coupon_info = null;
                String commissionRate = dataObj.getCommissionRate();
                if (coupon_info1 != null && !coupon_info1.equals("")) {
                    int star = coupon_info1.indexOf(20943);//参数为字符的ascii码
                    coupon_info = coupon_info1.substring(star + 1, coupon_info1.length() - 1);
                    dataJson.put("zk_money", Integer.parseInt(coupon_info));
                } else {
                    dataJson.put("zk_money", 0);
                }
                dataJson.put("zk_money", coupon_info);
                dataJson.put("istmall", isTmall);
                dataJson.put("agent", 0l);
                dataJson.put("commissionRate", commissionRate);
                dataArray.add(dataJson);
            }
            data.put("data", dataArray);
            data.put("count", count);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return data;
    }
    /**
     * 淘宝搜索首页专用
     * @param request
     * @param uid
     * @return
     */
    @Override
    public JSONObject indexSearch(TbkDgMaterialOptionalRequest request, Long uid) {
        Userinfo ufo = userinfoMapper.selectByPrimaryKey(uid);
        if (ufo == null) {
            return null;
        }
        TaobaoClient client = new DefaultTaobaoClient(TAOBAOURL, APPKEY, SECRET);

        request.setAdzoneId(71784050073l);
        Double score = Double.valueOf(ufo.getScore());
        JSONObject data = new JSONObject();
        TbkDgMaterialOptionalResponse rsp = null;
        try {
            rsp = client.execute(request);
            JSONArray dataArray = new JSONArray();
            List<TbkDgMaterialOptionalResponse.MapData> resultList = rsp.getResultList();
            if (resultList == null || resultList.size() == 0) {
                return data;
            }
            Long count = rsp.getTotalResults();
            if (ufo.getRoleId() == 1) {
                for (int i = 0; i < resultList.size(); i++) {
                    TbkDgMaterialOptionalResponse.MapData dataObj = resultList.get(i);
                    String coupon_info1 = dataObj.getCouponInfo();
                    String coupon_info = null;
                    JSONObject dataJson = GoodUtils.convertTaobao(dataObj);
                    //查找指定字符第一次出现的位置
                    if (coupon_info1 != null && !coupon_info1.equals("")) {
                        int star = coupon_info1.indexOf(20943);//参数为字符的ascii码
                        coupon_info = coupon_info1.substring(star + 1, coupon_info1.length() - 1);
                        dataJson.put("zk_money", Integer.parseInt(coupon_info));
                    } else {
                        dataJson.put("zk_money", 0);
                    }
                    Long commissionRate = Long.valueOf(dataObj.getCommissionRate());
                    dataJson.put("commissionRate", commissionRate);
                    BigDecimal agent = GoodUtils.commissonAritTaobao(dataObj.getZkFinalPrice(), dataObj.getCommissionRate(), rangeaa);
                    dataJson.put("istmall", dataObj.getUserType() == 1 ? true : false);
                    dataJson.put("agent", agent.setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
                    dataArray.add(dataJson);
                }
                data.put("data", dataArray);
                data.put("count", count);
                return data;
            }
            if (ufo.getRoleId() == 2) {
                for (int i = 0; i < resultList.size(); i++) {
                    TbkDgMaterialOptionalResponse.MapData dataObj = resultList.get(i);
                    String coupon_info1 = dataObj.getCouponInfo();
                    JSONObject dataJson = GoodUtils.convertTaobao(dataObj);
                    String coupon_info = null;
                    //查找指定字符第一次出现的位置
                    if (coupon_info1 != null && !coupon_info1.equals("")) {
                        int star = coupon_info1.indexOf(20943);//参数为字符的ascii码
                        coupon_info = coupon_info1.substring(star + 1, coupon_info1.length() - 1);
                        dataJson.put("zk_money", Integer.parseInt(coupon_info));
                    } else {
                        dataJson.put("zk_money", 0);
                    }
                    Long commissionRate = Long.valueOf(dataObj.getCommissionRate());
                    Double var3 = score / 100;
                    BigDecimal var4 = GoodUtils.commissonAritTaobao(dataObj.getZkFinalPrice(), dataObj.getCommissionRate(), rangeaa);
                    BigDecimal agent = var4.multiply(new BigDecimal(var3));
                    dataJson.put("istmall", dataObj.getUserType() == 1 ? true : false);
                    dataJson.put("agent", agent.setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
                    dataJson.put("commissionRate", commissionRate);
                    dataArray.add(dataJson);
                }
                data.put("data", dataArray);
                data.put("count", count);
                return data;
            }
            for (int i = 0; i < resultList.size(); i++) {
                TbkDgMaterialOptionalResponse.MapData dataObj = resultList.get(i);
                //查找指定字符第一次出现的位置
                JSONObject dataJson = GoodUtils.convertTaobao(dataObj);
                String coupon_info1 = dataObj.getCouponInfo();
                String coupon_info = null;
                String commissionRate = dataObj.getCommissionRate();
                if (coupon_info1 != null && !coupon_info1.equals("")) {
                    int star = coupon_info1.indexOf(20943);//参数为字符的ascii码
                    coupon_info = coupon_info1.substring(star + 1, coupon_info1.length() - 1);
                    dataJson.put("zk_money", Integer.parseInt(coupon_info));
                } else {
                    dataJson.put("zk_money", 0);
                }
                dataJson.put("zk_money", coupon_info);
                dataJson.put("istmall", dataObj.getUserType() == 1 ? true : false);
                dataJson.put("agent", 0l);
                dataJson.put("commissionRate", commissionRate);
                dataArray.add(dataJson);
            }
            data.put("data", dataArray);
            data.put("count", count);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 生成淘口令推广链接
     *
     * @param pid
     * @param good_id
     * @return
     */
    @Override
    public JSONObject convertTaobao(@NonNull Long pid, @NonNull Long good_id) {

        String taobaoSercahUrl = URL + "convert/id-to-token?";
        JSONObject temp = new JSONObject();
        Map<String, String> urlSign = new HashMap<>();
        urlSign.put("pid", APID + pid);
        urlSign.put("good_id", String.valueOf(good_id));
        urlSign.put("appkey", QQAPPKEY);

        String linkStringByGet = null;
        try {
            linkStringByGet = NetUtils.createLinkStringByGet(urlSign);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String res = restTemplate.getForObject(taobaoSercahUrl + linkStringByGet, String.class);
        String uland_url = JSON.parseObject(res).getJSONObject("data").getString("uland_url");
        String token = JSON.parseObject(res).getJSONObject("data").getString("token");
        temp.put("uland_url", uland_url);
        temp.put("tkLink", token);
        temp.put("url", null);
        return temp;
    }

    /**
     * 查询淘宝的商品详情
     *
     * @return
     */
    @Override
    public JSONObject deatil(Long goodId) {
        JSONObject var = new JSONObject();
        TaobaoClient client = new DefaultTaobaoClient(TAOBAOURL, APPKEY, SECRET);
        TbkItemInfoGetRequest req = new TbkItemInfoGetRequest();
        req.setNumIids(String.valueOf(goodId));
        req.setPlatform(2L);
        TbkItemInfoGetResponse rsp = null;
        try {
            JSONArray var1 = new JSONArray();
            rsp = client.execute(req);
            if (JSONObject.parseObject(rsp.getBody()).getJSONObject("error_response") != null) {
                var.put("list", var1);
                return var;
            }
            TbkItemInfoGetResponse.NTbkItem results = rsp.getResults().get(0);
            List<String> itemUrl = results.getSmallImages();
            var.put("list", itemUrl);
        } catch (ApiException e) {
            e.printStackTrace();
        }

        return var;
    }

    /**
     * 查询淘宝商品单个的缩略图
     * @param goodId
     * @return
     */
    public String deatilGoodList(Long goodId) {
        TaobaoClient client = new DefaultTaobaoClient(TAOBAOURL, APPKEY, SECRET);
        TbkItemInfoGetRequest req = new TbkItemInfoGetRequest();
        req.setNumIids(goodId.toString());
        req.setPlatform(2L);
        TbkItemInfoGetResponse rsp = null;
        try {
            rsp = client.execute(req);
            List<TbkItemInfoGetResponse.NTbkItem> results = rsp.getResults();
            if (results == null) {
                return null;
            }

            return results.get(0).getPictUrl();
        } catch (ApiException e) {
            e.printStackTrace();
        }

        return null;
    }
}
