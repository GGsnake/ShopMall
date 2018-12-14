package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.AgentDao;
import com.superman.superman.dao.OderMapper;
import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.model.Userinfo;
import com.superman.superman.service.MoneyService;
import com.superman.superman.service.OderService;
import lombok.NonNull;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liujupeng on 2018/12/13.
 */
@Service("moneyService")
public class MoneyServiceImpl implements MoneyService {
    @Autowired
    private UserinfoMapper userinfoMapper;
    @Autowired
    private AgentDao agentDao;
    @Autowired
    private OderService oderService;
    @Autowired
    private OderMapper oderMapper;
    //获取待结算
    public Long queryWaitMoney(@NonNull Long uid){
        Userinfo user = userinfoMapper.selectByPrimaryKey(uid);
        if (user==null){
            return null;
        }
        var roleId = user.getRoleId();
        var pddPid = user.getPddpid();
        switch (roleId) {
            case 1:
                Long totalMoney=0l;
                //总代自己的收入
                //拼多多平台订单
                Integer tempMy = oderMapper.selectPid(pddPid);
                totalMoney= Long.valueOf(tempMy==null?0:tempMy);
                // TODO  淘宝京东订单查询

                //查询代理或者直属粉丝
                List<String> lowIdList = agentDao.queryForAgentId(uid.intValue());
                if (lowIdList == null || lowIdList.size() == 0) {
                    return totalMoney;
                }
                //查询运营商下所有用户的详情集合
                List<Userinfo> userInfosList = userinfoMapper.selectInUserInfo(lowIdList);
                if (userInfosList==null||userInfosList.size()==0){
                    return totalMoney;
                }
                //代理用户信息列表
                ArrayList<Userinfo> agentIdList = new ArrayList<>(50);
                //粉丝用户信息列表
                ArrayList<Object> fansIdList = new ArrayList<>(50);
                for (Userinfo useId : userInfosList) {
                    if (useId.getRoleId() == 2) {
                        agentIdList.add(useId);
                        continue;
                    }
                    fansIdList.add(useId.getPddpid());
                }
                Long fansMoney=0l;
                if (fansIdList.size()!=0){
                    //查询所有粉丝的收入
                    //拼多多的订单
                    Integer temp = oderService.countPddOderForIdList(fansIdList);
                    fansMoney+=(temp==null?0:temp);

                }

                //我的代理的所有收入
                Long agentMoney = 0l;
                if (agentIdList.size() == 0) {
                    return fansMoney+totalMoney;
                }
                for (Userinfo userio : agentIdList) {
                    Long agentId = userio.getId();
                    String agentPddId = userio.getPddpid();
                    //根据每个代理的不同佣金比率计算我的收入
                    Long myScore = 100l - userio.getScore();
                    //计算拼多多收入
                    Long lowAgentMoney =0l;
                    Integer temp = oderMapper.selectPid(agentPddId);
                    lowAgentMoney=temp==null?0l:temp*myScore/100;

                    //查询我的下级粉丝
                    var uidList = agentDao.queryForAgentId(agentId.intValue());
                    if (uidList == null || uidList.size() == 0) {
                        agentMoney+=lowAgentMoney;
                        continue;
                    }

                    //查询出粉丝的拼多多PID集合
                    List<String> userinfos = userinfoMapper.selectIn(uidList);
                    if (userinfos == null || userinfos.size() == 0) {
                        agentMoney+=lowAgentMoney;
                        continue;
                    }

                    //查询出粉丝贡献的订单收入
                    Integer fans = oderService.countPddOderForIdList(userinfos);
                    if (fans == null) {
                        agentMoney+=lowAgentMoney;
                        continue;
                    }
                    Long fansM = fans * myScore / 100;
                    agentMoney+=(lowAgentMoney+fansM);
                }
                Long allMoney = totalMoney + fansMoney + agentMoney;
                return allMoney;

            //代理
            case 2:
                //查询我的订单收入
                Long agentScore= Long.valueOf(user.getScore());
                Integer MyMoney = oderMapper.selectPid(pddPid);
                Long meIncome = MyMoney == null ? 0l : MyMoney*agentScore/100;
                //查询我的下级粉丝
                var uidList = agentDao.queryForAgentId(uid.intValue());
                if (uidList == null || uidList.size() == 0) {
                    return meIncome;
                }
                //查询出粉丝的PID集合
                List<String> userinfos = userinfoMapper.selectIn(uidList);
                //如果粉丝没有贡献
                if (userinfos == null || userinfos.size() == 0) {
                    return meIncome;
                }
                //查询出粉丝贡献的订单收入
                Integer fans = oderService.countPddOderForIdList(userinfos);
                if (fans == null||fans==0) {
                    return meIncome;
                }
                Long fansM = fans*agentScore/100;
                return fansM+meIncome;
            //粉丝
            case 3:
                return 0l;
            default:
//                logger.warn("switch穿透" + System.currentTimeMillis());
                break;
        }
        return null;
    }
    //获取已经结算
    public Long queryFinishMoney(Long uid){
        Userinfo user = userinfoMapper.selectByPrimaryKey(uid);
        var roleId = user.getRoleId();
        var pddPid = user.getPddpid();
        switch (roleId) {
            case 1:
                Long totalMoney=0l;
                //总代自己的收入
                //拼多多平台订单
                Integer tempMy = oderMapper.selectPidForFinish(pddPid);
                totalMoney= Long.valueOf(tempMy==null?0:tempMy);
                // TODO  淘宝京东订单查询

                //查询代理或者直属粉丝
                List<String> lowIdList = agentDao.queryForAgentId(uid.intValue());
                if (lowIdList == null || lowIdList.size() == 0) {
                    return totalMoney;
                }
                //查询运营商下所有用户的详情集合
                List<Userinfo> userInfosList = userinfoMapper.selectInUserInfo(lowIdList);
                if (userInfosList==null||userInfosList.size()==0){
                    return totalMoney;
                }
                //代理用户信息列表
                ArrayList<Userinfo> agentIdList = new ArrayList<>(50);
                //粉丝用户信息列表
                ArrayList<Object> fansIdList = new ArrayList<>(50);
                for (Userinfo useId : userInfosList) {
                    if (useId.getRoleId() == 2) {
                        agentIdList.add(useId);
                        continue;
                    }
                    fansIdList.add(useId.getPddpid());
                }
                Long fansMoney=0l;
                if (fansIdList.size()!=0){
                    //查询所有粉丝的收入
                    //拼多多的订单
                    Integer temp = oderMapper.selectPidInFinish(fansIdList);
                    fansMoney+=(temp==null?0:temp);

                }

                //我的代理的所有收入
                Long agentMoney = 0l;
                if (agentIdList.size() == 0) {
                    return fansMoney+totalMoney;
                }
                for (Userinfo userio : agentIdList) {
                    Long agentId = userio.getId();
                    String agentPddId = userio.getPddpid();
                    //根据每个代理的不同佣金比率计算我的收入
                    Long myScore = 100l - userio.getScore();
                    //计算拼多多收入
                    Long lowAgentMoney =0l;
                    Integer temp = oderMapper.selectPidForFinish(agentPddId);
                    lowAgentMoney=temp==null?0l:temp*myScore/100;

                    //查询我的下级粉丝
                    var uidList = agentDao.queryForAgentId(agentId.intValue());
                    if (uidList == null || uidList.size() == 0) {
                        agentMoney+=lowAgentMoney;
                        continue;
                    }

                    //查询出粉丝的拼多多PID集合
                    List<String> userinfos = userinfoMapper.selectIn(uidList);
                    if (userinfos == null || userinfos.size() == 0) {
                        agentMoney+=lowAgentMoney;
                        continue;
                    }

                    //查询出粉丝贡献的订单收入
                    Integer fans = oderMapper.selectPidInFinish(userinfos);
                    if (fans == null) {
                        agentMoney+=lowAgentMoney;
                        continue;
                    }
                    Long fansM = fans * myScore / 100;
                    agentMoney+=(lowAgentMoney+fansM);
                }
                Long allMoney = totalMoney + fansMoney + agentMoney;
                return allMoney;

            //代理
            case 2:
                //查询我的订单收入
                Long agentScore= Long.valueOf(user.getScore());
                Integer MyMoney = oderMapper.selectPidForFinish(pddPid);
                Long meIncome = MyMoney == null ? 0l : MyMoney*agentScore/100;
                //查询我的下级粉丝
                var uidList = agentDao.queryForAgentId(uid.intValue());
                if (uidList == null || uidList.size() == 0) {
                    return meIncome;
                }
                //查询出粉丝的PID集合
                List<String> userinfos = userinfoMapper.selectIn(uidList);
                //如果粉丝没有贡献
                if (userinfos == null || userinfos.size() == 0) {
                    return meIncome;
                }
                //查询出粉丝贡献的订单收入
                Integer fans = oderMapper.selectPidInFinish(userinfos);
                if (fans == null) {
                    return meIncome;
                }
                Long fansM = fans*agentScore/100;
                return fansM+meIncome;
            //粉丝
            case 3:
                return 0l;
            default:
//                logger.warn("switch穿透" + System.currentTimeMillis());
                break;
        }
        return null;
    }

}
