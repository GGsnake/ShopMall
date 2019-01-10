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

    String userLogin(User user);

    Boolean createUser(UserRegiser userinfo);

    Boolean createUserByPhone(Userinfo userinfo);

    Userinfo queryUserByPhone(String userPhone);

    Userinfo queryByUid(Long uid);


    Userinfo queryByWx(String  uid);

    Integer createInvCode(Long uid);

    JSONObject createTbPid();

    Boolean upAgent(Integer uid, Integer agentId, Integer score);
}
