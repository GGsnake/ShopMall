package com.superman.superman.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.annotation.LoginRequired;
import com.superman.superman.model.CollectBean;
import com.superman.superman.req.Collect;
import com.superman.superman.service.CollectService;
import com.superman.superman.utils.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/** 收藏控制器
 * Created by liujupeng on 2018/11/20.
 */
@Log
@RestController
@RequestMapping("/collect")
public class CollectController {
    @Autowired
    private CollectService collectService;

    /**
     * 获取我的收藏 分页加载
     * @param request
     * @param pageParam
     * @return
     */
    @LoginRequired
    @PostMapping("/my")
    public WeikeResponse getMyCollect(HttpServletRequest request, PageParam pageParam) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_USER_NOT_EXIST);
        }
        if (pageParam == null || pageParam.getPageNo() == 0 || pageParam.getPageSize() == 0) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        }
        PageParam temp = new PageParam(pageParam.getPageNo(), pageParam.getPageSize());
        JSONArray collectBeans = collectService.queryCollect(Long.valueOf(uid), temp);
        Integer count = collectService.countCollect(Long.valueOf(uid));
        JSONObject data = new JSONObject();
        data.put("list", collectBeans);
        data.put("count", count);
        return WeikeResponseUtil.success(data);
    }

    /**
     * 新增我的收藏
     * @param request
     * @param body
     * @return
     */
    @LoginRequired
    @PostMapping("/add")
    public WeikeResponse addMyCollect(HttpServletRequest request,@RequestBody String body) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null)
            return WeikeResponseUtil.fail(ResponseCode.COMMON_USER_NOT_EXIST);

        Collect param = JSONObject.parseObject(body, Collect.class);
        if (!param.param())
            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        CollectBean data=new CollectBean();
        data.setTitle(param.getTitle());
        data.setPrice(param.getPrice());
        data.setGoodId(param.getGoodId());
        data.setSrc(param.getSrc());
        data.setImage(param.getImage());
        data.setUserId(Long.valueOf(uid));
        data.setCoupon_price(param.getCoupon_price());
        data.setCoupon(param.getCoupon());
        data.setPromotion_rate(param.getPromotion_rate());
        data.setVolume(param.getVolume());
        Boolean aBoolean = collectService.addCollect(data);
        if (aBoolean==false)
            return WeikeResponseUtil.fail(ResponseCode.ADD_GOODS_ERROR);
        return WeikeResponseUtil.success();
    }

    /**
     * 删除我的收藏
     * @param request
     * @param id
     * @return
     */
    @LoginRequired
    @PostMapping("/{id}")
    public WeikeResponse deleteMyCollect(HttpServletRequest request,@PathVariable(value = "id") Integer id) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null||id==null)
            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        Boolean flag = collectService.deleteCollect(id, Long.valueOf(uid));
        if (flag)
            return WeikeResponseUtil.success();
        return WeikeResponseUtil.fail(ResponseCode.DELETE_ERROR);
    }
}
