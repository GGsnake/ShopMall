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
import com.superman.superman.utils.PageParam;
import lombok.NonNull;
import lombok.var;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Autowired
    private AgentDao agentDao;

    public JSONObject getAllOder(Long uid, List status, PageParam pageParam) {
        Userinfo userinfo = userinfoMapper.selectByPrimaryKey(uid);
        Integer roleId = userinfo.getRoleId();
        if (roleId==2){
            JSONObject temp = oderService.queryPddOder(uid, status, pageParam);
            return temp;
        }
        JSONObject temp = oderService.queryPddOder(uid, status, pageParam);
        List<OderPdd> data = (List<OderPdd>) temp.get("data");
        if (data==null){
            JSONObject var1=new JSONObject();
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
    public JSONObject getTaobaoOder(Long uid, List status, PageParam pageParam) {
        Userinfo userinfo = userinfoMapper.selectByPrimaryKey(uid);
        Integer roleId = userinfo.getRoleId();
        if (roleId==2){
            JSONObject var = oderService.queryTbOder(uid, status, pageParam);
            return var;
        }
        JSONObject var = oderService.queryTbOder(uid, status, pageParam);
        JSONArray data = var.getJSONArray("data");
        if (data==null){
            JSONObject var1=new JSONObject();
            var1.put("data", null);
            var1.put("count", 0);
            return var1;
        }
        for (int i = 0; i < data.size(); i++) {
            JSONObject var32= (JSONObject) data.get(i);
            Integer score = agentDao.queryUserScoreTb(var32.getLong("pid"));
            Long sc = 100l - score;
            Double promotionAmount = var32.getDouble("comssion")*100d;
            Double money = promotionAmount *ra sc / 100d;
        }
        for (Tboder oder : data) {
            Tboder var1 = new Tboder();
            BeanUtils.copyProperties(oder, var1);

            var1.setCommission(money);
            var2.add(var1);
        }
        temp.put("data", var2);
        return temp;
    }
    public JSONObject getJdOder(Long uid, List status, PageParam pageParam) {
        Userinfo userinfo = userinfoMapper.selectByPrimaryKey(uid);
        Integer roleId = userinfo.getRoleId();
        if (roleId==2){
            JSONObject temp = oderService.queryPddOder(uid, status, pageParam);
            return temp;
        }
        JSONObject temp = oderService.queryPddOder(uid, status, pageParam);
        List<OderPdd> data = (List<OderPdd>) temp.get("data");
        if (data==null){
            JSONObject var1=new JSONObject();
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
