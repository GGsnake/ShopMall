//package com.superman.superman;
//
//import com.superman.superman.service.MemberService;
//import com.superman.superman.service.OderService;
//import com.superman.superman.utils.EveryUtils;
//import lombok.extern.java.Log;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.HashSet;
//
//@Log
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class MemberServiceTest {
//    @Autowired
//    MemberService memberService;
//    @Autowired
//    OderService oderService;
//    @Test
//    public void getMyMoney(){
//        HashSet<Long> uidSet = new HashSet<>();
//        uidSet.add(65l);
//        Long aLong = oderService.superQueryOderForUidList(EveryUtils.setToList(uidSet), 1);
//        log.warning(aLong.toString());
//    }
//}
