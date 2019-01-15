package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.*;
import com.superman.superman.model.*;
import com.superman.superman.req.UserRegiser;
import com.superman.superman.service.UserApiService;
import lombok.NonNull;
import lombok.extern.java.Log;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

/**
 * Created by liujupeng on 2018/11/6.
 */
@Log
@Service("userServiceApi")
public class UserApiServiceImpl implements UserApiService {
    private String REDIS_PRIFEX = "token:";
    private Long EXPRESS_TIME = 36000l;
    @Autowired
    private AgentDao agentDao;
    @Autowired
    private HotUserMapper hotUserMapper;

    @Autowired
    private UserinfoMapper userinfoMapper;


    @Override
    public void query() {


    }


    /**
     * 创建新用户
     * @param usr
     * @return
     */
    @Override
    public Boolean createUser(UserRegiser usr) {
        JSONObject data = createPid();
        if (data == null || data.size() == 0) {
            return false;
        }
        usr.setTbpid(data.getLong("tb"));
        usr.setPddpid(data.getString("pdd"));
        int flag = userinfoMapper.insert(usr);
        createInvCode(usr.getUserphone());
        return flag == 0 ? false : true;
    }

    @Override
    public Boolean createUserByPhone(UserRegiser userinfo) {
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


    @Override
    public Userinfo queryUserByPhone(@NonNull String userPhone) {
        Userinfo info = userinfoMapper.selectByPhone(userPhone);
        return info;
    }


    @Override
    public Userinfo queryByUid(@NonNull Long uid) {
        var userinfo = userinfoMapper.selectByPrimaryKey(uid);
        return userinfo;
    }

    @Override
    public Userinfo queryByWx(@NonNull String wx) {
        Userinfo userinfo = userinfoMapper.queryUserWxOpenId(wx);
        return userinfo;
    }

    /**
     * 异步创建邀请码
     * @param phone
     * @return
     */
    @Async
    @Override
    public Integer createInvCode(String phone) {
        try {
            Userinfo userinfo = userinfoMapper.selectByPhone(phone);
            if (userinfo==null){
                log.warning("用户不存在 所以创建邀请码失败 手机号===" + phone);
                return 0;
            }

            Integer flag = userinfoMapper.insertCode(userinfo.getId());
            if (flag == null) {
                log.warning("用户创建邀请码表 失败 手机号==="+phone);
                return 0;
            }
        } catch (Exception e) {
            log.warning("用户创建邀请码失败 手机号==="+phone+"异常信息"+e.getMessage());
            return 0;
        }
        return 1;
    }

    /**
     * 从pid池里取出pid
     *
     * @return
     */
    @Override
    public synchronized JSONObject createPid() {
        Long tbpid = hotUserMapper.createTbPid();
        String pddpid = hotUserMapper.createPddPid();
        if (tbpid == null || pddpid == null) {
            return null;
        }
        hotUserMapper.deleteTbPid(tbpid);
        hotUserMapper.deletePddPid(pddpid);
        JSONObject temp = new JSONObject();
        temp.put("tb", tbpid);
        temp.put("pdd", pddpid);
        return temp;
    }

    /**
     * 粉丝升级到代理
     * @param uid
     * @param agentId
     * @param score
     * @return
     */
    @Override
    @Transactional
    public Boolean upAgent(Integer uid, Integer agentId, Integer score) {
        Userinfo godUser = userinfoMapper.selectByPrimaryKey(Long.valueOf(agentId));
        if (godUser.getRoleId() != 1) {
            return false;
        }
        Userinfo agent = userinfoMapper.selectByPrimaryKey(Long.valueOf(uid));
        if (agent == null || agent.getRoleId() != 3) {
            return false;
        }
        Agent temp = agentDao.queryForUserIdSimple(uid);
        if (temp == null || temp.getAgentId() != agentId) {
            return false;
        }

        try {
            Integer flag = agentDao.upAgent(score, uid);
            Integer flag1 = agentDao.upAgentTime(uid);
            if (flag == 1 && flag1 == 1) {
                return true;
            }
        } catch (Exception e) {
            log.warning("代理" + uid + "升级的时候没有更新时间");
            throw new RuntimeException("升级代理失败");
        }
        log.warning("代理" + uid + "升级的时候没有更新时间");
        throw new RuntimeException("升级代理失败");

    }

}
