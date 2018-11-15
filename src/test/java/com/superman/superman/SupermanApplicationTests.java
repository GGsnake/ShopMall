package com.superman.superman;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.PddDao;
import com.superman.superman.model.Dd;
import com.superman.superman.utils.EveryUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SupermanApplicationTests {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private PddDao pddDao;
    private final static Logger logger = LoggerFactory.getLogger(SupermanApplicationTests.class);

    @Test
    public void test()  {
//        String pathname = "D:\\file\\policy1040.html";
//test11(pathname);
        Long day = EveryUtils.getDay();
        System.out.println(day);//        List<Dd> tttttt = pddDao.seletTTTA();
//
//        for (Dd d : tttttt) {
//            stringRedisTemplate.opsForValue().set("Policy:" + d.getId(), JSON.toJSONString(d));
//        }
//        for (Dd d:tttttt)
//        {
//            stringRedisTemplate.opsForValue().set("Policy:"+String.valueOf(d.getId()), JSON.toJSONString(d));
//
//        }


//        for (int i=1;i<100)
//        {
//        List<Dd> tttttt = pddDao.seletTTTA();

//        for (int i=1;i<10000;i++){
//            java.lang.String s = stringRedisTemplate.opsForValue().get("policy:"+i);
////            if (s==null){
////                continue;
////            }
//            JSONObject jsonObject = JSON.parseObject(s);
//            java.lang.String ssd = (java.lang.String) jsonObject.get("inform_content");
//            File file =new File("D:\\file\\policy"+i+".html");
//            Writer out =new FileWriter(file);
//            out.write(ssd);
//            out.close();
//
//        }

//        for (Dd d : tttttt) {
//            stringRedisTemplate.opsForValue().setBit("Policy:" + d.getId(), JSON.toJSONString(d).to,true);
//        }


    }

//
//        String o = (String) redisTemplate.opsForValue().get("Policy:2");
//        logger.info(o.toString());

//            stringRedisTemplate.opsForValue().set("Policy:"+String.valueOf(d.getId()), JSON.toJSONString(d));
//
//        }

//        Assert.assertEquals("111", stringRedisTemplate.opsForValue().get("aaa"));
}

//    @Test
//    public void testObj() throws Exception {

//        ValueOperations<String, String> operations=redisTemplate.opsForValue();
//        operations.set("com.neox", "ssssssssssssssss");
//        operations.set("com.neo.f", "ssssssssssssssssss",1, TimeUnit.SECONDS);
//        Thread.sleep(1000);
//
//        //redisTemplate.delete("com.neo.f");
//        boolean exists=redisTemplate.hasKey("com.neo.f");
//        if(exists){
//            System.out.println("exists is true");
//        }else{
//            System.out.println("exists is false");
//        }
//        // Assert.assertEquals("aa", operations.get("com.neo.f").getUserName());
//    }

