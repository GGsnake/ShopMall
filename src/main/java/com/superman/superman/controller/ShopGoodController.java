package com.superman.superman.controller;

import com.superman.superman.annotation.LoginRequired;
import com.superman.superman.dto.GoodsDetailReq;
import com.superman.superman.dto.GoodsSearchReq;
import com.superman.superman.platform.CommonService;
import com.superman.superman.service.TaoBaoApiService;
import com.superman.superman.service.impl.PddApiServiceImpl;
import com.superman.superman.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by snake on 2018/11/8.
 */
@RestController
@RequestMapping("/Shop")
public class ShopGoodController {
    @Autowired
    RestTemplate restTemplate;

    private List<CommonService> platformCommonServices;

    /**
     * 超级搜索引擎
     * 具体请求参数和第三方平台api文档的一致
     * @param
     * @return
     */
    @PostMapping("/Search")
    public Response Search(GoodsSearchReq req) {
        if (this.platformCommonServices != null) {
            for (CommonService cs : this.platformCommonServices) {
                if (cs.getPlatform()==req.getPlatform()) {
                    return ResponseUtil.success(cs.searchGoods(req));
                }
            }
        }
        return null;
    }
    /**
     * 商品详情接口
     * @param req
     * @return
     */
    @LoginRequired
    @GetMapping("/Detail")
    public Response Detail(HttpServletRequest request, GoodsDetailReq req) {
//        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
//        if (uid == null) {
//            return ResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
//        }
        if (this.platformCommonServices != null) {
            for (CommonService cs : this.platformCommonServices) {
                if (cs.getPlatform()==req.getPlatform()) {
                    return ResponseUtil.success(cs.goodDetail(req));
                }
            }
        }
        return null;
    }


}