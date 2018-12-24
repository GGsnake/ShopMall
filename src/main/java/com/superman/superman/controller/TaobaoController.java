package com.superman.superman.controller;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.annotation.LoginRequired;
import com.superman.superman.req.TaoBaoSerachBean;
import com.superman.superman.service.TaoBaoApiService;
import com.superman.superman.utils.Constants;
import com.superman.superman.utils.ResponseCode;
import com.superman.superman.utils.WeikeResponse;
import com.superman.superman.utils.WeikeResponseUtil;
import com.taobao.api.request.TbkDgMaterialOptionalRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
//        JSONObject data = taoBaoApiServicel.se(Long.valueOf(uid), q, cat, is_tmall, has_coupon, page_no.longValue(), page_size.longValue(), sort, null);
        JSONObject jsonObject = taoBaoApiServicel.serachGoodsAll(taoBaoSerachBean, Long.valueOf(uid));
        return WeikeResponseUtil.success(jsonObject);

    }

    @LoginRequired
    @GetMapping("/opt")
    public WeikeResponse optIndex(HttpServletRequest request, Integer opt) {
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
//        JSONObject data = taoBaoApiServicel.se(Long.valueOf(uid), q, cat, is_tmall, has_coupon, page_no.longValue(), page_size.longValue(), sort, null);
        JSONObject jsonObject = taoBaoApiServicel.serachGoodsAll(taoBaoSerachBean, Long.valueOf(uid));
        return WeikeResponseUtil.success(jsonObject);

    }
}
