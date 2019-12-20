package com.superman.superman.service.impl;

import com.superman.superman.model.User;
import com.superman.superman.model.Userinfo;
import com.superman.superman.model.enums.Platform;
import com.superman.superman.platform.CommonService;
import com.superman.superman.service.GoodsService;
import com.superman.superman.utils.PageParam;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Override
    public String authLogin(Platform platform, User user) {
        CommonService platformService = getPlatform(platform);
        return platformService.authLogin(user);
    }
}
