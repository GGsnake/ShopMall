package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.*;
import com.superman.superman.event.CreateUserEvent;
import com.superman.superman.manager.ConfigQueryManager;
import com.superman.superman.model.*;
import com.superman.superman.req.UserRegiser;
import com.superman.superman.service.UserService;
import com.superman.superman.utils.Constants;
import com.superman.superman.utils.EveryUtils;
import com.superman.superman.utils.net.NetUtils;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by liujupeng on 2018/11/6.
 */
@Log
@Service("userServiceApi")
public class UserServiceImpl implements UserService, ApplicationEventPublisherAware {
    @Autowired
    private AgentDao agentDao;
    @Autowired
    private HotUserMapper hotUserMapper;
    @Autowired
    private UserinfoMapper userinfoMapper;

    @Autowired
    private ConfigQueryManager configQueryManager;

    private ApplicationEventPublisher eventPublisher;

    @Override
    public Userinfo queryUserByPhone(String userPhone) {
        Objects.requireNonNull(userPhone);
        Optional<Userinfo> info = userinfoMapper.selectByPhone(userPhone);
        return info.get();
    }

    @Override
    public Userinfo queryByUid(@NonNull Long uid) {
        Userinfo userinfo = userinfoMapper.selectByPrimaryKey(uid);
        return userinfo;
    }

    /**
     * 创建新用户
     *
     * @param regiser
     * @return
     */
    @Override
    @Transactional
    public Boolean createUser(UserRegiser regiser) {
        Objects.requireNonNull(regiser);
        Integer present = userinfoMapper.insert(regiser).get();
        if (present == 0)
            new RuntimeException("新增用户失败");
        eventPublisher.publishEvent(new CreateUserEvent(this, regiser));
        return true;
    }

    /**
     * 邀请用户注册
     *
     * @param map
     * @return
     */
    @Override
    @Transactional
    public Boolean invitation(Map<String, Object> map) {
        String userPhone = String.valueOf(map.get("userPhone"));
        String agentId = String.valueOf(map.get("agentId"));
        UserRegiser userinfo = new UserRegiser();
        userinfo.setUserphone(userPhone);
        userinfo.setRoleId(3);
        //创建用户(未关联微信)
        if (createUser(userinfo)) {
            //获取用户Id
            userinfoMapper.selectByPhone(userPhone).ifPresent(usr -> {
                usr.setPid(Integer.valueOf(agentId));
                Integer update = Optional.ofNullable(userinfoMapper.updatePid(usr)).get();
                if (update == 0) {
                    throw new RuntimeException("关联用户失败");
                }
                Agent agent = new Agent();
                agent.setUserId(usr.getId().intValue());
                agent.setAgentId(Integer.valueOf(agentId));
                //关联邀请关系
                Optional<Integer> insert = agentDao.insert(agent);
                if (insert.get() == 0) {
                    throw new RuntimeException("关联用户失败");
                }
            });
            return true;

        }
        throw new RuntimeException("创建新用户失败");
    }

    @Override
    public Userinfo queryByWx(@NonNull String wx) {
        Userinfo userinfo = userinfoMapper.queryUserWxOpenId(wx);
        return userinfo;
    }

    @Override
    public Userinfo queryUserInfo(Userinfo userinfo) {
        Objects.requireNonNull(userinfo);
        return userinfoMapper.queryUserInfoSingle(userinfo).get();
    }

    @Override
    public void queryUserTree(Userinfo userinfo, StringBuilder tree) {
        Optional<Userinfo> userinfo1 = userinfoMapper.queryUserInfoSingle(userinfo);
        tree.insert(0, userinfo1.get().getId() + ",");
        if (userinfo1.get().getPid() == null) {
            return;
        }
        Userinfo chid = new Userinfo();
        chid.setId(userinfo1.get().getPid().longValue());
        queryUserTree(chid, tree);
    }


    /**
     * 从pid池里取出pid
     *
     * @return
     */
    @Override
    @Transactional
    public synchronized Map<String, Object> createPid() {
        JSONObject temp = new JSONObject();
        String pddpid = hotUserMapper.createPddPid();
        String jdPid = hotUserMapper.createJdPid();
        if (pddpid == null || jdPid == null) {
            throw new RuntimeException("pid不足");
        }
        Integer deleteJdPid = hotUserMapper.deleteJdPid(jdPid);
        Integer deletePddPid = hotUserMapper.deletePddPid(pddpid);
        if (deleteJdPid == 0 || deletePddPid == 0) {
            throw new RuntimeException("pid删除失败");
        }
        temp.put("pdd", pddpid);
        temp.put("jd", jdPid);
        return temp;
    }


    @Override
    public String taobaoOAuth(Userinfo userinfo) {
        String taobaoappkey = configQueryManager.queryForKey("TAOBAOAPPKEY");
        String url = "https://oauth.taobao.com/authorize?";
        Map<String, String> props = new HashMap<String, String>();
        props.put("response_type", "code");
        props.put("client_id", taobaoappkey);
        props.put("redirect_uri", "http://www.quanhuangmaoyi.com:8080/other/tbAuth");
        props.put("view", "wap");
        props.put("state", userinfo.getId().toString());
        String reqUrl = NetUtils.convertUrlParam(props);
        return url + reqUrl;
    }

    @Override
    public String relationBak(Userinfo userinfo) {
        String apkey = configQueryManager.queryForKey("MiaoAppKey");
        //渠道邀请码
        String invitercode = configQueryManager.queryForKey("INVITERCODE");
        String encoderString = EveryUtils.getURLEncoderString("http://xxxxxxxxxxxxxx/tbAuth");
        Map<String, String> props = new HashMap<String, String>();
        props.put("apkey", apkey);
        props.put("infotype", "1");
        props.put("invitercode", invitercode);
        props.put("custompar", userinfo.getId().toString());
        props.put("return_url", encoderString);
        props.put("oauth_style", "wap");
        String reqUrl = NetUtils.convertUrlParam(props);
        return Constants.MIAO_BAK_URL + reqUrl;

    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }


    @Async
    @EventListener
    public void handleCreateUserEvent(CreateUserEvent event) {
        String userphone = event.getRegiser().getUserphone();
        Objects.requireNonNull(userphone);
        userinfoMapper.selectByPhone(userphone).ifPresent(tx -> {
            userinfoMapper.insertCode(tx.getId());
            return;
        });
        log.warning("用户创建邀请码表 失败 手机号===" + userphone);
//        JSONObject data = (JSONObject) createPid();
//        String pdd = data.getString("pdd");
//        String jd = data.getString("jd");
//        if (data == null || pdd == null || jd == null) {
//            log.warning("警告PId不足" + EveryUtils.getNowday());
//            throw new RuntimeException("新增用户失败原因 PID不足");
//        }
//        regiser.setPddpid(pdd);
//        regiser.setJdpid(jd);
    }
}
