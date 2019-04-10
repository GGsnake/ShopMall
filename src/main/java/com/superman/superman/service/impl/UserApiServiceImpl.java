package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.*;
import com.superman.superman.manager.ConfigQueryManager;
import com.superman.superman.model.*;
import com.superman.superman.req.UserRegiser;
import com.superman.superman.service.UserApiService;
import com.superman.superman.utils.Constants;
import com.superman.superman.utils.EveryUtils;
import com.superman.superman.utils.net.NetUtils;
import lombok.NonNull;
import lombok.extern.java.Log;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
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

    @Autowired
    private ConfigQueryManager configQueryManager;
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
        String pdd = data.getString("pdd");
        String jd = data.getString("jd");
        if (data == null || pdd == null || jd == null) {
            log.warning("警告PId不足" + EveryUtils.getNowday());
            throw new RuntimeException("新增用户失败原因 PID不足");
        }
        regiser.setPddpid(pdd);
        regiser.setJdpid(jd);
        int flag = userinfoMapper.insert(regiser);
        if (flag == 0) {
            throw new RuntimeException("新增用户失败");

        }
        createInvCode(regiser.getUserphone());
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
        String code = String.valueOf(map.get("code"));

        UserRegiser userinfo = new UserRegiser();
        userinfo.setUserphone(userPhone);
        userinfo.setRoleId(3);
        //创建用户(未关联微信)
        Boolean flag = createUser(userinfo);
        if (flag) {
            //获取用户Id
            Userinfo data = userinfoMapper.selectByPhone(userPhone);
            //根据邀请码查询代理的Id
            Integer agentId = userinfoMapper.queryUserCode(Long.valueOf(code));
            Agent agent = new Agent();
            agent.setUserId(data.getId().intValue());
            agent.setAgentId(agentId);
            //关联邀请关系
            int insert = agentDao.insert(agent);
            if (insert == 0) {
                throw new RuntimeException("关联用户失败");
            }
            return true;
        }
        throw new RuntimeException("创建新用户失败");
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
        JSONObject temp = new JSONObject();
//        Long tbpid = hotUserMapper.createTbPid();
        String pddpid = hotUserMapper.createPddPid();
        String jdPid = hotUserMapper.createJdPid();
        if (pddpid == null || jdPid == null) {
            throw new RuntimeException("pid不足");
        }
//        Integer deleteTbPid = hotUserMapper.deleteTbPid(tbpid);
        Integer deleteJdPid = hotUserMapper.deleteJdPid(jdPid);
        Integer deletePddPid = hotUserMapper.deletePddPid(pddpid);
        if (deleteJdPid == 0 || deletePddPid == 0) {
            throw new RuntimeException("pid删除失败");
        }
//        temp.put("tb", tbpid);
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
        if (temp == null || temp.size() == 0 || temp.get(0).getAgentId() != agentId) {
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
        return url+reqUrl;
    }

    @Override
    public String relationBak(Userinfo userinfo) {
        String apkey = configQueryManager.queryForKey("MiaoAppKey");
        //渠道邀请码
        String invitercode = configQueryManager.queryForKey("INVITERCODE");
        String encoderString = EveryUtils.getURLEncoderString("http://www.quanhuangmaoyi.com:8080/tbAuth");
        Map<String, String> props = new HashMap<String, String>();
        props.put("apkey", apkey);
        props.put("infotype", "1");
        props.put("invitercode", invitercode);
        props.put("custompar",userinfo.getId().toString());
        props.put("return_url",encoderString);
        props.put("oauth_style","wap");
        String reqUrl = NetUtils.convertUrlParam(props);
        return Constants.MIAO_BAK_URL +reqUrl;

    }

}
