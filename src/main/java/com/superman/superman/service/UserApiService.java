package com.superman.superman.service;

import com.superman.superman.model.Role;
import com.superman.superman.model.Test;
import com.superman.superman.model.User;
import com.superman.superman.model.Userinfo;

/**
 * Created by liujupeng on 2018/11/6.
 */
public interface UserApiService {
    void query();
    String userLogin(User user);
    Boolean createUser(Userinfo userinfo);
     Userinfo queryUserByPhone(String userPhone);
    Test queryT(Test user);
    Role queryR(Role user);
    Userinfo  queryByUid(Long uid);
}
