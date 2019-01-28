package com.superman.superman.service;

import com.superman.superman.model.Userinfo;
import com.superman.superman.req.UserRegiser;

import java.util.Map;

/**
 * Created by liujupeng on 2018/11/6.
 */
public interface UserApiService {
    Boolean createUser(UserRegiser userinfo);

    Integer createUserByPhone(UserRegiser userinfo);

    Userinfo queryUserByPhone(String userPhone);

    Userinfo queryByUid(Long uid);


    Userinfo queryByWx(String  uid);

    Integer createInvCode(String phone);

    Map<String, Object> createPid();

    Boolean upAgent(Integer uid, Integer agentId, Integer score);
}
