package com.superman.superman.platform;

import com.superman.superman.model.enums.Platform;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拼多多官方的API接口服务
 * Created by GGsnake on 2018/11/14.
 */
@Service
class PddBasePlatformService extends AbstractCommonService {

    @Override
    public void searchGoods(Object baseGoodSearchRequest) {

    }

    @Override
    public void authLogin(HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    public void convertUrl(HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    public Platform getPlatform() {
        return Platform.PDD;
    }
}
