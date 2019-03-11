package com.superman.superman;//package com.superman.superman;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.PayDao;
import com.superman.superman.dao.SysJhTaobaoHotDao;
import com.superman.superman.model.SysJhTaobaoHot;
import com.superman.superman.service.TaoBaoApiService;
import com.superman.superman.utils.net.NetUtils;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.TbkDgMaterialOptionalRequest;
import com.taobao.api.request.TbkItemInfoGetRequest;
import com.taobao.api.response.TbkDgMaterialOptionalResponse;
import lombok.extern.java.Log;
import netscape.javascript.JSObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log
@RunWith(SpringRunner.class)
@SpringBootTest
public class JdApiTest {
    @Value("${juanhuang.range}")
    private Double RANGE;
    @Value("${miao.apkey}")
    private String APKEY;
    @Value("${miao.tbname}")
    private String TBNAME;
    @Value("${miao.url}")
    private String URL;
    @Value("${tb.appkey}")
    private String APPKEY;
    @Value("${tb.secret}")
    private String SECRET;
    @Value("${tb.adzoneid}")
    private String APPID;
    @Value("${tb.pid}")
    private Long PID;
    @Value("${tb.api-url}")
    private String TAOBAOURL;

    @Autowired
    TaoBaoApiService taoBaoApiService;

    @Autowired
    SysJhTaobaoHotDao sysJhTaobaoHotDao;

    @Autowired
    RestTemplate restTemplate;

    //通过
    @Test
    public void convertTkl() {
        taoBaoApiService.convertTaobaoTkl("￥vxfnbIAP8uA￥");
    }

    //通过
    @Test
    public void sortcoms() {
        List<SysJhTaobaoHot> sysJhTaobaoHots = sysJhTaobaoHotDao.queryAllCommissionRate();
        for (int i = 0; i < sysJhTaobaoHots.size(); i++) {
            SysJhTaobaoHot sysJhTaobaoHot = new SysJhTaobaoHot();
            sysJhTaobaoHot.setOrderCommiss(i + 1);
            sysJhTaobaoHot.setId(sysJhTaobaoHots.get(i).getId());
            sysJhTaobaoHotDao.updateCommissionRate(sysJhTaobaoHot);
        }
    }

    //通过
    @Test
    public void sortVol() {
        List<SysJhTaobaoHot> sysJhTaobaoHots = sysJhTaobaoHotDao.queryAllvolume();
        for (int i = 0; i < sysJhTaobaoHots.size(); i++) {
            JSONObject data = new JSONObject();
            data.put("orderVolume", i + 1);
            data.put("id", sysJhTaobaoHots.get(i).getId());
            sysJhTaobaoHotDao.updateorderVolume(data);
        }

    }

    @Test
    public void sortJDddd() {

        for (long j = 1; j < 5000; j++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String jdurl =  "https://api.open.21ds.cn/jd_api_v1/getjdunionitems?";
            Map<String, String> urlSign = new HashMap<>();
            urlSign.put("apkey", "c003483d-fd72-c78a-72ec-1d0e6034919d");
//            Integer cid=652;
            Integer cid=670;
//            Integer cid=652;
//            Integer cid=652;
//            Integer cid=652;
//            urlSign.put("cid1", "1320");
            urlSign.put("cid1", cid.toString());
//            urlSign.put("cid1", "737");
//            urlSign.put("cid1", "1620");
//            urlSign.put("cid1", "6728");
            urlSign.put("pageSize", String.valueOf(20));
            urlSign.put("pageIndex", String.valueOf(j));
            urlSign.put("isCoupon", "1");
            String linkStringByGet = null;
            try {
                linkStringByGet = NetUtils.createLinkStringByGet(urlSign);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            JSONObject temp = new JSONObject();
            JSONArray dataArray = new JSONArray();
            String res = restTemplate.getForObject(jdurl + linkStringByGet, String.class);
            JSONObject allData = JSON.parseObject(res).getJSONObject("data");
            dataArray = allData.getJSONArray("lists");
            for (int i = 0; i < dataArray.size(); i++) {
                JSONObject jdJson = (JSONObject) dataArray.get(i);
                JSONObject jdData = new JSONObject();
                //单价2*
                Double price = jdJson.getJSONObject("priceInfo").getDouble("price");
                JSONArray coupon = jdJson.getJSONObject("couponInfo").getJSONArray("couponList");
                JSONObject img = (JSONObject) jdJson.getJSONObject("imageInfo").getJSONArray("imageList").get(0);
                //佣金金额
                Double commissionA = jdJson.getJSONObject("commissionInfo").getDouble("commission");
                //佣金比例
                Double comsA = jdJson.getJSONObject("commissionInfo").getDouble("commissionShare");

                BigDecimal coms = new BigDecimal(comsA);
                BigDecimal commission = new BigDecimal(commissionA);
                BigDecimal priceall = new BigDecimal(price);
                Map<String, Object> h = new HashMap<>();
                //判断是否有优惠卷
                if (coupon != null && coupon.size() != 0) {
                    JSONObject couponList = (JSONObject) coupon.get(0);
                    BigDecimal discount = new BigDecimal(couponList.getInteger("discount"));
                    BigDecimal subtract = priceall.subtract(discount);
                    h.put("zkFinalPrice", subtract.doubleValue());
                    h.put("coupon", discount.doubleValue());
                } else {
                    h.put("zkFinalPrice", price);
                    h.put("coupon", 0);
                }
                h.put("jdurl", "http://" + jdJson.getString("materialUrl"));
                h.put("pictUrl", img.getString("url"));
                h.put("shopTitle", jdJson.getJSONObject("shopInfo").getString("shopName"));
                h.put("title", jdJson.getString("skuName"));
                h.put("commissionRate", commission);
                h.put("comssion", coms);
                h.put("cid", cid);
                h.put("volume", jdJson.getInteger("inOrderCount30Days"));
                h.put("numIid", jdJson.getLong("skuId"));
                h.put("istamll", 0);
                sysJhTaobaoHotDao.saveJd(h);
            }

        }


//        urlSign.put("sortName", "inOrderCount30Days");
//        urlSign.put("sort", goodsReq.getSort());
//        urlSign.put("isHot", "1");


        JSONArray templist = new JSONArray();

    }


    @Test
    public void search() {

        TaobaoClient client = new DefaultTaobaoClient(TAOBAOURL, APPKEY, SECRET);
        for (long i = 1; i < 5000; i++) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            TbkDgMaterialOptionalRequest request = new TbkDgMaterialOptionalRequest();
            request.setPageSize(100l);
            request.setPageNo(i);
            request.setAdzoneId(92706150203l);
            request.setHasCoupon(true);
            request.setCat("16,15,14,13,12,11,10,9,8,7");
//            request.setCat("50016348,50026523,50025705,21,19,29,50010404,16,50002766,50008090");
//            request.setCat("50016348,50026523,50025705,21,19,29,50010404,16,50002766,50008090");
            TbkDgMaterialOptionalResponse rsp = null;
            try {
                rsp = client.execute(request);
                List<TbkDgMaterialOptionalResponse.MapData> resultList = rsp.getResultList();
                resultList.forEach(mapData -> {
                    String couponInfo = mapData.getCouponInfo();
                    int star = mapData.getCouponInfo().indexOf(20943);//参数为字符的ascii码
                    couponInfo = mapData.getCouponInfo().substring(star + 1, couponInfo.length() - 1);
                    Integer couple = Integer.parseInt(couponInfo);
                    Double commiss = commissonAritTaobao(mapData.getZkFinalPrice(), mapData.getCommissionRate(), couple).doubleValue() / 10;

                    if (mapData.getVolume() > 100 && commiss > 3 && couple > 15) {
                        log.warning(mapData.getTitle());
                        Map<String, Object> h = new HashMap<>();
                        h.put("pictUrl", mapData.getPictUrl());
                        h.put("shopTitle", mapData.getShopTitle());
                        h.put("title", mapData.getTitle());
                        h.put("commissionRate", commiss);
                        long comssion = new BigDecimal(mapData.getCommissionRate()).longValue();
                        h.put("comssion", comssion);
                        h.put("coupon", couple);
                        h.put("zkFinalPrice", new BigDecimal(mapData.getZkFinalPrice()).doubleValue());
                        h.put("volume", mapData.getVolume().intValue());
                        h.put("numIid", mapData.getNumIid());
                        h.put("istamll", mapData.getUserType());
                        sysJhTaobaoHotDao.save(h);

                    }
                });
            } catch (Exception e) {

            }
        }

    }

