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
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Autowired
    private UserinfoMapper userinfoMapper;

    @Autowired
    private OderMapper oderMapper;

    @Value("${juanhuang.range}")
    private Integer range;


    //获取已结算待结算
    public Long queryCashMoney(@NonNull Long uid, @NonNull Integer status, Userinfo user) {
        if (user == null) {
            return null;
        }
        var roleId = user.getRoleId();
        HashSet<Long> uidSet = new HashSet<>();
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
                Long allMoney =0l;
                if (uidSet.size()!=0){
                    allMoney=oderService.superQueryOderForUidList(EveryUtils.setToList(uidSet), status);
                }

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
        return 0l;
    }

    @Override
    public JSONObject queryMyIncome(Long uid) {
        return null;
    }
//
//    @Override
//    public JSONObject queryMyIncome(Long uid) {
//
//        JSONObject data = new JSONObject();
//        Long todayTime = EveryUtils.getToday();
//        Long todayEndTime = todayTime + 86400;
//        Long yesDayTime = todayTime - 86400;
//        Long yesDayEndTime = todayTime - 1;
//        //本月第一天
//        Long timesMonthmorning = EveryUtils.getTimesMonthmorning();
//        //本月最后一天
//        Long timesMonthmorningLast = EveryUtils.getTimesMonthmorningLast();
//        //上月第一天
//        Long lastMonthTime = EveryUtils.getTopStar();
//        //上月最后一天
//        Long lastMonthTimeEnd = EveryUtils.getEnd();
//
//        //今日贡献佣金
//        Double todayMoneyCount = 0d;
//        //昨日贡献佣金
//        Double yesDayMoneyCount = 0d;
//        //本月贡献佣金
//        Double yesMonthMoneyvarCount = 0d;
//        //上月贡献佣金
//        Double yesLastMonthMoneyCount = 0d;
//
//        //今日订单数量
//        Integer todayCount = 0;
//        //昨日订单数量
//        Integer yesDayCount = 0;
//        //本月订单数量
//        Integer yesMonthCount = 0;
//        //上月订单数量
//        Integer yesLastMonthCount = 0;
//
//
//        Userinfo userinfo = userinfoMapper.selectByPrimaryKey(uid);
//        Integer myrole = userinfo.getRoleId();
//        //判断身份
//        if (myrole== 2) {
//            String tbs = EveryUtils.timeStamp2Date(String.valueOf(todayTime), null);
//            String tbe = EveryUtils.timeStamp2Date(String.valueOf(todayEndTime), null);
//            MemberDetail var1 = oderMapper.sumAllDevOderByOderCreateTimeForMb(uid.intValue(), tbs, tbe, todayTime, todayEndTime);
//            if (myrole == 1) {
//                todayMoneyCount = (var1.getMoney() * range / 100d);
//
//            }
//            if (myrole == 2) {
//                todayMoneyCount = (var1.getMoney() * range / 100d) * (mySc / 100d);
//            }
//            todayCount = var1.getSums();
//
//            String tbs1 = EveryUtils.timeStamp2Date(String.valueOf(yesDayTime), null);
//            String tbe1 = EveryUtils.timeStamp2Date(String.valueOf(yesDayEndTime), null);
//            MemberDetail var2 = oderMapper.sumAllDevOderByOderCreateTimeForMb(userId.intValue(), tbs1, tbe1, yesDayTime, yesDayEndTime);
//            if (myrole == 1) {
//                yesDayMoneyCount = (var2.getMoney() * range / 100d);
//
//            }
//            if (myrole == 2) {
//                yesDayMoneyCount = (var2.getMoney() * range / 100d) * (mySc / 100d);
//            }
//            yesDayCount = var2.getSums();
//
//
//            String tbs2 = EveryUtils.timeStamp2Date(String.valueOf(timesMonthmorning), null);
//            String tbe2 = EveryUtils.timeStamp2Date(String.valueOf(timesMonthmorningLast), null);
//            MemberDetail var3 = oderMapper.sumAllDevOderByOderCreateTimeForMb(userId.intValue(), tbs2, tbe2, timesMonthmorning, timesMonthmorningLast);
//            if (myrole == 1) {
//                yesMonthMoneyvarCount = (var3.getMoney() * range / 100d);
//
//            }
//            if (myrole == 2) {
//                yesMonthMoneyvarCount = (var3.getMoney() * range / 100d) * (mySc / 100d);
//            }
//            yesMonthCount = var3.getSums();
//            String tbs3 = EveryUtils.timeStamp2Date(String.valueOf(lastMonthTime), null);
//            String tbe3 = EveryUtils.timeStamp2Date(String.valueOf(lastMonthTimeEnd), null);
//            MemberDetail var4 = oderMapper.sumAllDevOderByOderCreateTimeForMb(userId.intValue(), tbs3, tbe3, lastMonthTime, lastMonthTimeEnd);
//            if (myrole == 1) {
//                yesLastMonthMoneyCount = (var4.getMoney() * range / 100d);
//
//            }
//            if (myrole == 2) {
//                yesLastMonthMoneyCount = (var4.getMoney() * range / 100d) * (mySc / 100d);
//            }
//            yesLastMonthCount = var4.getSums();
//            data.put("today", todayMoneyCount.intValue());
//            data.put("todayOder", todayCount);
//            data.put("yesday", yesDayMoneyCount.intValue());
//            data.put("yesdayOder", yesDayCount);
//            data.put("yesMonday", yesMonthMoneyvarCount.intValue());
//            data.put("yesMondayOder", yesMonthCount);
//            data.put("lastMonday", yesLastMonthMoneyCount.intValue());
//            data.put("lastMondayOder", yesLastMonthCount);
//            return data;
//        }
//        //判断是代理
//        if (myrole == 1) {
//            List<Agent> agentInfo = agentDao.queryForUserId(userId.intValue());
//            if (agentInfo == null) {
//                return null;
//            }
//            Long agentId = Long.valueOf(agentInfo.get(0).getAgentId());
//            if (agentId != my.getId()) {
//                return null;
//            }
//            Integer agentSc = 100 - userinfo.getScore();
//            HashSet oderOpen = new HashSet();
//            HashSet oderOpen1 = new HashSet();
//            HashSet oderOpen2 = new HashSet();
//            HashSet oderOpen3 = new HashSet();
//            List<Long> uidlist = new ArrayList<>(10);
//            uidlist.add(userId);
//            List<Long> agents = agentDao.queryForAgentIdNew(userId.intValue());
//            if (agents != null && agents.size() != 0) {
//                uidlist.addAll(agents);
//            }
//            String tbs = EveryUtils.timeStamp2Date(String.valueOf(todayTime), null);
//            String tbe = EveryUtils.timeStamp2Date(String.valueOf(todayEndTime), null);
//            MemberDetail var1 = oderMapper.sumAllDevOderByOderCreateTimeForAgent(uidlist, tbs, tbe, todayTime, todayEndTime);
//            todayMoneyCount = (var1.getMoney() * range / 100d) * (agentSc / 100d);
//            todayCount = var1.getSums();
//
//            String tbs1 = EveryUtils.timeStamp2Date(String.valueOf(yesDayTime), null);
//            String tbe1 = EveryUtils.timeStamp2Date(String.valueOf(yesDayEndTime), null);
//            MemberDetail var2 = oderMapper.sumAllDevOderByOderCreateTimeForAgent(uidlist, tbs1, tbe1, yesDayTime, yesDayEndTime);
//            yesDayMoneyCount = (var2.getMoney() * range / 100d) * (agentSc / 100d);
//            yesDayCount = var2.getSums();
//
//            String tbs2 = EveryUtils.timeStamp2Date(String.valueOf(timesMonthmorning), null);
//            String tbe2 = EveryUtils.timeStamp2Date(String.valueOf(timesMonthmorningLast), null);
//            MemberDetail var3 = oderMapper.sumAllDevOderByOderCreateTimeForAgent(uidlist, tbs2, tbe2, timesMonthmorning, timesMonthmorningLast);
//            yesMonthMoneyvarCount = (var3.getMoney() * range / 100d) * (agentSc / 100d);
//            yesMonthCount = var3.getSums();
//
//            String tbs3 = EveryUtils.timeStamp2Date(String.valueOf(lastMonthTime), null);
//            String tbe3 = EveryUtils.timeStamp2Date(String.valueOf(lastMonthTimeEnd), null);
//            MemberDetail var4 = oderMapper.sumAllDevOderByOderCreateTimeForAgent(uidlist, tbs3, tbe3, lastMonthTime, lastMonthTimeEnd);
//            yesLastMonthMoneyCount = (var4.getMoney() * range / 100d) * (agentSc / 100d);
//            yesLastMonthCount = var4.getSums();
//            data.put("today", todayMoneyCount.intValue());
//            data.put("todayOder", todayCount);
//            data.put("todayOpen", oderOpen.size());
//            data.put("yesday", yesDayMoneyCount.intValue());
//            data.put("yesdayOder", yesDayCount);
//            data.put("yesdayOpen", oderOpen1.size());
//            data.put("yesMonday", yesMonthMoneyvarCount.intValue());
//            data.put("yesMondayOder", yesMonthCount.intValue());
//            data.put("yesMondayOpen", oderOpen2.size());
//            data.put("lastMonday", yesLastMonthMoneyCount.intValue());
//            data.put("lastMondayOder", yesLastMonthCount);
//            data.put("lastMondayOpen", oderOpen3.size());
//            data.put("agentSum", uidlist.size());
//            data.put("empMoney", userinfo.getScore());
//            data.put("agentDate", agentInfo.get(0).getUpdateTime());
//            return data;
//        }
//        return null;

}
