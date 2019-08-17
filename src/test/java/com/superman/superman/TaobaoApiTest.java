//package com.superman.superman;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.superman.superman.dao.PayDao;
//import com.superman.superman.dao.SysJhTaobaoHotDao;
//import com.superman.superman.model.SysJhTaobaoHot;
//import com.superman.superman.service.TaoBaoApiService;
//import com.superman.superman.utils.GoodUtils;
//import com.taobao.api.ApiException;
//import com.taobao.api.DefaultTaobaoClient;
//import com.taobao.api.TaobaoClient;
//import com.taobao.api.request.TbkDgItemCouponGetRequest;
//import com.taobao.api.request.TbkDgMaterialOptionalRequest;
//import com.taobao.api.request.TbkDgOptimusMaterialRequest;
//import com.taobao.api.request.TbkItemInfoGetRequest;
//import com.taobao.api.response.TbkDgItemCouponGetResponse;
//import com.taobao.api.response.TbkDgMaterialOptionalResponse;
//import com.taobao.api.response.TbkDgOptimusMaterialResponse;
//import lombok.extern.java.Log;
//import netscape.javascript.JSObject;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.math.BigDecimal;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Log
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class TaobaoApiTest {
//    @Value("${juanhuang.range}")
//    private Double RANGE;
//    @Value("${miao.apkey}")
//    private String APKEY;
//    @Value("${miao.tbname}")
//    private String TBNAME;
//    @Value("${miao.url}")
//    private String URL;
//    @Value("${tb.appkey}")
//    private String APPKEY;
//    @Value("${tb.secret}")
//    private String SECRET;
//    @Value("${tb.adzoneid}")
//    private String APPID;
//    @Value("${tb.pid}")
//    private Long PID;
//    @Value("${tb.api-url}")
//    private String TAOBAOURL;
////
////    @Autowired
////    TaoBaoApiService taoBaoApiService;
//
//    @Autowired
//    SysJhTaobaoHotDao sysJhTaobaoHotDao;
//
//    //
////    //通过
////    @Test
////    public void convertTkl() {
////        taoBaoApiService.convertTaobaoTkl("￥vxfnbIAP8uA￥");
////    }
//    //通过
//    @Test
//    public void sortcoms() {
//        List<SysJhTaobaoHot> sysJhTaobaoHots = sysJhTaobaoHotDao.queryAllCommissionRate();
//        for (int i = 0; i < sysJhTaobaoHots.size(); i++) {
//            SysJhTaobaoHot sysJhTaobaoHot = new SysJhTaobaoHot();
//            sysJhTaobaoHot.setOrderCommiss(i + 1);
//            sysJhTaobaoHot.setId(sysJhTaobaoHots.get(i).getId());
//            sysJhTaobaoHotDao.updateCommissionRate(sysJhTaobaoHot);
//        }
//    }
//
//    //通过
//    @Test
//    public void sortVol() {
//        List<SysJhTaobaoHot> sysJhTaobaoHots = sysJhTaobaoHotDao.queryAllvolume();
//        for (int i = 0; i < sysJhTaobaoHots.size(); i++) {
//            JSONObject data = new JSONObject();
//            data.put("orderVolume", i + 1);
//            data.put("id", sysJhTaobaoHots.get(i).getId());
//            sysJhTaobaoHotDao.updateorderVolume(data);
//        }
//
//    }
//
//    //通过
//    @Test
//    public void sortCopu() {
//        List<SysJhTaobaoHot> sysJhTaobaoHots = sysJhTaobaoHotDao.queryAllcoupon();
//        for (int i = 0; i < sysJhTaobaoHots.size(); i++) {
//            JSONObject data = new JSONObject();
//            data.put("orderCoupon", i + 1);
//            data.put("id", sysJhTaobaoHots.get(i).getId());
//            sysJhTaobaoHotDao.updateorderCoupon(data);
//        }
//    }
//    @Test
//    public void query() throws ApiException {
//        TaobaoClient client = new DefaultTaobaoClient(TAOBAOURL, APPKEY, SECRET);
//
//        for (long i = 1; i < 50; i++) {
//            try {
//                Thread.sleep(300);
//            } catch (InterruptedException e) {
//                e.printStackTrace();}
//                TbkDgOptimusMaterialRequest req = new TbkDgOptimusMaterialRequest();
//                req.setPageSize(30L);
//                req.setAdzoneId(92706150203l);
//                req.setPageNo(i);
//                req.setMaterialId(4071l);
//                TbkDgOptimusMaterialResponse rsp = client.execute(req);
//                JSONArray jsonObject = JSONObject.parseObject(rsp.getBody()).getJSONObject("tbk_dg_optimus_material_response").getJSONObject("result_list").getJSONArray("map_data");
//                jsonObject.forEach(temp -> {
//                    JSONObject mapData = (JSONObject) temp;
//                    Integer coupon_amount = mapData.getInteger("coupon_amount");
//                    String commission_rate = mapData.getString("commission_rate");
//                    Double comssion = new BigDecimal(commission_rate).doubleValue()/100;
//                    Double orig_price = new BigDecimal(mapData.getString("orig_price")).doubleValue();
//                    Map<String, Object> h = new HashMap<>();
//                    h.put("pictUrl", "http:" + mapData.getString("pict_url"));
//                    h.put("shopTitle", mapData.getString("nick"));
//                    h.put("title", mapData.getString("title"));
//                    if (coupon_amount == 0) {
//                        h.put("commissionRate", orig_price *comssion);
//                        h.put("zkFinalPrice", orig_price);
//                    } else {
//                        Double zk = orig_price * 100 - coupon_amount * 100;
//                        h.put("commissionRate", (zk * comssion) / 100);
//                        h.put("zkFinalPrice", zk / 100);
//                    }
//                    h.put("comssion", comssion * 10);
//
//                    h.put("coupon", coupon_amount);
//                    h.put("volume", mapData.getInteger("sell_num"));
//                    h.put("numIid", mapData.getLong("item_id"));
//                    h.put("istamll", mapData.getInteger("user_type"));
//                    h.put("opt", 6);
//                    sysJhTaobaoHotDao.saveopt(h);
//                });
//            }
//        }
//
//
//    @Test
//    public void search() {
//
//        TaobaoClient client = new DefaultTaobaoClient(TAOBAOURL, APPKEY, SECRET);
//        for (long i = 1; i < 5000; i++) {
//            try {
//                Thread.sleep(300);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            TbkDgMaterialOptionalRequest request = new TbkDgMaterialOptionalRequest();
//            request.setPageSize(100l);
//            request.setPageNo(i);
//            request.setAdzoneId(92706150203l);
//            request.setHasCoupon(true);
//            request.setCat("16,15,14,13,12,11,10,9,8,7");
////            request.setCat("50016348,50026523,50025705,21,19,29,50010404,16,50002766,50008090");
////            request.setCat("50016348,50026523,50025705,21,19,29,50010404,16,50002766,50008090");
//            TbkDgMaterialOptionalResponse rsp = null;
//            try {
//                rsp = client.execute(request);
//                List<TbkDgMaterialOptionalResponse.MapData> resultList = rsp.getResultList();
//                resultList.forEach(mapData -> {
//                    String couponInfo = mapData.getCouponInfo();
//                    int star = mapData.getCouponInfo().indexOf(20943);//参数为字符的ascii码
//                    couponInfo = mapData.getCouponInfo().substring(star + 1, couponInfo.length() - 1);
//                    Integer couple = Integer.parseInt(couponInfo);
//                    Double commiss = commissonAritTaobao(mapData.getZkFinalPrice(), mapData.getCommissionRate(), couple).doubleValue() / 10;
//
////                    if (mapData.getVolume() > 100 && commiss > 3 && couple > 100) {
//                    if (couple > 100) {
//                        log.warning(mapData.getTitle());
//                        Map<String, Object> h = new HashMap<>();
//                        h.put("pictUrl", mapData.getPictUrl());
//                        h.put("shopTitle", mapData.getShopTitle());
//                        h.put("title", mapData.getTitle());
//                        h.put("commissionRate", commiss);
//                        long comssion = new BigDecimal(mapData.getCommissionRate()).longValue();
//                        h.put("comssion", comssion);
//                        h.put("coupon", couple);
//                        h.put("zkFinalPrice", new BigDecimal(mapData.getZkFinalPrice()).doubleValue());
//                        h.put("volume", mapData.getVolume().intValue());
//                        h.put("numIid", mapData.getNumIid());
//                        h.put("istamll", mapData.getUserType());
//                        sysJhTaobaoHotDao.saveJd(h);
//
//                    }
//                });
//            } catch (Exception e) {
//
//            }
//        }
//
//    }
//
//    @Test
//    public void search123() {
//
//        TaobaoClient client = new DefaultTaobaoClient(TAOBAOURL, APPKEY, SECRET);
//        for (long i = 1; i < 2; i++) {
//            try {
//                Thread.sleep(300);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            TbkDgMaterialOptionalRequest request = new TbkDgMaterialOptionalRequest();
//            request.setPageSize(100l);
//            request.setPageNo(i);
//            request.setAdzoneId(92706150203l);
//            request.setHasCoupon(true);
//            request.setIsTmall(true);
//            request.setQ("小巨蛋");
////            request.setCat("16,15,14,13,12,11,10,9,8,7");
////            request.setCat("8,20,21,30,14,50012164,29,5,16,50002766");
//            TbkDgMaterialOptionalResponse rsp = null;
//            try {
//                rsp = client.execute(request);
//
//                List<TbkDgMaterialOptionalResponse.MapData> resultList = rsp.getResultList();
//                resultList.forEach(mapData -> {
//                    String couponInfo = mapData.getCouponInfo();
//                    int star = mapData.getCouponInfo().indexOf(20943);//参数为字符的ascii码
//                    couponInfo = mapData.getCouponInfo().substring(star + 1, couponInfo.length() - 1);
//                    Integer couple = Integer.parseInt(couponInfo);
//                    Double commiss = commissonAritTaobao(mapData.getZkFinalPrice(), mapData.getCommissionRate(), couple).doubleValue() / 10;
//                    Integer cc = Integer.parseInt(couponInfo);
//
//                    if (mapData.getVolume() > 100 && commiss > 3 && couple > 15) {
//                        log.warning(mapData.getTitle());
//                        Map<String, Object> h = new HashMap<>();
//                        h.put("pictUrl", mapData.getPictUrl());
//                        h.put("shopTitle", mapData.getShopTitle());
//                        h.put("title", mapData.getTitle());
//                        h.put("commissionRate", commiss);
//                        long comssion = new BigDecimal(mapData.getCommissionRate()).longValue();
//                        h.put("comssion", comssion);
//                        h.put("coupon", couple);
//                        h.put("zkFinalPrice", new BigDecimal(mapData.getZkFinalPrice()).doubleValue());
//                        h.put("volume", mapData.getVolume().intValue());
//                        h.put("numIid", mapData.getNumIid());
//                        h.put("istamll", mapData.getUserType());
//                        sysJhTaobaoHotDao.save(h);
//
//                    }
//                });
//            } catch (Exception e) {
//
//            }
//        }
//
//    }
//    @Test
//    public void search1() {
//
//        TaobaoClient client = new DefaultTaobaoClient(TAOBAOURL, APPKEY, SECRET);
//        for (long i = 1; i < 2; i++) {
//            try {
//                Thread.sleep(300);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            TbkDgMaterialOptionalRequest request = new TbkDgMaterialOptionalRequest();
//            request.setPageSize(100l);
//            request.setPageNo(i);
//            request.setAdzoneId(92706150203l);
//            request.setHasCoupon(true);
//            request.setIsTmall(true);
////            request.setQ("小巨蛋");
//            request.setCat("16,15,14,13,12,11,10,9,8,7");
////            request.setCat("8,20,21,30,14,50012164,29,5,16,50002766");
//            TbkDgMaterialOptionalResponse rsp = null;
//            try {
//                rsp = client.execute(request);
//
//                List<TbkDgMaterialOptionalResponse.MapData> resultList = rsp.getResultList();
//                resultList.forEach(mapData -> {
//                    String couponInfo = mapData.getCouponInfo();
//                    int star = mapData.getCouponInfo().indexOf(20943);//参数为字符的ascii码
//                    couponInfo = mapData.getCouponInfo().substring(star + 1, couponInfo.length() - 1);
//                    Integer couple = Integer.parseInt(couponInfo);
//                    Double commiss = commissonAritTaobao(mapData.getZkFinalPrice(), mapData.getCommissionRate(), couple).doubleValue() / 10;
//                    Integer cc = Integer.parseInt(couponInfo);
//
//                    if (mapData.getVolume() > 100 && commiss > 3 && couple > 15) {
//                        log.warning(mapData.getTitle());
//                        Map<String, Object> h = new HashMap<>();
//                        h.put("pictUrl", mapData.getPictUrl());
//                        h.put("shopTitle", mapData.getShopTitle());
//                        h.put("title", mapData.getTitle());
//                        h.put("commissionRate", commiss);
//                        long comssion = new BigDecimal(mapData.getCommissionRate()).longValue();
//                        h.put("comssion", comssion);
//                        h.put("coupon", couple);
//                        h.put("zkFinalPrice", new BigDecimal(mapData.getZkFinalPrice()).doubleValue());
//                        h.put("volume", mapData.getVolume().intValue());
//                        h.put("numIid", mapData.getNumIid());
//                        h.put("istamll", mapData.getUserType());
//                        sysJhTaobaoHotDao.save(h);
//
//                    }
//                });
//            } catch (Exception e) {
//
//            }
//        }
//
//    }
//
//    @Test
//    public void search2() throws InterruptedException, ApiException {
//        for (long i = 1; i < 1000; i++) {
//            Thread.sleep(350);
//            TaobaoClient client = new DefaultTaobaoClient(TAOBAOURL, APPKEY, SECRET);
////        TaobaoClient client = new DefaultTaobaoClient(TAOBAOURL, "25377219", "4c3e6b9e6484ce2982bca63d524564c1");
//
//            TbkDgMaterialOptionalRequest request = new TbkDgMaterialOptionalRequest();
////            TbkDgOptimusMaterialRequest req = new TbkDgOptimusMaterialRequest();
//            request.setPageSize(60l);
//            request.setPageNo(i);
//            request.setAdzoneId(92706150203l);
//            request.setHasCoupon(true);
////            request.setAdzoneId(71784050073l);
////            request.setHasCoupon(true);
////            request.setIsTmall(true);
////            request.setQ("小巨蛋");
//            request.setCat("7,6,4,5");
////            request.setCat("8,20,                                                                                                                                                                                                                             21,30,14,50012164,29,5,16,50002766");
////            request.setCat("50016348,50026523,50025705,21,19,29,50010404,16,50002766,50008090");
//            TbkDgMaterialOptionalResponse rsp = null;
//            try {
//                rsp = client.execute(request);
//
//                List<TbkDgMaterialOptionalResponse.MapData> resultList = rsp.getResultList();
//                resultList.forEach(mapData -> {
//                    String couponInfo = mapData.getCouponInfo();
//                    int star = mapData.getCouponInfo().indexOf(20943);//参数为字符的ascii码
//                    couponInfo = mapData.getCouponInfo().substring(star + 1, couponInfo.length() - 1);
//                    Integer couple = Integer.parseInt(couponInfo);
//                    Double commiss=commissonAritTaobao(mapData.getZkFinalPrice(), mapData.getCommissionRate(),couple).doubleValue()/10;
//                    if (mapData.getVolume() > 5 && couple >100) {
//                        log.warning(mapData.getTitle());
//                        Map<String,Object> h=new HashMap<>();
//                        h.put("pictUrl",mapData.getPictUrl());
//                        h.put("shopTitle",mapData.getShopTitle());
//                        h.put("title",mapData.getTitle());
//                        h.put("commissionRate",commiss);
//                        long comssion = new BigDecimal(mapData.getCommissionRate()).longValue();
//                        h.put("comssion",comssion);
//                        h.put("coupon",couponInfo);
//                        h.put("zkFinalPrice",  new BigDecimal(mapData.getZkFinalPrice()).doubleValue());
//                        h.put("volume",mapData.getVolume().intValue());
//                        h.put("numIid",mapData.getNumIid());
//                        h.put("istamll",mapData.getUserType());
//                        h.put("opt",0);
//                        sysJhTaobaoHotDao.saveopt(h);
//
//                    }
//                });
//
//
//            } catch (Exception e) {
//
//            }
//        }
////        TbkDgOptimusMaterialRequest req = new TbkDgOptimusMaterialRequest();
////        for (long i = 1; i < 111; i++) {
////
////                Thread.sleep(300);
////                req.setMaterialId(4071l);
////                req.setPageNo(i);
////                req.setPageSize(20l);
////                req.setAdzoneId(92706150203l);
////
////                TaobaoClient client = new DefaultTaobaoClient(TAOBAOURL, APPKEY, SECRET);
////                TbkDgOptimusMaterialResponse rsp = null;
////
////                    rsp = client.execute(req);
////                    List<TbkDgOptimusMaterialResponse.MapData> resultList = rsp.getResultList();
////                    if (resultList == null || resultList.size() == 0) {
////
////                    }
////                    for (int j = 0; j < resultList.size(); j++) {
////                        TbkDgOptimusMaterialResponse.MapData dataObj = resultList.get(j);
////                        Long coupon_info1 = dataObj.getCouponAmount();
////                     if (coupon_info1!=0){
////                         log.warning(dataObj.getItemId().toString());
////                     }
////
////                    }
////
////
////
////        }
//    }
//
//
////            if (ufo.getRoleId() == 1) {
////                for (int i = 0; i < resultList.size(); i++) {
////                    TbkDgOptimusMaterialResponse.MapData dataObj = resultList.get(i);
////                    Long coupon_info1 = dataObj.getCouponAmount();
////                    JSONObject dataJson = new JSONObject();
////                    Integer couple = coupon_info1.intValue() * 100;
////                    dataJson.put("zk_money", couple);
////                   else {
////                        BigDecimal var4 = GoodUtils.commissonAritTaobao(dataObj.getZkFinalPrice(), dataObj.getCommissionRate());
////                        dataJson.put("agent", var4.setScale(1, BigDecimal.ROUND_DOWN).doubleValue() * 10);
////                    }
////                    dataJson.put("price", Double.valueOf(dataObj.getZkFinalPrice()) * 100);
////                    dataJson.put("volume", dataObj.getVolume());
////                    dataJson.put("goodId", dataObj.getItemId());
////                    dataJson.put("imgUrl", dataObj.getPictUrl());
////                    dataJson.put("goodName", dataObj.getTitle());
////                    dataJson.put("hasCoupon", 1);
////                    dataJson.put("zk_price", Double.valueOf(dataObj.getZkFinalPrice()) * 100 - couple);
////
////                    Long commissionRate = Long.valueOf(dataObj.getCommissionRate());
////                    dataJson.put("commissionRate", commissionRate / 10);
////                    dataJson.put("shopName", dataObj.getShopTitle());
////                    dataJson.put("istmall", dataObj.getUserType() == 1 ? true : false);
////                    dataArray.add(dataJson);
////                }
////                data.put("data", dataArray);
////                data.put("count", 10);
////                return data;
////            }
//
//
//    public static BigDecimal commissonAritTaobao(String zk, String rate, Integer copuon) {
//        BigDecimal var0 = new BigDecimal(zk);
//        BigDecimal var1 = new BigDecimal(copuon);
//        BigDecimal divide = var0.subtract(var1);
//        long rate1 = new BigDecimal(rate).longValue();
//        Double var4 = rate1 / 1000d;
//        Double var5 = divide.doubleValue() * var4;
//        return new BigDecimal(var5).setScale(2, BigDecimal.ROUND_DOWN);
//    }
//
//
//}
//
