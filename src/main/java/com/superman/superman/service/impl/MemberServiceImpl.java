package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.Dto.MemberDetail;
import com.superman.superman.dao.AgentDao;
import com.superman.superman.dao.OderMapper;
import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.model.Agent;
import com.superman.superman.model.Oder;
import com.superman.superman.model.Test;
import com.superman.superman.model.Userinfo;
import com.superman.superman.service.MemberService;
import com.superman.superman.service.MoneyService;
import com.superman.superman.service.OderService;
import com.superman.superman.service.UserApiService;
import com.superman.superman.utils.Constants;
import com.superman.superman.utils.EveryUtils;
import com.superman.superman.utils.PageParam;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.var;


import java.math.BigDecimal;
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
    private UserApiService userApiService;
    @Autowired
    private OderMapper oderMapper;

    @Value("${juanhuang.range}")
    private Integer range;

    /**
     * 获取预估收入
     *
     * @param uid
     * @return
     */
    @Override
    public JSONObject getMyMoney(@NonNull Long uid) {
        JSONObject myJson = new JSONObject();
        Userinfo user = userinfoMapper.selectByPrimaryKey(uid);
        Integer roleId = user.getRoleId();
        String userphoto = user.getUserphoto();
        String username = user.getUsername();
        //存储用户集合
        HashSet<Long> uidSet = new HashSet<>();
        uidSet.add(uid);

        myJson.put("roleId", roleId);
        myJson.put("image", userphoto == null ? Constants.IMG_DEFAUT : userphoto);
        myJson.put("name", username == null ? Constants.USERNAME_DEFAUT : username);
        switch (roleId) {
            case 1:
                Long myMoney = oderService.superQueryOderForUidList(EveryUtils.setToList(uidSet), 0);
                Long AllMoney = moneyService.queryCashMoney(uid, 0, user);
                myJson.put("myMoney", (AllMoney +myMoney));
                myJson.put("myTeamMoney",(AllMoney +myMoney)-myMoney);
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
                //查询我的订单收入
                Long myMoney2 = oderService.superQueryOderForUidList(EveryUtils.setToList(uidSet), 0);
                Long AllMoney2 = moneyService.queryCashMoney(uid, 0, user);
                myJson.put("myMoney", AllMoney2);
                myJson.put("myTeamMoney", AllMoney2 - myMoney2);
                List<Long> var2 = agentDao.queryForAgentIdNew(uid.intValue());
                uidSet.addAll(var2);
                myJson.put("myTeamCount", uidSet.size());
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

    /**
     * 获取我的团队直属会员和直属代理
     *
     * @param userId
     * @param pageParam 分页参数
     * @return
     */
    @Override
    public JSONObject getMyTeam(Long userId, PageParam pageParam) {
        if (pageParam == null) {
            return null;
        }
        List<Long> agents = null;
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

    /**
     * 总代的查找代理的会员下级
     *
     * @param userId
     * @param pageParam
     * @return
     */
    @Override
    public JSONObject getMyNoFans(Long userId, PageParam pageParam) {

        Integer sum = agentDao.countNoMyFansSum(userId);
        JSONObject data = new JSONObject();
        data.put("sum", sum);
        if (sum == 0 || sum == null) {
            data.put("data", new JSONArray());
            return data;
        }
        List<Agent> idList = agentDao.countNoMyFans(userId, pageParam.getStartRow(), pageParam.getPageSize());

        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < idList.size(); i++) {
            Userinfo agent = userinfoMapper.selectByPrimaryKey(Long.valueOf(idList.get(i).getAgentId()));
            Userinfo uid = userinfoMapper.selectByPrimaryKey(Long.valueOf(idList.get(i).getUserId()));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", agent.getUsername());
            jsonObject.put("pidname", uid.getUsername());
            jsonObject.put("image", uid.getUserphoto());
            jsonObject.put("uid", uid.getId());
            jsonArray.add(jsonObject);
        }
        data.put("data", jsonArray);
        return data;
    }

    @Override
    public JSONObject queryMemberDetail(Long userId, Integer myid) {

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
        Userinfo my = userinfoMapper.selectByPrimaryKey(Long.valueOf(myid));
        Integer myrole = my.getRoleId();
        Integer mySc = my.getScore();

        //判断是粉丝
        if (userinfo.getRoleId() == 3) {
            String tbs = EveryUtils.timeStamp2Date(String.valueOf(todayTime), null);
            String tbe = EveryUtils.timeStamp2Date(String.valueOf(todayEndTime), null);
            MemberDetail var1 = oderMapper.sumAllDevOderByOderCreateTimeForMb(userId.intValue(), tbs, tbe, todayTime, todayEndTime);
            if (myrole == 1) {
                todayMoneyCount = (var1.getMoney() * range / 100d);

            }
            if (myrole == 2) {
                todayMoneyCount = (var1.getMoney() * range / 100d) * (mySc / 100d);
            }
            todayCount = var1.getSums();

            String tbs1 = EveryUtils.timeStamp2Date(String.valueOf(yesDayTime), null);
            String tbe1 = EveryUtils.timeStamp2Date(String.valueOf(yesDayEndTime), null);
            MemberDetail var2 = oderMapper.sumAllDevOderByOderCreateTimeForMb(userId.intValue(), tbs1, tbe1, yesDayTime, yesDayEndTime);
            if (myrole == 1) {
                yesDayMoneyCount = (var2.getMoney() * range / 100d);

            }
            if (myrole == 2) {
                yesDayMoneyCount = (var2.getMoney() * range / 100d) * (mySc / 100d);
            }
            yesDayCount = var2.getSums();


            String tbs2 = EveryUtils.timeStamp2Date(String.valueOf(timesMonthmorning), null);
            String tbe2 = EveryUtils.timeStamp2Date(String.valueOf(timesMonthmorningLast), null);
            MemberDetail var3 = oderMapper.sumAllDevOderByOderCreateTimeForMb(userId.intValue(), tbs2, tbe2, timesMonthmorning, timesMonthmorningLast);
            if (myrole == 1) {
                yesMonthMoneyvarCount = (var3.getMoney() * range / 100d);

            }
            if (myrole == 2) {
                yesMonthMoneyvarCount = (var3.getMoney() * range / 100d) * (mySc / 100d);
            }
            yesMonthCount = var3.getSums();
            String tbs3 = EveryUtils.timeStamp2Date(String.valueOf(lastMonthTime), null);
            String tbe3 = EveryUtils.timeStamp2Date(String.valueOf(lastMonthTimeEnd), null);
            MemberDetail var4 = oderMapper.sumAllDevOderByOderCreateTimeForMb(userId.intValue(), tbs3, tbe3, lastMonthTime, lastMonthTimeEnd);
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
                return null;
            }
            Integer agentSc = 100 - userinfo.getScore();
            HashSet oderOpen = new HashSet();
            HashSet oderOpen1 = new HashSet();
            HashSet oderOpen2 = new HashSet();
            HashSet oderOpen3 = new HashSet();
            List<Long> uidlist = new ArrayList<>(10);
            uidlist.add(userId);
            List<Long> agents = agentDao.queryForAgentIdNew(userId.intValue());
            if (agents != null && agents.size() != 0) {
                uidlist.addAll(agents);
            }
            String tbs = EveryUtils.timeStamp2Date(String.valueOf(todayTime), null);
            String tbe = EveryUtils.timeStamp2Date(String.valueOf(todayEndTime), null);
            MemberDetail var1 = oderMapper.sumAllDevOderByOderCreateTimeForAgent(uidlist, tbs, tbe, todayTime, todayEndTime);
            todayMoneyCount = (var1.getMoney() * range / 100d) * (agentSc / 100d);
            todayCount = var1.getSums();

            String tbs1 = EveryUtils.timeStamp2Date(String.valueOf(yesDayTime), null);
            String tbe1 = EveryUtils.timeStamp2Date(String.valueOf(yesDayEndTime), null);
            MemberDetail var2 = oderMapper.sumAllDevOderByOderCreateTimeForAgent(uidlist, tbs1, tbe1, yesDayTime, yesDayEndTime);
            yesDayMoneyCount = (var2.getMoney() * range / 100d) * (agentSc / 100d);
            yesDayCount = var2.getSums();

            String tbs2 = EveryUtils.timeStamp2Date(String.valueOf(timesMonthmorning), null);
            String tbe2 = EveryUtils.timeStamp2Date(String.valueOf(timesMonthmorningLast), null);
            MemberDetail var3 = oderMapper.sumAllDevOderByOderCreateTimeForAgent(uidlist, tbs2, tbe2, timesMonthmorning, timesMonthmorningLast);
            yesMonthMoneyvarCount = (var3.getMoney() * range / 100d) * (agentSc / 100d);
            yesMonthCount = var3.getSums();

            String tbs3 = EveryUtils.timeStamp2Date(String.valueOf(lastMonthTime), null);
            String tbe3 = EveryUtils.timeStamp2Date(String.valueOf(lastMonthTimeEnd), null);
            MemberDetail var4 = oderMapper.sumAllDevOderByOderCreateTimeForAgent(uidlist, tbs3, tbe3, lastMonthTime, lastMonthTimeEnd);
            yesLastMonthMoneyCount = (var4.getMoney() * range / 100d) * (agentSc / 100d);
            yesLastMonthCount = var4.getSums();
            data.put("today", todayMoneyCount.intValue());
            data.put("todayOder", todayCount);
            data.put("todayOpen", oderOpen.size());
            data.put("yesday", yesDayMoneyCount.intValue());
            data.put("yesdayOder", yesDayCount);
            data.put("yesdayOpen", oderOpen1.size());
            data.put("yesMonday", yesMonthMoneyvarCount.intValue());
            data.put("yesMondayOder", yesMonthCount.intValue());
            data.put("yesMondayOpen", oderOpen2.size());
            data.put("lastMonday", yesLastMonthMoneyCount.intValue());
            data.put("lastMondayOder", yesLastMonthCount);
            data.put("lastMondayOpen", oderOpen3.size());
            data.put("agentSum", uidlist.size());
            data.put("empMoney", userinfo.getScore());
            data.put("agentDate", agentInfo.get(0).getUpdateTime());
            return data;
        }
        return null;
    }


}
