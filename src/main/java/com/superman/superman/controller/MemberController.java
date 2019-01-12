package com.superman.superman.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.superman.superman.annotation.LoginRequired;
import com.superman.superman.dao.AgentDao;
import com.superman.superman.dao.SysAdviceDao;
import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.model.ApplyCash;
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
import lombok.extern.java.Log;
import me.hao0.wepay.core.Wepay;
import me.hao0.wepay.core.WepayBuilder;
import me.hao0.wepay.model.pay.PayRequest;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by liujupeng on 2018/11/9.
 */
@Log
@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private UserinfoMapper userinfoMapper;
    @Autowired
    private AgentDao agentDao;
    //    @Autowired
//    PddApiService pddApiService;
    @Autowired
    MemberService memberService;
    @Autowired
    UserApiService userApiService;
    @Autowired
    MoneyService moneyService;
    @Autowired
    SysAdviceDao sysAdviceDao;
    @Value("${domain.codeurl}")
    private String URL;

    @ApiOperation(value = "我的个人页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", required = true, dataType = "String", paramType = "/me"),
    })
    @LoginRequired
    @PostMapping("/me")
    public WeikeResponse myIndex(HttpServletRequest request) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_USER_NOT_EXIST);
        }
        JSONObject data = memberService.getMyMoney(Long.valueOf(uid));
        return WeikeResponseUtil.success(data);
    }

    /**
     * 会员中心
     * @param request
     * @return
     */
    @LoginRequired
    @PostMapping("/memberDetail")
    public WeikeResponse memberDetail(HttpServletRequest request) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_USER_NOT_EXIST);
        }
        Long aLong = Long.valueOf(uid);
        Userinfo userinfo = userApiService.queryByUid(aLong);
        if (userinfo == null||userinfo.getRoleId()!=1) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_USER_NOT_EXIST);
        }
        Integer under = agentDao.queryForUserIdCount(aLong);
        Integer sub = agentDao.countNoMyFansSum(aLong);
        Integer var = agentDao.queryForUserIdCountToday(aLong);
        Integer var1 = agentDao.countNoMyFansSumToday(aLong);

        JSONObject data = new JSONObject();
        data.put("add", var + var1);
        data.put("under", under);
        data.put("sub", sub);
        data.put("joinTime", userinfo.getCreatetime());
        return WeikeResponseUtil.success(data);
    }

    /**
     * 个人佣金提现接口
     *
     * @param request
     * @return
     */
    @LoginRequired
    @PostMapping("/cash")
    public WeikeResponse getCash(HttpServletRequest request) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_USER_NOT_EXIST);
        }
        JSONObject data = new JSONObject();
        Userinfo user = userinfoMapper.selectByPrimaryKey(Long.valueOf(uid));
        Long waitMoney = moneyService.queryCashMoney(Long.valueOf(uid), 0, user);
        Long finishMoney = moneyService.queryCashMoney(Long.valueOf(uid), 1, user);
        Long cash = 0l;
        //TODO
        data.put("waitMoney", waitMoney);
        data.put("finishMoney", finishMoney);
        data.put("cash", cash);
        return WeikeResponseUtil.success(data);
    }

    /**
     * 个人佣金提现申请接口
     *
     * @param request
     * @return
     */
    @LoginRequired
    @GetMapping("/apply")
    public WeikeResponse apply(HttpServletRequest request, Long money, String account, String name) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null || money == null || account == null || name == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        }
        if (money < 0 || money > 99999) {
            return WeikeResponseUtil.fail(ResponseCode.MONEY_MAX);
        }
        Userinfo user = userinfoMapper.selectByPrimaryKey(Long.valueOf(uid));
        if (user == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_USER_NOT_EXIST);
        }
        ApplyCash applyCash = new ApplyCash();
        applyCash.setUserid(user.getId().intValue());
        applyCash.setMoney(money);
        applyCash.setAccount(account);
        applyCash.setName(name);
        Integer temp = sysAdviceDao.applyCash(applyCash);
        if (temp == 1) {
            return WeikeResponseUtil.success();
        }
        log.warning("用户提现失败-UID=" + uid);
        return WeikeResponseUtil.fail("100063", "申请提现失败请重试");

    }

    /**
     * 个人佣金提现申请查询 分页
     *
     * @param request
     * @return
     */
    @LoginRequired
    @GetMapping("/queryApply")
    public WeikeResponse queryApply(HttpServletRequest request, PageParam pageParam) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        }
        PageParam param = new PageParam(pageParam.getPageNo(), pageParam.getPageSize());
        List<ApplyCash> temp = sysAdviceDao.queryApplyCash(Integer.valueOf(uid), param.getStartRow(), pageParam.getPageSize());
        return WeikeResponseUtil.success(temp);
    }

    /**
     * 查看会员详情
     *
     * @param request
     * @param id
     * @return
     */
    @LoginRequired
    @PostMapping("/child")
    public WeikeResponse getChild(HttpServletRequest request, Long id) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_USER_NOT_EXIST);
        }
        Userinfo userinfo = userinfoMapper.selectByPrimaryKey(Long.valueOf(uid));
        Integer roleId = userinfo.getRoleId();

        if (roleId == 3) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        }
        JSONObject var = new JSONObject();
        var = memberService.queryMemberDetail(id, userinfo.getId().intValue());
        return WeikeResponseUtil.success(var);
    }   //

    /**
     * 升级代理接口
     *
     * @param request
     * @param id      要升级的用户id
     * @param score   佣金比率
     * @return
     */
    @LoginRequired
    @PostMapping("/upAgent")
    public WeikeResponse upAgent(HttpServletRequest request, Integer id, Integer score) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_USER_NOT_EXIST);
        }
        if (score < 0 || score > 100) {
            return WeikeResponseUtil.fail(ResponseCode.INT_CUSY);
        }
        Boolean var = userApiService.upAgent(id, Integer.valueOf(uid), score);
        if (var == false) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_AUTHORITY_ERROR);
        }
        return WeikeResponseUtil.success(var);
    }


}
