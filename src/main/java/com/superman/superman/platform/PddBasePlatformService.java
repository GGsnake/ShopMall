package com.superman.superman.platform;

import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.pop.request.PddDdkGoodsSearchRequest;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkGoodsSearchResponse;
import com.superman.superman.dto.GoodsSearchReq;
import com.superman.superman.dto.GoodsSearchResponse;
import com.superman.superman.model.enums.Platform;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 拼多多官方的API接口服务
 * Created by snake on 2018/11/14.
 */
@Service
class PddBasePlatformService extends AbstractCommonService {
    String KEY = "48dcc8985c8e4838be2dea3aa9b6176f";
    String SECRET = "62b91c07f697bce84beec4123b01f6e108d2fa38";
    private final PopHttpClient client;

    public PddBasePlatformService() {
        client = new PopHttpClient(KEY, SECRET);
    }


    @Override
    public GoodsSearchResponse searchGoods(GoodsSearchReq req) {
        GoodsSearchResponse response = new GoodsSearchResponse();
        List<GoodsSearchResponse.GoodDetail> goodsSearchResponseArrayList = new ArrayList<>();
        PddDdkGoodsSearchRequest request = new PddDdkGoodsSearchRequest();
        request.setKeyword(req.getKeyword());
        request.setOptId(req.getOpt());
        request.setPage(req.getPageNo() == null ? 1 : req.getPageNo());
        request.setPageSize(req.getPageSize() == null ? 10 : req.getPageSize());
//        request.setWithCoupon(req.getHasCoupon() == null ? false : true);
        try {
            PddDdkGoodsSearchResponse pddDdkGoodsSearchResponse = client.syncInvoke(request);
            PddDdkGoodsSearchResponse.GoodsSearchResponse goodsSearchResponse = pddDdkGoodsSearchResponse.getGoodsSearchResponse();
            response.setGoodsDetailList(goodsSearchResponseArrayList);
            if (goodsSearchResponse == null||  goodsSearchResponse.getTotalCount()==0) return response;
            response.setTotalCount(goodsSearchResponse.getTotalCount());
            goodsSearchResponse.getGoodsList().forEach(detail -> {
                BigDecimal divide = new BigDecimal(detail.getMinNormalPrice()).movePointLeft(2);
                goodsSearchResponseArrayList
                        .add(new GoodsSearchResponse.GoodDetail()
                                .setPrice(divide)
                                .setGoodName(detail.getGoodsName())
                                .setHasCoupon(detail.getHasCoupon() ? 1 : 0)
                        );
            });
            return response.setGoodsDetailList(goodsSearchResponseArrayList);
        } catch (Exception e) {
            logger.error("请求拼多多接口失败 请重试{}", e.getMessage());
        }
        return null;
    }

    @Override
    public Platform getPlatform() {
        return Platform.PDD;
    }
}
