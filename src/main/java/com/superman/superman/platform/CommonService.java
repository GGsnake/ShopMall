package com.superman.superman.platform;

import com.superman.superman.dto.GoodsDetailReq;
import com.superman.superman.dto.GoodsDetailResponse;
import com.superman.superman.dto.GoodsSearchReq;
import com.superman.superman.dto.GoodsSearchResponse;
import com.superman.superman.model.User;
import com.superman.superman.model.enums.Platform;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 公用服务抽象
 */
public interface CommonService {
    /**
     * 商品搜索服务
     * @param GoodsSearchReq
     * @return
     */
    GoodsSearchResponse searchGoods(GoodsSearchReq GoodsSearchReq);
    /**
     * 商品搜索服务
     * @param GoodsSearchReq
     * @return
     */
    GoodsDetailResponse goodDetail(GoodsDetailReq GoodsSearchReq);
    /**
     * 执行授权操作
     * @return
     */
    String authLogin(User user);
    /**
     * 转链操作
     * @return
     */
    void convertUrl(HttpServletRequest request, HttpServletResponse response);


    Platform getPlatform();
}
