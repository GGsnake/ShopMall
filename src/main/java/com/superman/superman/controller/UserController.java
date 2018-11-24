package com.superman.superman.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.annotation.LoginRequired;
import com.superman.superman.dao.OderMapper;
import com.superman.superman.model.TokenModel;
import com.superman.superman.model.User;
import com.superman.superman.model.Userinfo;
import com.superman.superman.service.LogService;
import com.superman.superman.service.TokenService;
import com.superman.superman.service.UserApiService;
import com.superman.superman.utils.*;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by liujupeng on 2018/11/6.
 */
@RestController
public class UserController {

    //    @Autowired
//    private PddApiService pddApiService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserApiService userServiceApi;

    @Autowired
    private LogService logService;


    @PostMapping("/getBillList")
    public Result pddGoodList(@RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
                              @RequestParam(value = "pagesize", defaultValue = "10", required = false) Integer pagesize,
                              @RequestParam(value = "pid", required = false) String pid, @RequestParam(value = "goodlist[]", required = false) Long[] goodlist
    ) {
//        userServiceApi.query();

//        String billList = pddApiService.getBillList(pid, page, String.valueOf(pagesize));
//        String s = pddApiService.newPromotion(pid, goodlist);
        return Result.ok(null);
    }

    @LoginRequired
    @PostMapping("/index")
    public User redisIndex(HttpServletRequest request) {
        request.getHeader("token");

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
    @PostMapping(value = "/createUser")
    public Result createUser(@RequestParam(value = "mobile") String mobile,
                             @RequestParam(value = "pwd") String pwd,
//                             @RequestParam(value = "code") String code,
                             @RequestParam(value = "pid", required = false) String pid) {

        Userinfo user = new Userinfo();
        user.setUserphone(mobile);
        user.setLoginpwd(pwd);
//        user.set(code);
        Userinfo info = userServiceApi.queryUserByPhone(user.getUserphone());
        if (info != null) {
            return Result.error("手机号已注册");
        }
        Boolean oprear = userServiceApi.createUser(user);
        if (oprear) {
            return Result.ok("注册成功");
        }
//        JSONObject jsonObject = SmsUtil.sendLoginSmsVcode("13692939345");
        return Result.error("验证码错误");
    }

    @PostMapping("/login")
    public Object Login(HttpServletRequest request,@RequestBody Map<String, Object> reqMap) {
        String userName = RequestUtil.getMapString(reqMap.get("user_name").toString());
        String passWord = RequestUtil.getMapString(reqMap.get("pass_word").toString());
        //判断用户名是否存在
        Userinfo user = userServiceApi.queryUserByPhone(userName);
        if (user == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_USER_NOT_EXIST);

        }

        //获取数据库中的密码，与输入的密码加密后比对
        if (!DigestUtils.md5DigestAsHex(passWord.getBytes()).equals(user.getLoginpwd())) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_USER_PASSWORD_ERROR);
        }
        //异步上报登录记录
        logService.addUserLoginLog(user.getId(),request.getRemoteAddr());
        //生成一个token，保存用户登录状态
        TokenModel model = tokenService.createToken(String.valueOf(user.getId()));

        return WeikeResponseUtil.success(model);
    }
    /**
     * 通过wx登陆
     *
     * @param reqMap
     * @return
     */
//    @PostMapping("/wx/login")
//    public Object LoginWX(@RequestBody Map<String, Object> reqMap) {
//        String code = RequestUtil.getMapString(reqMap.get("wx_code").toString());
//        //微信接口
//        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + Constants.APPID +
//                "&secret=" + Constants.SECRET + "&js_code=" + code + "&grant_type=authorization_code";
//        //restTemplate请求微信的接口，获取微信的sessionId
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET,
//                null, String.class);
//        if (responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK) {
//            String response = responseEntity.getBody();
//            /*
//            //string转jsonObject,
//            正常返回的JSON数据包{"openid": "OPENID","session_key": "SESSIONKEY"}
//             */
//            JSONObject responseObject = JSONObject.parseObject(response);
//            String wxOpenId = responseObject.get("openid").toString();
//            String sessionKey = responseObject.get("session_key").toString();
//            User user = userServer.findUserByWxId(wxOpenId);
//            //wxOpenId与id都不存在则创建一个新用户
//            if (user == null) {
//                UserDto userDto = userServer.autoRegisterUser(wxOpenId);
//                //生成一个token，保存用户登录状态
//                TokenModel model = tokenManager.createToken(userDto.getUser().getId());
//                return ResultUtil.ok(model);
//            }else {
//                //生成一个token，保存用户登录状态
//                TokenModel model = tokenManager.createToken(user.getId());
//                return ResultUtil.ok(model);
//            }
//        }
//        return ResultUtil.fail();
//    }


}
