package com.superman.superman.platform;

import com.superman.superman.platform.dto.baseGoodSearchRequest;
import com.superman.superman.platform.dto.BaseGoodSearchResponse;

/**
 * 公用服务抽象
 */
public interface CommonService {
    /**
     * 商品搜索服务
     * @param baseGoodSearchRequest
     * @return
     */
    BaseGoodSearchResponse <T>   searchGoods(T baseGoodSearchRequest );
}
