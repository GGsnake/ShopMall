package com.superman.superman.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.annotation.LoginRequired;
import com.superman.superman.model.Oder;
import com.superman.superman.model.Userinfo;
import com.superman.superman.service.MemberService;
import com.superman.superman.service.MoneyService;
import com.superman.superman.service.PddApiService;
import com.superman.superman.service.UserApiService;
import com.superman.superman.utils.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    MoneyService moneyService;

    @GetMapping("/myTeam")
    public WeikeResponse getTeam() {
        PageParam pageParam = new PageParam();
        JSONObject myTeam = memberService.getMyTeam(6l,pageParam);
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
    @LoginRequired
    @PostMapping("/me")
    public WeikeResponse myIndex(HttpServletRequest request)  {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid==null){
            return WeikeResponseUtil.fail(ResponseCode.COMMON_USER_NOT_EXIST);
        }
        JSONObject data = memberService.getMyMoney(Long.valueOf(uid));
        return WeikeResponseUtil.success(data);
    }

    @LoginRequired
    @PostMapping("/momeny")
    public WeikeResponse getMomeny(HttpServletRequest request)  {
//        String uid = (String) request.getAttribute("uid");

        JSONObject myMoney = memberService.getMyMoneyOf(6l);
        return WeikeResponseUtil.success(myMoney);
    }

    /**
     * 个人佣金提现接口
     * @param request
     * @return
     */
    @LoginRequired
    @PostMapping("/cash")
    public WeikeResponse getCash(HttpServletRequest request)  {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid==null){
            return WeikeResponseUtil.fail(ResponseCode.COMMON_USER_NOT_EXIST);
        }
        JSONObject data=new JSONObject();
        Long waitMoney = moneyService.queryWaitMoney(Long.valueOf(uid));
        Long finishMoney = moneyService.queryFinishMoney(Long.valueOf(uid));
        Long cash =0l;
        data.put("waitMoney",waitMoney);
        data.put("finishMoney",finishMoney);
        data.put("cash",cash);
        return WeikeResponseUtil.success(data);
    }

}
