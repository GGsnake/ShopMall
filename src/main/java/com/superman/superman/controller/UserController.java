package com.superman.superman.controller;

import com.superman.superman.service.LogService;
import com.superman.superman.service.TokenService;
import com.superman.superman.service.UserService;
import com.superman.superman.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * Created by snake on 2018/11/6.
 */
@CrossOrigin(origins = "*")
@RestController
public class UserController {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserService userServiceApi;
    @Autowired
    private LogService logService;
//    /**
//     * 通过手机号登录
//     *
//     * @param request
//     * @param phone
//     * @param validate
//     * @return
//     */
//    @PostMapping("/loginUser")
//    public Response loginUser(HttpServletRequest request, String phone, String validate) {
//        if (phone == null || validate == null) {
//            return ResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
//        }
//        Integer code = (Integer) redisTemplate.opsForValue().get(Constants.SMS_LOGIN + phone);
//
//        if (code == null || !validate.equals(code.toString())) {
//            return ResponseUtil.fail("1000134", "验证码错误");
//        }
//        Userinfo user = userinfoMapper.selectByPhone(phone);
//        if (user == null) {
//            UserRegiser re = new UserRegiser();
//            re.setRoleId(3);
//            re.setUserphone(phone);
//            Boolean flag = userServiceApi.createUser(re);
//            if (!flag) {
//                return ResponseUtil.fail("1000142", "创建用户失败 请重试");
//            }
//            user = userinfoMapper.selectByPhone(phone);
//            if (user == null) {
//                return ResponseUtil.fail("1000142", "创建用户失败 请重试");
//            }
//        }
//        Long id = user.getId();
//        String wxopenid = user.getWxopenid();
//        String userRid = user.getRid();
//        if (wxopenid == null) {
//            JSONObject data = new JSONObject();
//            String vai = "phone_token:" + UUID.randomUUID();
//            redisTemplate.opsForValue().set(vai, phone);
//            redisTemplate.expire(vai, 600, TimeUnit.SECONDS);
//            data.put("message", "未绑定微信号 请绑定");
//            data.put("phone_token", vai);
//            return ResponseUtil.success(data);
//        }
//        if (userRid==null){
//            String redirect_uri = userServiceApi.relationBak(user);
//            JSONObject data = new JSONObject();
//            data.put("message", "请绑定淘宝渠道");
//            data.put("url",redirect_uri);
//            return ResponseUtil.success(data);
//        }
//        //异步上报登录记录
//        logService.addUserLoginLog(id, request.getRemoteAddr());
//        //生成一个token，保存用户登录状态
//        TokenModel model = tokenService.createToken(id.toString());
//        return ResponseUtil.success(model);
//    }
//
//
//    /**
//     * 通过微信登陆
//     *
//     * @return
//     */
//    @PostMapping("/wxLogin")
//    public Response LoginWX(String wx, HttpServletRequest request) {
////        Userinfo userinfo = userServiceApi.queryByWx(wx);
////        if (userinfo== null || userinfo.getUserphone() == null) {
////            return ResponseUtil.fail("1000124", "请先关联您的手机号");
////        }
////        if (userinfo.getRid()==null){
////            String redirect_uri = userServiceApi.relationBak(userinfo);
////            JSONObject data = new JSONObject();
////            data.put("message", "请绑定淘宝渠道");
////            data.put("url",redirect_uri);
////            return ResponseUtil.success(data);
////        }
////        //异步上报登录
////        logService.addUserLoginLog(userinfo.getId(), request.getRemoteAddr());
////        //生成一个token，保存用户登录状态
////        return ResponseUtil.success(tokenService.createToken(userinfo.getId().toString()));
//    }


//    /**
//     * 绑定微信
//     *
//     * @param bindWxToUser
//     * @return
//     */
//    @PostMapping("/bindWx")
//    public Response bindWx(BindWxToUser bindWxToUser, HttpServletRequest request) {
//        if (bindWxToUser.isNone()) {
//            return ResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
//        }
//        //查询该微信号是否授权过
//        Userinfo user = userServiceApi.queryByWx(bindWxToUser.getWx());
//        if (user != null) {
//            return ResponseUtil.fail("1000125", "微信号已经有关联的手机");
//        }
//
//        Boolean isVaild = redisTemplate.hasKey(bindWxToUser.getToken());
//        if (!isVaild) {
//            return ResponseUtil.fail("1000129", "请先登录或注册手机号");
//        }
//        String phone = (String) redisTemplate.opsForValue().get(bindWxToUser.getToken());
//        Userinfo var = userServiceApi.queryUserByPhone(phone);
//        if (var == null) {
//            return ResponseUtil.fail("1000126", "该手机账号不存在请先注册");
//        }
//        if (var.getWxopenid() != null) {
//            return ResponseUtil.fail("1000128", "该手机账号已经绑定微信");
//        }
//
//        UpdateWxOpenId temp = new UpdateWxOpenId();
//        temp.setName(bindWxToUser.getNickname());
//        temp.setId(bindWxToUser.getWx());
//        temp.setPhoto(bindWxToUser.getHeadimgurl());
//        temp.setPhone(phone);
//
//        Integer flag = userinfoMapper.updateUserWxOpenId(temp);
//        if (flag == 0) {
//            return ResponseUtil.fail("1000127", "绑定手机号失败");
//        }
//        logService.addUserLoginLog(var.getId(), request.getRemoteAddr());
//        //生成一个token，保存用户登录状态
//        TokenModel model = tokenService.createToken(var.getId().toString());
//
//        return ResponseUtil.success(model);
//    }

    /**
     * 短信发送模块
     *
     * @param phone
     * @return
     */
    @PostMapping("/sendSMS")
    public Response sendSMS(String phone) {
        if (phone == null || !EveryUtils.isMobile(phone)) {
            return ResponseUtil.fail("1000240", "请输入正确的手机号");
        }
        //验证码
        String vaild = Constants.SMS_LOGIN + phone;

        if (redisTemplate.hasKey(vaild)) {
            return ResponseUtil.fail("1000241", "短信发送间隔太快，请稍后");
        }
        int code = (int) ((Math.random() * 9 + 1) * 100000);

        return ResponseUtil.fail("1000242", "短信商未知错误");
    }

}
