package com.superman.superman.service.impl;

import com.jd.open.api.sdk.response.cps.UnionServiceQueryCommissionOrdersResponse;
import com.superman.superman.dao.ScoreDao;
import com.superman.superman.model.ScoreBean;
import com.superman.superman.service.JdApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * Created by liujupeng on 2018/11/14.
 */
@Service("jdApiService")
public class JdApiServiceImpl implements JdApiService {
    @Autowired
    private ScoreDao scoreDao;

    //    @Cacheable(value="signonCache",key="'petstore:signon:'+#username", unless="#result==null")
    @Cacheable(value = "scoreBean", key = "#id")
    public ScoreBean queryJdOder(String id) {
        ScoreBean scoreBean=new ScoreBean();
        scoreBean.setUserId(Long.valueOf(id));
        scoreBean.setDataSrc(1);
        ScoreBean exit = scoreDao.isExit(scoreBean);
        return exit;
    }
//
//    @CachePut(value = "concurrenmapcache")
//    public long save() {
//        long timestamp = new Timestamp(System.currentTimeMillis()).getTime();
//        System.out.println("进行缓存：" + timestamp);
//        return timestamp;
//    }
//
//    public long getByCache() {
//        ScoreBean exit = scoreDao.isExit(scoreBean);
//        return exit;
//    }
//    @Override
//    public String queryJdOder() {
//        UnionSearchGoodsParamQueryRequest request=new UnionSearchGoodsParamQueryRequest();
//        UnionServiceQueryCommissionOrdersResponse
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

}

