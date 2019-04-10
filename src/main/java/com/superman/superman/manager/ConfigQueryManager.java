package com.superman.superman.manager;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.annotation.FastCache;
import com.superman.superman.dao.SettingDao;
import com.superman.superman.model.Config;
import com.superman.superman.redis.RedisUtil;
import com.superman.superman.utils.WeikeResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service("configQueryManager")
public class ConfigQueryManager {
    @Autowired
    private SettingDao settingDao;
    @FastCache(timeOut = 10)
    public String queryForKey(String key) {
        String config = settingDao.querySetting(key).getConfigValue();
        return config;
    }
}
