//package com.superman.superman.controller;
//
//import com.superman.superman.service.CollectService;
//import com.superman.superman.utils.WeikeResponse;
//import com.superman.superman.utils.WeikeResponseUtil;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.java.Log;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
///**
// * Created by liujupeng on 2018/11/20.
// */
//@Log
//@RestController
//@RequestMapping("/collect")
//public class CollectController {
//    @Autowired
//    private CollectService collectService;
//    @ApiOperation(value = "获取我的收藏", notes = "获取我的收藏")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "type", value = "平台类型", required = false, dataType = "Integer", paramType = "/Search"),
//            @ApiImplicitParam(name = "keyword", value = "关键词", required = false, dataType = "Integer"),
//            @ApiImplicitParam(name = "sort", value = "排序方式", required = false, dataType = "Integer")
//    })
//    @PostMapping("/my")
//    public WeikeResponse getMyCollect(@RequestHeader(value = "token",required = false) String token){
//        //token权限校验
////        collectService.addCollect("");
//        return WeikeResponseUtil.success("成功");
//    }
//
//    @ApiOperation(value = "全局搜索", notes = "全局搜索")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "type", value = "平台类型", required = false, dataType = "Integer", paramType = "/Search"),
//            @ApiImplicitParam(name = "sort", value = "排序方式", required = false, dataType = "Integer")
//    })
//    @PostMapping("/add")
//    public WeikeResponse addMyCollect(@RequestHeader(value = "token",required = false) String token){
//        //token权限校验
////        collectService.addCollect("");
//        return WeikeResponseUtil.success("成功");
//    }
//
//
//    @ApiOperation(value = "删除收藏", notes = "单次删除")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "type", value = "平台类型", required = false, dataType = "Integer", paramType = "/Search"),
//            @ApiImplicitParam(name = "sort", value = "排序方式", required = false, dataType = "Integer")
//    })
//    @PostMapping("/delete")
//    public WeikeResponse deleteMyCollect(@RequestHeader(value = "token",required = false) String token){
//        //token权限校验
////        collectService.addCollect("");
//        return WeikeResponseUtil.success("成功");
//    }
//}
