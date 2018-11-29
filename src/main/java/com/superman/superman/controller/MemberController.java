package com.superman.superman.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.annotation.LoginRequired;
import com.superman.superman.model.Oder;
import com.superman.superman.model.Userinfo;
import com.superman.superman.service.MemberService;
import com.superman.superman.service.PddApiService;
import com.superman.superman.service.UserApiService;
import com.superman.superman.utils.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by liujupeng on 2018/11/9.
 */
@RestController
@RequestMapping("/member")
public class MemberController {
//    @Autowired
//    PddApiService pddApiService;
    @Autowired
    MemberService memberService;
    @Autowired
    UserApiService userApiService;

    @GetMapping("/myTeam")
    public WeikeResponse getTeam() {
        PageParam pageParam = new PageParam();
        JSONObject myTeam = memberService.getMyTeam(1l,pageParam);

        return WeikeResponseUtil.success(myTeam);

    }
//
//    @GetMapping("/qu")
//    public String jsons()  {
////        return myMoney.toString();
//    }

    @ApiOperation(value = "我的个人页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", required = true, dataType = "String", paramType = "/me"),
    })
    @PostMapping("/me")
    public WeikeResponse myIndex(HttpServletRequest request)  {
        Long uid = (Long) request.getAttribute("uid");
        JSONObject myMoney = memberService.getMyMoney(uid);
        return WeikeResponseUtil.success(myMoney);
    }

}
