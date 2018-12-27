package com.superman.superman.controller;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.annotation.LoginRequired;
import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.manager.OderManager;
import com.superman.superman.model.Oder;
import com.superman.superman.service.OderService;
import com.superman.superman.utils.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by liujupeng on 2018/11/24.
 */
@RestController
@RequestMapping("/oder")
public class MyOderController {

    @Autowired
    private OderService oderService;
    @Autowired
    private OderManager oderManager;

    @ApiOperation(value = "我的订单", notes = "灵活搜索")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "devId", value = "平台类型", required = false, dataType = "Integer", paramType = "/Search"),
            @ApiImplicitParam(name = "status", value = "订单状态", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "sort", value = "排序方式", required = false, dataType = "Integer")
    })
    @LoginRequired
    @PostMapping("/myOder")
    public WeikeResponse queryAllOder(HttpServletRequest request, PageParam pageParam, Integer devId, Integer status) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null || status == null || status >= 3 || status < 0) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        }
        var param = new PageParam(pageParam.getPageNo(), pageParam.getPageSize());
        List statusList = ConvertUtils.getStatus(devId, status);
        JSONObject allOder = new JSONObject();
        if (devId == 0) {
            allOder = oderManager.getTaobaoOder(Long.valueOf(uid), statusList, param);
        }
        if (devId == 1) {
            allOder = oderManager.getAllOder(Long.valueOf(uid), statusList, param);
        }
        return WeikeResponseUtil.success(allOder);
    }
}



