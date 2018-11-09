package com.superman.superman.controller;

import com.superman.superman.service.MemberService;
import com.superman.superman.service.impl.PddApiServiceImpl;
import com.superman.superman.utils.Result;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by liujupeng on 2018/11/8.
 */
@RestController
@RequestMapping("/Shop")
public class ShopGoodController {
    @Autowired
    private PddApiServiceImpl pddApiService;
    @Autowired
    private MemberService memberService;

    @GetMapping("/index")
    public Result getIndex() {
//        String pddGoodList = pddApiService.getPddGoodList();
        return Result.ok();
    }

    /**
     *
     * @param page
     * @param pagesize
     * @param type 平台 0 淘宝 1 拼多多 2
     * @param keyword
     * @param sort
     * @return
     */
    @ApiOperation(value="全局搜索", notes="全局搜索")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "平台类型", required = false, dataType = "Integer",paramType = "/Search"),
            @ApiImplicitParam(name = "keyword", value = "关键词", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "sort", value = "排序方式", required = false, dataType = "Integer")
    })
    @PostMapping("/Search")
    public Result Search(@RequestParam(value = "page", defaultValue = "1", required = false) Integer page, @RequestParam(value = "pagesize", defaultValue = "10", required = false) Integer pagesize,  @RequestParam(value = "type", defaultValue = "0", required = false) Integer type, @RequestParam(value = "keyword", defaultValue = "", required = false) String keyword, @RequestParam(value = "sort", defaultValue = "0", required = false) Integer sort

    ) {

        if (type==0){
            String pddGoodList = pddApiService.getPddGoodList(1l);
            return Result.ok(pddGoodList);
        }
        if (type==1){

        }
        if (type==2){

        }
        if (type==3){

        }

        return Result.ok();
    }
}
