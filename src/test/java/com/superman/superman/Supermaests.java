package com.superman.superman;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.AgentDao;
import com.superman.superman.dao.OderMapper;
import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.model.*;
import com.superman.superman.service.*;
import com.superman.superman.utils.PageParam;
import lombok.extern.java.Log;
import lombok.var;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liujupeng on 2018/11/15.
 */
@Log
@RunWith(SpringRunner.class)
@SpringBootTest
public class Supermaests {

//    @Autowired
//    ScoreService scoreService;
//    @Autowired
//    CollectService collectService;
    @Autowired
    UserinfoMapper userinfoMapper;
    @Autowired
    MemberService memberService;
    @Autowired
    LogService logService;
    @Autowired
    OderService oderService;
    @Autowired
    OderMapper oderMapper;
//    @Autowired
//    AgentDao agentDao;

    @Test
    public void test() throws Exception {
//        var a=new ArrayList<>();
//        a.add(1);
//        a.add(2);
//        a.add(3);
//        Agent agent = new Agent();
//        agent.setAgentId(33);
//        agent.setUserId(113);
//        agentDao.insert(agent);
//        var a=new UserLog();
//        a.setUserId(22);
//        a.setOperation(0);
//        a.setIp("111111111111111111111");
//        logService.addUserLoginLog(a);
//        List<Userinfo> userinfos = userinfoMapper.selectIn(a);
//        log.warning(userinfos.toString());
//        for (long i=0;i<20;i++){
//            scoreService.recordBrowse(33l,i);
//        }
//        Long aLong = scoreService.countLooks(33l);
//        Long s = scoreService.countLooks(33l);
//        ScoreBean scoreBean1 = new ScoreBean();
//        scoreBean1.setUserId(122l);
//        scoreBean1.setScoreType(0);
//        scoreBean1.setScore(20002222l);
//        jdApiService.queryJdOder(scoreBean1);

    }
    @Test
    public void pddoder(){
        PageParam gp=new PageParam();
        gp.setPageNo(1);
        gp.setPageSize(2);

//        JSONObject myMoney = memberService.getMyMoney(1l);
        JSONObject myMoney = memberService.getMyNoFans(6l,gp);
//        Integer integer = oderMapper.sumMoneyForIdToScore(0.2d,"4165519_37497222");
        log.warning(myMoney.toJSONString());
//        List<Oder> oders = oderService.queryPddOderListToId("4165519_37497333",-1);
//        List<Oder> oders = oderService.queryPddOderListToId("4165519_37497333");
//        List<Oder> oders = oderService.queryPddOderListToId("4165519_37497333");
    }



}
