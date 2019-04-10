package com.superman.superman.service;

import com.superman.superman.model.UserLog;
import org.apache.ibatis.annotations.Insert;

/**
 * Created by liujupeng on 2018/11/24.
 */
public interface LogService {

    void addUserLoginLog(Long uid,String ip);
}
