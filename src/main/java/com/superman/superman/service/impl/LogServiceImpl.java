package com.superman.superman.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.superman.superman.dao.LogDao;
import com.superman.superman.model.UserLog;
import com.superman.superman.model.enums.LogOperationEnum;
import com.superman.superman.service.LogService;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created by snake on 2018/11/24.
 */
@Log
@Service("logService")
public class LogServiceImpl  extends ServiceImpl<LogDao,UserLog> implements LogService,IService<UserLog>  {
    /**
     * 异步上报用户登录
     *
     * @param uid
     * @param ip
     */
    @Async
    @Override
    public void addUserLoginLog(Long uid, String ip) {
        UserLog log = new UserLog();
        log.setUserId(uid);
        log.setOperationEnum(LogOperationEnum.USER_LOGIN);
        log.setIp(ip);
        save(log);
    }
}
