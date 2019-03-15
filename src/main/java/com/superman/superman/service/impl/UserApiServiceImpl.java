package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.*;
import com.superman.superman.model.*;
import com.superman.superman.req.UserRegiser;
import com.superman.superman.service.UserApiService;
import com.superman.superman.utils.EveryUtils;
import lombok.NonNull;
import lombok.extern.java.Log;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by liujupeng on 2018/11/6.
 */
@Log
@Service("userServiceApi")
public class UserApiServiceImpl implements UserApiService {
    @Autowired
    private AgentDao agentDao;
    @Autowired
    private HotUserMapper hotUserMapper;
    @Autowired
    private UserinfoMapper userinfoMapper;
    /**
     * 创建新用户
     *
     * @param regiser
     * @return
     */
    @Override
    @Transactional
    public Boolean createUser(UserRegiser regiser) {
        JSONObject data = (JSONObject) createPid();
        Long tb = data.getLong("tb");
        String pdd = data.getString("pdd");
        String jd = data.getString("jd");
        if (data == null || tb == null|| pdd == null || jd == null) {
            log.warning("警告PId不足" + EveryUtils.getNowday());
            throw  new RuntimeException("新增用户失败原因 PID不足");
        }
        regiser.setTbpid(tb);
        regiser.setPddpid(pdd);
        regiser.setJdpid(jd);
        int flag = userinfoMapper.insert(regiser);
        if (flag==0){
            throw  new RuntimeException("新增用户失败");
        }
        createInvCode(regiser.getUserphone());
        return true;
    }

    @Override
    public Integer createUserByPhone(UserRegiser regiser) {
        String userphone = regiser.getUserphone();
        if ( userphone== null) {
            return 1;
        }
        Userinfo info = queryUserByPhone(userphone);
        //查询手机号是否注册过
        if (info != null) {
            return 2;
        }
        regiser.setRoleId(3);
        regiser.setScore(0);
        Boolean oprear = createUser(regiser);
        if (oprear) {
            return 0;
        }
        return 4;
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
     *
     * @param phone
     * @return
     */
    @Async
    @Override
    public Integer createInvCode(String phone) {
        try {
            Userinfo userinfo = userinfoMapper.selectByPhone(phone);
            if (userinfo == null) {
                log.warning("用户不存在 所以创建邀请码失败 手机号===" + phone);
                return 0;
            }

            Integer flag = userinfoMapper.insertCode(userinfo.getId());
            if (flag == null) {
                log.warning("用户创建邀请码表 失败 手机号===" + phone);
                return 0;
            }
        } catch (Exception e) {
            log.warning("用户创建邀请码失败 手机号===" + phone + "异常信息" + e.getMessage());
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
    @Transactional
    public synchronized Map<String, Object> createPid() {
        Long tbpid = hotUserMapper.createTbPid();
        String pddpid = hotUserMapper.createPddPid();
        String jdPid = hotUserMapper.createJdPid();
        if (tbpid == null || pddpid == null || jdPid == null) {
            throw new RuntimeException("pid不足");
        }
        Integer deleteTbPid = hotUserMapper.deleteTbPid(tbpid);
        Integer deleteJdPid = hotUserMapper.deleteJdPid(jdPid);
        Integer deletePddPid = hotUserMapper.deletePddPid(pddpid);
        if (deleteJdPid == 0 || deletePddPid == 0 || deleteTbPid == 0) {
            throw new RuntimeException("pid删除失败");
        }
        JSONObject temp = new JSONObject();
        temp.put("tb", tbpid);
        temp.put("pdd", pddpid);
        temp.put("jd", jdPid);
        return temp;
    }

    /**
     * 粉丝升级到代理
     *
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
            log.warning("该用户不是运营商,无权限升级" + uid);
            return false;
        }
        Userinfo agent = userinfoMapper.selectByPrimaryKey(Long.valueOf(uid));
        if (agent == null || agent.getRoleId() != 3) {
            log.warning("被升级的用户应该是粉丝" + uid);
            return false;
        }
        List<Agent> temp = agentDao.queryForUserId(uid);
        if (temp == null ||temp.size()==0|| temp.get(0).getAgentId() != agentId) {
            log.warning("该用户不是您的粉丝" + uid);
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
