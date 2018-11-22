//package com.superman.superman.controller;
//
//import com.superman.superman.annotation.LoginRequired;
//import com.superman.superman.model.ScoreBean;
//import com.superman.superman.service.JdApiService;
//import com.superman.superman.service.ScoreService;
//import com.superman.superman.utils.EveryUtils;
//import com.superman.superman.utils.WeikeResponse;
//import com.superman.superman.utils.WeikeResponseUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.concurrent.TimeUnit;
//
///**
// * Created by liujupeng on 2018/11/14.
// */
//@RestController
//@RequestMapping("/score")
//public class ScoreController  {
//
//    @Autowired
//    private RedisTemplate redisTemplate;
//    @Autowired
//    private ScoreService scoreService;
//    @Autowired
//    private JdApiService jdApiService;
//
//    //浏览商品积分上报
//    @PostMapping("/upVis")
//    public WeikeResponse upVis(@RequestParam(value = "userId")Integer userId, @RequestParam(value = "goodId")Long goodId) {
//        //TODO token校验
//        String kv="score:" + userId.toString();
//        return  WeikeResponseUtil.success("重新创建");
//
//    }
//    //积分查询
//    @LoginRequired
//    @PostMapping("/quu")
//    public WeikeResponse quee() {
//        //TODO token校验
//        ScoreBean scoreBean=new ScoreBean();
//        Boolean query = scoreService.isExitSign(scoreBean);
//        return WeikeResponseUtil.success(query);
//    }
//    //积分查询
//    @PostMapping("/ts")
//    public WeikeResponse ts(String id) {
//        //TODO token校验
//
//        ScoreBean query = jdApiService.queryJdOder(id);
//        return WeikeResponseUtil.success(query);
//    }
//    //积分查询
//    @LoginRequired
//    @PostMapping("/dede")
//    public WeikeResponse dede(String id) {
//        //TODO token校验
//
//        ScoreBean query = jdApiService.queryJdOder(id);
//        return WeikeResponseUtil.success(query);
//    }
//
//    //每日浏览商品积分领取
//    @PostMapping("/dayScore")
//    public WeikeResponse dayScore(HttpServletRequest request) {
//        //TODO token校验
//
//        String uid=request.getParameter("Constants.CURRENT_USER_ID");
//        Long sum = scoreService.countLooks(uid);
//        if (sum==10){
//            ScoreBean scoreBean=new ScoreBean();
//            scoreBean.setUserId(uid);
//            scoreBean.setScore(10l);
//            scoreBean.setScoreType(0);
//            scoreBean.setDataSrc(2);
//            if (scoreService.isExitSign(scoreBean)){
//                return WeikeResponseUtil.fail("100042","今日已经签到");
//            }
//
//            Boolean flag = scoreService.addScore(scoreBean);
//            return WeikeResponseUtil.success(null);
//        }
//        return WeikeResponseUtil.fail("100041","浏览次数不足");
//
//
//    }
//    //每日分享积分领取
//    @PostMapping("/shareScore")
//    public WeikeResponse shareScore() {
//        //TODO token校验
//        //TODO 校验
//        Long uid=2l;
//        Boolean exit = scoreService.countShare(uid);
//        if (!exit){
//            return WeikeResponseUtil.fail("100042","今日未分享");
//        }
//        ScoreBean scoreBean=new ScoreBean();
//        scoreBean.setUserId(uid);
//        scoreBean.setScore(5l);
//        scoreBean.setScoreType(0);
//        //签到分享
//        scoreBean.setDataSrc(1);
//       if (scoreService.isExitSign(scoreBean)){
//           return WeikeResponseUtil.fail("100042","今日已经签到");
//       }
//
//        Boolean flag = scoreService.addScore(scoreBean);
//        return WeikeResponseUtil.success(null);
//    }
//}
