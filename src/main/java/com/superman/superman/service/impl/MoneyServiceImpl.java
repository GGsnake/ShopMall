package com.superman.superman.service.impl;

import com.superman.superman.dao.AgentDao;
import com.superman.superman.model.Userinfo;
import com.superman.superman.service.MoneyService;
import com.superman.superman.service.OderService;
import com.superman.superman.utils.EveryUtils;
import lombok.NonNull;
import lombok.extern.java.Log;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by liujupeng on 2018/12/13.
 */
@Log
@Service("moneyService")
public class MoneyServiceImpl implements MoneyService {
    @Autowired
    private AgentDao agentDao;
    @Autowired
    private OderService oderService;


    //获取已结算待结算
    public Long queryCashMoney(@NonNull Long uid, @NonNull Integer status, Userinfo user) {
        if (user == null) {
            return null;
        }
        var roleId = user.getRoleId();
        HashSet<Long> uidSet = new HashSet<>();
        uidSet.add(uid);
        switch (roleId) {
            case 1:
                //查询代理或者直属粉丝
                List<Userinfo> userInfosList = agentDao.superQueryFansUserInfo(uid.intValue());
                //代理用户信息列表
                ArrayList<Userinfo> agentIdList = new ArrayList<>(20);
                for (Userinfo useId : userInfosList) {
                    if (useId.getRoleId() == 2) {
                        agentIdList.add(useId);
                        continue;
                    }
                    uidSet.add(useId.getId());
                }
                Long allMoney = oderService.superQueryOderForUidList(EveryUtils.setToList(uidSet), status);

                Long agentMoneyTemp = 0l;
                for (Userinfo userio : agentIdList) {
                    HashSet<Long> agentUidSet = new HashSet<>();
                    Long agentId = userio.getId();
                    agentUidSet.add(agentId);
                    //根据每个代理的不同佣金比率计算我的收入
                    Long myScore = 100l - userio.getScore();
                    //计算拼多多收入

                    List<Long> agentFansIdList = agentDao.queryForAgentIdNew(agentId.intValue());
                    if (agentFansIdList != null || agentFansIdList.size() != 0) {
                        agentUidSet.addAll(agentFansIdList);
                    }
                    Long temp = oderService.superQueryOderForUidList(EveryUtils.setToList(agentUidSet), status);
                    agentMoneyTemp += temp == 0 ? 0l : temp * myScore / 100;
                }
                return allMoney + agentMoneyTemp;

            //代理
            case 2:
                Long meIncome = 0l;
                //查询我的订单收入
                Long agentScore = Long.valueOf(user.getScore());
                //查询我的下级粉丝
                List<Long> agentFansIdList = agentDao.queryForAgentIdNew(uid.intValue());
                if (agentFansIdList != null || agentFansIdList.size() != 0) {
                    uidSet.addAll(agentFansIdList);
                }
                Long MyMoney = oderService.superQueryOderForUidList(EveryUtils.setToList(uidSet), status);
                meIncome = MyMoney == 0 ? 0l : MyMoney * agentScore / 100;
                return meIncome;
            //粉丝
            case 3:
                return 0l;
            default:
                log.warning("switch穿透" + System.currentTimeMillis());
                break;

        }
        return null;
    }


}
