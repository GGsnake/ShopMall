package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.ScoreDao;
import com.superman.superman.dao.UserDao;
import com.superman.superman.model.ScoreBean;
import com.superman.superman.model.User;
import com.superman.superman.model.Userinfo;
import com.superman.superman.service.ScoreService;
import com.superman.superman.utils.Constants;
import com.superman.superman.utils.EveryUtils;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by liujupeng on 2018/11/14.
 */
@Log
@Service("ScoreService")
public class ScoreServiceImpl implements ScoreService {
    @Autowired
    private ScoreDao scoreDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Value("${juanhuang.look}")
    private String read_key;

    //查询今天是否已经领取 过每日浏览积分
    public Boolean isExitSign(ScoreBean scoreBean) {
        ScoreBean exit = scoreDao.isExit(scoreBean);
        return exit != null ? true : false;
    }

    @Override
    public JSONObject myScore(Integer uid) {
        JSONObject var = new JSONObject();
        Integer score = scoreDao.countScore(Long.valueOf(uid));
        var.put("score", score);
        Long looks = countLooks(uid.longValue());
        var.put("looks", looks);
        Boolean var1 = countShare(uid.longValue());
        if (var1 == false) {
            var.put("share", 0);
        } else {
            var.put("share", 1);
        }
        return var;
    }

    //查询今天每日浏览商品次数
    @Override
    public Long countLooks(Long uid) {
        String kv = read_key + uid.toString() + EveryUtils.getToday();

        SetOperations setOperations = redisTemplate.opsForSet();
        Long size = setOperations.size(kv);
        return size > 10 ? 10 : size;
    }

    //今日是否分享
    @Override
    public Boolean countShare(Long uid) {

        return redisTemplate.opsForSet().isMember(Constants.INV_LOG, Constants.INV_LOG + EveryUtils.getNowday() + ":" + uid);
    }


    /**
     * 记录浏览商品次数
     *
     * @param uid
     * @param goodId
     * @return
     */
    @Override
    public String recordBrowse(String uid, Long goodId) {
        String kv = read_key + uid + EveryUtils.getToday();
        if (redisTemplate.hasKey(kv)) {
            redisTemplate.opsForSet().add(kv, goodId.toString());
            redisTemplate.boundSetOps(kv).expireAt(new Date(EveryUtils.getDayEndUnix()));
            return null;
        }
        redisTemplate.opsForSet().add(kv, goodId.toString());
        redisTemplate.boundSetOps(kv).expireAt(new Date(EveryUtils.getDayEndUnix()));


        return null;
    }

    /**
     * 增加积分
     *
     * @param scoreBean
     * @return
     */
    @Transactional
    public Boolean addScore(ScoreBean scoreBean) {
        try {
            scoreDao.addScore(scoreBean);
            Userinfo user = new Userinfo();
            user.setId(scoreBean.getUserId());
            user.setUserScore(scoreBean.getScore().intValue());
            userDao.updateUserScore(user);
            return true;
        } catch (Exception e) {
            log.warning("积分");
            e.printStackTrace();
            throw new RuntimeException();
        }

    }

}
