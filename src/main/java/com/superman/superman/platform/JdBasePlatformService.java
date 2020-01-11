package com.superman.superman.platform;

import com.superman.superman.dto.GoodsSearchReq;
import com.superman.superman.dto.GoodsSearchResponse;
import com.superman.superman.model.User;
import com.superman.superman.model.enums.Platform;
import com.superman.superman.platform.dto.PddGoodSearchRequest;
import com.superman.superman.platform.dto.baseGoodSearchRequest;
import com.superman.superman.platform.dto.BaseGoodSearchResponse;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 京东官方的API接口服务
 * Created by GGsnake on 2018/11/14.
 */
@Service
class JdBasePlatformService extends AbstractCommonService {


    @Override
    public GoodsSearchResponse searchGoods(GoodsSearchReq req) {
        return null;
    }


    @Override
    public void convertUrl(HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    public Platform getPlatform() {
        return Platform.JD;
    }
}
