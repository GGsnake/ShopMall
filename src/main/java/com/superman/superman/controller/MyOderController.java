package com.superman.superman.controller;

import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.service.OderService;
import com.superman.superman.utils.WeikeResponse;
import com.superman.superman.utils.WeikeResponseUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liujupeng on 2018/11/24.
 */
@RestController
public class MyOderController {
    @Autowired
    private OderService oderService;
    @Autowired
    private UserinfoMapper userinfoMapper;


    @ApiOperation(value = "我的订单", notes = "灵活搜索")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "devId", value = "平台类型", required = false, dataType = "Integer", paramType = "/Search"),
            @ApiImplicitParam(name = "status", value = "订单状态", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "sort", value = "排序方式", required = false, dataType = "Integer")
    })
    @PostMapping("/myOder")
    public WeikeResponse queryAllOder(@RequestParam Integer devId, Integer status) {
        var userinfo = userinfoMapper.selectByPrimaryKey(2l);
        var pddPid = userinfo.getPddpid();
        if (devId == 0) {
            var pddOderList = oderService.queryPddOderListToId(pddPid, status,status);
            return WeikeResponseUtil.success(pddOderList);
        }
        return WeikeResponseUtil.success();
    }


}
