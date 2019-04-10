package com.superman.superman.service;

import com.superman.superman.model.Userinfo;
import com.superman.superman.req.UserRegiser;

import java.util.Map;

/**
 * Created by liujupeng on 2018/11/6.
 */
public interface UserApiService {
    Boolean createUser(UserRegiser userinfo);

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
     * 异步创建用户的邀请码
     * @param phone
     * @return
     */
    Integer createInvCode(String phone);

    /**
     * 为用户分配平台推广位接口
     * @return
     */
    Map<String, Object> createPid();

    /**
     * 运营商升级粉丝为代理接口
     *
     * @param uid
     * @param agentId
     * @param score
     * @return
     */
    Boolean upAgent(Integer uid, Integer agentId, Integer score);
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
