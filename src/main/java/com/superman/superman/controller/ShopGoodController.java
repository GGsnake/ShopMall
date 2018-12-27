package com.superman.superman.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.annotation.LoginRequired;
import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.model.Userinfo;
import com.superman.superman.req.JdSerachReq;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

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
    private MemberService memberService;
    @Autowired
    private UserApiService userApiService;
    //    static final String SERVER_URL = "https://api.jd.com/routerjson";
//    static final String accessToken = "ed69acd6-dbc7-4fc5-a830-135e63d19692";
//    static final String appKey = "D4236C4D973B80F70F8B8929E2C226CB";
//    static final String appSecret = "2d0d4a0563e543dab280774a8b946db3";
    //    public JdClient client = new DefaultJdClient(SERVER_URL, accessToken, appKey, appSecret);
    private final static Logger logger = LoggerFactory.getLogger(ShopGoodController.class);


    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/index")
    public Result getIndex() {
//        String pddGoodList = pddApiService.getPddGoodList();
        long l = System.currentTimeMillis();
        Object o = redisTemplate.opsForValue().get("Policy:1");
        logger.info(String.valueOf(System.currentTimeMillis() - l));
        return Result.ok(String.valueOf(System.currentTimeMillis() - l));
    }

    /**
     * @param page
     * @param pagesize
     * @param type     平台 0 淘宝 1 拼多多 2
     * @param keyword
     * @param sort
     * @return
     */
    @ApiOperation(value = "全局搜索", notes = "全局搜索")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "平台类型 0拼多多 1淘宝 2京东 3天猫", required = false, dataType = "Integer", paramType = "/Search"),
            @ApiImplicitParam(name = "keyword", value = "关键词", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "sort", value = "排序方式 ", required = false, dataType = "Integer")
    })
    @LoginRequired
    @PostMapping("/Search")
    public WeikeResponse Search(HttpServletRequest request, @RequestParam(value = "page", defaultValue = "1", required = false) Integer page, @RequestParam(value = "pagesize", defaultValue = "10", required = false) Integer pagesize, @RequestParam(value = "type", defaultValue = "0", required = false) Integer type, @RequestParam(value = "keyword", defaultValue = "", required = false) String keyword, @RequestParam(value = "sort", defaultValue = "0", required = false) Integer sort,
                                @RequestParam(value = "with_coupon", defaultValue = "0", required = false) Integer with_coupon, @RequestParam(value = "opt", required = false) Long opt, @RequestParam(value = "tbsort", required = false, defaultValue = "tk_rate_des") String tbsort, @RequestParam(value = "tbcat", required = false) String tbcat

    ) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        }
        JSONObject data;
        if (type == 0) {
            data = pddApiService.getPddGoodList(Long.valueOf(uid), pagesize, page, sort, with_coupon == 0 ? true : false, keyword, opt, 1);
            return WeikeResponseUtil.success(data);
        }
        if (type == 1) {
//            data = taoBaoApiService.serachGoods(Long.valueOf(uid), keyword, null, true, true, page.longValue(), pagesize.longValue(), tbsort, null);
            TbkDgMaterialOptionalRequest req = new TbkDgMaterialOptionalRequest();
            req.setPageNo(Long.valueOf(page));
            req.setPageSize(Long.valueOf(pagesize));
            req.setIsTmall(false);
            if (tbcat != null) {
                req.setCat(tbcat);
            }
            req.setQ(keyword);
            data = taoBaoApiService.serachGoodsAll(req, Long.valueOf(uid));
            return WeikeResponseUtil.success(data);
        }
        if (type == 2) {
            JdSerachReq var1 = new JdSerachReq();
            var1.setKeyword(keyword);
            var1.setPage(page);
            var1.setPagesize(pagesize);
            data = jdApiService.serachGoodsAll(var1, Long.valueOf(uid));
            return WeikeResponseUtil.success(data);
        }
        if (type == 3) {
            JdSerachReq var1 = new JdSerachReq();
            var1.setKeyword(keyword);
            var1.setPage(page);
            var1.setPagesize(pagesize);
            data = jdApiService.serachGoodsAll(var1, Long.valueOf(uid));
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
            @ApiImplicitParam(name = "goodId", value = "商品Id", required = true, dataType = "Integer")
    })
    @LoginRequired
    @PostMapping("/Detail")
    public WeikeResponse Detail(HttpServletRequest request, Long goodId, Integer devId) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        }
        JSONObject var = new JSONObject();

        if (devId == 0) {
            var = taoBaoApiService.deatil(goodId, Long.valueOf(uid));
        }
        if (devId == 1) {
            var = pddApiService.pddDetail(goodId, uid);

        }
        if (devId == 2) {
//            var=pddApiService.pddDetail(goodId, Long.valueOf(uid));

        }
        if (devId == 3) {

        }
        return WeikeResponseUtil.success(var);
    }


}