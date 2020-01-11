package com.superman.superman.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.annotation.LoginRequired;
import com.superman.superman.dao.ScoreDao;
import com.superman.superman.manager.ConfigQueryManager;
import com.superman.superman.model.ScoreBean;
import com.superman.superman.model.Userinfo;
import com.superman.superman.redis.RedisUtil;
import com.superman.superman.service.ScoreService;
import com.superman.superman.utils.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by snake on 2018/11/14.
 */
@Log
@RestController
@RequestMapping("/score")
public class ScoreController {
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private ScoreDao scoreDao;
    @Autowired
    private ConfigQueryManager configQueryManager;
    @Autowired
    RedisUtil redisUtil;
    // 每日浏览商品 积分领取
    @LoginRequired
    @PostMapping("/dayScore")
    public Response dayScore(HttpServletRequest request) {
//        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
//        if (uid == null) {
//            return ResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
//        }
//        Long sum = scoreService.countLooks(Long.valueOf(uid));
//        if (sum == 9) {
//            Long signScore = Long.valueOf(configQueryManager.queryForKey("LookScore"));
//            ScoreBean scoreBean = new ScoreBean();
//            scoreBean.setUserId(Long.valueOf(uid));
//            scoreBean.setScore(signScore);
//            scoreBean.setScoreType(1);
//            scoreBean.setDataSrc(2);
//            if (scoreService.isExitSign(scoreBean)) {
//                return ResponseUtil.fail("100042", "今日已经签到");
//            }
//            Boolean flag = scoreService.addScore(scoreBean);
//            if (flag) {
//                return ResponseUtil.success();
//            }
//        }
        return ResponseUtil.fail("100041", "浏览次数不足");
    }
    //每日签到 积分领取
    @LoginRequired
    @GetMapping("/sign")
    public Response sign(HttpServletRequest request) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null) {
            return ResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        }
        //签到积分奖励
        String signScore1 = configQueryManager.queryForKey("SignScore");
        if (StringUtils.isEmpty(signScore1)){
            log.warning("警告 签到积分奖励设置错误--------------------");
            return ResponseUtil.fail("1000522", "服务器内部错误");
        }
        Long signScore = Long.valueOf(signScore1);
        ScoreBean scoreBean = new ScoreBean();
        scoreBean.setDataSrc(3);
        scoreBean.setUserId(Long.valueOf(uid));
        scoreBean.setScoreType(1);
        scoreBean.setDay(EveryUtils.getNowday());
        scoreBean.setScore(signScore);
        ScoreBean bean = scoreDao.isExit(scoreBean);
        if (bean != null) {
            return ResponseUtil.fail("1000322", "已签到过");
        }
        Userinfo user = new Userinfo();
        scoreDao.addScore(scoreBean);
        user.setId(Long.valueOf(uid));
        user.setUserscore(scoreBean.getScore().intValue());
        Integer flag = scoreDao.updateUserScore(user);
        if (flag == 0) {
            return ResponseUtil.fail("1000322", "签到失败请稍后重试");
        }
        return ResponseUtil.success();
    }

    /**
     * 积分查询
     *
     * @param request
     * @return
     */
    @LoginRequired
    @PostMapping("/myScore")
    public Response dede(HttpServletRequest request) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null) {
            return ResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        }
        JSONObject data = scoreService.myScore(Integer.valueOf(uid));
        return ResponseUtil.success(data);
    }

    /**
     * 积分提现
     *
     * @param request
     * @return
     */
    @LoginRequired
    @PostMapping("/cash")
    public Response cash(HttpServletRequest request) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null) {
            return ResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        }
//        Boolean data = scoreService.scoreToCash(Long.valueOf(uid));
        return ResponseUtil.success(null);
    }




}
