package com.superman.superman.service.impl;

import com.superman.superman.dao.PddDao;
import com.superman.superman.dao.UserDao;
import com.superman.superman.model.Role;
import com.superman.superman.model.Test;
import com.superman.superman.model.User;
import com.superman.superman.model.UserPdd;
import com.superman.superman.service.UserApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liujupeng on 2018/11/6.
 */
@Service("userServiceApi")
public class UserApiServiceImpl implements UserApiService {
    private String REDIS_PRIFEX="token:";
    private Long EXPRESS_TIME=36000l;
    @Autowired
    private PddDao pddDao;
    @Autowired
    private UserDao userDao;

    private final static Logger logger = LoggerFactory.getLogger(UserApiServiceImpl.class);


    @Override
    public void query() {
        UserPdd userPdd = pddDao.selectUsers(112l);
        logger.info(userPdd.getUserPid());
        logger.info(String.valueOf(userPdd.getUserId()));

        String s=null;
    }

    @Override
    public String userLogin(User user) {
        return null;
    }

    @Override
    public Boolean createUser(User user) {
        return null;
    }

//    public String userLogin(User user) {
//        if (user==null||user.getUserPhone()==null||user.getLoginPwd()==null){
//            return "请填写";
//        }
//        User loginUser =  userDao.selectByPhone(user.getUserPhone());
//        if (loginUser==null){
//            return "用户不存在";
//        }
//        if (!DigestUtils.md5DigestAsHex(user.getLoginPwd().getBytes()).equals(loginUser.getLoginPwd())){
//            return null;
//        }
//        String token= String.valueOf(UUID.randomUUID());
//        loginUser.setLoginPwd("");
//        redisUtils.set(REDIS_PRIFEX+token, JSONObject.toJSON(loginUser),EXPRESS_TIME);
//
//        return token;
//    }
//
//    @Override
//    public Boolean createUser(User user) {
//        if (user.getUserPhone()==null||user.getLoginSecret()==null||user.getLoginPwd()==null){
//            return false;
//        }
//        String code = redisUtils.get("SMS:"+user.getUserPhone());
//        if (code==null||!user.getLoginSecret().equals(code)){
//            return false;
//        }
//        if (user.getPayPwd()!=null){
//            //TODO 插入邀请码
//        }
//        String loginPwd= DigestUtils.md5DigestAsHex(user.getLoginPwd().getBytes());
//        user.setLoginPwd(loginPwd);
//        Integer loginUser =  userDao.createUser(user);
//        if (loginUser==0){
//            return false;
//        }
//
//        return true;
//    }

    @Override
    public User queryUserByPhone(User user) {
        if (user.getUserPhone()==null){
            return null;
        }
        User info= userDao.selectByPhone(user.getUserPhone());
        if (info==null){
            return null;
        }
        return info;
    }

    @Override
    public Test queryT(Test user) {
        return pddDao.selectT(Long.valueOf(user.getUserId()));
    }

    @Override
    public Role queryR(Role user) {
        return pddDao.selectR(Long.valueOf(user.getUserId()));
    }

}
