package com.superman.superman.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.annotation.LoginRequired;
import com.superman.superman.model.ScoreBean;
import com.superman.superman.model.Userinfo;
import com.superman.superman.service.MemberService;
import com.superman.superman.utils.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by liujupeng on 2018/11/29.
 */
@RestController
@RequestMapping("/team")
public class TeamController {
    @Autowired
    private MemberService memberService;
    /**
     * 查看我的直推会员 （分页）
     * @param request
     * @param pageParam
     * @return
     */
    @LoginRequired
    @PostMapping("/myTeam")
    public WeikeResponse getMyTeam(HttpServletRequest request,PageParam pageParam) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_USER_NOT_EXIST);
        }
        PageParam var1 = new PageParam(pageParam.getPageNo(), pageParam.getPageSize());
        JSONObject data = memberService.getMyTeam(Long.valueOf(uid), var1);
        return WeikeResponseUtil.success(data);
    }


    /**
     * 查看我会员下级 （分页）
     * @param request
     * @param pageParam
     * @return
     */
    @LoginRequired
    @PostMapping("/myFans")
    public WeikeResponse getMyFansNoMe(HttpServletRequest request,PageParam pageParam) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_USER_NOT_EXIST);
        }
        PageParam var = new PageParam(pageParam.getPageNo(), pageParam.getPageSize());
        JSONObject data = memberService.getMyNoFans(Long.valueOf(uid), var);
        return WeikeResponseUtil.success(data);
    }


}
