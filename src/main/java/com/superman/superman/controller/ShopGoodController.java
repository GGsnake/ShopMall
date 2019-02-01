package com.superman.superman.controller;

import com.alibaba.fastjson.JSONObject;
import com.pdd.pop.sdk.http.api.request.PddDdkGoodsSearchRequest;
import com.superman.superman.annotation.LoginRequired;
import com.superman.superman.redis.RedisUtil;
import com.superman.superman.service.JdApiService;
import com.superman.superman.service.TaoBaoApiService;
import com.superman.superman.service.impl.PddApiServiceImpl;
import com.superman.superman.utils.*;
import com.taobao.api.request.TbkDgMaterialOptionalRequest;
import jd.union.open.goods.query.request.GoodsReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * Created by liujupeng on 2018/11/8.
 */
@RestController
@RequestMapping("/Shop")
public class ShopGoodController {
    @Autowired
    private PddApiServiceImpl pddApiService;
    @Autowired
    private JdApiService jdApiService;
    @Autowired
    private TaoBaoApiService taoBaoApiService;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 超级搜索引擎
     *
     * @param type    平台 0 拼多多 1 淘宝 2京东 3天猫
     * @param  //具体参数和第三方平台api文档的一直
     * @return
     */
    @LoginRequired
    @PostMapping("/Search")
    public WeikeResponse Search(HttpServletRequest request, @RequestParam(value = "type", defaultValue = "0", required = false) Integer type,
                                @RequestParam(value = "keyword", defaultValue = "", required = false) String keyword,
                                @RequestParam(value = "sort", defaultValue = "0", required = false) Integer sort,
                                @RequestParam(value = "with_coupon", defaultValue = "0", required = false) Integer with_coupon,
                                @RequestParam(value = "jd_coupon", defaultValue = "1", required = false) Integer jd_coupon,
                                @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                @RequestParam(value = "pageNo", defaultValue = "1", required = false) Integer pageNo,
                                @RequestParam(value = "tbsort", required = false, defaultValue = "tk_rate_des") String tbsort,
                                @RequestParam(value = "jdsort", required = false, defaultValue = "commissionShare") String jdsort,
                                @RequestParam(value = "jdorder", required = false, defaultValue = "desc") String jdorder,
                                String tbcat, Integer cid, Long opt) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        }
        JSONObject data;
        if (type == 0) {
            //拼多多搜索引擎
            PddDdkGoodsSearchRequest pddSerachBean = new PddDdkGoodsSearchRequest();
            pddSerachBean.setPage(pageNo);
            pddSerachBean.setPageSize(pageSize);
            pddSerachBean.setKeyword(keyword);
            pddSerachBean.setWithCoupon(with_coupon == 0 ? true : false);
            pddSerachBean.setOptId(opt);
            pddSerachBean.setSortType(sort);
            data = pddApiService.serachGoodsAll(pddSerachBean, Long.valueOf(uid));
            return WeikeResponseUtil.success(data);
        }
        if (type == 1) {
            //淘宝搜索引擎
            TbkDgMaterialOptionalRequest req = new TbkDgMaterialOptionalRequest();
            req.setPageNo(Long.valueOf(pageNo));
            req.setPageSize(Long.valueOf(pageSize));
            req.setIsTmall(false);
            req.setSort(tbsort);
            if (tbcat != null && Integer.valueOf(tbcat) != 0) {
                req.setCat(tbcat);
            }
            if (keyword.equals("") || keyword == null) {
                req.setQ("");
            } else {
                req.setQ(keyword);
            }
            data = taoBaoApiService.serachGoodsAll(req, Long.valueOf(uid));
            return WeikeResponseUtil.success(data);
        }
        if (type == 2) {
            //京东搜索引擎
            GoodsReq goodsReq = new GoodsReq();
            goodsReq.setKeyword(keyword);
            if (cid != null) {
                goodsReq.setCid3(Long.valueOf(cid));
            }
            goodsReq.setSort(jdorder);
            goodsReq.setSortName(jdsort);
            goodsReq.setPageIndex(pageNo);
            goodsReq.setPageSize(pageSize);
            goodsReq.setIsCoupon(jd_coupon);
            data = jdApiService.serachGoodsAllJd(goodsReq, Long.valueOf(uid));
            return WeikeResponseUtil.success(data);
        }
        if (type == 3) {
            //天猫
            TbkDgMaterialOptionalRequest req = new TbkDgMaterialOptionalRequest();
            req.setPageNo(Long.valueOf(pageNo));
            req.setPageSize(Long.valueOf(pageSize));
            req.setIsTmall(true);
            req.setSort(tbsort);
            if (tbcat != null && Integer.valueOf(tbcat) != 0) {
                req.setCat(tbcat);
            }
            if (keyword.equals("") || keyword == null) {
                req.setQ("");
            } else {
                req.setQ(keyword);
            }
            req.setQ(keyword);
            data = taoBaoApiService.serachGoodsAll(req, Long.valueOf(uid));
            return WeikeResponseUtil.success(data);
        }
        return null;
    }

    /**
     * 商品详情接口
     *
     * @param goodId 商品Id
     * @param type   平台类型 0 淘宝天猫 1 拼多多 2 京东
     * @return
     */
    @LoginRequired
    @GetMapping("/Detail")
    public WeikeResponse Detail(HttpServletRequest request, Long goodId, Integer type) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        }
        String key = "Detail:" + type.toString() + uid + goodId;
        if (redisUtil.hasKey(key)) {
            return WeikeResponseUtil.success(JSONObject.parseObject(redisUtil.get(key)));
        }
        JSONObject data = new JSONObject();
        if (type == 0) {
            data = taoBaoApiService.deatil(goodId);
        }
        if (type == 1) {
            data = pddApiService.pddDetail(goodId);
        }
        if (type == 2) {
            data = jdApiService.jdDetail(goodId);
        }
        redisUtil.set(key, data.toJSONString());
        redisUtil.expire(key, 20, TimeUnit.SECONDS);
        return WeikeResponseUtil.success(data);
    }


}