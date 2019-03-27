package com.superman.superman.controller;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.annotation.LoginRequired;
import com.superman.superman.service.TaoBaoApiService;
import com.superman.superman.utils.*;
import com.taobao.api.request.TbkDgMaterialOptionalRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by liujupeng on 2018/12/10.
 */
@RestController
@RequestMapping("taobao")
public class TaobaoController {
    @Autowired
    private TaoBaoApiService taoBaoApiServicel;

    @LoginRequired
    @GetMapping("/index")
    public WeikeResponse index(HttpServletRequest request, TbkDgMaterialOptionalRequest taoBaoSerachBean) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        }
        if (taoBaoSerachBean.getQ() == null) {
            taoBaoSerachBean.setQ("");
        }
        JSONObject data = taoBaoApiServicel.serachGoodsAll(taoBaoSerachBean, Long.valueOf(uid));
        return WeikeResponseUtil.success(data);
    }

    /**
     * 淘宝物料搜索引擎
     * @param request
     * @param taoBaoSerachBean
     * @return
     */
    @LoginRequired
    @GetMapping("/superTaoBao")
    public WeikeResponse superTaoBao(HttpServletRequest request, TbkDgMaterialOptionalRequest taoBaoSerachBean) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        }

        JSONObject data = taoBaoApiServicel.serachGoodsAll(taoBaoSerachBean, Long.valueOf(uid));
        return WeikeResponseUtil.success(data);
    }
    /**
     * 首页的类目活动入口
     *
     * @param request
     * @param opt       1 上百券 2 聚划算 3 9.9包邮 4生活家居 5爆款 6精选 7淘宝首页
     * @param sort
     * @param pageParam
     * @return
     */
    @LoginRequired
    @GetMapping("/opt")
    public WeikeResponse optIndex(HttpServletRequest request, Integer opt,
                                  @RequestParam(value = "sort", required = false, defaultValue = "0 ") Integer sort, PageParam pageParam) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        }
        PageParam param = new PageParam(pageParam.getPageNo(), pageParam.getPageSize());
        JSONObject paramData = new JSONObject();
        paramData.put("start", param.getStartRow());
        paramData.put("end", param.getPageSize());
        String orderFiled = null;
        if (sort == 1) {
            //券后价
            orderFiled = "couponPrice";
        }
        if (sort == 2) {
            //销量
            orderFiled = "volume";
        }
        if (sort == 3) {
            //券额度
            orderFiled = "coupon";
        }
        paramData.put("opt", opt);
        paramData.put("sort", orderFiled);
        JSONObject jsonObject = taoBaoApiServicel.goodLocalSuperForOpt(paramData, Long.valueOf(uid), null);
        return WeikeResponseUtil.success(jsonObject);
    }

    /**
     * 淘口令解析
     *
     * @param request
     * @param tkl
     * @return
     */
    @LoginRequired
    @GetMapping("/convertTb")
    public WeikeResponse convertTKl(HttpServletRequest request, String tkl) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null || tkl == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        }
        JSONObject jsonObject = taoBaoApiServicel.convertTaobaoTkl(tkl);
        if (jsonObject == null) {
            return WeikeResponseUtil.fail("100088", "淘口令不正确");
        }
        return WeikeResponseUtil.success(jsonObject);
    }


}
