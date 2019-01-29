package com.superman.superman.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pdd.pop.sdk.http.api.request.PddDdkGoodsSearchRequest;
import com.superman.superman.annotation.LoginRequired;
import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.model.Userinfo;
import com.superman.superman.redis.RedisUtil;
import com.superman.superman.req.JdSerachReq;
import com.superman.superman.req.PddSerachBean;
import com.superman.superman.service.JdApiService;
import com.superman.superman.service.MemberService;
import com.superman.superman.service.TaoBaoApiService;
import com.superman.superman.service.UserApiService;
import com.superman.superman.service.impl.PddApiServiceImpl;
import com.superman.superman.utils.*;
import com.taobao.api.request.TbkDgMaterialOptionalRequest;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jd.union.open.goods.query.request.GoodsReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

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
     * @param type    平台 0 拼多多 1 淘宝 2京东 3天猫
     * @param keyword
     * @param sort
     * @return
     */
    @LoginRequired
    @PostMapping("/Search")
    public WeikeResponse Search(HttpServletRequest request, @RequestParam(value = "type", defaultValue = "0", required = false) Integer type, @RequestParam(value = "keyword", defaultValue = "", required = false) String keyword, @RequestParam(value = "sort", defaultValue = "0", required = false) Integer sort,
                                @RequestParam(value = "with_coupon", defaultValue = "0", required = false) Integer with_coupon, @RequestParam(value = "jd_coupon", defaultValue = "1", required = false) Integer jd_coupon, @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize, @RequestParam(value = "pageNo", defaultValue = "1", required = false) Integer pageNo,
                                Integer cid, Long opt, @RequestParam(value = "tbsort", required = false, defaultValue = "tk_rate_des") String tbsort, @RequestParam(value = "jdsort", required = false, defaultValue = "commissionShare") String jdsort, @RequestParam(value = "jdorder", required = false, defaultValue = "desc") String jdorder, String tbcat

    ) {
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
     * @param goodId
     * @return
     */
    @ApiOperation(value = "商品详情", notes = "单个ID")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goodId", value = "商品Id", required = true, dataType = "Long")
    })
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
        JSONObject var = new JSONObject();
        if (type == 0) {
            var = taoBaoApiService.deatil(goodId);
        }
        if (type == 1) {
            var = pddApiService.pddDetail(goodId);
        }
        if (type == 2) {
            var = jdApiService.jdDetail(goodId);
        }
        redisUtil.set(key, var.toJSONString());
        redisUtil.expire(key, 20, TimeUnit.SECONDS);
        return WeikeResponseUtil.success(var);
    }


}