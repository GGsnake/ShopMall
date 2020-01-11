package com.superman.superman.platform;

import com.superman.superman.dto.GoodsSearchReq;
import com.superman.superman.dto.GoodsSearchResponse;
import com.superman.superman.model.enums.Platform;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 淘宝官方的API接口服务
 * Created by GGsnake on 2018/11/14.
 */
@Service
class TaobaoBasePlatformService extends AbstractCommonService {

    @Override
    public GoodsSearchResponse searchGoods(GoodsSearchReq baseGoodSearchRequest) {
        return null;
    }


    @Override
    public void convertUrl(HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    public Platform getPlatform() {
        return Platform.TAOBAO;
    }
}
