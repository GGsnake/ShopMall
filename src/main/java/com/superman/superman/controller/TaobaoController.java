package com.superman.superman.controller;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.annotation.LoginRequired;
import com.superman.superman.service.TaoBaoApiService;
import com.superman.superman.utils.*;
import com.taobao.api.request.TbkDgMaterialOptionalRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by liujupeng on 2018/12/20.
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
     * @param opt
     * @param tbsort
     * @param pageParam
     * @return
     */
    @LoginRequired
    @GetMapping("/opt")
    public WeikeResponse optIndex(HttpServletRequest request, Integer opt, @RequestParam(value = "tbsort", required = false, defaultValue = "total_sales_des") String tbsort, PageParam pageParam) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        }
        TbkDgMaterialOptionalRequest taoBaoSerachBean = new TbkDgMaterialOptionalRequest();
        if (opt == 1) {
            taoBaoSerachBean.setQ("淘抢购");
        }
        if (opt == 2) {
            taoBaoSerachBean.setQ("聚划算");

        }
        if (opt == 3) {
            taoBaoSerachBean.setQ("9.9包邮");

        }
        if (opt == 4) {
            taoBaoSerachBean.setQ("生活家居");
        }
        if (opt == 5) {
            taoBaoSerachBean.setQ("爆");
        }
        taoBaoSerachBean.setPageSize(Long.valueOf(pageParam.getPageSize()));
        taoBaoSerachBean.setPageNo(Long.valueOf(pageParam.getPageNo()));
        taoBaoSerachBean.setSort(tbsort);
//        JSONObject data = taoBaoApiServicel.se(Long.valueOf(uid), q, cat, is_tmall, has_coupon, page_no.longValue(), page_size.longValue(), sort, null);
        JSONObject jsonObject = taoBaoApiServicel.indexSearch(taoBaoSerachBean, Long.valueOf(uid));
        return WeikeResponseUtil.success(jsonObject);
    }

    /**
     * 首页爆款宝贝
     *
     * @param request
     * @param opt
     * @param tbsort
     * @param pageParam
     * @return
     */
    @LoginRequired
    @GetMapping("/hotGoods")
    public WeikeResponse hotGoods(HttpServletRequest request, Integer opt, @RequestParam(value = "tbsort", required = false, defaultValue = "total_sales_des") String tbsort, PageParam pageParam) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        }
        TbkDgMaterialOptionalRequest taoBaoSerachBean = new TbkDgMaterialOptionalRequest();
        if (opt == 1) {
            taoBaoSerachBean.setQ("淘抢购");
        }
        if (opt == 2) {
            taoBaoSerachBean.setQ("聚划算");

        }
        if (opt == 3) {
            taoBaoSerachBean.setQ("9.9包邮");

        }
        if (opt == 4) {
            taoBaoSerachBean.setQ("生活家居");
        }
        taoBaoSerachBean.setPageSize(Long.valueOf(pageParam.getPageSize()));
        taoBaoSerachBean.setPageNo(Long.valueOf(pageParam.getPageNo()));
        taoBaoSerachBean.setSort(tbsort);
        JSONObject jsonObject = taoBaoApiServicel.indexSearch(taoBaoSerachBean, Long.valueOf(uid));
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
        if (uid == null||tkl==null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        }
        JSONObject jsonObject = taoBaoApiServicel.convertTaobaoTkl(tkl);
        if (jsonObject==null){
            return WeikeResponseUtil.fail("100088","淘口令不正确");
        }
        return WeikeResponseUtil.success(jsonObject);
    }


}
