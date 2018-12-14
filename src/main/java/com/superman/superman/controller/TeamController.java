package com.superman.superman.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.annotation.LoginRequired;
import com.superman.superman.model.ScoreBean;
import com.superman.superman.model.Userinfo;
import com.superman.superman.service.MemberService;
import com.superman.superman.utils.Constants;
import com.superman.superman.utils.PageParam;
import com.superman.superman.utils.WeikeResponse;
import com.superman.superman.utils.WeikeResponseUtil;
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
    private  MemberService memberService;



    @ApiOperation(value = "查看我的直推会员",notes = "分页加载")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "dsadasdasdsadsadsad", required = true, dataType = "String"),

    })
    @LoginRequired
    @PostMapping("/myTeam")
    public WeikeResponse getMyTeam(HttpServletRequest request,PageParam pageParam) {
        PageParam pageParam1=new PageParam(pageParam.getPageNo(),pageParam.getPageSize());
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        JSONObject query = memberService.getMyTeam(Long.valueOf(uid),pageParam1);

        return WeikeResponseUtil.success(query);
    }

    @ApiOperation(value = "查看我会员下级",notes = "分页加载")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "dsadasdasdsadsadsad", required = true, dataType = "String"),
    })
    @LoginRequired
    @PostMapping("/myFans")
    public WeikeResponse getMyFansNoMe(HttpServletRequest request, @RequestBody PageParam pageParam) {
        PageParam pageParam1=new PageParam(pageParam.getPageNo(),pageParam.getPageSize());
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        JSONObject query = memberService.getMyNoFans(Long.valueOf(uid),pageParam1);
        return WeikeResponseUtil.success(query);
    }
//   @ApiOperation(value = "查看会员详情")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "userId", value = "112", required = true, dataType = "Long"),
//    })
//    @LoginRequired
//    @PostMapping("/detail")
//    public WeikeResponse detail(HttpServletRequest request,Long userId) {
//        //TODO 权限待校验
//        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
//
//
//        Userinfo query = memberService.queryMemberDetail(userId);
//
//        return WeikeResponseUtil.success(query);
//    }


}
