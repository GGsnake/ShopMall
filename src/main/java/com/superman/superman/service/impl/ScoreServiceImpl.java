package com.superman.superman.service.impl;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.ScoreDao;
import com.superman.superman.model.ScoreBean;
import com.superman.superman.model.Userinfo;
import com.superman.superman.service.ScoreService;
import com.superman.superman.utils.EveryUtils;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
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
    private OtherService otherService;

    @Autowired
    private RedisTemplate redisTemplate;
    @Value("${juanhuang.look}")
    private String read_key;
    @Value("${juanhuang.sign}")
    private String sign_key;
    //查询今天是否已经领取 过每日浏览积分
    public Boolean isExitSign(ScoreBean scoreBean) {
        ScoreBean exit = scoreDao.isExit(scoreBean);
        return exit != null ? true : false;
    }

    /**
     * 查询当天积分
     *
     * @param
     * @return
     */
    @Override
    public JSONObject myScore(Integer uid) {
        JSONObject var = new JSONObject();
        //查询积分
        Integer score = scoreDao.countScore(Long.valueOf(uid));
        //查询今日商品浏览次数
        Long looks = countLooks(uid.longValue());
        var.put("score", score);
        var.put("looks", looks);
        //是否已签到
        if (isSign(uid.longValue())) {
            var.put("sign", 1);
        } else {
            var.put("sign", 0);
        }
        //查询今日是否已分享
        if (isShare(uid.longValue())) {
            var.put("share", 1);
        } else {
            var.put("share", 0);
        }
        return var;
    }


    /**
     * 记录浏览商品次数
     *
     * @param uid
     * @param goodId
     * @return
     */
    @Override
    @Async
    public String recordBrowse(String uid, Long goodId) {
        SetOperations setOperations = redisTemplate.opsForSet();
        String kv = read_key + uid + EveryUtils.getToday();
        if (redisTemplate.hasKey(kv)) {
            long number = setOperations.size(kv);
            if (number == 10) {
                ScoreBean scoreBean = new ScoreBean();
                scoreBean.setDataSrc(2);
                scoreBean.setUserId(Long.valueOf(uid));
                scoreBean.setScoreType(1);
                scoreBean.setDay(EveryUtils.getNowday());
                String lookScore = otherService.querySetting("LookScore").getConfigValue();
                scoreBean.setScore(Long.valueOf(lookScore));
                ScoreBean flag = scoreDao.isExit(scoreBean);
                if (flag == null) {
                    Userinfo user = new Userinfo();
                    scoreDao.addScore(scoreBean);
                    user.setId(Long.valueOf(uid));
                    user.setUserscore(Integer.valueOf(lookScore));
                    scoreDao.updateUserScore(user);
                }
                return null;
            }
            if (number < 11) {
                setOperations.add(kv, goodId.toString());
//                redisTemplate.boundSetOps(kv).expireAt(new Date(EveryUtils.getDayEndUnix()));
                return null;
            }
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

    /**
     * 判断是否已签到
     *
     * @param id
     * @return
     */
    public Boolean isSign(Long id) {
        Integer signscore= Integer.valueOf(otherService.querySetting("SignScore").getConfigValue());
        ScoreBean scoreBean = new ScoreBean();
        scoreBean.setDataSrc(3);
        scoreBean.setUserId(id);
        scoreBean.setScoreType(1);
        scoreBean.setDay(EveryUtils.getNowday());
        scoreBean.setScore(signscore.longValue());
        ScoreBean flag = scoreDao.isExit(scoreBean);
        if (flag != null) {
            return true;
        }
        return false;
    }

    //查询今天每日浏览商品次数
    @Override
    public Long countLooks(Long uid) {
        String kv = read_key + uid.toString() + EveryUtils.getToday();
        Long size = redisTemplate.opsForSet().size(kv);
        return size > 10 ? 10 : size;
    }

    //判断今日是否分享
    @Override
    public Boolean isShare(Long uid) {
        String shareScore = otherService.querySetting("ShareScore").getConfigValue();

        ScoreBean scoreBean = new ScoreBean();
        scoreBean.setDataSrc(4);
        scoreBean.setUserId(uid);
        scoreBean.setScoreType(1);
        scoreBean.setDay(EveryUtils.getNowday());
        scoreBean.setScore(Long.valueOf(shareScore));
        ScoreBean flag = scoreDao.isExit(scoreBean);
        if (flag != null) {
            return true;
        }
        return false;
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
