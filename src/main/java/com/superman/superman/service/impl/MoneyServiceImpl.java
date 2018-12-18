package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.AgentDao;
import com.superman.superman.dao.OderMapper;
import com.superman.superman.dao.TboderMapper;
import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.model.Userinfo;
import com.superman.superman.service.MoneyService;
import com.superman.superman.service.OderService;
import lombok.NonNull;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private TboderMapper tboderMapper;


    //获取待结算
    public Long queryWaitMoney(@NonNull Long uid) {
        Userinfo user = userinfoMapper.selectByPrimaryKey(uid);
        if (user == null) {
            return null;
        }
        var roleId = user.getRoleId();
        var pddPid = user.getPddpid();
        var tbpid = user.getTbpid();
        Map dataMap = new HashMap();
        dataMap.put("pdd", pddPid);
        dataMap.put("uid", uid);
        dataMap.put("tb", tbpid);
        switch (roleId) {
            case 1:
                Long totalMoney = 0l;
                //总代自己的收入
                //拼多多平台订单
                Long tempMy = queryAllFinishSimple(dataMap, 0);
                totalMoney += tempMy == null ? 0 : tempMy;
                //查询代理或者直属粉丝
                List<String> lowIdList = agentDao.queryForAgentId(uid.intValue());
                if (lowIdList == null || lowIdList.size() == 0) {
                    return totalMoney;
                }
                //查询运营商下所有用户的详情集合
                List<Userinfo> userInfosList = userinfoMapper.selectInUserInfo(lowIdList);
                if (userInfosList == null || userInfosList.size() == 0) {
                    return totalMoney;
                }
                //代理用户信息列表
                ArrayList<Userinfo> agentIdList = new ArrayList<>(50);
                //粉丝用户信息列表
                ArrayList<Userinfo> fansIdList = new ArrayList<>(50);
                for (Userinfo useId : userInfosList) {
                    if (useId.getRoleId() == 2) {
                        agentIdList.add(useId);
                        continue;
                    }
                    fansIdList.add(useId);
                }
                Long fansMoney = 0l;
                if (fansIdList.size() != 0) {
                    //查询所有粉丝的收入
                    Integer temp = oderService.countPddOderForIdList(fansIdList,0);
                    fansMoney += (temp == null ? 0 : temp);

                }

                //我的代理的所有收入
                Long agentMoney = 0l;
                if (agentIdList.size() == 0) {
                    return fansMoney + totalMoney;
                }
                for (Userinfo userio : agentIdList) {
                    Long agentId = userio.getId();
                    String agentPddId = userio.getPddpid();
                    //根据每个代理的不同佣金比率计算我的收入
                    Long myScore = 100l - userio.getScore();
                    //计算拼多多收入
                    Long lowAgentMoney = 0l;
                    Integer temp = oderMapper.selectPid(agentPddId);
                    lowAgentMoney = temp == null ? 0l : temp * myScore / 100;

                    //查询我的下级粉丝
                    var uidList = agentDao.queryForAgentId(agentId.intValue());
                    if (uidList == null || uidList.size() == 0) {
                        agentMoney += lowAgentMoney;
                        continue;
                    }

                    //查询出粉丝的PID集合
                    List<Userinfo> userinfos = userinfoMapper.selectIn(uidList);
                    if (userinfos == null || userinfos.size() == 0) {
                        agentMoney += lowAgentMoney;
                        continue;
                    }

                    //查询出粉丝贡献的订单收入
                    Integer fans = oderService.countPddOderForIdList(userinfos,0);
                    if (fans == null) {
                        agentMoney += lowAgentMoney;
                        continue;
                    }
                    Long fansM = fans * myScore / 100;
                    agentMoney += (lowAgentMoney + fansM);
                }
                Long allMoney = totalMoney + fansMoney + agentMoney;
                return allMoney;

            //代理
            case 2:
                Long meIncome = 0l;
                //查询我的订单收入
                Long agentScore = Long.valueOf(user.getScore());
                Long MyMoney = queryAllFinishSimple(dataMap,0);
                meIncome += MyMoney == null ? 0l : MyMoney * agentScore / 100;
                //查询我的下级粉丝
                var uidList = agentDao.queryForAgentIdNew(uid.intValue());
                if (uidList == null || uidList.size() == 0) {
                    return meIncome;
                }
//                //查询出粉丝的PID集合
//                List<Userinfo> userinfos = userinfoMapper.selectIn(uidList);
//                //如果粉丝没有贡献
//                if (userinfos == null || userinfos.size() == 0) {
//                    return meIncome;
//                }
                //查询出粉丝贡献的订单收入
                Long fans = oderService.superQueryOderForUidList(uidList,0);
//                Integer fans = oderService.countPddOderForIdList(userinfos,0);
                if (fans == null || fans == 0) {
                    return meIncome;
                }
                Long fansM = fans * agentScore / 100;
                return fansM + meIncome;
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
    public Long queryFinishMoney(Long uid) {
        Userinfo user = userinfoMapper.selectByPrimaryKey(uid);
        if (user == null) {
            return null;
        }
        var roleId = user.getRoleId();
        var pddPid = user.getPddpid();
        var tbpid = user.getTbpid();
        Map dataMap = new HashMap();
        dataMap.put("pdd", pddPid);
        dataMap.put("uid", uid);
        dataMap.put("tb", tbpid);
        switch (roleId) {
            case 1:
                Long totalMoney = 0l;
                //总代自己的收入
                //拼多多平台订单
                Long tempMy = queryAllFinishSimple(dataMap,1);
                totalMoney = tempMy == null ? 0 : tempMy;
                // TODO  淘宝京东订单查询

                //查询代理或者直属粉丝
                List<String> lowIdList = agentDao.queryForAgentId(uid.intValue());
                if (lowIdList == null || lowIdList.size() == 0) {
                    return totalMoney;
                }
                //查询运营商下所有用户的详情集合
                List<Userinfo> userInfosList = userinfoMapper.selectInUserInfo(lowIdList);
                if (userInfosList == null || userInfosList.size() == 0) {
                    return totalMoney;
                }
                //代理用户信息列表
                ArrayList<Userinfo> agentIdList = new ArrayList<>(30);
                //粉丝用户信息列表
                ArrayList<Userinfo> fansIdList = new ArrayList<>(30);
                for (Userinfo useId : userInfosList) {
                    if (useId.getRoleId() == 2) {
                        agentIdList.add(useId);
                        continue;
                    }
                    fansIdList.add(useId);
                }
                Long fansMoney = 0l;
                if (fansIdList.size() != 0) {
                    //查询所有粉丝的收入
                    //拼多多的订单
                    Integer temp = oderService.countPddOderForIdList(fansIdList,1);
                    fansMoney += (temp == null ? 0 : temp);

                }

                //我的代理的所有收入
                Long agentMoney = 0l;
                if (agentIdList.size() == 0) {
                    return fansMoney + totalMoney;
                }
                for (Userinfo userio : agentIdList) {
                    Long agentId = userio.getId();
                    Map dataMapAgent = new HashMap();
                    dataMapAgent.put("pdd", userio.getPddpid());
                    dataMapAgent.put("uid", uid);
                    dataMapAgent.put("tb", userio.getTbpid());
                    //根据每个代理的不同佣金比率计算我的收入
                    Long myScore = 100l - userio.getScore();
                    //计算拼多多收入
                    Long lowAgentMoney = 0l;
                    Long temp = queryAllFinishSimple(dataMap,1);
                    lowAgentMoney = temp == null ? 0l : temp * myScore / 100;

                    //查询我的下级粉丝
                    var uidList = agentDao.queryForAgentId(agentId.intValue());
                    if (uidList == null || uidList.size() == 0) {
                        agentMoney += lowAgentMoney;
                        continue;
                    }

                    //查询出粉丝的拼多多PID集合
                    List<Userinfo> userinfos = userinfoMapper.selectIn(uidList);
                    if (userinfos == null || userinfos.size() == 0) {
                        agentMoney += lowAgentMoney;
                        continue;
                    }

                    //查询出粉丝贡献的订单收入
                    Integer fans = oderService.countPddOderForIdList(userinfos,1);
                    if (fans == null) {
                        agentMoney += lowAgentMoney;
                        continue;
                    }
                    Long fansM = fans * myScore / 100;
                    agentMoney += (lowAgentMoney + fansM);
                }
                Long allMoney = totalMoney + fansMoney + agentMoney;
                return allMoney;

            //代理
            case 2:
                //查询我的订单收入
                Long agentScore = Long.valueOf(user.getScore());
                Long MyMoney = queryAllFinishSimple(dataMap,1);
                Long meIncome = MyMoney == null ? 0l : MyMoney * agentScore / 100;
                //查询我的下级粉丝
                var uidList = agentDao.queryForAgentId(uid.intValue());
                if (uidList == null || uidList.size() == 0) {
                    return meIncome;
                }
                //查询出粉丝的PID集合
                List<Userinfo> userinfos = userinfoMapper.selectIn(uidList);
                //如果粉丝没有贡献
                if (userinfos == null || userinfos.size() == 0) {
                    return meIncome;
                }
                //查询出粉丝贡献的订单收入
                Integer fans = oderService.countPddOderForIdList(userinfos,1);
                if (fans == null) {
                    return meIncome;
                }
                Long fansM = fans * agentScore / 100;
                return fansM + meIncome;
            //粉丝
            case 3:
                return 0l;
            default:
//                logger.warn("switch穿透" + System.currentTimeMillis());
                break;
        }
        return null;
    }


    @Override
    public Long queryAllFinishSimple(Map map,@NonNull Integer flag) {
        String pdd = String.valueOf(map.get("pdd"));
        Long tb = (Long) map.get("tb");
        Long jd = (Long) map.get("jd");
        Long uid = (Long) map.get("uid");
        Integer pddMoney = 0;
        Long tbMoney = 0l;
        if (flag == 0) {
            tbMoney=oderMapper.superQueryWaitMoneyForUserSimple(uid.intValue());
//            pddMoney = oderMapper.selectPid(pdd);
//            tbMoney = tboderMapper.selectPid(tb);
        }
        if (flag == 1) {
            pddMoney = oderMapper.selectPidForFinish(pdd);
            tbMoney = tboderMapper.selectPidForFinish(tb);
        }
        pddMoney = pddMoney == null ? 0 : pddMoney;
        tbMoney = tbMoney == null ? 0l : tbMoney;
        return pddMoney + tbMoney;
    }


}
