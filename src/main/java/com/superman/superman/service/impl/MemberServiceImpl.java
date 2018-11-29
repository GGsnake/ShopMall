package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.AgentDao;
import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.model.Agent;
import com.superman.superman.model.Role;
import com.superman.superman.model.Test;
import com.superman.superman.model.Userinfo;
import com.superman.superman.service.MemberService;
import com.superman.superman.service.OderService;
import com.superman.superman.service.UserApiService;
import com.superman.superman.utils.PageParam;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.var;


import java.util.ArrayList;
import java.util.List;

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

    @Override
    public String aaaa() {
        return null;
    }

    @Override
    public String bbb() {
        return null;
    }

    @Override
    public String ccc() {
        Long uid = 0l;
        var t1 = new Test();

        t1.setAgentId(uid.intValue());
//        pddDao.selectRchid(t1);
        return null;
    }
    //获取预估收入
    @Override
    public JSONObject getMyMoney(@NonNull Long uid) {
        var role = userinfoMapper.selectByPrimaryKey(uid);
        var roleId = role.getRoleId();
        var pddPid = role.getPddpid();
        String userphoto = role.getUserphoto();
        String username = role.getUsername();
        JSONObject myJson=new JSONObject();
        myJson.put("roleId",roleId);
        myJson.put("image",userphoto==null?null:userphoto);
        myJson.put("name",username==null?null:username);
        switch (roleId) {
            case 1:
                //总代自己的收入
                Long myMoney = Long.valueOf(oderService.countPddOderForId(pddPid));
                myJson.put("myMoney",myMoney);
                myJson.put("myAgentCount",0);
                myJson.put("myTeamCount",1);
                myJson.put("myTeamMoney",0);
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
                Long agentSum=0l;
                if (agentIdList == null || agentIdList.size() == 0) {
                    myJson.put("myMoney",fansMoney+myMoney);
                    myJson.put("myTeamCount",lowIdList.size()+1);
                    myJson.put("myTeamMoney",fansMoney);
                    return myJson;
                }

                myJson.put("myAgentCount",agentIdList.size());
                for (Userinfo userio : agentIdList) {
                    Long agentId = userio.getId();
                    String agentPddId = userio.getPddpid();
                    //根据每个代理的不同佣金比率计算我的收入
                    Long myScore = 100l - userio.getScore();

                    Integer lowAgentMoney = oderService.countPddOderForId(agentPddId);


                    //查询我的下级粉丝
                    var uidList = agentDao.queryForAgentId(agentId.intValue());

                    if (uidList == null || uidList.size() == 0) {
                        agentMoney += ((lowAgentMoney * myScore)/100);
                        continue;
                    }
                    agentSum+=uidList.size();
                    //查询出粉丝的PID集合
                    List<String> userinfos = userinfoMapper.selectIn(uidList);
                    if (userinfos == null||userinfos.size()==0) {
                        agentMoney += ((lowAgentMoney * myScore)/100);
                        continue;
                    }
                    //查询出粉丝贡献的订单收入
                    Integer fans = oderService.countPddOderForIdList(userinfos);
                    if (fans == null) {
                        agentMoney += ((lowAgentMoney * myScore)/100);
                        continue;
                    }

                    agentMoney += ((lowAgentMoney + fans)*myScore)/100;


                }
                Long allMoney= myMoney + fansMoney + agentMoney;
                myJson.put("myMoney",allMoney);
                myJson.put("myTeamCount",userinfosList.size()+agentSum+1);
                myJson.put("myTeamMoney",allMoney-myMoney);


                return myJson;

            //代理
            case 2:
                //查询我的订单收入
                var temp = oderService.countPddOderForId(pddPid);
                var meIncome = temp == null ? 0 : temp;
                myJson.put("myMoney",meIncome);
                myJson.put("myTeamCount",1);
                myJson.put("myTeamMoney",0);
                //查询我的下级粉丝
                var uidList = agentDao.queryForAgentId(uid.intValue());
                if (uidList == null|| uidList.size() == 0) {
                    return myJson;
                }
                //查询出粉丝的PID集合
                List<String> userinfos = userinfoMapper.selectIn(uidList);
                //如果粉丝没有贡献
                if (userinfos == null || uidList.size() == 0) {
                    myJson.put("myTeamCount",uidList.size()+1);
                    return myJson;
                }
                myJson.put("myTeamCount",uidList.size()+1);
                //查询出粉丝贡献的订单收入
                var fans = oderService.countPddOderForIdList(userinfos);
                if (fans == null) {
                    return myJson;

                }
                Integer all = meIncome + fans;
                myJson.put("myMoney",all);
                myJson.put("myTeamMoney",all-meIncome);

                return myJson;
            //粉丝
            case 3:
                break;
            default:
                logger.warn("switch穿透"+System.currentTimeMillis());
                break;
        }
        return null;
    }

    @Override
    public JSONObject getMyTeam(Long uid, Integer i, Integer i1) {

        getMyMoney(uid);
        return null;
    }

    @Override
    public JSONObject getMyTeam(long l, PageParam pageParam) {
        List<Long> agents = agentDao.queryForUserIdLimt(l,1,2);
        List<Userinfo> userList = userinfoMapper.selectInUserInfo(agents);
        List<JSONObject> jsonList = new ArrayList<>(40);

        for (Userinfo us:userList)
        {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("roleId",us.getRoleId());
            jsonObject.put("username",us.getUsername());
            jsonObject.put("image",us.getUserphoto());
            jsonObject.put("id",us.getId());
            jsonObject.put("chidSum",0);
            if (us.getRoleId()==2){
                Integer chidSum = agentDao.countRecommd(us.getId());
                jsonObject.put("chidSum",chidSum);
            }
            jsonList.add(jsonObject);
        }
        logger.warn(agents.toString());
        return null;
    }

    @Override
    public JSONObject getMyNoFans(long l, PageParam pageParam) {
        List<Agent> idList = agentDao.countRecommdToSum(l, 1, 1);

        JSONObject o = (JSONObject) JSONObject.toJSON(idList);
        return o;
    }

