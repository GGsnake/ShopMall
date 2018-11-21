package com.superman.superman.controller;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.annotation.LoginRequired;
import com.superman.superman.service.MemberService;
import com.superman.superman.service.PddApiService;
import com.superman.superman.utils.MyException;
import com.superman.superman.utils.Result;
import com.superman.superman.utils.WeikeResponse;
import com.superman.superman.utils.WeikeResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liujupeng on 2018/11/9.
 */
@RestController
@RequestMapping("/team")
public class MemberController {
    @Autowired
    PddApiService pddApiService;
    @Autowired
    MemberService memberService;

    @LoginRequired
    @GetMapping("/myTeam")
    public WeikeResponse getTeam() {
        JSONObject myTeam = memberService.getMyTeam(1l);
        return WeikeResponseUtil.success(myTeam);

    }
    @GetMapping("/js")
    public String jsons()  throws  MyException{
       throw new MyException("test");

    }

}
