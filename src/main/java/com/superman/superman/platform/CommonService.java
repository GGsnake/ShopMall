package com.superman.superman.platform;

import com.superman.superman.model.enums.Platform;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 公用服务抽象
 */
public interface CommonService {
    /**
     * 商品搜索服务
     * @param baseGoodSearchRequest
     * @return
     */
    void  searchGoods(Object baseGoodSearchRequest);
    /**
     * 执行授权操作
     * @return
     */
    void authLogin(HttpServletRequest request, HttpServletResponse response);
    /**
     * 转链操作
     * @return
     */
    void convertUrl(HttpServletRequest request, HttpServletResponse response);


    Platform getPlatform();
}
