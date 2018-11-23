package com.superman.superman;

import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.model.CollectBean;
import com.superman.superman.model.ScoreBean;
import com.superman.superman.model.Userinfo;
import com.superman.superman.service.CollectService;
import com.superman.superman.service.JdApiService;
import com.superman.superman.service.ScoreService;
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

    @Test
    public void test() throws Exception {
        var a=new ArrayList<>();
        a.add(1);
        a.add(2);
        a.add(3);
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



}
