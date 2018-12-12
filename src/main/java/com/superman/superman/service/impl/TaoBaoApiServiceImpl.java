package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.model.Userinfo;
import com.superman.superman.service.TaoBaoApiService;
import com.superman.superman.utils.HttpRequest;
import com.superman.superman.utils.NetUtils;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.NTbkItem;
import com.taobao.api.request.TbkDgMaterialOptionalRequest;
import com.taobao.api.request.TbkItemGetRequest;
import com.taobao.api.response.TbkItemGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by liujupeng on 2018/12/4.
 */
@Service("taoBaoApiService")
public class TaoBaoApiServiceImpl implements TaoBaoApiService {
    final String URL = "http://tkapi.apptimes.cn/";
    final String TAOBAOURL = "http://tkapi.apptimes.cn/";
    final String APPKEY = "25338125";
    final String SECRET = "c4d36be247e477a9d88704f022e1c514";
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    UserinfoMapper userinfoMapper;

    @Override
    public JSONObject serachGoods(Long uid, String Keywords, String cat, Boolean isTmall, Long page_no, Long page_size, String sort, String itemloc) {
//        Userinfo ufo = userinfoMapper.selectByPrimaryKey(uid);
//        if (ufo == null) {
//            return null;
//        }
//        String taobaoSercahUrl = url + "good/list?";
//        Map<String, String> urlSign = new HashMap<>();
//        urlSign.put("q", "裤子");
//        urlSign.put("page_size", "15");
//        urlSign.put("page_no", "1");
//        urlSign.put("has_coupon", "true");
//        urlSign.put("sort", "tk_rate_des");
//        JSONObject data = new JSONObject();
//
//        String linkStringByGet = null;
//        try {
//            linkStringByGet = NetUtils.createLinkStringByGet(urlSign);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        String res = restTemplate.getForObject(taobaoSercahUrl + linkStringByGet, String.class);
//        JSONArray good_list = JSONObject.parseObject(res).getJSONObject("data").getJSONArray("good_list");
//        Integer count = JSONObject.parseObject(res).getJSONObject("data").getInteger("count");
//        JSONArray dataArray = new JSONArray();
//
//        if (ufo.getRoleId() == 1) {
//            for (int i = 0; i < good_list.size(); i++) {
//
//                JSONObject dataObj = (JSONObject) good_list.get(i);
//                JSONObject dataJson = new JSONObject();
//                String coupon_info1 = dataObj.getString("coupon_info");
//                //查找指定字符第一次出现的位置
//                int star = coupon_info1.indexOf(20943);//参数为字符的ascii码
//
//                String coupon_info = dataObj.getString("coupon_info").substring(star+1,coupon_info1.length()-1);
//
//                dataJson.put("zk_price", Double.valueOf(dataObj.getString("zk_final_price")).intValue());
//                dataJson.put("price", Double.valueOf(dataObj.getString("reserve_price")).intValue());
//                dataJson.put("volume", dataObj.getInteger("volume").toString());
//                dataJson.put("goodId", dataObj.getInteger("num_iid"));
//                dataJson.put("imgUrl", dataObj.getString("pict_url"));
//                dataJson.put("goodName", dataObj.getString("title"));
//                dataJson.put("istmall", false);
//                dataJson.put("zk_money", Integer.parseInt(coupon_info));
//                dataJson.put("agent", 111l);
//                dataArray.add(dataJson);
//            }
//
//            data.put("data", dataArray);
//
//            data.put("count", count);
//        }
//
        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        TbkDgMaterialOptionalRequest req = new TbkDgMaterialOptionalRequest();
        req.setStartDsr(10L);
        req.setPageSize(20L);
        req.setPageNo(1L);
        req.setPlatform(1L);
        req.setEndTkRate(1234L);
        req.setStartTkRate(1234L);
        req.setEndPrice(10L);
        req.setStartPrice(10L);
        req.setIsOverseas(false);
        req.setIsTmall(false);
        req.setSort("tk_rate_des");
        req.setItemloc("杭州");
        req.setCat("16,18");
        req.setQ("女装");
        req.setMaterialId(2836L);
        req.setHasCoupon(false);
        req.setIp("13.2.33.4");
        req.setAdzoneId(123L);
        req.setNeedFreeShipment(true);
        req.setNeedPrepay(true);
        req.setIncludePayRate30(true);
        req.setIncludeGoodRate(true);
        req.setIncludeRfdRate(true);
        req.setNpxLevel(2L);
        req.setEndKaTkRate(1234L);
        req.setStartKaTkRate(1234L);
        req.setDeviceEncrypt("MD5");
        req.setDeviceValue("xxx");
        req.setDeviceType("IMEI");
        TbkDgMaterialOptionalResponse rsp = client.execute(req);
        System.out.println(rsp.getBody());
        return data;
    }
}
