package com.superman.superman.controller;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.annotation.LoginRequired;
import com.superman.superman.model.Userinfo;
import com.superman.superman.service.JdApiService;
import com.superman.superman.service.PddApiService;
import com.superman.superman.service.TaoBaoApiService;
import com.superman.superman.service.UserApiService;
import com.superman.superman.utils.Constants;
import com.superman.superman.utils.ResponseCode;
import com.superman.superman.utils.WeikeResponse;
import com.superman.superman.utils.WeikeResponseUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by liujupeng on 2018/12/17.
 */
@Log
@RestController
@RequestMapping("other")
public class OtherController {
    @Autowired
    private TaoBaoApiService taoBaoApiService;

    @Autowired
    private PddApiService pddApiService;

    @Autowired
    private JdApiService jdApiService;

    @Autowired
    private UserApiService userApiService;

    @LoginRequired
    @PostMapping("/convert")
    public WeikeResponse convert(HttpServletRequest request,Long goodId,Integer devId)  {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid==null)
            return WeikeResponseUtil.fail(ResponseCode.COMMON_USER_NOT_EXIST);
        if (goodId==null)
            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        Userinfo userinfo = userApiService.queryByUid(Long.valueOf(uid));
        String pddpid = userinfo.getPddpid();
        String tbpid = userinfo.getTbpid();
        String jdpid = userinfo.getJdpid();
        JSONObject data=new JSONObject();
        if (devId==0)
            data = taoBaoApiService.convertTaobao(Long.valueOf(tbpid), goodId);

        if (devId==1)
            data = pddApiService.convertPdd(pddpid,goodId);

        if (devId==2)
            data = jdApiService.convertJd(goodId, Long.valueOf(tbpid));

        if (devId==3)
            data = taoBaoApiService.convertTaobao(goodId, Long.valueOf(tbpid));
        return WeikeResponseUtil.success(data);
    }
}