    @Test
    public void search1() {

        TaobaoClient client = new DefaultTaobaoClient(TAOBAOURL, APPKEY, SECRET);
        for (long i = 1; i < 2; i++) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            TbkDgMaterialOptionalRequest request = new TbkDgMaterialOptionalRequest();
            request.setPageSize(100l);
            request.setPageNo(i);
            request.setAdzoneId(92706150203l);
            request.setHasCoupon(true);
            request.setIsTmall(true);
            request.setQ("小巨蛋");
//            request.setCat("16,15,14,13,12,11,10,9,8,7");
//            request.setCat("8,20,21,30,14,50012164,29,5,16,50002766");
            TbkDgMaterialOptionalResponse rsp = null;
            try {
                rsp = client.execute(request);

                List<TbkDgMaterialOptionalResponse.MapData> resultList = rsp.getResultList();
                resultList.forEach(mapData -> {
                    String couponInfo = mapData.getCouponInfo();
                    int star = mapData.getCouponInfo().indexOf(20943);//参数为字符的ascii码
                    couponInfo = mapData.getCouponInfo().substring(star + 1, couponInfo.length() - 1);
                    Integer couple = Integer.parseInt(couponInfo);
                    Double commiss = commissonAritTaobao(mapData.getZkFinalPrice(), mapData.getCommissionRate(), couple).doubleValue() / 10;
                    Integer cc = Integer.parseInt(couponInfo);

                    if (mapData.getVolume() > 100 && commiss > 3 && couple > 15) {
                        log.warning(mapData.getTitle());
                        Map<String, Object> h = new HashMap<>();
                        h.put("pictUrl", mapData.getPictUrl());
                        h.put("shopTitle", mapData.getShopTitle());
                        h.put("title", mapData.getTitle());
                        h.put("commissionRate", commiss);
                        long comssion = new BigDecimal(mapData.getCommissionRate()).longValue();
                        h.put("comssion", comssion);
                        h.put("coupon", couple);
                        h.put("zkFinalPrice", new BigDecimal(mapData.getZkFinalPrice()).doubleValue());
                        h.put("volume", mapData.getVolume().intValue());
                        h.put("numIid", mapData.getNumIid());
                        h.put("istamll", mapData.getUserType());
                        sysJhTaobaoHotDao.save(h);

                    }
                });
            } catch (Exception e) {

            }
        }

    }

    public static BigDecimal commissonAritTaobao(String zk, String rate, Integer copuon) {
        BigDecimal var0 = new BigDecimal(zk);
        BigDecimal var1 = new BigDecimal(copuon);
        BigDecimal divide = var0.subtract(var1);
        long rate1 = new BigDecimal(rate).longValue();
        Double var4 = rate1 / 1000d;
        Double var5 = divide.doubleValue() * var4;
        return new BigDecimal(var5).setScale(2, BigDecimal.ROUND_DOWN);
    }


}

