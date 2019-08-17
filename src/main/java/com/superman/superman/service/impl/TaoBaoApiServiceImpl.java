package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.annotation.FastCache;
import com.superman.superman.dao.SysJhTaobaoHotDao;
import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.manager.ConfigQueryManager;
import com.superman.superman.model.SysJhTaobaoAll;
import com.superman.superman.model.Userinfo;
import com.superman.superman.redis.RedisUtil;
import com.superman.superman.service.TaoBaoApiService;
import com.superman.superman.utils.GoodUtils;
import com.superman.superman.utils.net.NetUtils;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.TbkDgMaterialOptionalRequest;
import com.taobao.api.request.TbkItemInfoGetRequest;
import com.taobao.api.response.TbkDgMaterialOptionalResponse;
import com.taobao.api.response.TbkItemInfoGetResponse;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by liujupeng on 2018/12/4.
 */
@Log
@Service("taoBaoApiService")
public class TaoBaoApiServiceImpl implements TaoBaoApiService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserinfoMapper userinfoMapper;
    @Autowired
    private SysJhTaobaoHotDao sysJhTaobaoHotDao;
    @Value("${juanhuang.range}")
    private Double RANGE;
    @Autowired
    private ConfigQueryManager configQueryManager;

    final static int UNIT = 100;

    @Override
    public JSONObject serachGoodsAll(TbkDgMaterialOptionalRequest request, Long uid) {
        Userinfo ufo = userinfoMapper.selectByPrimaryKey(uid);
        if (ufo == null) {
            return null;
        }
        JSONObject data = new JSONObject();
        String taobaoAdzoneId = configQueryManager.queryForKey("TAOBAOAdzoneId");
        String appkey = configQueryManager.queryForKey("TAOBAOAPPKEY");
        String secret = configQueryManager.queryForKey("TAOBAOSECRET");
        String taobaourl = configQueryManager.queryForKey("TAOBAOURL");
        TaobaoClient client = new DefaultTaobaoClient(taobaourl, appkey, secret);
        //设置淘宝搜索引擎请求参数 (淘宝客API 通用物料搜索)
        request.setAdzoneId(Long.valueOf(taobaoAdzoneId));
        Double score = Double.valueOf(ufo.getScore());
        Boolean isTmall = request.getIsTmall();

        TbkDgMaterialOptionalResponse rsp = null;
        try {
            rsp = client.execute(request);
            List<TbkDgMaterialOptionalResponse.MapData> resultList = rsp.getResultList();
            if (resultList == null || resultList.size() == 0) {
                return data;
            }
            Long count = rsp.getTotalResults();
            if (count == 0) {
                return data;
            }
            TbkDgMaterialOptionalResponse.MapData dataObj = null;
            JSONArray dataArray = new JSONArray();
            int length = resultList.size();
            if (ufo.getRoleId() == 1) {
                for (int i = 0; i < length; i++) {
                    dataObj = resultList.get(i);
                    String coupon_info1 = dataObj.getCouponInfo();
                    String coupon_info = null;
                    JSONObject dataJson = GoodUtils.convertTaobao(dataObj);
                    //查找指定字符第一次出现的位置
                    if (coupon_info1 != null && !coupon_info1.equals("")) {
                        int star = coupon_info1.indexOf(20943);//参数为字符的ascii码
                        coupon_info = coupon_info1.substring(star + 1, coupon_info1.length() - 1);
                        Integer couple = Integer.parseInt(coupon_info) * UNIT;
                        dataJson.put("zk_money", couple);
                        BigDecimal agent = GoodUtils.commissonAritTaobao(dataObj.getZkFinalPrice(), dataObj.getCommissionRate(), couple / UNIT);
                        dataJson.put("agent", agent.setScale(1, BigDecimal.ROUND_DOWN).doubleValue() * 10);
                        dataJson.put("hasCoupon", 1);
                        dataJson.put("zk_price", Double.valueOf(dataObj.getZkFinalPrice()) * UNIT - couple);
                    } else {
                        BigDecimal agent = GoodUtils.commissonAritTaobao(dataObj.getZkFinalPrice(), dataObj.getCommissionRate());
                        dataJson.put("agent", agent.setScale(1, BigDecimal.ROUND_DOWN).doubleValue() * 10);
                        dataJson.put("zk_price", Double.valueOf(dataObj.getZkFinalPrice()) * UNIT);
                        dataJson.put("zk_money", 0);
                        dataJson.put("hasCoupon", 0);
                    }
                    Long commissionRate = Long.valueOf(dataObj.getCommissionRate());
                    dataJson.put("commissionRate", commissionRate / 10);
                    dataJson.put("shopName", dataObj.getShopTitle());
                    dataJson.put("istmall", isTmall);
                    dataArray.add(dataJson);
                }
                data.put("data", dataArray);
                data.put("count", count);
                return data;
            }
            if (ufo.getRoleId() == 2) {
                Double var3 = score / 100;
                for (int i = 0; i < length; i++) {

                    dataObj = resultList.get(i);
                    String coupon_info1 = dataObj.getCouponInfo();
                    JSONObject dataJson = GoodUtils.convertTaobao(dataObj);
                    String coupon_info = null;
                    //查找指定字符第一次出现的位置
                    if (coupon_info1 != null && !coupon_info1.equals("")) {
                        int star = coupon_info1.indexOf(20943);//参数为字符的ascii码
                        coupon_info = coupon_info1.substring(star + 1, coupon_info1.length() - 1);
                        Integer couple = Integer.parseInt(coupon_info) * UNIT;
                        BigDecimal var4 = GoodUtils.commissonAritTaobao(dataObj.getZkFinalPrice(), dataObj.getCommissionRate(), couple / UNIT);
                        BigDecimal agent = var4.multiply(new BigDecimal(var3));
                        dataJson.put("agent", agent.setScale(11, BigDecimal.ROUND_DOWN).doubleValue() * 10);
                        dataJson.put("zk_money", couple);
                        dataJson.put("hasCoupon", 1);
                        dataJson.put("zk_price", Double.valueOf(dataObj.getZkFinalPrice()) * UNIT - couple);
                    } else {
                        BigDecimal var4 = GoodUtils.commissonAritTaobao(dataObj.getZkFinalPrice(), dataObj.getCommissionRate());
                        BigDecimal agent = var4.multiply(new BigDecimal(var3));
                        dataJson.put("agent", agent.setScale(11, BigDecimal.ROUND_DOWN).doubleValue() * 10);
                        dataJson.put("zk_money", 0);
                        dataJson.put("hasCoupon", 0);
                        dataJson.put("zk_price", Double.valueOf(dataObj.getZkFinalPrice()) * UNIT);

                    }
                    Long commissionRate = Long.valueOf(dataObj.getCommissionRate());

                    dataJson.put("istmall", isTmall);
                    dataJson.put("shopName", dataObj.getShopTitle());

                    dataJson.put("commissionRate", commissionRate / 10);
                    dataArray.add(dataJson);
                }
                data.put("data", dataArray);
                data.put("count", count);
                return data;
            }
            for (int i = 0; i < length; i++) {
                dataObj = resultList.get(i);
                //查找指定字符第一次出现的位置
                JSONObject dataJson = GoodUtils.convertTaobao(dataObj);
                String coupon_info1 = dataObj.getCouponInfo();
                String coupon_info = null;
                Long commissionRate = Long.valueOf(dataObj.getCommissionRate());
                if (coupon_info1 != null && !coupon_info1.equals("")) {
                    int star = coupon_info1.indexOf(20943);//参数为字符的ascii码
                    coupon_info = coupon_info1.substring(star + 1, coupon_info1.length() - 1);
                    Integer couple = Integer.parseInt(coupon_info) * UNIT;
                    dataJson.put("zk_money", couple);
                    dataJson.put("hasCoupon", 1);
                    dataJson.put("zk_price", Double.valueOf(dataObj.getZkFinalPrice()) * UNIT - couple);
                } else {
                    dataJson.put("zk_money", 0);
                    dataJson.put("hasCoupon", 0);
                    dataJson.put("zk_price", Double.valueOf(dataObj.getZkFinalPrice()) * UNIT);
                }
                dataJson.put("istmall", isTmall);
                dataJson.put("agent", 0l);
                dataJson.put("shopName", dataObj.getShopTitle());
                dataJson.put("commissionRate", commissionRate / 10);
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
     * 淘宝本地类目引擎   根据opt选择判断模块
     *
     * @param param
     * @param uid
     * @param status
     * @return
     */
    @Override
    public JSONObject goodLocalSuperForOpt(JSONObject param, Long uid, Integer status) {
        Userinfo ufo = userinfoMapper.selectByPrimaryKey(uid);
        if (ufo == null) {
            return null;
        }
        //用户的分成比例
        Double score = Double.valueOf(ufo.getScore());
        //默认降序
        param.put("order", "desc");
        List<SysJhTaobaoAll> sysJhTaobaoHots = sysJhTaobaoHotDao.queryLocalAllOpt(param);
        Integer count = sysJhTaobaoHotDao.countLocalAllOpt(param);
        if (count == 0) {
            return new JSONObject();
        }
        param.clear();
        JSONArray dataArray = new JSONArray();
        JSONObject data = param;
        SysJhTaobaoAll dataObj = null;
        int len = sysJhTaobaoHots.size();
        if (ufo.getRoleId() == 1) {
            for (int i = 0; i < len; i++) {
                dataObj = sysJhTaobaoHots.get(i);
                JSONObject bean = GoodUtils.convertLocalTaobao(dataObj);
                //查找指定字符第一次出现的位置
                bean.put("zk_money", dataObj.getCoupon() * UNIT);
                bean.put("hasCoupon", 1);
                bean.put("zk_price", dataObj.getCouponprice().doubleValue() * UNIT);
                bean.put("commissionRate", dataObj.getCommissionrate().doubleValue() * UNIT);
                BigDecimal agent = GoodUtils.commissonAritLocalTaobao(dataObj.getCommission().doubleValue());
                bean.put("shopName", dataObj.getShoptitle());
                bean.put("istmall", dataObj.getIstamll() == 0 ? false : true);
                bean.put("agent", agent.doubleValue() * UNIT);
                dataArray.add(bean);
            }

        }
        if (ufo.getRoleId() == 2) {
            for (int i = 0; i < len; i++) {
                dataObj = sysJhTaobaoHots.get(i);
                JSONObject bean = GoodUtils.convertLocalTaobao(dataObj);
                //查找指定字符第一次出现的位置
                bean.put("zk_money", dataObj.getCoupon() * UNIT);
                bean.put("hasCoupon", 1);
                bean.put("zk_price", dataObj.getCouponprice().doubleValue() * UNIT);
                bean.put("commissionRate", dataObj.getCommissionrate().doubleValue() * UNIT);
                BigDecimal agent = GoodUtils.commissonAritLocalTaobao(dataObj.getCommission().doubleValue());
                bean.put("shopName", dataObj.getShoptitle());
                bean.put("istmall", dataObj.getIstamll() == 0 ? false : true);
                bean.put("agent", agent.doubleValue() * score);
                dataArray.add(bean);
            }
        }
        if (ufo.getRoleId() == 3) {
            for (int i = 0; i < len; i++) {
                dataObj = sysJhTaobaoHots.get(i);
                JSONObject bean = GoodUtils.convertLocalTaobao(dataObj);
                //查找指定字符第一次出现的位置
                bean.put("zk_money", dataObj.getCoupon() * UNIT);
                bean.put("hasCoupon", 1);
                bean.put("zk_price", dataObj.getCouponprice().doubleValue() * UNIT);
                bean.put("commissionRate", dataObj.getCommissionrate().doubleValue() * UNIT);
                bean.put("shopName", dataObj.getShoptitle());
                bean.put("istmall", dataObj.getIstamll() == 0 ? false : true);
                bean.put("agent", 0);
                dataArray.add(bean);
            }
        }

        data.put("data", dataArray);
        data.put("count", count);
        return data;
    }


    /**
     * 生成淘口令推广链接
     *
     * @param rid
     * @param good_id
     * @return
     */
    @Override
    @FastCache(timeOut = 200)
    public JSONObject convertTaobao(@NonNull String rid, @NonNull Long good_id) {
        JSONObject temp = new JSONObject();
        Map<String, String> urlSign = new HashMap<>();
        String URL = configQueryManager.queryForKey("MiaoTbUrl");
        String adzoneId = configQueryManager.queryForKey("TBAdzoneId");
        String miaoAppKey = configQueryManager.queryForKey("MiaoAppKey");
        String tbname = configQueryManager.queryForKey("TAOBAONAME");
        //生成推广连接请求地址前缀
        String taobaoSercahUrl = URL + "getitemgyurl?";
        //喵有券appkey
        urlSign.put("apkey", miaoAppKey);
        //渠道专属推广位pid
        urlSign.put("pid", adzoneId);
        urlSign.put("itemid", good_id.toString());
        urlSign.put("tbname", tbname);
        urlSign.put("shorturl", "1");
        urlSign.put("tpwd", "1");
        //渠道ID
        urlSign.put("relationid", rid);


        String reqUrl = NetUtils.convertUrlParam(urlSign);
        String res = null;
        try {
            res = restTemplate.getForObject(taobaoSercahUrl + reqUrl, String.class);
        } catch (Exception e) {
            log.warning("请求喵有券生成淘宝推广链接错误 错误信息=" + e.getMessage());
        }
        Integer code = JSON.parseObject(res).getInteger("code");
        if (code == 200) {
            JSONObject taoBaoCovert = JSONObject.parseObject(res).getJSONObject("result").getJSONObject("data");
            String uland_url = taoBaoCovert.getString("short_url");
            String token = taoBaoCovert.getString("tpwd");
            temp.put("uland_url", uland_url);
            temp.put("tkLink", token);
            temp.put("url", null);
            return temp;
        } else {
            log.warning("喵有券生成淘宝推广链接失败 错误信息=" + res);
        }
        return null;
    }

    /**
     * 淘口令解析API
     *
     * @param tkl
     * @return
     */
    @Override
    @FastCache(timeOut = 200)
    public JSONObject convertTaobaoTkl(@NonNull String tkl) {
        String miaoAppKey = configQueryManager.queryForKey("MiaoAppKey");
        String MiaoTbUrl = configQueryManager.queryForKey("MiaoTbUrl");
        String taobaoSercahUrl = MiaoTbUrl + "jiexitkl?";
        JSONObject temp = new JSONObject();
        Map<String, String> urlSign = new HashMap<>();
        urlSign.put("apkey", miaoAppKey);
        urlSign.put("kouling", tkl);
        String reqUrl = NetUtils.convertUrlParam(urlSign);
        String res = restTemplate.getForObject(taobaoSercahUrl + reqUrl, String.class);
        Integer code = JSON.parseObject(res).getInteger("code");
        if (code == 200) {
            JSONObject taoBaoCovert = JSONObject.parseObject(res).getJSONObject("data");
            //	淘口令链接
            String url = taoBaoCovert.getString("url");
            String title = taoBaoCovert.getString("title");
            String pic = taoBaoCovert.getString("pic");
            String ownerid = taoBaoCovert.getString("ownerid");
            String youxiaoqi = taoBaoCovert.getString("youxiaoqi");
            temp.put("url", url);
            temp.put("title", title);
            temp.put("pic", pic);
            temp.put("ownerid", ownerid);
            temp.put("youxiaoqi", youxiaoqi);
            return temp;
        }
        return null;
    }


    /**
     * 查询淘宝的商品详情
     *
     * @return
     */
    @Override
    @FastCache(timeOut = 120)
    public JSONObject deatil(@NonNull Long goodId) {
        JSONObject data = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        String appkey = configQueryManager.queryForKey("TAOBAOAPPKEY");
        String secret = configQueryManager.queryForKey("TAOBAOSECRET");
        String taobaourl = configQueryManager.queryForKey("TAOBAOURL");
        TaobaoClient client = new DefaultTaobaoClient(taobaourl, appkey, secret);
        TbkItemInfoGetRequest req = new TbkItemInfoGetRequest();
        req.setNumIids(String.valueOf(goodId));
        req.setPlatform(2L);
        TbkItemInfoGetResponse rsp = null;
        try {
            rsp = client.execute(req);
            if (JSONObject.parseObject(rsp.getBody()).getJSONObject("error_response") != null) {
                data.put("list", jsonArray);
                return data;
            }
            TbkItemInfoGetResponse.NTbkItem results = rsp.getResults().get(0);
            List<String> itemUrl = results.getSmallImages();
            data.put("list", itemUrl);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 查询淘宝商品单个的缩略图
     *
     * @param goodId
     * @return
     */

    public String deatilGoodList(@NonNull Long goodId) {
        String key = "deatilGoodList:" + goodId;
        if (redisUtil.hasKey(key)) {
            return redisUtil.get(key);
        }
        String appkey = configQueryManager.queryForKey("TAOBAOAPPKEY");
        String secret = configQueryManager.queryForKey("TAOBAOSECRET");
        String taobaourl = configQueryManager.queryForKey("TAOBAOURL");

        TaobaoClient client = new DefaultTaobaoClient(taobaourl, appkey, secret);
        TbkItemInfoGetRequest req = new TbkItemInfoGetRequest();
        req.setNumIids(goodId.toString());
        req.setPlatform(2L);
        TbkItemInfoGetResponse rsp = null;
        try {
            rsp = client.execute(req);
            List<TbkItemInfoGetResponse.NTbkItem> results = rsp.getResults();
            if (results == null || results.get(0) == null) {
                return null;
            }
            String pictUrl = results.get(0).getPictUrl();
            redisUtil.set(key, pictUrl);
            redisUtil.expire(key, 250, TimeUnit.SECONDS);
            return pictUrl;
        } catch (ApiException e) {
            log.warning("查询淘宝商品缩略图异常 异常代码=" + e.getMessage());
        }
        return null;
    }
}
