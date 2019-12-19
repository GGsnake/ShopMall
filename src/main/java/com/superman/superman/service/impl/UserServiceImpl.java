package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.superman.superman.dao.*;
import com.superman.superman.dto.UserCreateReq;
import com.superman.superman.event.CreateUserEvent;
import com.superman.superman.manager.ConfigQueryManager;
import com.superman.superman.model.*;
import com.superman.superman.model.enums.UserLevel;
import com.superman.superman.service.UserService;
import com.superman.superman.utils.Constants;
import com.superman.superman.utils.EveryUtils;
import com.superman.superman.utils.ShareCodeUtils;
import com.superman.superman.utils.net.NetUtils;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by snake on 2018/11/6.
 */
@Log
@Service("userServiceApi")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService, ApplicationEventPublisherAware {
    @Autowired
    private HotUserMapper hotUserMapper;
    @Autowired
    private ConfigQueryManager configQueryManager;

    private ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void createUser(UserCreateReq req) {
        existUserByPhone(req.getPhone()).ifPresent(ex -> {
            throw new RuntimeException("手机号已被注册");
        });
        User user = new User();
        user.setMobile(req.getPhone()).setUserlevel(UserLevel.LOW);
        save(user);
        eventPublisher.publishEvent(new CreateUserEvent(this, req));
    }

    @Async
    @EventListener
    public void handleCreateUserEvent(CreateUserEvent event) {
        UserCreateReq req = event.getUser();
        User user = existUserByPhone(req.getPhone()).orElse(null);
        if (user == null) {
            log.warning("用户不存在");
            return;
        }
        String invCode = ShareCodeUtils.idToCode(user.getUserId());
        User existSuperUser = findSuperUserByCode(req.getInviteCode()).orElse(null);
        int treeLevel = 1;
        LinkedList<Long> userList=new LinkedList<>();
        userList.add(user.getUserId());
        if (existSuperUser != null) {
            changeUserTree(existSuperUser.getUserId(), userList);
            treeLevel =userList.size();
            user.setInviterUser(existSuperUser.getUserId());
            user.setSuperUser(existSuperUser.getUserId());
            String tree = StringUtils.join(userList, ",");
            user.setUserTree(tree);
        }
        user.setTreeLevel(treeLevel);
        user.setInviterCode(invCode);
        saveOrUpdate(user);
    }

    private void changeUserTree(Long userId,LinkedList<Long> userList) {
        combineUserLevel(userId, userList);
        //TODO会员其他奖励逻辑
    }


    private void combineUserLevel(Long userId, LinkedList<Long> userList) {
        User user =getById(userId);
        if (user == null)
            return;
        userList.addFirst(userId);
        combineUserLevel(user.getSuperUser(), userList);
    }

    @Override
    public Optional<User> existUserByPhone(String phone) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", phone);
        return Optional.ofNullable(getOne(queryWrapper));
    }

    @Override
    public Optional<User> findSuperUserByCode(String code) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("inviter_code", code);
        return Optional.ofNullable(getOne(queryWrapper));
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

}
