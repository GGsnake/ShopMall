package com.superman.superman.service;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.model.User;
import com.superman.superman.model.Userinfo;
import com.superman.superman.req.UserRegiser;

/**
 * Created by liujupeng on 2018/11/6.
 */
public interface UserApiService {
    void query();
    Boolean createUser(UserRegiser userinfo);

    Boolean createUserByPhone(UserRegiser userinfo);

    Userinfo queryUserByPhone(String userPhone);

    Userinfo queryByUid(Long uid);


    Userinfo queryByWx(String  uid);

    Integer createInvCode(String phone);

    JSONObject createPid();

    Boolean upAgent(Integer uid, Integer agentId, Integer score);
}
