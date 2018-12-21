//package com.superman.superman;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.superman.superman.dao.AgentDao;
//import com.superman.superman.dao.AllDevOderMapper;
//import com.superman.superman.dao.OderMapper;
//import com.superman.superman.dao.UserinfoMapper;
//import com.superman.superman.model.*;
//import com.superman.superman.req.InvCode;
//import com.superman.superman.req.OderPdd;
//import com.superman.superman.service.*;
//import com.superman.superman.utils.EveryUtils;
//import com.superman.superman.utils.HttpDeal;
//import com.superman.superman.utils.PageParam;
//import com.taobao.api.ApiException;
//import com.taobao.api.DefaultTaobaoClient;
//import com.taobao.api.TaobaoClient;
//import com.taobao.api.domain.NTbkItem;
//import com.taobao.api.request.TbkItemGetRequest;
//import com.taobao.api.request.TbkUatmFavoritesGetRequest;
//import com.taobao.api.response.TbkItemGetResponse;
//import com.taobao.api.response.TbkUatmFavoritesGetResponse;
//import lombok.extern.java.Log;
//import lombok.var;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.util.ClassUtils;
//
//import javax.imageio.ImageIO;
//import javax.imageio.stream.FileImageOutputStream;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.math.BigInteger;
//import java.net.MalformedURLException;
//import java.net.URISyntaxException;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by liujupeng on 2018/11/15.
// */
//@Log
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class Supermaests {
//
////    @Autowired
////    ScoreService scoreService;
////    @Autowired
////    CollectService collectService;
//    @Autowired
//    UserinfoMapper userinfoMapper;
//    @Autowired
//    AllDevOderMapper allDevOderMapper;
//    @Autowired
//    MoneyService moneyService;
//    @Autowired
//    MemberService memberService;
//    @Autowired
//    LogService logService;
//    @Autowired
//    OderService oderService;
//    @Autowired
//    TaoBaoApiService taoBaoApiService;
//    @Autowired
//    OtherService otherService;
//    @Autowired
//    OderMapper oderMapper;
////    @Autowired
////    AgentDao agentDao;
//    public static void main(String[] args){
//     System.out.println(String.valueOf(EveryUtils.getEnd()));
//    }
//    @Test
//    public void test() throws Exception {
//        final String url = "http://gw.api.taobao.com/router/rest";
//        final String appkey = "25338125";
//        final String secret = "c4d36be247e477a9d88704f022e1c514";
//
////        var li=new ArrayList<>();
////        li.add("4165519_37896294");
////
////        Integer integer = oderMapper.selectPidInOderTime(li, 1542435600l, 1542435710l);
////        log.warning(String.valueOf(integer));
//        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
//        TbkUatmFavoritesGetRequest req = new TbkUatmFavoritesGetRequest();
//        req.setPageNo(1L);
//        req.setPageSize(20L);
//        req.setFields("favorites_title,favorites_id,type");
//        req.setType(1l);
//        TbkUatmFavoritesGetResponse rsp = client.execute(req);
//        System.out.println(rsp.getBody());
////        JSONObject jsonObject = memberService.queryMemberDetail(4l);
////        log.warning(jsonObject.toJSONString());
////        var a=new ArrayList<>();
////        a.add(1);
////        a.add(2);
////        a.add(3);
////        Agent agent = new Agent();
////        agent.setAgentId(33);
////        agent.setUserId(113);
////        agentDao.insert(agent);
////        var a=new UserLog();
////        a.setUserId(22);
////        a.setOperation(0);
////        a.setIp("111111111111111111111");
////        logService.addUserLoginLog(a);
////        List<Userinfo> userinfos = userinfoMapper.selectIn(a);
////        log.warning(userinfos.toString());
////        for (long i=0;i<20;i++){
////            scoreService.recordBrowse(33l,i);
////        }
//
////        Long aLong = scoreService.countLooks(33l);
////        Long s = scoreService.countLooks(33l);
////        ScoreBean scoreBean1 = new ScoreBean();
////        scoreBean1.setUserId(122l);
////        scoreBean1.setScoreType(0);
////        scoreBean1.setScore(20002222l);
////        jdApiService.queryJdOder(scoreBean1);
//
//    }
//    @Test
//    public void pddoder(){
////        JSONObject jsonObject = taoBaoApiService.serachGoods(6l,"ab", null, null, 1l, 10l, null, null);
////
////        log.warning(jsonObject.toJSONString());
////        String clientId = "bbc1737d63e44e278dbffa9e96a7eca3";
////        String clientSecret = "5e1a03eb561bac0c63c5efc8c1472119fc3ad405";
////        PopHttpClient client = new PopHttpClient("https://gw-api.pinduoduo.com/api/router", clientId, clientSecret);
////        PddDdkOrderListIncrementGetRequest request = new PddDdkOrderListIncrementGetRequest();
////        request.setEndUpdateTime(1542380831l);
////        request.setStartUpdateTime(1542298031l);
////        request.setPage(1);
////        request.setPageSize(10);
////
////        try {
////            PddDdkOrderListIncrementGetResponse response = client.syncInvoke(request);
////            System.out.println(response.getOrderListGetResponse().getOrderList().get(0).getGoodsName());
////
////        } catch (Exception var11) {
////            System.out.println(var11);
////        }
//
////        JSONObject myMoney = memberService.getMyMoney(1l);
////        JSONObject myMoney = memberService.getMyNoFans(6l,gp);
////        Integer integer = oderMapper.sumMoneyForIdToScore(0.2d,"4165519_37497222");
////        log.warning(myMoney.toJSONString());
////        List<Oder> oders = oderService.queryPddOderListToId("4165519_37497333",-1);
////        List<Oder> oders = oderService.queryPddOderListToId("4165519_37497333");
////        List<Oder> oders = oderService.queryPddOderListToId("4165519_37497333");
//    }
//    @Test
//    public void sum(){
////        JSONObject jsonObject = memberService.getMyMoney(5l);
////        log.warning(jsonObject.toJSONString());
////        JSONObject jsonObject = taoBaoApiService.serachGoods(6l,"蛇皮", null, null, 1l, 20l, "tk_rate_des", null);
////        log.warning(jsonObject.toJSONString());
////        String clientId = "bbc1737d63e44e278dbffa9e96a7eca3";
////        String clientSecret = "5e1a03eb561bac0c63c5efc8c1472119fc3ad405";
////        PopHttpClient client = new PopHttpClient("https://gw-api.pinduoduo.com/api/router", clientId, clientSecret);
////        PddDdkOrderListIncrementGetRequest request = new PddDdkOrderListIncrementGetRequest();
////        request.setEndUpdateTime(1542380831l);
////        request.setStartUpdateTime(1542298031l);
////        request.setPage(1);
////        request.setPageSize(10);
////
////        try {
////            PddDdkOrderListIncrementGetResponse response = client.syncInvoke(request);
////            System.out.println(response.getOrderListGetResponse().getOrderList().get(0).getGoodsName());
////
////        } catch (Exception var11) {
////            System.out.println(var11);
////        }
//
////        JSONObject myMoney = memberService.getMyMoney(1l);
////        JSONObject myMoney = memberService.getMyNoFans(6l,gp);
////        Integer integer = oderMapper.sumMoneyForIdToScore(0.2d,"4165519_37497222");
////        log.warning(myMoney.toJSONString());
////        List<Oder> oders = oderService.queryPddOderListToId("4165519_37497333",-1);
////        List<Oder> oders = oderService.queryPddOderListToId("4165519_37497333");
////        List<Oder> oders = oderService.queryPddOderListToId("4165519_37497333");
//    }
//    @Autowired UserApiService userApiService;
//    @Test
//    public void queryFinishMoney(){
//        List<OderPdd> integer = allDevOderMapper.queryPddPageSize(null,6l,0,10);
//        log.warning(integer.toString());
//    }
//    @Test
//    public void conver(){
//        JSONObject aLong = taoBaoApiService.convertTaobao(73613400232l,583856228119l);
//        log.warning(String.valueOf(aLong));
//    }
//    @Test
//    public void netthread(){
//        new Thread("线程1"){
//            @Override
//            public void run(){
//                System.out.println("我开始了1");
//                System.out.println(userApiService.createTbPid());
//            }
//        }.start();
//        new Thread("线程2"){
//            @Override
//            public void run(){
//                System.out.println("我开始了22");
//                System.out.println(userApiService.createTbPid());
//            }
//        }.start();
//    }
//    @Test
//    public void count() throws IOException, URISyntaxException {
////        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"/static/";
////        BufferedImage img = otherService.crateQRCode("http://www.baidu.com");
////        File writeName = new File(path+"/img/21321.jpg");
////        FileImageOutputStream fileImageOutputStream=new FileImageOutputStream(writeName);
////        ImageIO.write(img, "JPEG",fileImageOutputStream);
////        ByteArrayOutputStream s = otherService.crateQRCode("http://www.baidu.com");
////        ByteArrayOutputStream
////        HttpDeal.saveImageToDisk(QRCODEURL + "app=qr.get&data=www.baidu.com",232l);
////        log.warning(s.toString());
////        List a=new ArrayList();
////
////        JSONObject aLong = taoBaoApiService.countWaitTb();
//    }
//
//
//
//}
