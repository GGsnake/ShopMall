package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.AgentDao;
import com.superman.superman.dao.OderMapper;
import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.dto.MemberDetail;
import com.superman.superman.model.Agent;
import com.superman.superman.model.Userinfo;
import com.superman.superman.service.MoneyService;
import com.superman.superman.service.OderService;
import com.superman.superman.utils.EveryUtils;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by snake on 2018/12/13.
 */
@Log
@Service("moneyService")
public class MoneyServiceImpl implements MoneyService {
    @Autowired
    private AgentDao agentDao;
    @Autowired
    private OderService oderService;
    @Value("${juanhuang.range}")
    private Integer range;

    //获取已结算待结算
    public Long queryCashMoney(@NonNull Integer status, Userinfo user) {
        if (user == null) {
            return null;
        }
        Long uid = user.getId();
        Integer roleId = user.getRoleId();
        HashSet<Long> uidSet = new HashSet<>();
        uidSet.add(uid);
        switch (roleId) {
            case 1:
                //查询代理或者直属粉丝
                List<Userinfo> userInfosList = agentDao.superQueryFansUserInfo(uid.intValue());
                //代理用户信息列表
                ArrayList<Userinfo> agentIdList = new ArrayList<>(20);
                for (Userinfo useId : userInfosList) {
                    if (useId == null) {
                        continue;
                    }
                    if (useId.getRoleId() == 2) {
                        agentIdList.add(useId);
                        continue;
                    }
                    uidSet.add(useId.getId());
                }
                Long allMoney = 0l;
                allMoney = oderService.superQueryOderForUidList(EveryUtils.setToList(uidSet), status);
                if (allMoney != 0) {
                    allMoney = allMoney * range / 100;
                }

                Long agentMoneyTemp = 0l;
                for (Userinfo userio : agentIdList) {
                    HashSet<Long> agentUidSet = new HashSet<>();
                    agentUidSet.add(userio.getId());
                    //根据每个代理的不同佣金比率计算我的收入
                    Long myScore = 100l - userio.getScore();
                    //先查询自己的订单预估收入
                    Long isMyMoney = oderService.superQueryOderForUidList(EveryUtils.setToList(agentUidSet),status);
                    if (isMyMoney != 0) {
                        //代理的收入经过平台扣费后再根据代理的佣金比率进行计算
                        isMyMoney = isMyMoney * range / 100 * myScore / 100;
                    }
                    agentMoneyTemp += isMyMoney;
                    //查询自己的粉丝下级
                    List<Long> fansIdList = agentDao.queryForAgentIdNew(userio.getId().intValue());
                    agentUidSet.addAll(fansIdList);
                    if (fansIdList == null || fansIdList.size() == 0) {
                        continue;
                    }
                    agentUidSet.clear();
                    agentUidSet.addAll(fansIdList);
                    //代理的粉丝贡献的预估收入
                    Long fansMoney =  oderService.superQueryOderForUidList(EveryUtils.setToList(agentUidSet),status);
                    if (fansMoney != 0) {
                        fansMoney = fansMoney * range / 100 * myScore / 100;
                        agentMoneyTemp += fansMoney;
                    }
                }
                return allMoney + agentMoneyTemp;

            //代理
            case 2:
                // 佣金比率
                int score = user.getScore();
                //先查询自己的订单预估收入
                Long isMyMoney = oderService.superQueryOderForUidList(EveryUtils.setToList(uidSet), status);
                if (isMyMoney != 0) {
                    //代理的收入经过平台扣费后再根据代理的佣金比率进行计算
                    isMyMoney = isMyMoney * range / 100 * score / 100;
                }
                //查询自己的粉丝下级
                List<Long> fansIdList = agentDao.queryForAgentIdNew(uid.intValue());
                if (fansIdList != null || fansIdList.size() != 0) {
                    uidSet.clear();
                    uidSet.addAll(fansIdList);
                }
                Long fansMoney = oderService.superQueryOderForUidList(EveryUtils.setToList(uidSet), status);
                if (fansMoney != 0) {
                    fansMoney = fansMoney * range / 100 * score / 100;
                }
                return fansMoney + isMyMoney;
            //粉丝
            case 3:
                return 0l;
            default:
                log.warning("switch穿透" + System.currentTimeMillis());
                break;

        }
        return 0l;
    }

    /**
     * 查询用户的预估收入
     *
     * @param user
     * @return
     */
    @Override
    public Long queryCashMoney(@NonNull Userinfo user) {
        Long uid = user.getId();
        int roleId = user.getRoleId();
        HashSet<Long> uidSet = new HashSet<>();
        Long bossFansMoney = 0l;
        switch (roleId) {
            case 1:
                //查询代理或者直属粉丝
                List<Userinfo> userInfosList = agentDao.superQueryFansUserInfo(uid.intValue());
                //代理用户信息列表
                ArrayList<Userinfo> agentIdList = new ArrayList<>(20);
                for (Userinfo useId : userInfosList) {
                    if (useId == null) {
                        continue;
                    }
                    if (useId.getRoleId() == 2) {
                        agentIdList.add(useId);
                        continue;
                    }
                    //将直属粉丝先添加至查询预估收入
                    uidSet.add(useId.getId());
                }
                if (uidSet.size() != 0) {
                    bossFansMoney = oderService.superQueryOderForUidListToEstimate(EveryUtils.setToList(uidSet));
                    if (bossFansMoney != 0) {
                        bossFansMoney = bossFansMoney * range / 100;
                    }

                }
                //统计代理的收入
                Long agentMoneyTemp = 0l;
                for (Userinfo userio : agentIdList) {
                    HashSet<Long> agentUidSet = new HashSet<>();
                    agentUidSet.add(userio.getId());
                    //根据每个代理的不同佣金比率计算我的收入
                    Long myScore = 100l - userio.getScore();
                    //先查询自己的订单预估收入
                    Long isMyMoney = oderService.superQueryOderForUidListToEstimate(EveryUtils.setToList(agentUidSet));
                    if (isMyMoney != 0) {
                        //代理的收入经过平台扣费后再根据代理的佣金比率进行计算
                        isMyMoney = isMyMoney * range / 100 * myScore / 100;
                    }
                    agentMoneyTemp += isMyMoney;

                    //查询自己的粉丝下级
                    List<Long> fansIdList = agentDao.queryForAgentIdNew(userio.getId().intValue());
                    agentUidSet.addAll(fansIdList);
                    if (fansIdList == null || fansIdList.size() == 0) {
                        continue;
                    }

                    agentUidSet.clear();
                    agentUidSet.addAll(fansIdList);

                    //代理的粉丝贡献的预估收入
                    Long fansMoney = oderService.superQueryOderForUidListToEstimate(EveryUtils.setToList(agentUidSet));
                    if (fansMoney != 0) {
                        fansMoney = fansMoney * range / 100 * myScore / 100;
                        agentMoneyTemp += fansMoney;
                    }
                }
                return bossFansMoney + agentMoneyTemp;
            default:
                log.warning("switch穿透" + System.currentTimeMillis());
                break;

        }
        return 0l;
    }


}
