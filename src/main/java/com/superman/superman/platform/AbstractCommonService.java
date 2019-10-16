package com.superman.superman.platform;

import com.superman.superman.service.OderService;
import com.superman.superman.service.ScoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
/**
 * Created by GGsnake on 2019-10-16.
 * 基础功能
 */
abstract  class AbstractCommonService implements CommonService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ObjectProvider<OderService> oderServiceObjectProvider;
    @Autowired
    private ObjectProvider<ScoreService> scoreServiceObjectProvider;
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
    protected final ScoreService getScoreService() {
        return scoreServiceObjectProvider.getIfAvailable();
    }

}
