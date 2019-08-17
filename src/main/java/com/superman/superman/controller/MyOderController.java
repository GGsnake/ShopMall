package com.superman.superman.controller;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.annotation.LoginRequired;
import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.manager.OderManager;
import com.superman.superman.model.Oder;
import com.superman.superman.redis.RedisUtil;
import com.superman.superman.service.MemberService;
import com.superman.superman.service.OderService;
import com.superman.superman.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by liujupeng on 2018/11/24.
 */
@RestController
@RequestMapping("/oder")
public class MyOderController {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private OderManager oderManager;
    @Autowired
    private MemberService memberService;


    @LoginRequired
    @PostMapping("/myOder")
    public WeikeResponse queryAllOder(HttpServletRequest request, PageParam pageParam, Integer devId, Integer status) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null || status == null || status >= 3 || status < 0) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        }
        PageParam param = new PageParam(pageParam.getPageNo(), pageParam.getPageSize());
        List statusList = ConvertUtils.getStatus(devId, status);
        JSONObject allOder = new JSONObject();
        if (devId == 0) {
            allOder = oderManager.getTaobaoOder(Long.valueOf(uid), statusList, param);
        }
        if (devId == 1) {
            allOder = oderManager.getTaobaoOder(Long.valueOf(uid), statusList, param);
        }
        if (devId == 2) {
            allOder = oderManager.getJdOder(Long.valueOf(uid), statusList, param);
        }
        if (devId == 3) {
            allOder = oderManager.getPddOder(Long.valueOf(uid), statusList, param);
        }
        return WeikeResponseUtil.success(allOder);
    }

    /**
     * 我的收益报表接口
     * @param request
     * @return
     */
    @LoginRequired
    @PostMapping("/InCome")
    public WeikeResponse queryInCome(HttpServletRequest request) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        }
        //缓存
        String key = "queryInCome:" + uid;
        if (redisUtil.hasKey(key)) {
            return WeikeResponseUtil.success(JSONObject.parseObject(redisUtil.get(key)));
        }
        JSONObject data = memberService.queryMemberDetail(Long.valueOf(uid));
        redisUtil.set(key, data.toJSONString());
        redisUtil.expire(key, 30, TimeUnit.SECONDS);
        return WeikeResponseUtil.success(data);
    }
}



