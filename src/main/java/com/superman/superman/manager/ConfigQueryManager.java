package com.superman.superman.manager;

import com.alibaba.fastjson.JSONObject;
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
    @Autowired
    private RedisUtil redisUtil;

    public String queryForKey(String key) {
        String value = "Config:" + key;
        if (redisUtil.hasKey(value)) {
            return redisUtil.get(key);
        }
        String config = settingDao.querySetting(key).getConfigValue();
        redisUtil.set(key, config);
        redisUtil.expire(key, 60, TimeUnit.SECONDS);
        return config;
    }
}
