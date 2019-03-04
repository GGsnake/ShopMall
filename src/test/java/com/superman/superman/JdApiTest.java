//package com.superman.superman;
//
//import com.alibaba.fastjson.JSONObject;
//import com.superman.superman.dao.PayDao;
//import com.superman.superman.dao.SysJhTaobaoHotDao;
//import com.superman.superman.model.SysJhTaobaoHot;
//import com.superman.superman.service.TaoBaoApiService;
//import com.taobao.api.DefaultTaobaoClient;
//import com.taobao.api.TaobaoClient;
//import com.taobao.api.request.TbkDgMaterialOptionalRequest;
//import com.taobao.api.request.TbkItemInfoGetRequest;
//import com.taobao.api.response.TbkDgMaterialOptionalResponse;
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
//
//    @Autowired
//    TaoBaoApiService taoBaoApiService;
//
//    @Autowired
//    SysJhTaobaoHotDao sysJhTaobaoHotDao;
//
//    //通过
//    @Test
//    public void convertTkl() {
//        taoBaoApiService.convertTaobaoTkl("￥vxfnbIAP8uA￥");
//    }
//    //通过
//    @Test
//    public void sortcoms() {
//        List<SysJhTaobaoHot> sysJhTaobaoHots = sysJhTaobaoHotDao.queryAllCommissionRate();
//        for (int i=0 ;i<sysJhTaobaoHots.size();i++) {
//            SysJhTaobaoHot sysJhTaobaoHot = new SysJhTaobaoHot();
//            sysJhTaobaoHot.setOrderCommiss(i+1);
//            sysJhTaobaoHot.setId(sysJhTaobaoHots.get(i).getId());
//            sysJhTaobaoHotDao.updateCommissionRate(sysJhTaobaoHot);
//        }
//    }
//    //通过
//    @Test
//    public void sortVol() {
//        List<SysJhTaobaoHot> sysJhTaobaoHots = sysJhTaobaoHotDao.queryAllvolume();
//        for (int i=0 ;i<sysJhTaobaoHots.size();i++) {
//            JSONObject data=new JSONObject();
//            data.put("orderVolume",i+1);
//            data.put("id",sysJhTaobaoHots.get(i).getId());
//            sysJhTaobaoHotDao.updateorderVolume(data);
//        }
//
//    }
//    //通过
//    @Test
//    public void sortCopu() {
//        List<SysJhTaobaoHot> sysJhTaobaoHots = sysJhTaobaoHotDao.queryAllcoupon();
//        for (int i=0 ;i<sysJhTaobaoHots.size();i++) {
//            JSONObject data=new JSONObject();
//            data.put("orderCoupon",i+1);
//            data.put("id",sysJhTaobaoHots.get(i).getId());
//            sysJhTaobaoHotDao.updateorderCoupon(data);
//        }
//    }
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
//                    Double commiss=commissonAritTaobao(mapData.getZkFinalPrice(), mapData.getCommissionRate(),couple).doubleValue()/10;
//
//                    if (mapData.getVolume() > 100 && commiss> 3 && couple > 15) {
//                        log.warning(mapData.getTitle());
//                        Map<String,Object> h=new HashMap<>();
//                        h.put("pictUrl",mapData.getPictUrl());
//                        h.put("shopTitle",mapData.getShopTitle());
//                        h.put("title",mapData.getTitle());
//                        h.put("commissionRate",commiss);
//                        long comssion = new BigDecimal(mapData.getCommissionRate()).longValue();
//                        h.put("comssion",comssion);
//                        h.put("coupon",couple);
//                        h.put("zkFinalPrice",  new BigDecimal(mapData.getZkFinalPrice()).doubleValue());
//                        h.put("volume",mapData.getVolume().intValue());
//                        h.put("numIid",mapData.getNumIid());
//                        h.put("istamll",mapData.getUserType());
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
//                    Double commiss=commissonAritTaobao(mapData.getZkFinalPrice(), mapData.getCommissionRate(),couple).doubleValue()/10;
//                    Integer cc = Integer.parseInt(couponInfo);
//
//                    if (mapData.getVolume() > 100 && commiss> 3 && couple > 15) {
//                        log.warning(mapData.getTitle());
//                        Map<String,Object> h=new HashMap<>();
//                        h.put("pictUrl",mapData.getPictUrl());
//                        h.put("shopTitle",mapData.getShopTitle());
//                        h.put("title",mapData.getTitle());
//                        h.put("commissionRate",commiss);
//                        long comssion = new BigDecimal(mapData.getCommissionRate()).longValue();
//                        h.put("comssion",comssion);
//                        h.put("coupon",couple);
//                        h.put("zkFinalPrice",  new BigDecimal(mapData.getZkFinalPrice()).doubleValue());
//                        h.put("volume",mapData.getVolume().intValue());
//                        h.put("numIid",mapData.getNumIid());
//                        h.put("istamll",mapData.getUserType());
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
//    public static BigDecimal commissonAritTaobao(String zk, String rate,Integer copuon) {
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
