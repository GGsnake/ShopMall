package com.superman.superman.service;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.model.ScoreBean;

/**
 * Created by liujupeng on 2018/11/14.
 */
public interface JdApiService {
    ScoreBean queryJdOder(String id);
    JSONObject convertJd(Long jdpid,Long goodId);
}
