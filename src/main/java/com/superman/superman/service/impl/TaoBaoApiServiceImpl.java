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
import com.taobao.api.response.TbkDgMaterialOptionalResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
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

    @Override
    public JSONObject serachGoods(Long uid, String Keywords, String cat, Boolean isTmall, Boolean HasCoupon, Long page_no, Long page_size, String sort, String itemloc) {
        Userinfo ufo = userinfoMapper.selectByPrimaryKey(uid);
        if (ufo == null) {
            return null;
        }
        TaobaoClient client = new DefaultTaobaoClient(TAOBAOURL, APPKEY, SECRET);
        TbkDgMaterialOptionalRequest req = new TbkDgMaterialOptionalRequest();
        Integer score = ufo.getScore();
        req.setPageSize(page_size);
        req.setPageNo(page_no);
        req.setIsTmall(isTmall);
        req.setCat(cat);
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
                    String coupon_info1 = dataObj.getCouponInfo();
                    //查找指定字符第一次出现的位置
                    int star = coupon_info1.indexOf(20943);//参数为字符的ascii码
                    String coupon_info = coupon_info1.substring(star + 1, coupon_info1.length() - 1);
                    String commissionRate = dataObj.getCommissionRate();
                    JSONObject dataJson = GoodUtils.convertTaobao(dataObj);
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
                    String coupon_info1 = dataObj.getCouponInfo();
                    //查找指定字符第一次出现的位置
                    int star = coupon_info1.indexOf(20943);//参数为字符的ascii码
                    String coupon_info = coupon_info1.substring(star + 1, coupon_info1.length() - 1);
                    String commissionRate = dataObj.getCommissionRate();
                    JSONObject dataJson = GoodUtils.convertTaobao(dataObj);
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
                String var1 = dataObj.getCouponInfo();
                //查找指定字符第一次出现的位置
                int star = var1.indexOf(20943);//参数为字符的ascii码
                Integer coupon_info = Integer.valueOf(var1.substring(star + 1, var1.length() - 1));
                String commissionRate = dataObj.getCommissionRate();
                //TODO佣金
                JSONObject dataJson = GoodUtils.convertTaobao(dataObj);
                dataJson.put("zk_money", coupon_info);

                dataJson.put("istmall", isTmall.toString());
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

    @Override
    public JSONObject serachGoodsAll(TbkDgMaterialOptionalRequest request, Long uid) {
        Userinfo ufo = userinfoMapper.selectByPrimaryKey(uid);
        if (ufo == null) {
            return null;
        }
        request.setAdzoneId(71784050073l);

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
                    if (coupon_info1 != null&& !coupon_info1.equals("")) {
                        int star = coupon_info1.indexOf(20943);//参数为字符的ascii码
                        coupon_info = coupon_info1.substring(star + 1, coupon_info1.length() - 1);
                        dataJson.put("zk_money", Integer.parseInt(coupon_info));
                    }
                    else {
                        dataJson.put("zk_money", 0);
                    }
                    String commissionRate = dataObj.getCommissionRate();
                    dataJson.put("istmall", isTmall);
                    dataJson.put("agent", 111l);
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
                    //查找指定字符第一次出现的位置
                    int star = coupon_info1.indexOf(20943);//参数为字符的ascii码
                    String coupon_info = coupon_info1.substring(star + 1, coupon_info1.length() - 1);
                    String commissionRate = dataObj.getCommissionRate();
                    JSONObject dataJson = GoodUtils.convertTaobao(dataObj);
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
                String var1 = dataObj.getCouponInfo();
                //查找指定字符第一次出现的位置
                int star = var1.indexOf(20943);//参数为字符的ascii码
                Integer coupon_info = Integer.valueOf(var1.substring(star + 1, var1.length() - 1));
                String commissionRate = dataObj.getCommissionRate();
                //TODO佣金
                JSONObject dataJson = GoodUtils.convertTaobao(dataObj);
                dataJson.put("zk_money", coupon_info);

                dataJson.put("istmall", isTmall.toString());
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

    public JSONObject indexGoodList(Integer type, Long page_no, Long page_size) {
        JSONObject data = new JSONObject();
        JSONArray dataArray = new JSONArray();
        TaobaoClient client = new DefaultTaobaoClient(TAOBAOURL, APPKEY, SECRET);
        TbkDgMaterialOptionalRequest req = new TbkDgMaterialOptionalRequest();
        req.setAdzoneId(71784050073L);
        if (type == 1) {
            req.setQ("9.9元包邮");
        }
        if (type == 2) {
            req.setQ("9.9元包邮");
        }
        if (type == 3) {
            req.setQ("9.9元包邮");
        }
        if (type == 4) {
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
                String coupon_info1 = dataObj.getCouponInfo();
                //查找指定字符第一次出现的位置
                int star = coupon_info1.indexOf(20943);//参数为字符的ascii码
                Integer coupon_info = Integer.valueOf(coupon_info1.substring(star + 1, coupon_info1.length() - 1));
                String commissionRate = dataObj.getCommissionRate();
                JSONObject dataJson = GoodUtils.convertTaobao(dataObj);
                dataJson.put("istmall", null);
                dataJson.put("zk_money", coupon_info);
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

    @Override
    public Long countWaitTb(@NonNull List list) {
        Long count = tboderMapper.selectPidInTb(list);
        return count;
    }
}
