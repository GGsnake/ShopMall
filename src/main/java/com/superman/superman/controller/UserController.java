package com.superman.superman.controller;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.req.UpdateWxOpenId;
import com.superman.superman.annotation.LoginRequired;
import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.model.TokenModel;
import com.superman.superman.model.Userinfo;
import com.superman.superman.req.BindWxToUser;
import com.superman.superman.req.UserRegiser;
import com.superman.superman.service.LogService;
import com.superman.superman.service.TokenService;
import com.superman.superman.service.UserApiService;
import com.superman.superman.utils.*;
import com.superman.superman.utils.sms.SmsSendResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by liujupeng on 2018/11/6.
 */
@CrossOrigin(origins = "*")
@RestController
public class UserController {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserApiService userServiceApi;
    @Autowired
    private LogService logService;
    @Autowired
    private UserinfoMapper userinfoMapper;


    /**
     * 通过手机号登录
     *
     * @param request
     * @param phone
     * @param validate
     * @return
     */
    @PostMapping("/loginUser")
    public WeikeResponse loginUser(HttpServletRequest request, String phone, String validate) {
        if (phone == null || validate == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        }
        Integer code = (Integer) redisTemplate.opsForValue().get(Constants.SMS_LOGIN + phone);

        if (code == null || !validate.equals(code.toString())) {
            return WeikeResponseUtil.fail("1000134", "验证码错误");
        }
        Userinfo user = userinfoMapper.selectByPhone(phone);
        if (user == null) {
            UserRegiser re = new UserRegiser();
            re.setRoleId(3);
            re.setUserphone(phone);
            Boolean flag = userServiceApi.createUser(re);
            if (!flag) {
                return WeikeResponseUtil.fail("1000142", "创建用户失败 请重试");
            }
            user = userinfoMapper.selectByPhone(phone);
            if (user == null) {
                return WeikeResponseUtil.fail("1000142", "创建用户失败 请重试");
            }
        }
        Long id = user.getId();
        String wxopenid = user.getWxopenid();
        String userRid = user.getRid();
        if (wxopenid == null) {
            JSONObject data = new JSONObject();
            String vai = "phone_token:" + UUID.randomUUID();
            redisTemplate.opsForValue().set(vai, phone);
            redisTemplate.expire(vai, 600, TimeUnit.SECONDS);
            data.put("message", "未绑定微信号 请绑定");
            data.put("phone_token", vai);
            return WeikeResponseUtil.success(data);
        }
        if (userRid==null){
            String redirect_uri = userServiceApi.relationBak(user);
            JSONObject data = new JSONObject();
            data.put("message", "请绑定淘宝渠道");
            data.put("url",redirect_uri);
            return WeikeResponseUtil.success(data);
        }
        //异步上报登录记录
        logService.addUserLoginLog(id, request.getRemoteAddr());
        //生成一个token，保存用户登录状态
        TokenModel model = tokenService.createToken(id.toString());
        return WeikeResponseUtil.success(model);
    }


    /**
     * 通过微信登陆
     *
     * @return
     */
    @PostMapping("/wxLogin")
    public WeikeResponse LoginWX(String wx, HttpServletRequest request) {
        Userinfo userinfo = userServiceApi.queryByWx(wx);
        if (userinfo== null || userinfo.getUserphone() == null) {
            return WeikeResponseUtil.fail("1000124", "请先关联您的手机号");
        }
        if (userinfo.getRid()==null){
            String redirect_uri = userServiceApi.relationBak(userinfo);
            JSONObject data = new JSONObject();
            data.put("message", "请绑定淘宝渠道");
            data.put("url",redirect_uri);
            return WeikeResponseUtil.success(data);
        }
        //异步上报登录
        logService.addUserLoginLog(userinfo.getId(), request.getRemoteAddr());
        //生成一个token，保存用户登录状态
        return WeikeResponseUtil.success(tokenService.createToken(userinfo.getId().toString()));
    }

    /**
     * 绑定微信
     *
     * @param bindWxToUser
     * @return
     */
    @PostMapping("/bindWx")
    public WeikeResponse bindWx(BindWxToUser bindWxToUser, HttpServletRequest request) {
        if (bindWxToUser.isNone()) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        }
        //查询该微信号是否授权过
        Userinfo user = userServiceApi.queryByWx(bindWxToUser.getWx());
        if (user != null) {
            return WeikeResponseUtil.fail("1000125", "微信号已经有关联的手机");
        }

        Boolean isVaild = redisTemplate.hasKey(bindWxToUser.getToken());
        if (!isVaild) {
            return WeikeResponseUtil.fail("1000129", "请先登录或注册手机号");
        }
        String phone = (String) redisTemplate.opsForValue().get(bindWxToUser.getToken());
        Userinfo var = userServiceApi.queryUserByPhone(phone);
        if (var == null) {
            return WeikeResponseUtil.fail("1000126", "该手机账号不存在请先注册");
        }
        if (var.getWxopenid() != null) {
            return WeikeResponseUtil.fail("1000128", "该手机账号已经绑定微信");
        }

        UpdateWxOpenId temp = new UpdateWxOpenId();
        temp.setName(bindWxToUser.getNickname());
        temp.setId(bindWxToUser.getWx());
        temp.setPhoto(bindWxToUser.getHeadimgurl());
        temp.setPhone(phone);

        Integer flag = userinfoMapper.updateUserWxOpenId(temp);
        if (flag == 0) {
            return WeikeResponseUtil.fail("1000127", "绑定手机号失败");
        }
        if (var.getRid()==null){
            String redirect_uri = userServiceApi.relationBak(var);
            JSONObject data = new JSONObject();
            data.put("message", "请继续绑定淘宝渠道");
            data.put("url",redirect_uri);
            return WeikeResponseUtil.success(data);
        }
        logService.addUserLoginLog(var.getId(), request.getRemoteAddr());
        //生成一个token，保存用户登录状态
        TokenModel model = tokenService.createToken(var.getId().toString());

        return WeikeResponseUtil.success(model);
    }

    /**
     * 短信发送模块
     *
     * @param phone
     * @return
     */
    @PostMapping("/sendSMS")
    public WeikeResponse sendSMS(String phone) {
        if (phone == null || !EveryUtils.isMobile(phone)) {
            return WeikeResponseUtil.fail("1000240", "请输入正确的手机号");
        }
        String vaild = Constants.SMS_LOGIN + phone;
        if (redisTemplate.hasKey(vaild)) {
            return WeikeResponseUtil.fail("1000241", "短信发送间隔太快，请稍后");
        }
        int code = (int) ((Math.random() * 9 + 1) * 100000);
        SmsSendResponse result = SmsSendDemo.getSms(phone, String.valueOf(code));
        if (result.getCode().equals("0")) {
            redisTemplate.opsForValue().set(vaild, code);
            redisTemplate.expire(vaild, 120, TimeUnit.SECONDS);
            return WeikeResponseUtil.success("验证码发送成功");
        }
        return WeikeResponseUtil.fail("1000242", "短信商未知错误");
    }


}
