package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.*;
import com.superman.superman.model.Oder;
import com.superman.superman.model.Userinfo;
import com.superman.superman.req.OderPdd;
import com.superman.superman.service.OderService;
import com.superman.superman.utils.PageParam;
import lombok.NonNull;
import lombok.var;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liujupeng on 2018/11/24.
 */
@Service("oderService")
public class OderServiceImpl implements OderService {

    @Autowired
    private OderMapper oderMapper;
    @Autowired
    private TboderMapper tboderMapper;

    @Autowired
    private AgentDao agentDao;

    @Autowired
    private UserinfoMapper userinfoMapper;

    @Autowired
    private AllDevOderMapper allDevOderMapper;

//
//    @Override
//    public JSONObject queryPddOder(@NonNull Long uid, @NonNull Integer dev, @NonNull List status, PageParam pageParam) {
//
//        if (dev == 0) {
//            int count = allDevOderMapper.queryPddPageSizeCount(status, pdd);
////            data.put("list", list);
//            data.put("count", count);
//            return data;
//        }
//        if (dev == 1) {
//            List tb = new ArrayList();
//            for (Userinfo var3 : userinfos) {
//                tb.add(var3.getTbpid());
//            }
////            allDevOderMapper.queryPddPageSize(status, tb, pageParam.getStartRow(), pageParam.getPageSize());
//        }
//        if (dev == 2) {
//            List jd = new ArrayList();
//            for (Userinfo var3 : userinfos) {
//                jd.add(var3.getJdpid());
//            }
////            allDevOderMapper.queryPddPageSize(status, jd, pageParam.getStartRow(), pageParam.getPageSize());
//        }
//        return null;
//    }


    @Override
    public JSONObject queryPddOder(Long uid, List status, PageParam pageParam) {
        Userinfo userinfo = userinfoMapper.selectByPrimaryKey(uid);
        Integer roleId = userinfo.getRoleId();
        var data=new JSONObject();

        switch (roleId) {
            case 1:
                List<OderPdd> list = allDevOderMapper.queryPddPageSize(status, uid, pageParam.getStartRow(), pageParam.getPageSize());
                Integer count= allDevOderMapper.queryPddPageSizeCount(status, uid);
                data.put("data",list);
                data.put("count",count);
                return data;
            case 2:
                Integer score = userinfo.getScore();
                List<OderPdd> list1 = allDevOderMapper.queryPddPageSize(status, uid, pageParam.getStartRow(), pageParam.getPageSize());
                Integer count2= allDevOderMapper.queryPddPageSizeCount(status, uid);
                if (count2!=0){
                    List<OderPdd> var2 = new ArrayList<>();
                    for (OderPdd oder : list1) {
                        OderPdd var1 = new OderPdd();
                        BeanUtils.copyProperties(list1, var2);
                        Long promotionAmount = oder.getPromotion_amount();
                        Long money = promotionAmount * score / 100;
                        var1.setPromotion_amount(money);
                        var2.add(var1);
                    }
                    data.put("data",var2);
                    data.put("count",count2);
                    return data;
                }
                data.put("data",null);
                data.put("count",0);
                return data;
            case 3:
                break;
        }
        return null;
    }

    @Override
    public JSONObject queryJdOder(Long uid, List status, PageParam pageParam) {
        return null;
    }

    @Override
    public JSONObject queryTbOder(Long uid, List status, PageParam pageParam) {
        return null;
    }

    @Override
    public void saveOder(String id) {

    }

    @Override
    public void queryJdOder(String id) {

    }

    @Override
    public List<Oder> queryPddOderListToId(@NonNull String id, @NonNull Integer status, @NonNull Integer sort) {
        if (sort == 0) {
            var pddoder = oderMapper.queryPddOderList(id, status, "asc");
        }
        if (sort == 1) {
            var pddoder = oderMapper.queryPddOderList(id, status, "desc");
        }
        return null;
    }

    @Override
    public Integer countPddOderForId(String id) {
        return oderMapper.selectPid(id);
    }

    /**
     * 根据列表PID查询计算订单的总收入
     *
     * @param list
     * @return
     */
    @Override
    public Integer countPddOderForIdList(List<Userinfo> list, Integer flag) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        Integer pddMoney = 0;
        Integer tbMoney = 0;
        List<String> pddList = new ArrayList<>(30);
        List<Long> tbList = new ArrayList<>(30);
//        List<String> jdList=new ArrayList<>(30);
        for (int i = 0; i < list.size(); i++) {
            pddList.add(list.get(i).getPddpid());
            tbList.add(list.get(i).getTbpid());
//            jdList.add(list.get(i).getPddpid());
        }
        if (flag == 0) {
            pddMoney = oderMapper.selectPidIn(pddList);
            tbMoney = tboderMapper.selectPidIn(tbList);
        }
        if (flag == 1) {
            pddMoney = oderMapper.selectPidInFinish(pddList);
            tbMoney = tboderMapper.selectPidInFinish(tbList);
        }
//        Integer pddMoney = oderMapper.selectPidIn(list);
        pddMoney = pddMoney == null ? 0 : pddMoney;
        tbMoney = tbMoney == null ? 0 : tbMoney;
        return pddMoney + tbMoney;
    }

    @Override
    public void queryTbOder(@NonNull String id) {
//        String clientId = "your clientId";
//        String clientSecret = "your clientSecret";
//        String code = "your code";
//        String accessToken = "your accessToken";
//        String refreshToken = "your refreshToken";
//        PopHttpClient client = new PopHttpClient("http://zeus-api.order.a.test.yiran.com/api/router", clientId, clientSecret);
//        PddOrderListGetRequest request = new PddOrderListGetRequest();
//        request.setAccessToken(accessToken);
//        request.setRefundStatus(1);
//        request.setOrderStatus(1);
//        request.setStartConfirmAt(1538040111L);
//        request.setEndConfirmAt(1538050447L);
//        request.setPage(1);
//        request.setPageSize(1);
//
//        try {
//            PddOrderListGetResponse response = (PddOrderListGetResponse)client.syncInvoke(request);
//            client.generateAccessToken(code);
//            client.refreshAccessToken(refreshToken);
//        } catch (Exception var11) {
//            System.out.println(var11);
//        }


    }

    @Override
    public List<Oder> coutOderMoneyForTime(List<String> pid, Long star, Long end) {
        return oderMapper.selectPidInOderTime(pid, star, end);
    }

    @Override
    public Long superQueryOderForUidList(List<Long> uidList, Integer status) {
        Long money = 0l;
        if (status == 0) {
            money = oderMapper.superQueryForListToWait(uidList);
        }
        if (status == 1) {
            money = oderMapper.superQueryForListToFinsh(uidList);
        }
        return money;
    }

}
