package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.*;
import com.superman.superman.model.*;
import com.superman.superman.req.UserRegiser;
import com.superman.superman.service.UserApiService;
import lombok.NonNull;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

/**
 * Created by liujupeng on 2018/11/6.
 */
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
    @Autowired
    private RedisTemplate redisTemplate;


    private final static Logger logger = LoggerFactory.getLogger(UserApiServiceImpl.class);


    @Override
    public void query() {


    }

    @Override
    public String userLogin(User user) {
        return null;
    }

    @Override
    public Boolean createUser(UserRegiser usr) {
        ValueOperations var = redisTemplate.opsForValue();
//        String code = (String) var.get("SMS:" + usr.getUserphone());
//        if (code == null || !code.equals(usr.getCode())) {
//            return false;
//        }
        JSONObject data = createPid();
        if (data == null || data.size() == 0) {
            return false;
        }
        usr.setTbpid(data.getLong("tb"));
        usr.setPddpid(data.getString("pdd"));
        int flag = userinfoMapper.insert(usr);
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
            logger.warn("代理" + uid + "升级的时候没有更新时间");
            throw new RuntimeException("升级代理失败");
        }
        logger.warn("代理" + uid + "升级的时候没有更新时间");
        throw new RuntimeException("升级代理失败");

    }

}
