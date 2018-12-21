package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.HotUserMapper;
import com.superman.superman.dao.PddDao;
import com.superman.superman.dao.UserDao;
import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.model.*;
import com.superman.superman.service.UserApiService;
import lombok.NonNull;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liujupeng on 2018/11/6.
 */
@Service("userServiceApi")
public class UserApiServiceImpl implements UserApiService {
    private String REDIS_PRIFEX = "token:";
    private Long EXPRESS_TIME = 36000l;
    @Autowired
    private PddDao pddDao;
    @Autowired
    private HotUserMapper hotUserMapper;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserinfoMapper userinfoMapper;
    @Autowired
    private RedisTemplate redisTemplate;


    private final static Logger logger = LoggerFactory.getLogger(UserApiServiceImpl.class);


    @Override
    public void query() {
        UserPdd userPdd = pddDao.selectUsers(112l);
        logger.info(userPdd.getUserPid());
        logger.info(String.valueOf(userPdd.getUserId()));

        String s = null;
    }

    @Override
    public String userLogin(User user) {
        return null;
    }

    @Override
    public Boolean createUser(Userinfo userinfo) {
        var ha = redisTemplate.opsForValue();
//        var code =ha.get("SMS:"+userinfo.getUserphone());
//        if (code==null||!user.getLoginSecret().equals(code)){
//            return false;
        String loginPwd = DigestUtils.md5DigestAsHex(userinfo.getLoginpwd().getBytes());
        userinfo.setLoginpwd(loginPwd);
        JSONObject tbPid = createTbPid();
        if (tbPid == null||tbPid.size()==0) {
            return false;
        }
        userinfo.setTbpid(tbPid.getLong("tb"));
        userinfo.setPddpid(tbPid.getString("pdd"));
        int flag = userinfoMapper.insert(userinfo);
        return flag == 0 ? false : true;
    }

    @Override
    public Boolean createUserByPhone(Userinfo userinfo) {
        Userinfo info = queryUserByPhone(userinfo.getUserphone());
        if (info != null) {
            return false;
        }
        userinfo.setRoleId(3);
        userinfo.setScore(0);
        Boolean oprear = createUser(userinfo);
        if (oprear) {
            return true;
        }
        return false;
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
    public Userinfo queryUserByPhone(@NonNull String userPhone) {
        Userinfo info = userinfoMapper.selectByPhone(userPhone);
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

    @Override
    public Userinfo queryByUid(@NonNull Long uid) {
        var userinfo = userinfoMapper.selectByPrimaryKey(uid);
        return userinfo;
    }

    @Override
    public Integer createInvCode(Long uid) {
        try {
            Integer integer = userinfoMapper.insertCode(uid);
            if (integer == null) {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
        Integer id = userinfoMapper.queryCodeId(uid);
        return id;
    }

    /**
     * 从pid池里取出pid
     * @return
     */
    @Override
    public synchronized JSONObject createTbPid() {
        Long tbpid = hotUserMapper.createTbPid();
        String pddpid = hotUserMapper.createPddPid();
        if (tbpid == null||pddpid==null) {
            return null;
        }
        hotUserMapper.deleteTbPid(tbpid);
        hotUserMapper.deletePddPid(pddpid);
        JSONObject temp=new JSONObject();
        temp.put("tb",tbpid);
        temp.put("pdd",pddpid);
        return temp;
    }

}
