package com.superman.superman.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.service.MemberService;
import com.superman.superman.service.TaoBaoApiService;
import com.superman.superman.service.impl.PddApiServiceImpl;
import com.superman.superman.utils.EveryUtils;
import com.superman.superman.utils.Result;
import com.superman.superman.utils.WeikeResponse;
import com.superman.superman.utils.WeikeResponseUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

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
    private TaoBaoApiService taoBaoApiService;
    @Autowired
    private MemberService memberService;
    static final String SERVER_URL = "https://api.jd.com/routerjson";
    static final String accessToken = "ed69acd6-dbc7-4fc5-a830-135e63d19692";
    static final String appKey = "D4236C4D973B80F70F8B8929E2C226CB";
    static final String appSecret = "2d0d4a0563e543dab280774a8b946db3";
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
            @ApiImplicitParam(name = "type", value = "平台类型", required = false, dataType = "Integer", paramType = "/Search"),
            @ApiImplicitParam(name = "keyword", value = "关键词", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "sort", value = "排序方式", required = false, dataType = "Integer")
    })
    @PostMapping("/Search")
    public WeikeResponse Search(@RequestParam(value = "page", defaultValue = "1", required = false) Integer page, @RequestParam(value = "pagesize", defaultValue = "10", required = false) Integer pagesize, @RequestParam(value = "type", defaultValue = "0", required = false) Integer type, @RequestParam(value = "keyword", defaultValue = "", required = false) String keyword, @RequestParam(value = "sort", defaultValue = "0", required = false) Integer sort,
                                @RequestParam(value = "with_coupon", defaultValue = "0", required = false) Integer with_coupon

    ) {

        if (type == 0) {
            JSONObject pddGoodList = pddApiService.getPddGoodList(6l, pagesize, page, sort, with_coupon == 0 ? true : false, keyword, 2l, 1);
            return WeikeResponseUtil.success(pddGoodList);
        }
        if (type == 1) {
            JSONObject jsonObject = taoBaoApiService.serachGoods(keyword, null, true, page.longValue(), pagesize.longValue(), null, null);
            return WeikeResponseUtil.success(jsonObject);
//
//            UnionThemeGoodsServiceQueryCouponGoodsRequest request = new UnionThemeGoodsServiceQueryCouponGoodsRequest();
//            UnionThemeGoodsServiceQueryCouponGoodsResponse response;
//            request.setFrom(1);
//            request.setPageSize(10);
//            try {
//                response = client.execute(request);
//                logger.info(response.getQueryCouponGoodsResult());
//            } catch (JdException e) {
//                e.printStackTrace();
//            }
//

        }
        if (type == 2) {
            JSONObject re = pddApiService.pddDetail(3846603883l, String.valueOf(1));

            return WeikeResponseUtil.success(re);
        }
//        if (type == 3) {
//            UnionSearchGoodsParamQueryRequest request=new UnionSearchGoodsParamQueryRequest();
//
//            request.setPageIndex( 1 );
//            request.setPageSize( 10 );
//
//            try {
//
//                UnionSearchGoodsParamQueryResponse response=client.execute(request);
//                String queryResult = response.getQueryPromotionGoodsByParamResult();
//
//                JSONArray jsonObject = JSON.parseArray(queryResult);
//                for (int i = 0; i < jsonObject.size(); i++) {
//                    JSONObject o = (JSONObject) jsonObject.get(i);
//                    //佣金比率 千分比
//                    Long promotion_rate = o.getLong("promotion_rate");
//                    //最低团购价 千分比
//                    Long min_group_price = o.getLong("min_group_price");
//                    //优惠卷金额 千分比
//                    Long coupon_discount = o.getLong("coupon_discount");
//                    //佣金计算
//                    Float after = Float.valueOf(min_group_price - coupon_discount);
//                    Float promoto = Float.valueOf(promotion_rate) / 1000;
//                    Float comssion = Float.valueOf(after * promoto);
//                    Integer rmb = (int) (comssion * rang);
//                    Float bondList = (rmb * bonus);
//                    o.put("bond",bondList);
//                }
//
//                logger.info(response.getQueryPromotionGoodsByParamResult());
//
//            } catch (JdException e) {
//                e.printStackTrace();
//            }
//        }

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
    @PostMapping("/Detail")
    public WeikeResponse Detail(@RequestParam(value = "goodId", required = true) Integer goodId
) {
        JSONObject jsonObject = pddApiService.pddDetail(goodId.longValue(), "7");

        return WeikeResponseUtil.success(jsonObject);
    }


}