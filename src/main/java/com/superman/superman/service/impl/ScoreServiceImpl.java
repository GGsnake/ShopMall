package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.ScoreDao;
import com.superman.superman.model.ScoreBean;
import com.superman.superman.model.Userinfo;
import com.superman.superman.service.ScoreService;
import com.superman.superman.utils.Constants;
import com.superman.superman.utils.EveryUtils;
import com.superman.superman.utils.WeikeResponseUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by liujupeng on 2018/11/14.
 */
@Log
@Service("ScoreService")
public class ScoreServiceImpl implements ScoreService {
    @Autowired
    private ScoreDao scoreDao;

    @Autowired
    private RedisTemplate redisTemplate;
    @Value("${juanhuang.look}")
    private String read_key;
    @Value("${juanhuang.sign}")
    private String sign_key;
    @Value("${juanhuang.signscore}")
    private Integer signscore;

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
        String kv = sign_key + uid;
        if (redisTemplate.hasKey(kv)) {
            var.put("sign", 1);
        } else {
            var.put("sign", 0);
        }
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
        Long size = redisTemplate.opsForSet().size(kv);
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
        SetOperations setOperations = redisTemplate.opsForSet();
        String kv = read_key + uid + EveryUtils.getToday();
        if (redisTemplate.hasKey(kv)) {
            long number = setOperations.size(kv);
            if (number<11){
                return null;
            }
            if (number == 10) {
                ScoreBean scoreBean=new ScoreBean();
                scoreBean.setUserId(Long.valueOf(uid));
                scoreBean.setScore(10l);
                scoreBean.setScoreType(1);
                //积分来源
                scoreBean.setDataSrc(2);
                Boolean flag = addScore(scoreBean);
                if (!flag){
                    log.warning("用户id为"+uid+"=浏览商品积分增增加失败");
                }
                return null;
            }
            setOperations.add(kv, goodId.toString());
            redisTemplate.boundSetOps(kv).expireAt(new Date(EveryUtils.getDayEndUnix()));
            return null;
        }
        setOperations.add(kv, goodId.toString());
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
        Userinfo user = new Userinfo();
        try {
            scoreDao.addScore(scoreBean);
            user.setId(scoreBean.getUserId());
            user.setUserscore(scoreBean.getScore().intValue());
            Integer flag = scoreDao.updateUserScore(user);
            if (flag == 0) {
                log.warning("用户积分增加错误 UID=" + user.getId());
                throw new RuntimeException();
            }
            return true;
        } catch (Exception e) {
            log.warning("用户积分增加错误 ID=" + user.getId() + "----异常信息=" + e.getMessage());
            throw new RuntimeException();
        }

    }

    @Transactional
    public Boolean sign(Long id) {
        ValueOperations v = redisTemplate.opsForValue();
        String kv = sign_key + id;
        if (redisTemplate.hasKey(kv)) {
            return false;
        }
        v.set(kv, "");
        redisTemplate.boundValueOps(kv).expireAt(new Date(EveryUtils.getDayEndUnix()));
        try {
            Userinfo user = new Userinfo();
            ScoreBean scoreBean = new ScoreBean();
            scoreBean.setDataSrc(3);
            scoreBean.setUserId(id);
            scoreBean.setScoreType(1);
            scoreBean.setScore(Long.valueOf(signscore));
            scoreDao.addScore(scoreBean);
            user.setId(scoreBean.getUserId());
            user.setUserscore(scoreBean.getScore().intValue());
            Integer flag = scoreDao.updateUserScore(user);
            if (flag == 0) {
                redisTemplate.delete(kv);
                log.warning("用户积分增加错误 UID=" + user.getId());
                throw new RuntimeException();
            }
            return true;
        } catch (Exception e) {
            redisTemplate.delete(kv);
            log.warning("用户积分增加错误 ID=" + id + "----异常信息=" + e.getMessage());
            throw new RuntimeException();
        }
    }

    @Transactional
    public Boolean scoreToCash(Long id) {
        Integer score = scoreDao.countScore(id);
        if (score == 0) {
            return true;
        }
        if (score < 1000) {
            return false;
        }
        BigDecimal scoreBg = new BigDecimal(score).divide(new BigDecimal(1000));
        Userinfo userinfo = new Userinfo();
        userinfo.setCash(scoreBg.setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
        userinfo.setId(id);
        Integer flag = scoreDao.updateCash(userinfo);
        if (flag == 0) {
            throw new RuntimeException("积分提现异常");
        }
        Integer flag1 = scoreDao.updateScoreZero(userinfo);
        if (flag1 == 0) {
            throw new RuntimeException("积分提现异常");
        }
        return true;
    }

}
