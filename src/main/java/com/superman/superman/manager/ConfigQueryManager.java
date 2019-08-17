package com.superman.superman.manager;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.SettingDao;
import com.superman.superman.redis.RedisUtil;
import com.superman.superman.utils.WeikeResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service("configQueryManager")
public class ConfigQueryManager {
    @Autowired
    private SettingDao settingDao;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 配置文件 查找
     * @param value
     * @return
     */
    public String queryForKey(String value) {
        //缓存
        String key = "queryForKey:" + value;
        if (redisUtil.hasKey(key)) {
            return redisUtil.get(key);
        }
        String config = settingDao.querySetting(key).getConfigValue();
        redisUtil.set(key, config);
        redisUtil.expire(key, 60, TimeUnit.SECONDS);
        return config;
    }
}
