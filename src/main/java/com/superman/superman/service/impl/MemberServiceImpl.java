package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.AgentDao;
import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.model.Agent;
import com.superman.superman.model.Oder;
import com.superman.superman.model.Test;
import com.superman.superman.model.Userinfo;
import com.superman.superman.service.MemberService;
import com.superman.superman.service.OderService;
import com.superman.superman.service.UserApiService;
import com.superman.superman.utils.Constants;
import com.superman.superman.utils.EveryUtils;
import com.superman.superman.utils.PageParam;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.var;


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
    private UserApiService userApiService;

    /**
     * 获取预估收入
     *
     * @param uid
     * @return
     */
    @Override
    public JSONObject getMyMoney(@NonNull Long uid) {
        var user = userinfoMapper.selectByPrimaryKey(uid);
        var roleId = user.getRoleId();
        var pddPid = user.getPddpid();
        String userphoto = user.getUserphoto();
        String username = user.getUsername();
        JSONObject myJson = new JSONObject();
        myJson.put("roleId", roleId);
        myJson.put("image", userphoto == null ? Constants.IMG_DEFAUT : userphoto);
        myJson.put("name", username == null ? Constants.USERNAME_DEFAUT : username);
        switch (roleId) {
            case 1:
                //总代自己的收入
                Long myMoney=0l;
                //总代自己的收入
                //拼多多平台订单
                Integer tempMy = oderService.countPddOderForId(pddPid);
                myMoney= Long.valueOf(tempMy==null?0:tempMy);
                myJson.put("myMoney", myMoney);
                myJson.put("myAgentCount", 0);
                myJson.put("myTeamCount", 1);
                myJson.put("myTeamMoney", 0);
                //查询代理或者直属粉丝
                List<String> lowIdList = agentDao.queryForAgentId(uid.intValue());
                if (lowIdList == null || lowIdList.size() == 0) {
                    return myJson;
                }

                //查询运营商下所有用户的详情集合
                List<Userinfo> userinfosList = userinfoMapper.selectInUserInfo(lowIdList);

                //代理用户信息列表
                ArrayList<Userinfo> agentIdList = new ArrayList<>(80);
                //粉丝用户信息列表
                ArrayList<Object> fansIdList = new ArrayList<>(80);


                for (Userinfo useId : userinfosList) {
                    if (useId.getRoleId() == 2) {
                        agentIdList.add(useId);
                        continue;
                    }
                    fansIdList.add(useId.getPddpid());
                }


                //查询所有粉丝的收入
                Long fansMoney =0l;
                Integer fansTemp = oderService.countPddOderForIdList(fansIdList);
                fansMoney=fansTemp==null?0l:fansTemp;
                //我的代理的所有收入
                Long agentMoney = 0l;
                Long agentSum = 0l;
                if (agentIdList == null || agentIdList.size() == 0) {
                    myJson.put("myMoney", fansMoney + myMoney);
                    myJson.put("myTeamCount", lowIdList.size() + 1);
                    myJson.put("myTeamMoney", fansMoney);
                    return myJson;
                }

                myJson.put("myAgentCount", agentIdList.size());
                for (Userinfo userio : agentIdList) {
                    Long agentId = userio.getId();
                    String agentPddId = userio.getPddpid();
                    //根据每个代理的不同佣金比率计算我的收入
                    Long myScore = 100l - userio.getScore();
                    Long lowAgentMoney =0l;
                    Integer temp = oderService.countPddOderForId(agentPddId);
                    lowAgentMoney=temp==null?0l:temp*myScore/100;
                    //查询我的下级粉丝
                    var uidList = agentDao.queryForAgentId(agentId.intValue());

                    if (uidList == null || uidList.size() == 0) {
                        agentMoney +=lowAgentMoney;
                        continue;
                    }
                    agentSum += uidList.size();
                    //查询出粉丝的PID集合
                    List<String> userinfos = userinfoMapper.selectIn(uidList);
                    if (userinfos == null || userinfos.size() == 0) {
                        agentMoney +=lowAgentMoney;

                        continue;
                    }
                    //查询出粉丝贡献的订单收入
                    Integer fans = oderService.countPddOderForIdList(userinfos);
                    if (fans == null||fans==0) {
                        agentMoney +=lowAgentMoney;

                        continue;
                    }
                    Long fansM= (fans * myScore)/ 100;
                    agentMoney +=(lowAgentMoney +fansM) ;


                }
                Long allMoney = myMoney + fansMoney + agentMoney;
                myJson.put("myMoney", allMoney);
                myJson.put("myTeamCount", userinfosList.size() + agentSum + 1);
                myJson.put("myTeamMoney", allMoney - myMoney);


                return myJson;

            //代理
            case 2:
                //查询我的订单收入
                var agentScore=user.getScore();
                var temp = oderService.countPddOderForId(pddPid);
                var meIncome = temp == null ||temp==0? 0 : (temp*agentScore)/100;
                myJson.put("myMoney", meIncome);
                myJson.put("myTeamCount", 1);
                myJson.put("myTeamMoney", 0);
                //查询我的下级粉丝
                var uidList = agentDao.queryForAgentId(uid.intValue());
                if (uidList == null || uidList.size() == 0) {
                    return myJson;
                }
                myJson.put("myTeamCount", uidList.size() + 1);
                //查询出粉丝的PID集合
                List<String> userinfos = userinfoMapper.selectIn(uidList);
                //如果粉丝没有贡献
                if (userinfos == null || userinfos.size() == 0) {
                    return myJson;
                }
                //查询出粉丝贡献的订单收入
                var fans = oderService.countPddOderForIdList(userinfos);
                if (fans == null||fans==0) {
                    return myJson;

                }
                Integer all = meIncome  +(fans*agentScore)/100;
                myJson.put("myMoney", all);
                myJson.put("myTeamMoney", (all - meIncome));

                return myJson;
            //粉丝
            case 3:
                myJson.put("myAgentCount", 0);
                myJson.put("myTeamCount", 1);
                myJson.put("myTeamMoney", 0);
                myJson.put("myMoney", 0);
                myJson.put("myTeamMoney",0);
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
        List<Long> agents = new ArrayList<>(40);
        JSONObject data = new JSONObject();
        Userinfo userinfo = userinfoMapper.selectByPrimaryKey(userId);
        if (userinfo.getRoleId() == 1) {
            Integer sum = agentDao.queryForUserIdCount(userId);
            //查询一级代理和直属粉丝
            agents = agentDao.queryForUserIdLimt(userId, pageParam.getStartRow(), pageParam.getPageSize());
            if (agents == null || agents.size() == 0) {
                data.put("pageCount", 0);
                data.put("pageData", new JSONArray());
                return data;
            }
            List<Userinfo> userList = userinfoMapper.selectInUserInfo(agents);
            data.put("pageCount", sum);
            JSONArray jsonArray = new JSONArray();
            for (Userinfo us : userList) {
                String username = us.getUsername();
                username=username == null ? Constants.USERNAME_DEFAUT : username;
                String image = us.getUserphoto();
                image=image == null ? Constants.IMG_DEFAUT : image;
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
        Integer sum = agentDao.queryForUserIdCount(userId);
        //查询粉丝
        agents = agentDao.queryForUserIdLimt(userId, pageParam.getStartRow(), pageParam.getPageSize());
        if (agents == null || agents.size() == 0) {
            data.put("pageCount", 0);
            data.put("pageData", new JSONArray());
            return data;
        }
        List<Userinfo> userList = userinfoMapper.selectInUserInfo(agents);
        data.put("pageCount", sum);
        JSONArray jsonArray = new JSONArray();
        for (Userinfo us : userList) {
            String username = us.getUsername();
            username=username == null ? Constants.USERNAME_DEFAUT : username;
            String image = us.getUserphoto();
            image=image == null ? Constants.IMG_DEFAUT : image;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username",username);
            jsonObject.put("image", image);
            jsonObject.put("id", us.getId());
            jsonObject.put("joinTime", us.getCreatetime());
            jsonArray.add(jsonObject);
        }
        data.put("pageData", jsonArray);
        return data;

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
    public JSONObject queryMemberDetail(Long userId) {

        JSONObject data=new JSONObject();
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
        Long todayMoneyCount=0l;
        //昨日贡献佣金
        Long yesDayMoneyCount=0l;
        //本月贡献佣金
        Long yesMonthMoneyvarCount=0l;
        //上月贡献佣金
        Long yesLastMonthMoneyCount=0l;

        //本月订单数量
        //本月订单数量
        //本月订单数量
        //本月订单数量

        var pidList=new ArrayList<String>(30);
        Userinfo userinfo = userinfoMapper.selectByPrimaryKey(userId);
        //判断是粉丝
        if (userinfo.getRoleId()==3){
            String pddpid = userinfo.getPddpid();
            pidList.add(pddpid);
            List<Oder> todayMoney=oderService.coutOderMoneyForTime(pidList,todayTime,todayEndTime);
            if (todayMoney.size()!=0){
                for (int i = 0; i < todayMoney.size(); i++) {
                    todayMoneyCount+=todayMoney.get(i).getPromotionAmount();
                }

            }

            List<Oder> yesDayMoney=oderService.coutOderMoneyForTime(pidList,yesDayTime,yesDayEndTime);
            if (yesDayMoney.size()!=0){
                for (int i = 0; i < yesDayMoney.size(); i++) {
                    yesDayMoneyCount+=yesDayMoney.get(i).getPromotionAmount();

                }
            }

            List<Oder> yesMonthMoneyvar=oderService.coutOderMoneyForTime(pidList,timesMonthmorning,timesMonthmorningLast);
            if (yesMonthMoneyvar.size()!=0){
                for (int i = 0; i < yesMonthMoneyvar.size(); i++) {
                    yesMonthMoneyvarCount+= yesMonthMoneyvar.get(i).getPromotionAmount();

                }
            }

            List<Oder> yesLastMonthMoney=oderService.coutOderMoneyForTime(pidList,lastMonthTime,lastMonthTimeEnd);
            if (yesLastMonthMoney.size()!=0){
                for (int i = 0; i < yesLastMonthMoney.size(); i++) {
                    yesLastMonthMoneyCount+= yesLastMonthMoney.get(i).getPromotionAmount();

                }
            }

            data.put("today",todayMoneyCount);
            data.put("todayOder",todayMoney.size());
            data.put("yesday",yesDayMoneyCount);
            data.put("yesdayOder",yesDayMoney.size());
            data.put("yesMonday",yesMonthMoneyvarCount);
            data.put("yesMondayOder",yesMonthMoneyvar.size());
            data.put("lastMonday",yesLastMonthMoneyCount);
            data.put("lastMondayOder",yesLastMonthMoney.size());
            data.put("agentSum",0);
            data.put("empMoney",0);
            data.put("agentDate",0);
            return data;
        }
        //判断是代理
        if (userinfo.getRoleId()==2) {
            HashSet oderOpen=new HashSet();
            HashSet oderOpen1=new HashSet();
            HashSet oderOpen2=new HashSet();
            HashSet oderOpen3=new HashSet();
            List<Agent> agents = agentDao.queryForAgentList(userId.intValue());
            String pddpid = userinfo.getPddpid();
            pidList.add(pddpid);
            List<String> strings = agentDao.queryForAgentId(userId.intValue());
            List<Userinfo> userinfos = userinfoMapper.selectInFans(strings);
            for (Userinfo ufo:userinfos){
                pidList.add(ufo.getPddpid());
            }
            List<Oder> todayMoney=oderService.coutOderMoneyForTime(pidList,todayTime,todayEndTime);
            if (todayMoney.size()!=0){
                for (int i = 0; i < todayMoney.size(); i++) {
                    todayMoneyCount+=todayMoney.get(i).getPromotionAmount();
                    oderOpen.add(todayMoney.get(i).getpId());
                }
            }

            List<Oder> yesDayMoney=oderService.coutOderMoneyForTime(pidList,yesDayTime,yesDayEndTime);
            if (yesDayMoney.size()!=0){
                for (int i = 0; i < yesDayMoney.size(); i++) {
                    yesDayMoneyCount+=yesDayMoney.get(i).getPromotionAmount();
                    oderOpen1.add(yesDayMoney.get(i).getpId());
                }
            }

            List<Oder> yesMonthMoneyvar=oderService.coutOderMoneyForTime(pidList,timesMonthmorning,timesMonthmorningLast);
            if (yesMonthMoneyvar.size()!=0){
                for (int i = 0; i < yesMonthMoneyvar.size(); i++) {
                    yesMonthMoneyvarCount+= yesMonthMoneyvar.get(i).getPromotionAmount();
                    oderOpen2.add(yesMonthMoneyvar.get(i).getpId());
                }
            }

            List<Oder> yesLastMonthMoney=oderService.coutOderMoneyForTime(pidList,lastMonthTime,lastMonthTimeEnd);
            if (yesLastMonthMoney.size()!=0){
                for (int i = 0; i < yesLastMonthMoney.size(); i++) {
                    yesLastMonthMoneyCount+= yesLastMonthMoney.get(i).getPromotionAmount();
                    oderOpen3.add(yesLastMonthMoney.get(i).getpId());
                }
            }

            data.put("today",todayMoneyCount);
            data.put("todayOder",todayMoney.size());
            data.put("todayOpen",oderOpen.size());
            data.put("yesday",yesDayMoneyCount);
            data.put("yesdayOder",yesDayMoney.size());
            data.put("yesdayOpen",oderOpen1.size());
            data.put("yesMonday",yesMonthMoneyvarCount);
            data.put("yesMondayOder",yesMonthMoneyvar.size());
            data.put("yesMondayOpen",oderOpen2.size());
            data.put("lastMonday",yesLastMonthMoneyCount);
            data.put("lastMondayOder",yesLastMonthMoney.size());
            data.put("lastMondayOpen",oderOpen3.size());
            data.put("agentSum",strings.size());
            data.put("empMoney",userinfo.getScore());
            data.put("agentDate",agents.get(0).getCreateTime());
            return data;
        }
        return null;
    }

    @Override
    public JSONObject getMyMoneyOf(Long uid) {
        var user = userinfoMapper.selectByPrimaryKey(uid);
        if(user==null){
            return  null;
        }
        var roleId = user.getRoleId();
        var pddPid = user.getPddpid();
        JSONObject myJson = new JSONObject();
        switch (roleId) {
            case 1:
                //计算总代的分成比例

                //总代自己的收入
                Long myMoney = Long.valueOf(oderService.countPddOderForId(pddPid));
                //查询代理或者直属粉丝
                List<String> lowIdList = agentDao.queryForAgentId(uid.intValue());
                if (lowIdList == null || lowIdList.size() == 0) {
                    return myJson;
                }
                //查询运营商下所有用户的详情集合
                List<Userinfo> userinfosList = userinfoMapper.selectInUserInfo(lowIdList);

                //代理用户信息列表
                ArrayList<Userinfo> agentIdList = new ArrayList<>(80);
                //粉丝用户信息列表
                ArrayList<Object> fansIdList = new ArrayList<>(80);
                for (Userinfo useId : userinfosList) {
                    if (useId.getRoleId() == 2) {
                        agentIdList.add(useId);
                        continue;
                    }
                    fansIdList.add(useId.getPddpid());
                }
                //查询所有粉丝的收入
                Integer fansMoney = oderService.countPddOderForIdList(fansIdList);
                //我的代理的所有收入
                Long agentMoney = 0l;
                Long agentSum = 0l;
                if (agentIdList == null || agentIdList.size() == 0) {
                    myJson.put("myMoney", fansMoney + myMoney);
                    return myJson;
                }
                for (Userinfo userio : agentIdList) {

                    Long agentId = userio.getId();
                    Long agentScore = 100l-userio.getScore();

                    String agentPddId = userio.getPddpid();
                    //根据每个代理的不同佣金比率计算我的收入


                    Integer lowAgentMoney = oderService.countPddOderForId(agentPddId);


                    //查询我的下级粉丝
                    var uidList = agentDao.queryForAgentId(agentId.intValue());

                    if (uidList == null || uidList.size() == 0) {
                        agentMoney += ((lowAgentMoney * agentScore) / 100);
                        continue;
                    }
                    agentSum += uidList.size();
                    //查询出粉丝的PID集合
                    List<String> userinfos = userinfoMapper.selectIn(uidList);
                    if (userinfos == null || userinfos.size() == 0) {
                        agentMoney += ((lowAgentMoney * agentScore) / 100);
                        continue;
                    }
                    //查询出粉丝贡献的订单收入
                    Integer fans = oderService.countPddOderForIdList(userinfos);
                    if (fans == null) {
                        agentMoney += ((lowAgentMoney * agentScore) / 100);
                        continue;
                    }

                    agentMoney += ((lowAgentMoney + fans) * agentScore) / 100;


                }
                Long allMoney = myMoney + fansMoney + agentMoney;
                myJson.put("myMoney", allMoney);
                return myJson;

            //代理
            case 2:
                //查询我的订单收入
                Integer score = user.getScore();
                var temp = oderService.countPddOderForId(pddPid);
                var meIncome = temp == null ? 0 : temp;
                myJson.put("myMoney", (meIncome*score)/100);
                //查询我的下级粉丝
                var uidList = agentDao.queryForAgentId(uid.intValue());
                if (uidList == null || uidList.size() == 0) {
                    return myJson;
                }
                //查询出粉丝的PID集合
                List<String> userinfos = userinfoMapper.selectIn(uidList);
                //如果粉丝没有贡献
                if (userinfos == null || userinfos.size() == 0) {
                    return myJson;
                }
                //查询出粉丝贡献的订单收入
                var fans = oderService.countPddOderForIdList(userinfos);
                if (fans == null) {
                    return myJson;
                }
                Integer all = meIncome + fans;
                myJson.put("myMoney", (all*score)/100);

                return myJson;
            //粉丝
            case 3:
                break;
            default:
                logger.warn("switch穿透" + System.currentTimeMillis());
                break;
        }
        return null;
    }


//
//    public JSONObject queryMemberDetail(@NonNull Long userId,@NonNull Integer type) {
//        if (type==2){
//
//        }
//        Userinfo userinfo = userinfoMapper.selectByPrimaryKey(userId);
//        JSONObject data=new JSONObject();
//        List<Agent> agents = agentDao.queryForUserId(userId.intValue());
//        if (agents==null||agents.size()==0){
//            return data;
//        }
//        Agent agent = agents.get(0);
//        agent
//        if (userinfo.getRoleId()==2){
//            agentDao.
//            data.put("username",userinfo.getUsername());
//            data.put("username",userinfo.getUsername());
//            data.put("username",userinfo.getUsername());
//        }
//        return null;
//    }



}
