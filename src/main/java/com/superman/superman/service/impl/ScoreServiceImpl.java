package com.superman.superman.service.impl;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.ScoreDao;
import com.superman.superman.manager.ConfigQueryManager;
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
 * Created by snake on 2018/11/14.
 */
@Log
@Service("ScoreService")
public class ScoreServiceImpl implements ScoreService {
    @Autowired
    private ScoreDao scoreDao;
    @Autowired
    private ConfigQueryManager configQueryManager;

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
        Integer signscore= Integer.valueOf(configQueryManager.queryForKey("SignScore"));
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

    @Override
    public Boolean isShare(Long uid) {
        return null;
    }


}
