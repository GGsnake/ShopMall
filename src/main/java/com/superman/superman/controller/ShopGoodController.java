package com.superman.superman.controller;

import com.alibaba.fastjson.JSONObject;
import com.pdd.pop.sdk.http.api.request.PddAdBalanceGetRequest;
import com.pdd.pop.sdk.http.api.request.PddDdkGoodsSearchRequest;
import com.superman.superman.annotation.LoginRequired;
import com.superman.superman.dto.GoodsSearchReq;
import com.superman.superman.dto.GoodsSearchResponse;
import com.superman.superman.model.enums.Mall;
import com.superman.superman.model.enums.Platform;
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
    private PddApiServiceImpl pddApiService;
    @Autowired
    private TaoBaoApiService taoBaoApiService;
    @Autowired
    RestTemplate restTemplate;

    private HashMap<Platform, CommonService> platformCommonServiceMap;

    public ShopGoodController(Collection<CommonService> commonServices) {
        this.platformCommonServiceMap = commonServices.stream()
                .collect(HashMap::new, (bean, api) -> bean.put(api.getPlatform(), api), HashMap::putAll);
    }

    /**
     * 超级搜索引擎
     * 具体请求参数和第三方平台api文档的一致
     * @param
     * @return
     */
    @PostMapping("/Search")
    public Response Search(HttpServletRequest request, GoodsSearchReq req) {
//        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
//        if (uid == null)
//            return ResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        CommonService commonService = platformCommonServiceMap.get(req.getPlatform());
        return ResponseUtil.success(commonService.searchGoods(req));
    }

//        if (type == 1) {
//            //淘宝搜索引擎
//            TbkDgMaterialOptionalRequest req = new TbkDgMaterialOptionalRequest();
//            req.setPageNo(Long.valueOf(pageNo));
//            req.setPageSize(Long.valueOf(pageSize));
//            req.setIsTmall(false);
//            req.setSort(tbsort);
//            if (tbcat != null) {
//                req.setCat(tbcat);
//            }
//            if (keyword.equals("") || keyword == null) {
//                req.setQ("");
//            } else {
//                req.setQ(keyword);
//            }
//    data = taoBaoApiService.serachGoodsAll(req, Long.valueOf(uid));
//            return ResponseUtil.success(data);
//        }
//        if (type == 2) {
//            //京东搜索引擎
//            GoodsReq goodsReq = new GoodsReq();
//            goodsReq.setKeyword(keyword);
//            if (cid != null) {
//                goodsReq.setCid3(Long.valueOf(cid));
//            }
//            goodsReq.setSort(jdorder);
//            goodsReq.setSortName(jdsort);
//            goodsReq.setPageIndex(pageNo);
//            goodsReq.setPageSize(pageSize);
//            goodsReq.setIsCoupon(jd_coupon);
//            data = jdApiService.serachGoodsAllJd(goodsReq, Long.valueOf(uid));
//            return ResponseUtil.success(data);
//        }
//        if (type == 3) {
//            //天猫
//            TbkDgMaterialOptionalRequest req = new TbkDgMaterialOptionalRequest();
//            req.setPageNo(Long.valueOf(pageNo));
//            req.setPageSize(Long.valueOf(pageSize));
//            req.setIsTmall(true);
//            req.setSort(tbsort);
//            if (tbcat != null) {
//                req.setCat(tbcat);
//            }
//            if (keyword.equals("") || keyword == null) {
//                req.setQ("");
//            } else {
//                req.setQ(keyword);
//            }
//            req.setQ(keyword);
//            data = taoBaoApiService.serachGoodsAll(req, Long.valueOf(uid));
//            return ResponseUtil.success(data);
//        }

//    /**
//     * 本地淘宝搜索引擎
//     *
//     * @param type 平台 0 拼多多 1
//     * @param
//     * @return
//     */
//    @LoginRequired
//    @PostMapping("/good")
//    public Response Search(HttpServletRequest request, @RequestParam(value = "type", defaultValue = "0", required = false) Integer type,
//                                @RequestParam(value = "sort", defaultValue = "0", required = false) Integer sort,
//                                @RequestParam(value = "tbcat", defaultValue = "0", required = false) Integer cat,
//                                @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
//                                @RequestParam(value = "pageNo", defaultValue = "1", required = false) Integer pageNo) {
//        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
//        if (uid == null) {
//            return ResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
//        }
//        PageParam param = new PageParam(pageNo, pageSize);
//        JSONObject paramData = new JSONObject();
//        paramData.put("start", param.getStartRow());
//        paramData.put("end", param.getPageSize());
//        paramData.put("istamll", type);
//        if (cat != null && cat != 0) {
//            paramData.put("cat", cat);
//        }
//        JSONObject data = taoBaoApiService.goodLocalSuperForOpt(paramData, Long.valueOf(uid), 1);
//        return ResponseUtil.success(data);
//    }
//
//    /**
//     * 京东本地搜索引擎
//     *
//     * @param
//     * @param
//     * @return
//     */
//    @LoginRequired
//    @PostMapping("/jd")
//    public Response Search(HttpServletRequest request,
//                                Integer cid,
//                                @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
//                                @RequestParam(value = "pageNo", defaultValue = "1", required = false) Integer pageNo
//    ) {
//        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
//        if (uid == null) {
//            return ResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
//        }
//        PageParam pageParam = new PageParam(pageNo, pageSize);
//        JSONObject data = jdApiService.goodLocal(pageParam, Long.valueOf(uid), 1, cid);
//        return ResponseUtil.success(data);
//
//    }
//
//    /**
//     * 拼多多本地搜索引擎
//     *
//     * @param
//     * @param
//     * @return
//     */
//    @LoginRequired
//    @PostMapping("/pdd")
//    public Response pddSearch(HttpServletRequest request,
//                                   @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
//                                   @RequestParam(value = "pageNo", defaultValue = "1", required = false) Integer pageNo) {
//        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
//        if (uid == null) {
//            return ResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
//        }
//        PageParam pageParam = new PageParam(pageNo, pageSize);
//        JSONObject data = pddApiService.getLocalGoodsAll(pageParam, Long.valueOf(uid));
//        return ResponseUtil.success(data);
//
//    }

    /**
     * 商品详情接口
     *
     * @param goodId 商品Id
     * @param mall   平台类型  淘宝天猫  拼多多  京东
     * @return
     */
    @LoginRequired
    @GetMapping("/Detail")
    public Response Detail(HttpServletRequest request, Long goodId, Mall mall) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null) {
            return ResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        }
        JSONObject data = new JSONObject();
        if (mall == Mall.TAOBAO) {
            data = taoBaoApiService.deatil(goodId);
        }
        if (mall == Mall.PDD) {
            data = pddApiService.pddDetail(goodId);
        }
        if (mall == Mall.JD) {
//            data = jdApiService.jdDetail(goodId);
        }
        return ResponseUtil.success(data);
    }


}