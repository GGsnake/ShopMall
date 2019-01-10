package com.superman.superman.controller;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.annotation.LoginRequired;
import com.superman.superman.dao.ScoreDao;
import com.superman.superman.model.ScoreBean;
import com.superman.superman.service.JdApiService;
import com.superman.superman.service.ScoreService;
import com.superman.superman.utils.*;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * 积分查询
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
        JSONObject data = scoreService.myScore(Integer.valueOf(uid));
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

    //每日分享积分领取
    @LoginRequired
    @PostMapping("/shareScore")
    public WeikeResponse shareScore(HttpServletRequest request) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        }
        Boolean exit = scoreService.countShare(Long.valueOf(uid));
        if (!exit) {
            return WeikeResponseUtil.fail("100042", "今日未分享");
        }
        //签到分享
        ScoreBean scoreBean = new ScoreBean();
        scoreBean.setUserId(Long.valueOf(uid));
        scoreBean.setScore(5l);
        scoreBean.setScoreType(0);
        scoreBean.setDataSrc(1);

        if (scoreService.isExitSign(scoreBean)) {
            return WeikeResponseUtil.fail("100042", "今日已经签到");
        }
        Boolean flag = scoreService.addScore(scoreBean);
        if (flag) {
            return WeikeResponseUtil.success();
        }
        return WeikeResponseUtil.fail("100089", "签到失败 请重试");

    }
}
