package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dto.MemberDetail;
import com.superman.superman.dao.AgentDao;
import com.superman.superman.dao.OderMapper;
import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.model.Agent;
import com.superman.superman.model.Userinfo;
import com.superman.superman.service.MemberService;
import com.superman.superman.service.MoneyService;
import com.superman.superman.service.OderService;
import com.superman.superman.utils.Constants;
import com.superman.superman.utils.EveryUtils;
import com.superman.superman.utils.PageParam;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.*;

/**
 * Created by liujupeng on 2018/11/8.
 */
@Service("memberService")
public class MemberServiceImpl implements MemberService {
    private final static Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);
    @Autowired
    private AgentDao agentDao;
    @Autowired
    private OderService oderService;
    @Autowired
    private UserinfoMapper userinfoMapper;
    @Autowired
    private MoneyService moneyService;
    @Autowired
    private OderMapper oderMapper;

    @Value("${juanhuang.range}")
    private Integer range;

  
    @Override
    public JSONObject getMyMoney(@NonNull Long uid) {
        Userinfo user = userinfoMapper.selectByPrimaryKey(uid);
        Integer roleId = user.getRoleId();
        String userphoto = user.getUserphoto();
        String username = user.getUsername();
        //存储用户集合
        HashSet<Long> uidSet = new HashSet<>();
        uidSet.add(uid);

        JSONObject myJson = new JSONObject();
        myJson.put("roleId", roleId);
        myJson.put("image", userphoto == null ? Constants.IMG_DEFAUT : userphoto);
        myJson.put("name", username == null ? Constants.USERNAME_DEFAUT : username);
        switch (roleId) {
            case 1:
                //先统计自己的订单预估收入
                Long myMoney = oderService.superQueryOderForUidListToEstimate(EveryUtils.setToList(uidSet));
                if (myMoney != 0) {
                    myMoney = myMoney * range / 100;
                }
                //查询自己直属粉丝的订单预估收入
                Long AllMoney = moneyService.queryCashMoney(user);
                //合并直属粉丝和自己的总收入
                long tempMoney = AllMoney + myMoney;
                myJson.put("myMoney", tempMoney);
                myJson.put("myTeamMoney", AllMoney);
                //代理用户信息列表
                ArrayList<Userinfo> agentIdList = new ArrayList<>(20);
                //查询代理或者直属粉丝
                List<Userinfo> userInfosList = agentDao.superQueryFansUserInfo(uid.intValue());
                if (userInfosList != null || userInfosList.size() != 0) {
                    for (Userinfo var3 : userInfosList) {
                        if (var3 == null) {
                            continue;
                        }
                        if (var3.getRoleId() == 2) {
                            agentIdList.add(var3);
                            uidSet.add(var3.getId());
                            continue;
                        }
                        uidSet.add(var3.getId());
                    }
                }
                if (agentIdList.size() != 0) {
                    for (Userinfo userio : agentIdList) {
                        List<Long> agentFansIdList = agentDao.queryForAgentIdNew(userio.getId().intValue());
                        if (agentFansIdList != null || agentFansIdList.size() != 0) {
                            uidSet.addAll(agentFansIdList);
                        }
                    }
                }
                myJson.put("myAgentCount", agentIdList.size());
                myJson.put("myTeamCount", uidSet.size());
                return myJson;

            //代理
            case 2:
                // 佣金比率
                int score = user.getScore();
                //先查询自己的订单预估收入
                Long isMyMoney = oderService.superQueryOderForUidListToEstimate(EveryUtils.setToList(uidSet));
                if (isMyMoney != 0) {
                    //代理的收入经过平台扣费后再根据代理的佣金比率进行计算
                    isMyMoney = isMyMoney * range / 100 * score / 100;
                }
                //查询自己的粉丝下级
                List<Long> fansIdList = agentDao.queryForAgentIdNew(uid.intValue());
                uidSet.addAll(fansIdList);
                //团队人数
                int teamCount = uidSet.size();
                if (fansIdList == null || fansIdList.size() == 0) {
                    myJson.put("myMoney", isMyMoney);
                    myJson.put("myTeamMoney", 0);
                } else {
                    uidSet.clear();
                    uidSet.addAll(fansIdList);
                    //粉丝贡献的预估收入
                    Long fansMoney = oderService.superQueryOderForUidListToEstimate(EveryUtils.setToList(uidSet));
                    if (fansMoney != 0) {
                        fansMoney = fansMoney * range / 100 * score / 100;
                    }
                    myJson.put("myMoney", isMyMoney + fansMoney);
                    myJson.put("myTeamMoney", fansMoney);
                }
                myJson.put("myTeamCount", teamCount);
                myJson.put("myAgentCount", 0);
                return myJson;
            //粉丝
            case 3:
                myJson.put("myAgentCount", 0);
                myJson.put("myTeamCount", 1);
                myJson.put("myTeamMoney", 0);
                myJson.put("myMoney", 0);
                myJson.put("myTeamMoney", 0);
                return myJson;
            default:
                logger.warn("switch穿透" + System.currentTimeMillis());
                break;
        }
        return null;
    }

    
    @Override
    public JSONObject getMyTeam(Long userId, PageParam pageParam) {
        if (pageParam == null) {
            return null;
        }
        JSONObject data = new JSONObject();
        Userinfo var1 = userinfoMapper.selectByPrimaryKey(userId);
        Integer roleId = var1.getRoleId();
        Integer sum = agentDao.queryForUserIdCount(userId);
        if (roleId == 1) {
            //查询一级代理和直属粉丝
            List<Userinfo> userList = userinfoMapper.selectInUserInfoForAgentId(userId, pageParam.getStartRow(), pageParam.getPageSize());
            data.put("pageCount", sum);
            if (userList == null || userList.size() == 0) {
                data.put("pageData", new JSONArray());
                return data;
            }
            JSONArray jsonArray = new JSONArray();
            for (Userinfo us : userList) {
                String username = us.getUsername();
                username = username == null ? Constants.USERNAME_DEFAUT : username;
                String image = us.getUserphoto();
                image = image == null ? Constants.IMG_DEFAUT : image;
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("roleId", us.getRoleId());
                jsonObject.put("username", username);
                jsonObject.put("image", image);
                jsonObject.put("id", us.getId());
                jsonObject.put("chidSum", 0);
                if (us.getRoleId() == 2) {
                    Integer chidSum = agentDao.countRecommd(us.getId());
                    jsonObject.put("chidSum", chidSum);
                }
                jsonArray.add(jsonObject);
            }
            data.put("pageData", jsonArray);
            return data;
        }
        if (roleId == 2) {

            List<Userinfo> userList = userinfoMapper.selectInUserInfoForAgentId(userId, pageParam.getStartRow(), pageParam.getPageSize());
            data.put("pageCount", sum);
            if (userList == null || userList.size() == 0) {
                data.put("pageData", new JSONArray());
                return data;
            }
            JSONArray var2 = new JSONArray();
            for (Userinfo us : userList) {
                String username = us.getUsername();
                username = username == null ? Constants.USERNAME_DEFAUT : username;
                String image = us.getUserphoto();
                image = image == null ? Constants.IMG_DEFAUT : image;
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("username", username);
                jsonObject.put("image", image);
                jsonObject.put("id", us.getId());
                jsonObject.put("joinTime", us.getCreatetime());
                var2.add(jsonObject);
            }
            data.put("pageData", var2);
            return data;

        }
        return null;

    }

    @Override
    public JSONObject getMyNoFans(Long userId, PageParam pageParam) {
        JSONObject data = new JSONObject();
        //计算非直属粉丝数量
        Integer fansCount = agentDao.countNoMyFansSum(userId);
        data.put("sum", fansCount);
        if (fansCount == null || fansCount == 0) {
            data.put("data", new JSONArray());
            return data;
        }
        List<Agent> idList = agentDao.countNoMyFans(userId, pageParam.getStartRow(), pageParam.getPageSize());
        JSONArray dataArray = new JSONArray();
        for (int i = 0; i < idList.size(); i++) {
            Userinfo agent = userinfoMapper.selectByPrimaryKey(Long.valueOf(idList.get(i).getAgentId()));
            Userinfo uid = userinfoMapper.selectByPrimaryKey(Long.valueOf(idList.get(i).getUserId()));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", uid.getUsername());
            jsonObject.put("pidname", agent.getUsername());
            jsonObject.put("image", uid.getUserphoto());
            jsonObject.put("uid", uid.getId());
            dataArray.add(jsonObject);
        }
        data.put("data", dataArray);
        return data;
    }

    @Override
    public JSONObject queryMemberDetail(Long userId, Integer myId) {
        JSONObject data = new JSONObject();
        Long todayTime = EveryUtils.getToday();
        Long todayEndTime = todayTime + 86400;
        Long yesDayTime = todayTime - 86400;
        Long yesDayEndTime = todayTime - 1;
        //本月第一天
        Long timesMonthmorning = EveryUtils.getTimesMonthmorning();
        //本月最后一天
        Long timesMonthmorningLast = EveryUtils.getTimesMonthmorningLast();
        //上月第一天
        Long lastMonthTime = EveryUtils.getTopStar();
        //上月最后一天
        Long lastMonthTimeEnd = EveryUtils.getEnd();

        //今日贡献佣金
        Double todayMoneyCount = 0d;
        //昨日贡献佣金
        Double yesDayMoneyCount = 0d;
        //本月贡献佣金
        Double yesMonthMoneyvarCount = 0d;
        //上月贡献佣金
        Double yesLastMonthMoneyCount = 0d;

        //今日订单数量
        Integer todayCount = 0;
        //昨日订单数量
        Integer yesDayCount = 0;
        //本月订单数量
        Integer yesMonthCount = 0;
        //上月订单数量
        Integer yesLastMonthCount = 0;


        Userinfo userinfo = userinfoMapper.selectByPrimaryKey(userId);
        Userinfo my = userinfoMapper.selectByPrimaryKey(Long.valueOf(myId));
        Integer myrole = my.getRoleId();
        Integer mySc = my.getScore();
        double ranger = range / 100d;

        //判断是粉丝
        if (userinfo.getRoleId() == 3) {
            String tbs = EveryUtils.timeStamp2Date(String.valueOf(todayTime), null);
            String tbe = EveryUtils.timeStamp2Date(String.valueOf(todayEndTime), null);
            MemberDetail var1 = oderMapper.sumAllDevOderByOderCreateTimeForMb(userId.intValue(), tbs, tbe, todayTime, todayEndTime, todayTime * 1000, todayEndTime * 1000);
            if (myrole == 1) {
                todayMoneyCount = (var1.getMoney() * ranger);

            }
            if (myrole == 2) {
                todayMoneyCount = (var1.getMoney() * ranger) * (mySc / 100d);
            }
            todayCount = var1.getSums();

            String tbs1 = EveryUtils.timeStamp2Date(String.valueOf(yesDayTime), null);
            String tbe1 = EveryUtils.timeStamp2Date(String.valueOf(yesDayEndTime), null);
            MemberDetail var2 = oderMapper.sumAllDevOderByOderCreateTimeForMb(userId.intValue(), tbs1, tbe1, yesDayTime, yesDayEndTime, yesDayTime * 1000, yesDayEndTime * 1000);
            if (myrole == 1) {
                yesDayMoneyCount = (var2.getMoney() * ranger);
            }
            if (myrole == 2) {
                yesDayMoneyCount = (var2.getMoney() * ranger) * (mySc / 100d);
            }
            yesDayCount = var2.getSums();


            String tbs2 = EveryUtils.timeStamp2Date(String.valueOf(timesMonthmorning), null);
            String tbe2 = EveryUtils.timeStamp2Date(String.valueOf(timesMonthmorningLast), null);
            MemberDetail var3 = oderMapper.sumAllDevOderByOderCreateTimeForMb(userId.intValue(), tbs2, tbe2, timesMonthmorning, timesMonthmorningLast, timesMonthmorning * 1000, timesMonthmorningLast * 1000);
            if (myrole == 1) {
                yesMonthMoneyvarCount = (var3.getMoney() * ranger);

            }
            if (myrole == 2) {
                yesMonthMoneyvarCount = (var3.getMoney() * range / 100d) * (mySc / 100d);
            }
            yesMonthCount = var3.getSums();
            String tbs3 = EveryUtils.timeStamp2Date(String.valueOf(lastMonthTime), null);
            String tbe3 = EveryUtils.timeStamp2Date(String.valueOf(lastMonthTimeEnd), null);
            MemberDetail var4 = oderMapper.sumAllDevOderByOderCreateTimeForMb(userId.intValue(), tbs3, tbe3, lastMonthTime, lastMonthTimeEnd, lastMonthTime * 1000, lastMonthTimeEnd * 1000);
            if (myrole == 1) {
                yesLastMonthMoneyCount = (var4.getMoney() * range / 100d);

            }
            if (myrole == 2) {
                yesLastMonthMoneyCount = (var4.getMoney() * range / 100d) * (mySc / 100d);
            }
            yesLastMonthCount = var4.getSums();
            data.put("today", todayMoneyCount.intValue());
            data.put("todayOder", todayCount);
            data.put("yesday", yesDayMoneyCount.intValue());
            data.put("yesdayOder", yesDayCount);
            data.put("yesMonday", yesMonthMoneyvarCount.intValue());
            data.put("yesMondayOder", yesMonthCount);
            data.put("lastMonday", yesLastMonthMoneyCount.intValue());
            data.put("lastMondayOder", yesLastMonthCount);
            return data;
        }
        //判断是代理
        if (userinfo.getRoleId() == 2 && myrole == 1) {
            List<Agent> agentInfo = agentDao.queryForUserId(userId.intValue());
            if (agentInfo == null) {
                return null;
            }
            Long agentId = Long.valueOf(agentInfo.get(0).getAgentId());
            if (agentId != my.getId()) {
                logger.warn("用户Id为" + my.getId() + "进行了违法查询");
                return null;
            }
            Integer agentSc = 100 - userinfo.getScore();
            List<Long> uidlist = new ArrayList<>(10);
            uidlist.add(userId);
            List<Long> agents = agentDao.queryForAgentIdNew(userId.intValue());
            if (agents != null && agents.size() != 0) {
                uidlist.addAll(agents);
            }
            String tbs = EveryUtils.timeStamp2Date(String.valueOf(todayTime), null);
            String tbe = EveryUtils.timeStamp2Date(String.valueOf(todayEndTime), null);
            MemberDetail var1 = oderMapper.sumAllDevOderByOderCreateTimeForAgent(uidlist, tbs, tbe, todayTime, todayEndTime, todayTime * 1000, todayEndTime * 1000);
            Integer openS1 = oderMapper.countOpenOderForAgentGroupCreateTime(uidlist, tbs, tbe, todayTime, todayEndTime, todayTime * 1000, todayEndTime * 1000);
            todayMoneyCount = (var1.getMoney() * range / 100d) * (agentSc / 100d);
            todayCount = var1.getSums();

            String tbs1 = EveryUtils.timeStamp2Date(String.valueOf(yesDayTime), null);
            String tbe1 = EveryUtils.timeStamp2Date(String.valueOf(yesDayEndTime), null);
            MemberDetail var2 = oderMapper.sumAllDevOderByOderCreateTimeForAgent(uidlist, tbs1, tbe1, yesDayTime, yesDayEndTime, yesDayTime * 1000, yesDayEndTime * 1000);
            Integer openS2 = oderMapper.countOpenOderForAgentGroupCreateTime(uidlist, tbs1, tbe1, yesDayTime, yesDayEndTime, yesDayTime * 1000, yesDayEndTime * 1000);
            yesDayMoneyCount = (var2.getMoney() * range / 100d) * (agentSc / 100d);
            yesDayCount = var2.getSums();
            String tbs2 = EveryUtils.timeStamp2Date(String.valueOf(timesMonthmorning), null);
            String tbe2 = EveryUtils.timeStamp2Date(String.valueOf(timesMonthmorningLast), null);
            MemberDetail var3 = oderMapper.sumAllDevOderByOderCreateTimeForAgent(uidlist, tbs2, tbe2, timesMonthmorning, timesMonthmorningLast, timesMonthmorning * 1000, timesMonthmorningLast * 1000);
            Integer openS3 = oderMapper.countOpenOderForAgentGroupCreateTime(uidlist, tbs2, tbe2, timesMonthmorning, timesMonthmorningLast, timesMonthmorning * 1000, timesMonthmorningLast * 1000);

            yesMonthMoneyvarCount = (var3.getMoney() * range / 100d) * (agentSc / 100d);
            yesMonthCount = var3.getSums();

            String tbs3 = EveryUtils.timeStamp2Date(String.valueOf(lastMonthTime), null);
            String tbe3 = EveryUtils.timeStamp2Date(String.valueOf(lastMonthTimeEnd), null);
            MemberDetail var4 = oderMapper.sumAllDevOderByOderCreateTimeForAgent(uidlist, tbs3, tbe3, lastMonthTime, lastMonthTimeEnd, lastMonthTime * 1000, lastMonthTimeEnd * 1000);
            Integer openS4 = oderMapper.countOpenOderForAgentGroupCreateTime(uidlist, tbs3, tbe3, lastMonthTime, lastMonthTimeEnd, lastMonthTime * 1000, lastMonthTimeEnd * 1000);
            yesLastMonthMoneyCount = (var4.getMoney() * range / 100d) * (agentSc / 100d);
            yesLastMonthCount = var4.getSums();
            data.put("today", todayMoneyCount.intValue());
            data.put("todayOder", todayCount);
            data.put("todayOpen", openS1);
            data.put("yesday", yesDayMoneyCount.intValue());
            data.put("yesdayOder", yesDayCount);
            data.put("yesdayOpen", openS2);
            data.put("yesMonday", yesMonthMoneyvarCount.intValue());
            data.put("yesMondayOder", yesMonthCount.intValue());
            data.put("yesMondayOpen", openS3);
            data.put("lastMonday", yesLastMonthMoneyCount.intValue());
            data.put("lastMondayOder", yesLastMonthCount);
            data.put("lastMondayOpen", openS4);
            data.put("agentSum", uidlist.size());
            data.put("empMoney", userinfo.getScore());
            if (agentInfo.get(0).getUpdateTime() == null) {
                data.put("agentDate", agentInfo.get(0).getCreateTime());
            } else {
                data.put("agentDate", agentInfo.get(0).getUpdateTime());
            }
            return data;
        }
        return null;
    }

    @Override
    public JSONObject queryMemberDetail(Long userId) {
        JSONObject data = new JSONObject();
        Long todayTime = EveryUtils.getToday();
        Long todayEndTime = todayTime + 86400;
        Long yesDayTime = todayTime - 86400;
        Long yesDayEndTime = todayTime - 1;
        //本月第一天
        Long timesMonthmorning = EveryUtils.getTimesMonthmorning();
        //本月最后一天
        Long timesMonthmorningLast = EveryUtils.getTimesMonthmorningLast();
        //上月第一天
        Long lastMonthTime = EveryUtils.getTopStar();
        //上月最后一天
        Long lastMonthTimeEnd = EveryUtils.getEnd();
        //今日贡献佣金
        Double todayMoneyCount = 0d;
        //昨日贡献佣金
        Double yesDayMoneyCount = 0d;
        //本月贡献佣金
        Double yesMonthMoneyvarCount = 0d;
        //上月贡献佣金
        Double yesLastMonthMoneyCount = 0d;
        //今日订单数量
        Integer todayCount = 0;
        //昨日订单数量
        Integer yesDayCount = 0;
        //本月订单数量
        Integer yesMonthCount = 0;
        //上月订单数量
        Integer yesLastMonthCount = 0;
        //今日订单数量
        Double settle1 = 0d;
        //昨日订单数量
        Double settle2 = 0d;
        //本月订单数量
        Double settle3 = 0d;
        //上月订单数量
        Double settle4 = 0d;
        Userinfo userinfo = userinfoMapper.selectByPrimaryKey(userId);
        if (userinfo.getRoleId() == 3) {
            data.put("oderSum", yesMonthCount);
            data.put("inCome", 0);
            data.put("today", 0);
            data.put("todayOder", todayCount);
            data.put("todaySettle", 0);
            data.put("yesday", yesDayMoneyCount);
            data.put("yesdayOder", yesDayCount);
            data.put("yesdaySettle", 0);
            data.put("yesMonday", yesMonthMoneyvarCount);
            data.put("yesMondayOder", yesMonthCount);
            data.put("yesMondaySettle", 0);
            data.put("lastMonday", yesLastMonthMoneyCount);
            data.put("lastMondayOder", yesLastMonthCount);
            data.put("lastMondaySettle", 0);
            return data;
        }
        //如果是代理
        if (userinfo.getRoleId() == 2) {
            //该代理的分成比例
            Integer agentSc = userinfo.getScore();
            //存储代理的用户id 和其粉丝的用户id  集合
            List<Long> uidlist = new ArrayList<>(10);
            uidlist.add(userId);
            //查询粉丝
            List<Long> agents = agentDao.queryForAgentIdNew(userId.intValue());
            if (agents != null && agents.size() != 0) {
                uidlist.addAll(agents);
            }
            String tbs = EveryUtils.timeStamp2Date(String.valueOf(todayTime), null);
            String tbe = EveryUtils.timeStamp2Date(String.valueOf(todayEndTime), null);
            MemberDetail var1 = oderMapper.sumAllDevOderByOderCreateTimeForAgent(uidlist, tbs, tbe, todayTime, todayEndTime, todayTime * 1000, todayEndTime * 1000);
            todayMoneyCount = var1.getMoney() * range / 100d * agentSc / 100d;
            todayCount = var1.getSums();

            MemberDetail mb1 = oderMapper.sumAllDevOderByOderCreateTimeForAgentToSettle(uidlist, tbs, tbe, todayTime, todayEndTime, todayTime * 1000, todayEndTime * 1000);
            settle1 = mb1.getMoney() * range / 100d * agentSc / 100d;

            String tbs1 = EveryUtils.timeStamp2Date(String.valueOf(yesDayTime), null);
            String tbe1 = EveryUtils.timeStamp2Date(String.valueOf(yesDayEndTime), null);
            MemberDetail var2 = oderMapper.sumAllDevOderByOderCreateTimeForAgent(uidlist, tbs1, tbe1, yesDayTime, yesDayEndTime, yesDayTime * 1000, yesDayEndTime * 1000);
            yesDayMoneyCount = var2.getMoney() * range / 100d * agentSc / 100d;
            yesDayCount = var2.getSums();
            MemberDetail mb2 = oderMapper.sumAllDevOderByOderCreateTimeForAgentToSettle(uidlist, tbs1, tbe1, yesDayTime, yesDayEndTime, yesDayTime * 1000, yesDayEndTime * 1000);
            settle2 = mb2.getMoney() * range / 100d * agentSc / 100d;


            String tbs2 = EveryUtils.timeStamp2Date(String.valueOf(timesMonthmorning), null);
            String tbe2 = EveryUtils.timeStamp2Date(String.valueOf(timesMonthmorningLast), null);
            MemberDetail var3 = oderMapper.sumAllDevOderByOderCreateTimeForAgent(uidlist, tbs2, tbe2, timesMonthmorning, timesMonthmorningLast, timesMonthmorning * 1000, timesMonthmorningLast * 1000);
            yesMonthMoneyvarCount = var3.getMoney() * range / 100d * agentSc / 100d;
            yesMonthCount = var3.getSums();
            MemberDetail mb3 = oderMapper.sumAllDevOderByOderCreateTimeForAgentToSettle(uidlist, tbs2, tbe2, timesMonthmorning, timesMonthmorningLast, timesMonthmorning * 1000, timesMonthmorningLast * 1000);
            settle3 = mb3.getMoney() * range / 100d * agentSc / 100d;

            String tbs3 = EveryUtils.timeStamp2Date(String.valueOf(lastMonthTime), null);
            String tbe3 = EveryUtils.timeStamp2Date(String.valueOf(lastMonthTimeEnd), null);
            MemberDetail var4 = oderMapper.sumAllDevOderByOderCreateTimeForAgent(uidlist, tbs3, tbe3, lastMonthTime, lastMonthTimeEnd, lastMonthTime * 1000, lastMonthTimeEnd * 1000);
            yesLastMonthMoneyCount = var4.getMoney() * range / 100d * agentSc / 100d;
            yesLastMonthCount = var4.getSums();
            MemberDetail mb4 = oderMapper.sumAllDevOderByOderCreateTimeForAgentToSettle(uidlist, tbs3, tbe3, lastMonthTime, lastMonthTimeEnd, lastMonthTime * 1000, lastMonthTimeEnd * 1000);
            settle4 = mb4.getMoney() * range / 100d * agentSc / 100d;

            MemberDetail all = oderMapper.sumAllDevAllOder(uidlist);
            Double settle5 = all.getMoney() * range / 100d * agentSc / 100d;
            data.put("oderSum", all.getSums());
            data.put("inCome", settle5);
            data.put("today", todayMoneyCount);
            data.put("todayOder", todayCount);
            data.put("todaySettle", settle1);
            data.put("yesday", yesDayMoneyCount);
            data.put("yesdayOder", yesDayCount);
            data.put("yesdaySettle", settle2);
            data.put("yesMonday", yesMonthMoneyvarCount);
            data.put("yesMondayOder", yesMonthCount);
            data.put("yesMondaySettle", settle3);
            data.put("lastMonday", yesLastMonthMoneyCount);
            data.put("lastMondayOder", yesLastMonthCount);
            data.put("lastMondaySettle", settle4);
            return data;
        }
        if (userinfo.getRoleId() == 1) {
            //查询代理或者直属粉丝
            List<Userinfo> userInfosList = agentDao.superQueryFansUserInfo(userId.intValue());
            //代理用户信息列表
            ArrayList<Userinfo> agentIdList = new ArrayList<>(20);
            //自己和自己的直属粉丝UID 集合
            List<Long> uidlist = new ArrayList<>(10);
            uidlist.add(userId);
            List<Long> agents = agentDao.queryForAgentIdNew(userId.intValue());
            for (Userinfo useId : userInfosList) {
                if (useId == null) {
                    continue;
                }
                if (useId.getRoleId() == 2) {
                    agentIdList.add(useId);
                    continue;
                }
                uidlist.add(useId.getId());
            }
            //今日
            String tbs = EveryUtils.timeStamp2Date(String.valueOf(todayTime), null);
            String tbe = EveryUtils.timeStamp2Date(String.valueOf(todayEndTime), null);
            MemberDetail bossToday = oderMapper.sumAllDevOderByOderCreateTimeForAgent(uidlist, tbs, tbe, todayTime, todayEndTime, todayTime * 1000, todayEndTime * 1000);
            MemberDetail bossTodaymb1 = oderMapper.sumAllDevOderByOderCreateTimeForAgentToSettle(uidlist, tbs, tbe, todayTime, todayEndTime, todayTime * 1000, todayEndTime * 1000);
            Double bossTodaymb1Settlex = bossTodaymb1.getMoney() * range / 100d;
            Double todayBossMoney = bossToday.getMoney() * range / 100d;
            Integer todayBossCount = bossToday.getSums();
            Long agentMoneyTemp = 0l;
            Double todayAgentCount = 0d;
            Double settlex = 0d;
            Double todayAgentMoney = 0d;
            for (Userinfo userio : agentIdList) {
                Integer agentSc = 100 - userio.getScore();
                List<Long> uidlist1 = new ArrayList<>(10);
                uidlist1.add(userio.getId());
                List<Long> agents1 = agentDao.queryForAgentIdNew(userio.getId().intValue());
                if (agents1 != null && agents1.size() != 0) {
                    uidlist1.addAll(agents1);
                }
                MemberDetail var1 = oderMapper.sumAllDevOderByOderCreateTimeForAgent(uidlist1, tbs, tbe, todayTime, todayEndTime, todayTime * 1000, todayEndTime * 1000);
                todayAgentMoney += var1.getMoney() * range / 100d * agentSc / 100d;
                todayAgentCount += var1.getSums();
                MemberDetail mb1 = oderMapper.sumAllDevOderByOderCreateTimeForAgentToSettle(uidlist1, tbs, tbe, todayTime, todayEndTime, todayTime * 1000, todayEndTime * 1000);
                settlex += mb1.getMoney() * range / 100d * agentSc / 100d;
            }
            todayMoneyCount = todayBossMoney.intValue() + todayAgentMoney;
            todayCount = todayBossCount + todayAgentCount.intValue();
            Double s1 = bossTodaymb1Settlex + settlex;
            //今日
            String tbs1 = EveryUtils.timeStamp2Date(String.valueOf(yesDayTime), null);
            String tbe1 = EveryUtils.timeStamp2Date(String.valueOf(yesDayEndTime), null);
            MemberDetail bossToday2 = oderMapper.sumAllDevOderByOderCreateTimeForAgent(uidlist, tbs1, tbe1, yesDayTime, yesDayEndTime, yesDayTime * 1000, yesDayEndTime * 1000);
            MemberDetail bossTodaymb2 = oderMapper.sumAllDevOderByOderCreateTimeForAgentToSettle(uidlist, tbe1, tbe, yesDayTime, yesDayEndTime, yesDayTime * 1000, yesDayEndTime * 1000);
            Double bossTodaymb1Settlex2 = bossTodaymb2.getMoney() * range / 100d;
            Double todayBossMoney2 = bossToday2.getMoney() * range / 100d;
            Integer todayBossCount2 = bossToday2.getSums();
            Long agentMoneyTemp2 = 0l;
            Double todayAgentCount2 = 0d;
            Double settlex2 = 0d;
            Double todayAgentMoney2 = 0d;
            for (Userinfo userio : agentIdList) {
                Integer agentSc = 100 - userio.getScore();
                List<Long> uidlist1 = new ArrayList<>(10);
                uidlist1.add(userio.getId());
                List<Long> agents1 = agentDao.queryForAgentIdNew(userio.getId().intValue());
                if (agents1 != null && agents1.size() != 0) {
                    uidlist1.addAll(agents1);
                }
                MemberDetail var1 = oderMapper.sumAllDevOderByOderCreateTimeForAgent(uidlist1, tbs1, tbe1, yesDayTime, yesDayEndTime, yesDayTime * 1000, yesDayEndTime * 1000);
                todayAgentMoney2 += var1.getMoney() * range / 100d * agentSc / 100d;
                todayAgentCount2 += var1.getSums();
                MemberDetail mb1 = oderMapper.sumAllDevOderByOderCreateTimeForAgentToSettle(uidlist1, tbs1, tbe1, yesDayTime, yesDayEndTime, yesDayTime * 1000, yesDayEndTime * 1000);
                settlex2 += mb1.getMoney() * range / 100d * agentSc / 100d;
            }
            yesDayMoneyCount = todayBossMoney2.intValue() + todayAgentMoney2;
            yesDayCount = todayBossCount2 + todayAgentCount2.intValue();
            Double s2 = bossTodaymb1Settlex2 + settlex2;

            //今日
            String tbs2 = EveryUtils.timeStamp2Date(String.valueOf(timesMonthmorning), null);
            String tbe2 = EveryUtils.timeStamp2Date(String.valueOf(timesMonthmorningLast), null);
            MemberDetail bossToday3 = oderMapper.sumAllDevOderByOderCreateTimeForAgent(uidlist, tbs2, tbe2, timesMonthmorning, timesMonthmorningLast, timesMonthmorning * 1000, timesMonthmorningLast * 1000);
            MemberDetail bossTodaymb3 = oderMapper.sumAllDevOderByOderCreateTimeForAgentToSettle(uidlist, tbs2, tbe2, timesMonthmorning, timesMonthmorningLast, timesMonthmorning * 1000, timesMonthmorningLast * 1000);
            Double bossTodaymb1Settlex3 = bossTodaymb3.getMoney() * range / 100d;
            Double todayBossMoney3 = bossToday3.getMoney() * range / 100d;
            Integer todayBossCount3 = bossToday3.getSums();
            Long agentMoneyTemp3 = 0l;
            Double todayAgentCount3 = 0d;
            Double settlex3 = 0d;
            Double todayAgentMoney3 = 0d;
            for (Userinfo userio : agentIdList) {
                Integer agentSc = 100 - userio.getScore();
                List<Long> uidlist1 = new ArrayList<>(10);
                uidlist1.add(userio.getId());
                List<Long> agents1 = agentDao.queryForAgentIdNew(userio.getId().intValue());
                if (agents1 != null && agents1.size() != 0) {
                    uidlist1.addAll(agents1);
                }
                MemberDetail var1 = oderMapper.sumAllDevOderByOderCreateTimeForAgent(uidlist1, tbs2, tbe2, timesMonthmorning, timesMonthmorningLast, timesMonthmorning * 1000, timesMonthmorningLast * 1000);
                todayAgentMoney3 += var1.getMoney() * range / 100d * agentSc / 100d;
                todayAgentCount3 += var1.getSums();
                MemberDetail mb1 = oderMapper.sumAllDevOderByOderCreateTimeForAgentToSettle(uidlist1, tbs2, tbe2, timesMonthmorning, timesMonthmorningLast, timesMonthmorning * 1000, timesMonthmorningLast * 1000);
                settlex3 += mb1.getMoney() * range / 100d * agentSc / 100d;
            }
            yesMonthMoneyvarCount = todayBossMoney3.intValue() + todayAgentMoney3;
            yesMonthCount = todayBossCount3 + todayAgentCount3.intValue();  //今日
            Double s3 = bossTodaymb1Settlex3 + settlex3;

            String tbs3 = EveryUtils.timeStamp2Date(String.valueOf(lastMonthTime), null);
            String tbe3 = EveryUtils.timeStamp2Date(String.valueOf(lastMonthTimeEnd), null);
            MemberDetail bossToday4 = oderMapper.sumAllDevOderByOderCreateTimeForAgent(uidlist, tbs3, tbe3, lastMonthTime, lastMonthTimeEnd, lastMonthTime * 1000, lastMonthTimeEnd * 1000);
            MemberDetail bossTodaymb4 = oderMapper.sumAllDevOderByOderCreateTimeForAgentToSettle(uidlist, tbs3, tbe3, lastMonthTime, lastMonthTimeEnd, lastMonthTime * 1000, lastMonthTimeEnd * 1000);
            Double bossTodaymb1Settlex4 = bossTodaymb4.getMoney() * range / 100d;
            Double todayBossMoney4 = bossToday4.getMoney() * range / 100d;
            Integer todayBossCount4 = bossToday4.getSums();
            Long agentMoneyTemp4 = 0l;
            Double todayAgentCount4 = 0d;
            Double settlex4 = 0d;
            Double todayAgentMoney4 = 0d;
            for (Userinfo userio : agentIdList) {
                Integer agentSc = 100 - userio.getScore();
                List<Long> uidlist1 = new ArrayList<>(10);
                uidlist1.add(userio.getId());
                List<Long> agents1 = agentDao.queryForAgentIdNew(userio.getId().intValue());
                if (agents1 != null && agents1.size() != 0) {
                    uidlist1.addAll(agents1);
                }
                MemberDetail var1 = oderMapper.sumAllDevOderByOderCreateTimeForAgent(uidlist1, tbs3, tbe3, lastMonthTime, lastMonthTimeEnd, lastMonthTime * 1000, lastMonthTimeEnd * 1000);
                todayAgentMoney4 += var1.getMoney() * range / 100d * agentSc / 100d;
                todayAgentCount4 += var1.getSums();
                MemberDetail mb1 = oderMapper.sumAllDevOderByOderCreateTimeForAgentToSettle(uidlist1, tbs3, tbe3, lastMonthTime, lastMonthTimeEnd, lastMonthTime * 1000, lastMonthTimeEnd * 1000);
                settlex3 += mb1.getMoney() * range / 100d * agentSc / 100d;
            }
            yesLastMonthMoneyCount = todayBossMoney4.intValue() + todayAgentMoney4;
            yesLastMonthCount = todayBossCount4 + todayAgentCount4.intValue();
            Double s4 = bossTodaymb1Settlex4 + settlex4;

//            //今日
//            String tbs = EveryUtils.timeStamp2Date(String.valueOf(todayTime), null);
//            String tbe = EveryUtils.timeStamp2Date(String.valueOf(todayEndTime), null);
//            MemberDetail bossToday = oderMapper.sumAllDevOderByOderCreateTimeForAgent(uidlist, tbs, tbe, todayTime, todayEndTime, todayTime * 1000, todayEndTime * 1000);
//            MemberDetail bossTodaymb1 = oderMapper.sumAllDevOderByOderCreateTimeForAgentToSettle(uidlist, tbs, tbe, todayTime, todayEndTime, todayTime * 1000, todayEndTime * 1000);
//            Double  bossTodaymb1Settlex =  bossTodaymb1.getMoney() * range / 100d;
//            Double todayBossMoney = bossToday.getMoney() * range / 100d ;
//            Integer todayBossCount = bossToday.getSums();
//            Long agentMoneyTemp = 0l;
//            Double todayAgentCount = 0d;
//            Double settlex= 0d;
//            Double todayAgentMoney =0d;
//            for (Userinfo userio : agentIdList) {
//                Integer agentSc = 100-userio.getScore();
//                List<Long> uidlist1 = new ArrayList<>(10);
//                uidlist.add(userId);
//                List<Long> agents1 = agentDao.queryForAgentIdNew(userio.getId().intValue());
//                if (agents1 != null && agents1.size() != 0) {
//                    uidlist1.addAll(agents1);
//                }
//                MemberDetail var1 = oderMapper.sumAllDevOderByOderCreateTimeForAgent(uidlist1, tbs, tbe, todayTime, todayEndTime, todayTime * 1000, todayEndTime * 1000);
//                todayAgentMoney += var1.getMoney() * range / 100d * agentSc / 100d;
//                todayAgentCount +=var1.getSums();
//                MemberDetail mb1 = oderMapper.sumAllDevOderByOderCreateTimeForAgentToSettle(uidlist1, tbs, tbe, todayTime, todayEndTime, todayTime * 1000, todayEndTime * 1000);
//                settlex +=  mb1.getMoney() * range / 100d * agentSc / 100d;
//            }
//

//            MemberDetail all = oderMapper.sumAllDevAllOder(uidlist);
//            Double settle5 = (all.getMoney() * range / 100d) * (agentSc / 100d);
            Long myMoney = getMyMoney(userId).getLong("myMoney");
            data.put("oderSum", yesMonthCount);
            data.put("inCome", myMoney);
            data.put("today", todayMoneyCount);
            data.put("todayOder", todayCount);
            data.put("todaySettle", s1);
            data.put("yesday", yesDayMoneyCount);
            data.put("yesdayOder", yesDayCount);
            data.put("yesdaySettle", s2);
            data.put("yesMonday", yesMonthMoneyvarCount);
            data.put("yesMondayOder", yesMonthCount);
            data.put("yesMondaySettle", s3);
            data.put("lastMonday", yesLastMonthMoneyCount);
            data.put("lastMondayOder", yesLastMonthCount);
            data.put("lastMondaySettle", s4);
            return data;
        }
        return null;
    }


}
