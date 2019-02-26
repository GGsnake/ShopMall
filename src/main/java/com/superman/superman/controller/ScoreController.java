package com.superman.superman.controller;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.annotation.LoginRequired;
import com.superman.superman.dao.ScoreDao;
import com.superman.superman.model.ScoreBean;
import com.superman.superman.model.Userinfo;
import com.superman.superman.redis.RedisUtil;
import com.superman.superman.service.JdApiService;
import com.superman.superman.service.ScoreService;
import com.superman.superman.utils.*;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Size;
import java.util.concurrent.TimeUnit;

/**
 * Created by liujupeng on 2018/11/14.
 */
@RestController
@RequestMapping("/score")
public class ScoreController {
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private ScoreDao scoreDao;
    @Autowired
    RedisUtil redisUtil;
    @Value("${juanhuang.signscore}")
    private Integer signscore;

    //浏览商品积分上报
    @LoginRequired
    @GetMapping("/upVis")
    public WeikeResponse upVis(Long goodId, HttpServletRequest request) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid != null && goodId != null) {
            scoreService.recordBrowse(uid, goodId);
            return WeikeResponseUtil.success();
        }
        return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
    }

    //每日签到
    @LoginRequired
    @GetMapping("/sign")
    public WeikeResponse sign(HttpServletRequest request) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        }
        ScoreBean scoreBean = new ScoreBean();
        scoreBean.setDataSrc(3);
        scoreBean.setUserId(Long.valueOf(uid));
        scoreBean.setScoreType(1);
        scoreBean.setDay(EveryUtils.getNowday());
        scoreBean.setScore(Long.valueOf(signscore));
        ScoreBean exit = scoreDao.isExit(scoreBean);
        if (exit != null) {
            return WeikeResponseUtil.fail("1000322", "已签到过");
        }
        Userinfo user = new Userinfo();
        scoreDao.addScore(scoreBean);
        user.setId(Long.valueOf(uid));
        user.setUserscore(scoreBean.getScore().intValue());
        Integer flag = scoreDao.updateUserScore(user);
        if (flag == 0) {
            return WeikeResponseUtil.fail("1000322", "签到失败请稍后重试");
        }
        return WeikeResponseUtil.success();
    }

    /**
     * 积分查询
     *
     * @param request
     * @return
     */
    @LoginRequired
    @PostMapping("/myScore")
    public WeikeResponse dede(HttpServletRequest request) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        }
        String key = "myScore:" + uid;
        if (redisUtil.hasKey(key)) {
            return WeikeResponseUtil.success(JSONObject.parseObject(redisUtil.get(key)));
        }
        JSONObject data = scoreService.myScore(Integer.valueOf(uid));

        redisUtil.set(key, data.toJSONString());
        redisUtil.expire(key, 4, TimeUnit.SECONDS);
        return WeikeResponseUtil.success(data);
    }

    /**
     * 积分提现
     *
     * @param request
     * @return
     */
    @LoginRequired
    @PostMapping("/cash")
    public WeikeResponse cash(HttpServletRequest request) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        }
        Boolean data = scoreService.scoreToCash(Long.valueOf(uid));
        return WeikeResponseUtil.success(data);
    }


    // 每日浏览商品积分领取
    @LoginRequired
    @PostMapping("/dayScore")
    public WeikeResponse dayScore(HttpServletRequest request) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        }
        Long sum = scoreService.countLooks(Long.valueOf(uid));
        if (sum == 10) {
            ScoreBean scoreBean = new ScoreBean();
            scoreBean.setUserId(Long.valueOf(uid));
            scoreBean.setScore(10l);
            scoreBean.setScoreType(0);
            scoreBean.setDataSrc(2);
            if (scoreService.isExitSign(scoreBean)) {
                return WeikeResponseUtil.fail("100042", "今日已经签到");
            }
            Boolean flag = scoreService.addScore(scoreBean);
            if (flag) {
                return WeikeResponseUtil.success();
            }
        }
        return WeikeResponseUtil.fail("100041", "浏览次数不足");


    }


}
