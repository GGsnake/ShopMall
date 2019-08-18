package com.superman.superman.controller;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.annotation.LoginRequired;
import com.superman.superman.service.TaoBaoApiService;
import com.superman.superman.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Created by liujupeng on 2018/12/10.
 */
@RestController
@RequestMapping("taobao")
public class TaobaoController {
    @Autowired
    private TaoBaoApiService taoBaoApiServicel;

//    @LoginRequired
//    @GetMapping("/index")
//    public WeikeResponse index(HttpServletRequest request, TbkDgMaterialOptionalRequest taoBaoSerachBean) {
//        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
//        if (uid == null) {
//            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
//        }
//        if (taoBaoSerachBean.getQ() == null) {
//            taoBaoSerachBean.setQ("");
//        }
//        JSONObject data = taoBaoApiServicel.serachGoodsAll(taoBaoSerachBean, Long.valueOf(uid));
//        return WeikeResponseUtil.success(data);
//    }
//
//    /**
//     * 淘宝物料搜索引擎
//     * @param request
//     * @param taoBaoSerachBean
//     * @return
//     */
//    @LoginRequired
//    @GetMapping("/superTaoBao")
//    public WeikeResponse superTaoBao(HttpServletRequest request, TbkDgMaterialOptionalRequest taoBaoSerachBean) {
//        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
//        if (uid == null) {
//            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
//        }
//
//        JSONObject data = taoBaoApiServicel.serachGoodsAll(taoBaoSerachBean, Long.valueOf(uid));
//        return WeikeResponseUtil.success(data);
//    }
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
        JSONObject data = taoBaoApiServicel.convertTaobaoTkl(tkl);
        Optional.ofNullable(data).orElseGet(JSONObject::new);
        return WeikeResponseUtil.success(data);

    }


}
