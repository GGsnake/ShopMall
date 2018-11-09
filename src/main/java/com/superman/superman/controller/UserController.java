package com.superman.superman.controller;

import com.superman.superman.model.User;
import com.superman.superman.utils.Result;
import org.springframework.web.bind.annotation.*;

/**
 * Created by liujupeng on 2018/11/6.
 */
@RestController
public class UserController {

//    @Autowired
//    private PddApiService pddApiService;
//    @Autowired
//    private UserApiService userServiceApi;
    @GetMapping("/val")
    public void sysoo(){
        System.out.println("sssssssssssssss");
    }

    @PostMapping("/getBillList")
    public Result pddGoodList(@RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
                              @RequestParam(value = "pagesize", defaultValue = "10", required = false) Integer pagesize,
                              @RequestParam(value = "pid", required = false) String pid, @RequestParam(value = "goodlist[]", required = false) Long[] goodlist
                                )
    {
//        userServiceApi.query();

//        String billList = pddApiService.getBillList(pid, page, String.valueOf(pagesize));
//        String s = pddApiService.newPromotion(pid, goodlist);
        return Result.ok(null);
    }

    @RequestMapping("/index")
    public User redisIndex() {
        return null;
    }

//    @PostMapping("/gg")
//    public Result creat(@RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
//                              @RequestParam(value = "pagesize", defaultValue = "10", required = false) Integer pagesize,
//                              @RequestParam(value = "number", required = false) String pid, @RequestParam(value = "goodlist[]", required = false) Long[] goodlist
//    )
//    {
//
//        String s = pddApiService.newBillSingle(1);
//        return Result.ok(s);
//
//
//    }
//    @PostMapping(value = "/createUser")
//    public Result createUser(@RequestParam(value = "mobile") String mobile,
//                             @RequestParam(value = "pwd") String pwd,
//                             @RequestParam(value = "code") String code,
//                             @RequestParam(value = "pid",required = false) String pid){
//        User user=new User();
//        user.setUserPhone(mobile);
//        user.setLoginPwd(pwd);
//        user.setLoginSecret(code);
//        User info = userServiceApi.queryUserByPhone(user);
//        if (info!=null){
//            return Result.error("手机号已注册");
//        }
//        Boolean oprear = userServiceApi.createUser(user);
//        if (oprear){
//            return Result.ok("注册成功");
//        }
////        JSONObject jsonObject = SmsUtil.sendLoginSmsVcode("13692939345");
//        return Result.error("验证码错误");
//    }
//

}
