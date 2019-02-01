//package com.superman.superman;
//
//import com.superman.superman.service.ScoreService;
//import com.superman.superman.utils.SmsSendDemo;
//import com.superman.superman.utils.sms.SmsSendResponse;
//import lombok.extern.java.Log;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@Log
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class ScoreTest {
//    @Autowired
//    ScoreService scoreService;
//    @Test
//    public void cash(){
//        Boolean aBoolean = scoreService.scoreToCash(Long.valueOf(38));
//        log.warning(aBoolean.toString());
//    }
//    @Test
//    public void phone(){
//        SmsSendResponse sms = SmsSendDemo.getSms("13692939345","666663");
//        log.warning(sms.toString());
//    }
//}
