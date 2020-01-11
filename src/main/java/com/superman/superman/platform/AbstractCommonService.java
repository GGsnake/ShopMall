package com.superman.superman.platform;

import com.superman.superman.model.User;
import com.superman.superman.service.OderService;
import com.superman.superman.service.ScoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

/**
 * Created by snake on 2019-10-16.
 * 基础功能
 */
abstract  class AbstractCommonService implements CommonService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ObjectProvider<OderService> oderServiceObjectProvider;
    @Autowired
    private ObjectProvider<ScoreService> scoreServiceObjectProvider;
    @Autowired
    private ObjectProvider<RestTemplate> restTemplate;
    @Autowired
    private Environment environment;
    /**
     * 获取订单服务
     *
     * @return
     */
    protected final OderService getOderService() {
        return oderServiceObjectProvider.getIfAvailable();
    }
    /**
     * 获取积分服务
     *
     * @return
     */
    @Override
    public String authLogin(User user) {
        return "该平台暂时不支持授权操作";
    }

    /**
     * 获取积分服务
     *
     * @return
     */
    protected final ScoreService getScoreService() {
        return scoreServiceObjectProvider.getIfAvailable();
    }

    /**
     * 网络服务
     *
     * @return
     */
    protected final RestTemplate getRestTemplate() {
        return restTemplate.getIfAvailable();
    }

}
