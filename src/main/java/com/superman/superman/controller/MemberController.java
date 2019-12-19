//package com.superman.superman.controller;
//
//import com.alibaba.fastjson.JSONObject;
//import com.superman.superman.annotation.LoginRequired;
//import com.superman.superman.dao.AgentDao;
//import com.superman.superman.dao.SysAdviceDao;
//import com.superman.superman.dao.UserinfoMapper;
//import com.superman.superman.model.ApplyCash;
//import com.superman.superman.model.Userinfo;
//import com.superman.superman.redis.RedisUtil;
//import com.superman.superman.service.MemberService;
//import com.superman.superman.service.MoneyService;
//import com.superman.superman.service.UserService;
//import com.superman.superman.utils.*;
//import lombok.extern.java.Log;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.*;
//import java.util.concurrent.TimeUnit;
//
///**
// * Created by liujupeng on 2018/11/9.
// */
//@Log
//@RestController
//@RequestMapping("/member")
//public class MemberController {
//    @Autowired
//    private RedisUtil redisUtil;
//    @Autowired
//    private UserinfoMapper userinfoMapper;
//    @Autowired
//    private AgentDao agentDao;
//    @Autowired
//    MemberService memberService;
//    @Autowired
//    UserService userService;
//    @Autowired
//    MoneyService moneyService;
//    @Autowired
//    SysAdviceDao sysAdviceDao;
//    @LoginRequired
//    @PostMapping("/me")
//    public Response myIndex(HttpServletRequest request) {
//        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
//        if (uid == null) {
//            return ResponseUtil.fail(ResponseCode.COMMON_USER_NOT_EXIST);
//        }
//        String key = "me:" + uid;
//        if (redisUtil.hasKey(key)) {
//            return ResponseUtil.success(JSONObject.parseObject(redisUtil.get(key)));
//        }
//        JSONObject data = memberService.getMyMoney(Long.valueOf(uid));
//        redisUtil.set(key, data.toJSONString());
//        redisUtil.expire(key, 10, TimeUnit.SECONDS);
//        return ResponseUtil.success(data);
//    }
//
//    /**
//     * 会员中心
//     * @param request
//     * @return
//     */
//    @LoginRequired
//    @PostMapping("/memberDetail")
//    public Response memberDetail(HttpServletRequest request) {
//        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
//        if (uid == null) {
//            return ResponseUtil.fail(ResponseCode.COMMON_USER_NOT_EXIST);
//        }
//
//        Long aLong = Long.valueOf(uid);
//        Userinfo userinfo = userService.queryByUid(aLong);
//        if (userinfo == null||userinfo.getRoleId()!=1) {
//            return ResponseUtil.fail(ResponseCode.COMMON_USER_NOT_EXIST);
//        }
//        String key = "memberDetail:" + uid;
//        if (redisUtil.hasKey(key)) {
//            return ResponseUtil.success(JSONObject.parseObject(redisUtil.get(key)));
//        }
//        Integer under = agentDao.queryForUserIdCount(aLong);
//        Integer sub = agentDao.countNoMyFansSum(aLong);
//        Integer var = agentDao.queryForUserIdCountToday(aLong);
//        Integer var1 = agentDao.countNoMyFansSumToday(aLong);
//
//        JSONObject data = new JSONObject();
//        data.put("add", var + var1);
//        data.put("under", under);
//        data.put("sub", sub);
//        data.put("joinTime", userinfo.getCreatetime());
//        redisUtil.set(key, data.toJSONString());
//        redisUtil.expire(key, 10, TimeUnit.SECONDS);
//        return ResponseUtil.success(data);
//    }
//    /**
//     * 个人佣金提现接口
//     *
//     * @param request
//     * @return
//     */
//    @LoginRequired
//    @PostMapping("/cash")
//    public Response getCash(HttpServletRequest request) {
//        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
//        if (uid == null) {
//            return ResponseUtil.fail(ResponseCode.COMMON_USER_NOT_EXIST);
//        }
//        JSONObject data = new JSONObject();
//        Userinfo user = userinfoMapper.selectByPrimaryKey(Long.valueOf(uid));
//        Long waitMoney = moneyService.queryCashMoney(0, user);
//        Long finishMoney = moneyService.queryCashMoney(1, user);
//        data.put("waitMoney", waitMoney);
//        data.put("finishMoney", finishMoney);
//        data.put("cash", user.getCash()*100);
//        return ResponseUtil.success(data);
//    }
//
//    /**
//     * 个人佣金提现申请接口
//     *
//     * @param request
//     * @return
//     */
//    @LoginRequired
//    @GetMapping("/apply")
//    public Response apply(HttpServletRequest request, Long money, String account, String name) {
//        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
//        if (uid == null || money == null || account == null || name == null) {
//            return ResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
//        }
//        if (money < 0 || money > 99999) {
//            return ResponseUtil.fail(ResponseCode.MONEY_MAX);
//        }
//        Userinfo user = userinfoMapper.selectByPrimaryKey(Long.valueOf(uid));
//        if (user == null) {
//            return ResponseUtil.fail(ResponseCode.COMMON_USER_NOT_EXIST);
//        }
//        ApplyCash applyCash = new ApplyCash();
//        applyCash.setUserid(user.getId().intValue());
//        applyCash.setRoleid(user.getRoleId());
//        applyCash.setMoney(money);
//        applyCash.setAccount(account);
//        applyCash.setName(name);
//        Integer temp = sysAdviceDao.applyCash(applyCash);
//        if (temp == 1) {
//            return ResponseUtil.success();
//        }
//        log.warning("用户提现失败-UID=" + uid);
//        return ResponseUtil.fail("100063", "申请提现失败请重试");
//    }
//
//    /**
//     * 个人佣金提现申请查询 分页
//     *
//     * @param request
//     * @return
//     */
//    @LoginRequired
//    @GetMapping("/queryApply")
//    public Response queryApply(HttpServletRequest request, PageParam pageParam) {
//        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
//        if (uid == null) {
//            return ResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
//        }
//        PageParam param = new PageParam(pageParam.getPageNo(), pageParam.getPageSize());
//        Map<String,Object> map=new HashMap<>();
//        map.put("offset",param.getStartRow());
//        map.put("limit",param.getPageSize());
//        map.put("uid",uid);
//        List<ApplyCash> temp = sysAdviceDao.queryApplyCash(map);
//        return ResponseUtil.success(temp);
//    }
//
//    /**
//     * 查看会员详情
//     *
//     * @param request
//     * @param id
//     * @return
//     */
//    @LoginRequired
//    @PostMapping("/child")
//    public Response getChild(HttpServletRequest request, Long id) {
//        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
//        if (uid == null) {
//            return ResponseUtil.fail(ResponseCode.COMMON_USER_NOT_EXIST);
//        }
//        String key = "child:"+id.toString()+uid;
//        if (redisUtil.hasKey(key)) {
//            return ResponseUtil.success(JSONObject.parseObject(redisUtil.get(key)));
//        }
//        JSONObject var ;
//
//        Userinfo userinfo = userinfoMapper.selectByPrimaryKey(Long.valueOf(uid));
//        Integer roleId = userinfo.getRoleId();
//
//        if (roleId == 3) {
//            return ResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
//        }
//        var = memberService.queryMemberDetail(id, userinfo.getId().intValue());
//        redisUtil.set(key, var.toJSONString());
//        redisUtil.expire(key, 20, TimeUnit.SECONDS);
//        return ResponseUtil.success(var);
//    }
//
////    /**
////     * 升级代理接口
////     *
////     * @param request
////     * @param id      要升级的用户id
////     * @param score   佣金比率
////     * @return
////     */
////    @LoginRequired
////    @PostMapping("/upAgent")
////    public Response upAgent(HttpServletRequest request, Integer id, Integer score) {
////        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
////        if (uid == null) {
////            return ResponseUtil.fail(ResponseCode.COMMON_USER_NOT_EXIST);
////        }
////        if (score < 0 || score > 100) {
////            return ResponseUtil.fail(ResponseCode.INT_CUSY);
////        }
////        Boolean var = userService.upAgent(id, Integer.valueOf(uid), score);
////        if (!var) {
////            return ResponseUtil.fail(ResponseCode.COMMON_AUTHORITY_ERROR);
////        }
////        return ResponseUtil.success(var);
////    }
////
//
//}
