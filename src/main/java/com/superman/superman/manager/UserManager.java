package com.superman.superman.manager;

import com.superman.superman.service.impl.UserApiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户缓存层
 */
@Service("userManager")
public class UserManager {
    @Autowired
    private UserApiServiceImpl userApiService;




}
