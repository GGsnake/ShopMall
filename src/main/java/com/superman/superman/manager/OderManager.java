package com.superman.superman.manager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.AgentDao;
import com.superman.superman.dao.OderMapper;
import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.model.Oder;
import com.superman.superman.model.Tboder;
import com.superman.superman.model.Userinfo;
import com.superman.superman.req.OderPdd;
import com.superman.superman.service.OderService;
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
 * Created by liujupeng on 2018/11/21.
 */
@Service("oderManager")
public class OderManager {
    @Autowired
    private OderMapper oderMapper;
    @Autowired
    private OderService oderService;
    @Autowired
    private UserinfoMapper userinfoMapper;
    @Autowired
    private AgentDao agentDao;

    @Value("${juanhuang.range}")
    private Integer range;

    public JSONObject getAllOder(Long uid, List status, PageParam pageParam) {
        Userinfo userinfo = userinfoMapper.selectByPrimaryKey(uid);
        Integer roleId = userinfo.getRoleId();
        if (roleId == 3) {
            return null;
        }
        if (roleId == 2) {
            JSONObject temp = oderService.queryPddOder(uid, status, pageParam);
            return temp;
        }
        if (roleId == 1) {
            JSONObject temp = oderService.queryPddOder(uid, status, pageParam);
            List<OderPdd> data = (List<OderPdd>) temp.get("data");
            if (data == null) {
                JSONObject var1 = new JSONObject();
                var1.put("data", null);
                var1.put("count", 0);
                return var1;
            }
            JSONArray dataArray = new JSONArray();
//        for (int i = 0; i < data.size(); i++) {
//            Long sc = 100l - score;
//            //获得原始佣金
//            Double promotionAmount = chid.getDouble("comssion")*100d;
//            //平台抽成后的运营商佣金
//            Double money = (promotionAmount *range/100)*sc / 100d;
//            chid.put("comssion",new BigDecimal(money).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
//            dataArray.add(chid);
//        }hh
            for (OderPdd oder : data) {
                if (oder.getP_id().equals(userinfo.getPddpid())) {
                    JSONObject chid = ConvertUtils.convertOder(oder);
                    Long promotionAmount = oder.getPromotion_amount();
                    Double money = (promotionAmount * range / 100d);
                    chid.put("comssion", new BigDecimal(money).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
                    dataArray.add(chid);
                    continue;
                }

                JSONObject chid = ConvertUtils.convertOder(oder);
                Integer score = agentDao.queryUserScore(oder.getP_id());
                Long sc = 100l - score;
                Long promotionAmount = oder.getPromotion_amount();
                Double money = (promotionAmount * range / 100d) * sc / 100d;
                chid.put("comssion", new BigDecimal(money).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
                dataArray.add(chid);
            }
            temp.put("data", dataArray);
            return temp;
        }
        return null;
    }

    public JSONObject getTaobaoOder(Long uid, List status, PageParam pageParam) {
        Userinfo userinfo = userinfoMapper.selectByPrimaryKey(uid);
        Integer roleId = userinfo.getRoleId();
        if (roleId == 3) {
            return null;
        }
        JSONObject var = null;
        if (roleId == 2) {
            var = oderService.queryTbOder(uid, status, pageParam);
            return var;
        }
        var = oderService.queryTbOder(uid, status, pageParam);
        JSONArray json = var.getJSONArray("data");
        JSONArray dataArray = new JSONArray();
        if (json == null) {
            return var;
        }
        for (int i = 0; i < json.size(); i++) {
            JSONObject chid = (JSONObject) json.get(i);
            Integer score = agentDao.queryUserScoreTb(chid.getLong("pid"));
            Long sc = 100l - score;
            //获得原始佣金
            Double promotionAmount = chid.getDouble("comssion") * 1000d;
            //平台抽成后的运营商佣金
            Double money = (promotionAmount * range / 100) * sc / 100d;
            chid.put("comssion", new BigDecimal(money).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
            chid.remove("pid");
            dataArray.add(chid);
        }
        var.put("data", dataArray);
        return var;
    }

    public JSONObject getJdOder(Long uid, List status, PageParam pageParam) {
        Userinfo userinfo = userinfoMapper.selectByPrimaryKey(uid);
        Integer roleId = userinfo.getRoleId();
        if (roleId == 2) {
            JSONObject temp = oderService.queryPddOder(uid, status, pageParam);
            return temp;
        }
        JSONObject temp = oderService.queryPddOder(uid, status, pageParam);
        List<OderPdd> data = (List<OderPdd>) temp.get("data");
        if (data == null) {
            JSONObject var1 = new JSONObject();
            var1.put("data", null);
            var1.put("count", 0);
            return var1;
        }
        List<OderPdd> var2 = new ArrayList<>();
        for (OderPdd oder : data) {
            OderPdd var1 = new OderPdd();
            BeanUtils.copyProperties(oder, var1);
            Integer score = agentDao.queryUserScore(oder.getP_id());
            Long sc = 100l - score;
            Long promotionAmount = oder.getPromotion_amount();
            Long money = promotionAmount * sc / 100;
            var1.setPromotion_amount(money);
            var2.add(var1);
        }
        temp.put("data", var2);
        return temp;
    }

}
