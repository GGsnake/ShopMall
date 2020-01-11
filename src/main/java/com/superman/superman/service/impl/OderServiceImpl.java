package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.*;
import com.superman.superman.manager.ConfigQueryManager;
import com.superman.superman.model.JdOder;
import com.superman.superman.model.Oder;
import com.superman.superman.model.Tboder;
import com.superman.superman.model.Userinfo;
import com.superman.superman.req.OderPdd;
import com.superman.superman.service.OderService;
import com.superman.superman.service.TaoBaoApiService;
import com.superman.superman.utils.ConvertUtils;
import com.superman.superman.utils.PageParam;
import lombok.NonNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by snake on 2018/11/24.
 */
@Service("oderService")
public class OderServiceImpl implements OderService {
    @Autowired
    private OderMapper oderMapper;
    @Autowired
    private AllDevOderMapper allDevOderMapper;
    @Autowired
    private OrderSuperDao orderSuperDao;
    @Autowired
    private TaoBaoApiService taoBaoApiService;
//    @Autowired
//    private JdApiService jdApiService;
    @Value("${juanhuang.range}")
    private Integer range;

    @Override
    public JSONObject queryPddOder(Userinfo userinfo, List status, PageParam pageParam) {
        Long uid = userinfo.getId();
        Integer roleId = userinfo.getRoleId();
        JSONObject data = new JSONObject();

        switch (roleId) {
            case 1:
                List<OderPdd> list = allDevOderMapper.queryPddPageSize(status, uid, pageParam.getStartRow(), pageParam.getPageSize());
                Integer count = allDevOderMapper.queryPddPageSizeCount(status, uid);
                data.put("data", list);
                data.put("count", count);
                return data;
            case 2:
                Integer score = userinfo.getScore();
                List<OderPdd> list1 = null;
                list1 = allDevOderMapper.queryPddPageSize(status, uid, pageParam.getStartRow(), pageParam.getPageSize());
                Integer count2 = allDevOderMapper.queryPddPageSizeCount(status, uid);
                if (count2 != 0) {
                    JSONArray dataArray = new JSONArray();
                    for (OderPdd oder : list1) {
                        JSONObject chid = ConvertUtils.convertOder(oder);
                        Long promotionAmount = oder.getPromotion_amount();
                        Double money = (promotionAmount * range / 100d) * score / 100d;
                        chid.put("comssion", new BigDecimal(money).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
                        dataArray.add(chid);
                    }
                    data.put("data", dataArray);
                    data.put("count", count2);
                    return data;
                }
                data.put("data", list1);
                data.put("count", 0);
                return data;
            case 3:
                break;
        }
        return null;
    }

    @Override
    public JSONObject queryJdOder(Userinfo userinfo, List status, PageParam pageParam) {
        return null;
    }

    @Autowired
    private ConfigQueryManager configQueryManager;

    @Override
//    public JSONObject queryJdOder(Userinfo userinfo, List status, PageParam pageParam) {
//        Long uid = userinfo.getId();
//        Integer roleId = userinfo.getRoleId();
//        Integer score = userinfo.getScore();
//        JSONObject data = new JSONObject();
//        Map hashMap = new HashMap();
//        hashMap.put("id", uid);
//        hashMap.put("jd", status);
//        switch (roleId) {
//            case 1:
//                List<JdOder> list = allDevOderMapper.queryJdPageSize(status, uid, pageParam.getStartRow(), pageParam.getPageSize());
//                Integer count = allDevOderMapper.queryJdPageSizeCount(hashMap);
//                JSONArray jsonObjectList = new JSONArray();
//                for (int i = 0; i < list.size(); i++) {
//                    JSONObject tempData1 = new JSONObject();
//                    JSONObject jdurl = jdApiService.jdDetail(list.get(i).getSkuId());
//                    String url;
//                    if (jdurl != null) {
//                        JSONArray list2 = jdurl.getJSONArray("list");
//                        url = (String) list2.get(0);
//                    } else {
//                        url = configQueryManager.queryForKey("DetailImg");
//                    }
//                    tempData1.put("img", url);
//                    tempData1.put("title", list.get(i).getSkuName());
//                    tempData1.put("oderId", list.get(i).getOrderId());
//                    tempData1.put("time", list.get(i).getOrderTime() / 1000);
//                    tempData1.put("comssion", list.get(i).getEstimateFee());
//                    tempData1.put("pid", list.get(i).getPositionId());
//                    jsonObjectList.add(tempData1);
//                }
//                data.put("data", jsonObjectList);
//                data.put("count", count);
//
//                return data;
//            case 2:
//                List<JdOder> list1 = allDevOderMapper.queryJdPageSize(status, uid, pageParam.getStartRow(), pageParam.getPageSize());
//                Integer count1 = allDevOderMapper.queryJdPageSizeCount(hashMap);
//                if (count1 != 0) {
//                    JSONArray var23 = new JSONArray();
//                    for (int i = 0; i < list1.size(); i++) {
//                        JSONObject tempData1 = new JSONObject();
//                        Double promotionAmount = list1.get(i).getEstimateFee() * 100 * range / 100d;
//                        Double money = promotionAmount * score / 100d;
//                        JSONObject jdurl = jdApiService.jdDetail(list1.get(i).getSkuId());
//                        JSONArray list2 = jdurl.getJSONArray("list");
//                        String url = (String) list2.get(0);
//                        tempData1.put("img", url);
//                        tempData1.put("title", list1.get(i).getSkuName());
//                        tempData1.put("oderId", list1.get(i).getOrderId());
//                        tempData1.put("time", list1.get(i).getOrderTime() / 1000);
//                        tempData1.put("comssion", new BigDecimal(money).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
//                        tempData1.put("pid", list1.get(i).getPositionId());
//                        var23.add(tempData1);
//                    }
//                    data.put("data", var23);
//                    data.put("count", count1);
//                    return data;
//                }
//                data.put("data", null);
//                data.put("count", 0);
//                return data;
//            case 3:
//                break;
//        }
//
//        return null;
//    }

//    @Override
    public JSONObject queryTbOder(Userinfo userinfo, List status, PageParam pageParam) {
        Long uid = userinfo.getId();
        Integer roleId = userinfo.getRoleId();
        JSONObject data = new JSONObject();
        switch (roleId) {
            case 1:
                List<Tboder> list = allDevOderMapper.queryTbPageSize(status, uid, pageParam.getStartRow(), pageParam.getPageSize());
                Integer count = allDevOderMapper.queryTbPageSizeCount(status, uid);
                JSONArray jsonObjectList = new JSONArray();
                for (int i = 0; i < list.size(); i++) {
                    JSONObject tempData1 = new JSONObject();
                    String url = taoBaoApiService.deatilGoodList(list.get(i).getNumIid());
                    tempData1.put("img", url);
                    tempData1.put("title", list.get(i).getItemTitle());
                    tempData1.put("oderId", list.get(i).getTradeId());
                    tempData1.put("time", list.get(i).getOdercreateTime().getTime() / 1000);
                    Double commission = list.get(i).getCommission();
                    if (commission == 0) {
                        tempData1.put("comssion", list.get(i).getPub_share_pre_fee() * 100);

                    } else {
                        tempData1.put("comssion", commission * 100);
                    }
                    tempData1.put("pid", list.get(i).getRelation_id());
                    jsonObjectList.add(tempData1);
                }
                data.put("data", jsonObjectList);
                data.put("count", count);

                return data;
            case 2:
                Integer score = userinfo.getScore();
                List<Tboder> list1 = allDevOderMapper.queryTbPageSize(status, uid, pageParam.getStartRow(), pageParam.getPageSize());
                Integer count2 = allDevOderMapper.queryTbPageSizeCount(status, uid);
                if (count2 != 0) {
                    JSONArray var23 = new JSONArray();
                    for (Tboder oder : list1) {
                        JSONObject tempData = new JSONObject();
                        Double commission = oder.getCommission();
                        if (commission == 0) {
                            Double promotionAmount = oder.getPub_share_pre_fee() * range / 100d;
                            Double money = promotionAmount * score / 100d;
                            tempData.put("comssion", new BigDecimal(money).setScale(2, BigDecimal.ROUND_DOWN).doubleValue() * 100);
                        } else {
                            Double promotionAmount = oder.getCommission() * range / 100d;
                            Double money = promotionAmount * score / 100d;
                            tempData.put("comssion", new BigDecimal(money).setScale(2, BigDecimal.ROUND_DOWN).doubleValue() * 100);
                        }
                        String url = taoBaoApiService.deatilGoodList(oder.getNumIid());
                        tempData.put("img", url);
                        tempData.put("title", oder.getItemTitle());
                        tempData.put("oderId", oder.getTradeId());
                        tempData.put("time", oder.getOdercreateTime().getTime() / 1000);

                        var23.add(tempData);
                    }
                    data.put("data", var23);
                    data.put("count", count2);
                    return data;
                }
                data.put("data", null);
                data.put("count", 0);
                return data;
            case 3:
                break;
        }
        return null;
    }

    /**
     * 京东    订单状态      15.待付款,16.已付款,17.已完成,18.已结算
     * 淘宝    订单状态      3：订单结算，12：订单付款， 13：订单失效，14：订单成功
     * 拼多多   订单状态      -1 未支付; 0-已支付；1-已成团；2-确认收货；3-审核成功；4-审核失败（不可提现）
     * 5 -已经结算；8-非多多进宝商品（无佣金订单）
     *
     * @param status 0 未结算 1 已结算
     * @return
     */
    @Override
    public Long superQueryOderForUidList(List<Long> uidList, Integer status) {
        Long money = 0l;
        if (status == 0) {
            long jdMoney = oderMapper.queryForAllOrderListToWait(uidList, 1);
            long pddMoney = oderMapper.queryForAllOrderListToWait(uidList, 3);
            long tbMoney = oderMapper.queryForAllOrderListToWait(uidList, 2);
            money = jdMoney + pddMoney + tbMoney;
        }
        if (status == 1) {
            long jdMoney = oderMapper.queryForAllOrderListToFinsh(uidList, 1);
            long pddMoney = oderMapper.queryForAllOrderListToFinsh(uidList, 3);
            long tbMoney = oderMapper.queryForAllOrderListToFinsh(uidList, 2);
            money = jdMoney + pddMoney + tbMoney;
        }
        return money;
    }

    @Override
    public Long superQueryOderForUidListToEstimate(List<Long> uidList) {
        //TODO 待添加redis 缓存
        Integer jdTotal = orderSuperDao.queryAllDevOrder(uidList, 1);
        Integer tbTotal = orderSuperDao.queryAllDevOrder(uidList, 2);
        Integer pddTotal = orderSuperDao.queryAllDevOrder(uidList, 3);
        return jdTotal + tbTotal + pddTotal.longValue();
    }

}
