package com.superman.superman.service.impl;

import com.superman.superman.dao.ScoreDao;
import com.superman.superman.dao.UserDao;
import com.superman.superman.model.ScoreBean;
import com.superman.superman.model.User;
import com.superman.superman.service.ScoreService;
import com.superman.superman.utils.EveryUtils;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    //查询今天是否已经领取 过每日浏览积分
    public Boolean isExitSign(ScoreBean scoreBean) {
        ScoreBean exit = scoreDao.isExit(scoreBean);
        return exit!=null?true:false;
    }
    //查询今天每日浏览商品次数
    @Override
    public Long countLooks(Long uid) {
        String kv="score_look:" + uid.toString();
        SetOperations setOperations = redisTemplate.opsForSet();
        Long size = setOperations.size(kv);
        return size>10?10:size;
    }
    //今日是否分享
    @Override
    public Boolean countShare(Long uid) {
        String kv="score_share:" + uid.toString();
        return redisTemplate.hasKey(kv);
    }
    /**
     * 记录浏览商品次数
     * @param uid
     * @param goodId
     * @return
     */
    @Override
    public String recordBrowse(Long uid,Long goodId) {
        String kv="score:" + uid.toString();
        if (redisTemplate.hasKey(kv)){
            redisTemplate.opsForSet().add(kv,goodId.toString());
            return null;
        }
        redisTemplate.opsForSet().add(kv,goodId.toString());
        redisTemplate.expire(kv, EveryUtils.getDay(), TimeUnit.SECONDS);
        return null;
    }

    /**
     * 增加积分
     * @param scoreBean
     * @return
     */
    @Transactional
    public Boolean addScore(ScoreBean scoreBean) {
        try {
            scoreDao.addScore(scoreBean);
            User user=new User();
            user.setId(scoreBean.getUserId().intValue());
            user.setUserScore(scoreBean.getScore().intValue());

            userDao.updateUserScore(user);
            return true;
        }
        catch (Exception e){
            log.warning("积分");
            e.printStackTrace();
            return false;
        }

    }

}
