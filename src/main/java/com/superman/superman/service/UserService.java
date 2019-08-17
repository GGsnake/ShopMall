package com.superman.superman.service;

import com.superman.superman.model.Userinfo;
import com.superman.superman.req.UserRegiser;

import java.util.Map;

/**
 * Created by liujupeng on 2018/11/6.
 */
public interface UserService {
    /**
     * 创建用户
     * @param userinfo
     * @return
     */
    Boolean createUser(UserRegiser userinfo);
    /**
     * 创建用户
     * @param userinfo
     * @return
     */
    Userinfo queryUserByPhone(String userPhone);

    Userinfo queryByUid(Long uid);

    Boolean invitation(Map<String, Object> map);

    /**
     * 根据
     * @param wx
     * @return
     */
    Userinfo queryByWx(String wx);
    /**
     *
     * @param userinfo
     * @return
     */
    Userinfo queryUserInfo(Userinfo userinfo);
    /**
     *
     * @param userinfo
     * @return
     */
    void queryUserTree(Userinfo userinfo,StringBuilder tree);



    /**
     * 为用户分配平台推广位接口
     * @return
     */
    Map<String, Object> createPid();

    /**
     * 生成淘宝用户授权
     *
     * @return
     */
    String taobaoOAuth(Userinfo userinfo);
    /**
     * 淘宝授权回调喵有券备案渠道
     *
     * @return
     */
    String relationBak(Userinfo userinfo);
}
