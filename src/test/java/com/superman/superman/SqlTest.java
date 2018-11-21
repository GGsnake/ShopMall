package com.superman.superman;

import com.superman.superman.dao.OderMapper;
import com.superman.superman.model.CollectBean;
import com.superman.superman.model.Oder;
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

import java.util.List;

/**
 * Created by liujupeng on 2018/11/20.
 */
@Log
@RunWith(SpringRunner.class)
@SpringBootTest
public class SqlTest {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    ScoreService scoreService;
    @Autowired
    JdApiService jdApiService;
    @Autowired
    CollectService collectService;
    @Autowired
    OderMapper oderMapper;

    @Test
    public void add() throws Exception {
        var va=new CollectBean();
        va.setUserId(22l);
        va.setImage("yyyyyyyyyyyyl");
        va.setPromotion_rate(1500l);
        va.setCoupon(3000l);
        va.setCoupon_price(2000l);
        va.setSrc(0);
        va.setGoodId(10l);
        va.setPrice(5000l);
        va.setTitle("gggggggggg抢ssssssssssss购");
        collectService.addCollect(va);

    } @Test
    public void query() throws Exception {
        var a=collectService.queryCollect(22l);
        log.warning(a.toString());
    }
    @Test
    public void delete() throws Exception {
        var a=new Oder();
        a.setpId("4165519_37497333");
        var oders = oderMapper.selectAll(a);
        log.warning(oders.toString());
//        var a=collectService.deleteCollect(2);
//        log.warning(a.toString());
    }


}