//
//    @Override
//    public JSONObject getMyTeam(Long uid) {
//        //团队成员数量
//        Integer teamSum=0;
//        Integer agentSum=0;
//        Test t1=new Test();
//        t1.setAgentId(uid.intValue());
//        //
//
//        //直推会员Id列表
//        List uidList=new ArrayList();
//        List<TeamList> teamLists=new ArrayList();
//        JSONObject jsonObject = new JSONObject();
//        JSONArray json = new JSONArray();
//        JSONArray json1 = new JSONArray();
//        List<Test> test = pddDao.selectRchid(t1);
//        if (test!=null){
//            for (Test tt:test){
//                uidList.add(tt.getUserId());
//            }
//            List<Role> roles = pddDao.selectRchidlist(uidList);
//            //会员下级Id
//            List<Integer> uidLiLatp=new ArrayList();
//
//            for (Role tt:roles){
//                Long tId = tt.getUserId();
//                TeamList t=new TeamList();
//                t.setUserId(tt.getUserId());
//                t.setScore(tt.getScore());
//                if (tt.getScore()==0)
//                {
//                    t.setMember(0);
//                    t.setDaili(false);
//                }
//                if (tt.getScore()<100&&tt.getScore()!=0)
//                {
//                    t.setDaili(true);
//                    uidLiLatp.add(tId.intValue());
//                    Integer agSum = pddDao.countAgentId(tId.intValue());
//                    t.setMember(agSum);
//                }
//                teamLists.add(t);
//            }
//            List lowList=new ArrayList();
//            List<Test> vipNolist = pddDao.selectTchidlist(uidLiLatp);
//            for (Test tt:vipNolist){
//                lowList.add(tt.getUserId());
//            }
//            List<Role> roleList = pddDao.selectRchidlist(lowList);
//            teamSum=lowList.size()+teamLists.size();
//            agentSum=uidLiLatp.size();
//            for (TeamList a:teamLists)
//            {
//                    JSONObject jo = new JSONObject();
//                    jo.put("id", a.getUserId());
//                    jo.put("daili", a.getDaili());
//                    jo.put("member", a.getMember());
//                    jo.put("score", a.getScore());
//                    json.add(jo);
//            }
//            for (Role a:roleList)
//            {
//                JSONObject jo = new JSONObject();
//                jo.put("id", a.getUserId());
//                jo.put("score", a.getScore());
//                json1.add(jo);
//            }
//            jsonObject.put("zhishu", json);
//            jsonObject.put("xiaji", json1);
//            jsonObject.put("agentSum", agentSum);
//            jsonObject.put("teamSum", teamSum);
//            return jsonObject;
//
//        }
//        jsonObject.put("zhishu", null);
//        jsonObject.put("xiaji", null);
//        jsonObject.put("agentSum", 0);
//        jsonObject.put("teamSum", 0);
//        return jsonObject;
//    }


}
