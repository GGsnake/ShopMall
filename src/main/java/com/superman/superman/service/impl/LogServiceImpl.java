package com.superman.superman.service.impl;

import com.superman.superman.dao.LogDao;
import com.superman.superman.model.UserLog;
import com.superman.superman.service.LogService;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created by liujupeng on 2018/11/24.
 */
@Log
@Service("logService")
public class LogServiceImpl implements LogService {
    @Autowired
    private LogDao logDao;

    /**
     * 异步上报用户登录
     * @param uid
     * @param ip
     */
    @Async
    @Override
    public void addUserLoginLog(@NonNull Long uid,@NonNull String ip) {
        UserLog loge=new UserLog();
        loge.setUserId(uid.intValue());
        loge.setOperation(0);
        loge.setIp(ip);
        try {
            logDao.addUserLogin(loge);
        } catch (Exception e) {
            e.printStackTrace();
            log.warning("上报错误"+log.toString());
        }
    }
}
