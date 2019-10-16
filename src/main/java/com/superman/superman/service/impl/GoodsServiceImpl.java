package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.AllDevOderMapper;
import com.superman.superman.dao.OderMapper;
import com.superman.superman.dao.OrderSuperDao;
import com.superman.superman.manager.ConfigQueryManager;
import com.superman.superman.model.Tboder;
import com.superman.superman.model.Userinfo;
import com.superman.superman.model.enums.Platform;
import com.superman.superman.platform.CommonService;
import com.superman.superman.req.OderPdd;
import com.superman.superman.service.GoodsService;
import com.superman.superman.service.OderService;
import com.superman.superman.service.TaoBaoApiService;
import com.superman.superman.utils.ConvertUtils;
import com.superman.superman.utils.PageParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by GGsnake on 2019/10/16.
 */
@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {

    private final Map<Platform, CommonService> commonServiceMap;


    public GoodsServiceImpl(Collection<CommonService> repaymentGateways) {
        this.commonServiceMap = repaymentGateways.stream()
                .collect(HashMap::new, (map, platform) -> map.put(platform.getPlatform(), platform), HashMap::putAll);
    }

    /**
     * 获取来源
     *
     * @return
     */
    private CommonService getPlatform(Platform platform) {
        return Optional.ofNullable(commonServiceMap.get(platform))
                .orElseThrow(() -> new IllegalArgumentException("不支持"));
    }

    @Override
    public Object searchGoods(Platform platform, PageParam pageParam) {
        CommonService platformService = getPlatform(platform);
        platformService.searchGoods(null);
        return null;
    }
}
