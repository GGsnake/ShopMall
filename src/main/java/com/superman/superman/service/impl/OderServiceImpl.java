package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.*;
import com.superman.superman.model.Oder;
import com.superman.superman.model.Tboder;
import com.superman.superman.model.Userinfo;
import com.superman.superman.req.OderPdd;
import com.superman.superman.service.OderService;
import com.superman.superman.service.TaoBaoApiService;
import com.superman.superman.utils.ConvertUtils;
import com.superman.superman.utils.PageParam;
import lombok.NonNull;
import lombok.var;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liujupeng on 2018/11/24.
 */
@Service("oderService")
public class OderServiceImpl implements OderService {

    @Autowired
    private OderMapper oderMapper;
    @Autowired
    private TboderMapper tboderMapper;

    @Autowired
    private AgentDao agentDao;

    @Autowired
    private UserinfoMapper userinfoMapper;

    @Autowired
    private AllDevOderMapper allDevOderMapper;
    @Autowired
    private TaoBaoApiService taoBaoApiService;
    @Value("${juanhuang.range}")
    private Integer range;

    @Override
    public JSONObject queryPddOder(Long uid, List status, PageParam pageParam) {
        Userinfo userinfo = userinfoMapper.selectByPrimaryKey(uid);
        Integer roleId = userinfo.getRoleId();
        var data = new JSONObject();

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
    public JSONObject queryJdOder(Long uid, List status, PageParam pageParam) {
        return null;
    }

    @Override
    public JSONObject queryTbOder(Long uid, List status, PageParam pageParam) {
        Userinfo userinfo = userinfoMapper.selectByPrimaryKey(uid);
        Integer roleId = userinfo.getRoleId();
        var data = new JSONObject();
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
                    tempData1.put("comssion", list.get(i).getCommission());
                    tempData1.put("pid", list.get(i).getAdzoneId());
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
                        Double promotionAmount = oder.getCommission() * range / 100d;
                        Double money = promotionAmount * score / 100d;
                        String url = taoBaoApiService.deatilGoodList(oder.getNumIid());
                        tempData.put("img", url);
                        tempData.put("title", oder.getItemTitle());
                        tempData.put("oderId", oder.getTradeId());
                        tempData.put("time", oder.getOdercreateTime().getTime() / 1000);
                        tempData.put("comssion", new BigDecimal(money).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
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

    @Override
    public Long superQueryOderForUidList(List<Long> uidList, Integer status) {
        Long money = 0l;
        if (status == 0) {
            money = oderMapper.superQueryForListToWait(uidList);
        }
        if (status == 1) {
            money = oderMapper.superQueryForListToFinsh(uidList);
        }
        return money;
    }

}
