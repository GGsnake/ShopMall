package com.superman.superman.manager;

import com.superman.superman.dao.SettingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("configQueryManager")
public class ConfigQueryManager {
    @Autowired
    private SettingDao settingDao;

    /**
     * 配置文件 查找
     * @param key
     * @return
     */
    public String queryForKey(String key) {
        String config = settingDao.querySetting(key).getConfigValue();
        return config;
    }
}
