package com.superman.superman.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.superman.superman.dto.UserCreateReq;
import com.superman.superman.model.User;
import com.superman.superman.model.Userinfo;

import java.util.Map;
import java.util.Optional;

/**
 * Created by snake on 2018/11/6.
 */
public interface UserService  extends IService<User> {
    /**
     * 创建用户
     * @param req
     * @return
     */
    void createUser(UserCreateReq req);

    /**用户是否存在
     * @return
     */
    Optional<User> existUserByPhone(String phone);


    /**查找用户的直属上级
     * @return
     */
    Optional<User> findSuperUserByCode(String code);

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
