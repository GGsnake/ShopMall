package com.superman.superman.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.SettingDao;
import com.superman.superman.dao.TboderMapper;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
@Log
@Component
public class TbSche {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SettingDao settingDao;
    @Autowired
    private TboderMapper tboderMapper;

    //清除淘宝过时商品
//    @Scheduled(cron = "0 0 * * *  ?")
//    @Scheduled(fixedRate = 3000)
    public void timeOutTb() {
        Calendar c = Calendar.getInstance();
        int i = c.get(Calendar.HOUR_OF_DAY);
        if (i > 1) {
            String hdkAppKey = settingDao.querySetting("HDKAppKey").getConfigValue();
            StringBuilder reqData = new StringBuilder();
            reqData.append("http://v2.api.haodanku.com/get_down_items/");
            reqData.append("apikey/" + hdkAppKey + "/");
            reqData.append("start/" + (i - 1) + "/");
            reqData.append("end/" + i);
            String res = restTemplate.getForObject(reqData.toString(), String.class);
            JSONObject allData = JSON.parseObject(res);
            if (allData.getString("msg") != null && "SUCCESS".equals(allData.getString("msg"))) {
                JSONArray data = allData.getJSONArray("data");
                if (data != null && data.size() != 0) {
                    List list=new ArrayList();
                    data.forEach(temp->{
                        JSONObject chid= (JSONObject) temp;
                        String itemid = chid.getString("itemid");
                        Long id = Long.valueOf(itemid);
                        list.add(id);
                    });
                    int deleCount = tboderMapper.deleteByTimeOut(list);
                    log.warning("删除过期数据"+deleCount+"条");
                }
            }
        }


    }
}
