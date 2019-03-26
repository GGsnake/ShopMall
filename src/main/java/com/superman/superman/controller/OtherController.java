package com.superman.superman.controller;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.annotation.LoginRequired;
import com.superman.superman.dao.AgentDao;
import com.superman.superman.dao.ScoreDao;
import com.superman.superman.dao.SettingDao;
import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.model.*;
import com.superman.superman.redis.RedisUtil;
import com.superman.superman.req.UserRegiser;
import com.superman.superman.service.*;
import com.superman.superman.utils.*;
import com.superman.superman.utils.sign.EverySign;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by liujupeng on 2018/12/17.
 */
@CrossOrigin(origins = "*")
@Log
@RestController
@RequestMapping("other")
public class OtherController {
    @Autowired
    private TaoBaoApiService taoBaoApiService;
    @Autowired
    private OtherService otherService;
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private SettingDao settingDao;
    @Autowired
    private AgentDao agentDao;
    @Autowired
    private PddApiService pddApiService;
    @Value("${domain.url}")
    private String DOMAINURL;
    @Value("${domain.codeurl}")
    private String QINIUURLLAST;
    @Value("${domain.qnyurl}")
    private String QINIUURL;
    @Value("${server.port}")
    private Integer port;
    @Autowired
    private JdApiService jdApiService;
    @Autowired
    private UserinfoMapper userinfoMapper;
    @Autowired
    private SysAdviceService adviceService;
    @Autowired
    private ScoreDao scoreDao;

    @Autowired
    private SysDaygoodsService daygoodsService;
    @Autowired
    private SysFriendDtoService sysFriendDtoService;

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserApiService userApiService;
    @Autowired
    private MemberService memberService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 生成推广链接
     *
     * @param request
     * @param goodId
     * @param devId   0天猫淘宝 1拼多多  2京东
     * @return
     * @throws IOException
     */
    @LoginRequired
    @PostMapping("/convert")
    public WeikeResponse convert(HttpServletRequest request, Long goodId, Integer devId, String jdurl, String coupon) throws IOException {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null)
            return WeikeResponseUtil.fail(ResponseCode.COMMON_USER_NOT_EXIST);
        if (goodId == null && jdurl == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        }
        Userinfo userinfo = userinfoMapper.selectByPrimaryKey(Long.valueOf(uid));
        JSONObject data = null;
        //缓存
        String key = "convert:" + devId.toString() + uid + goodId + jdurl;
        if (redisUtil.hasKey(key)) {
            return WeikeResponseUtil.success(JSONObject.parseObject(redisUtil.get(key)));
        }
        String uland_url = null;
        if (devId == 0) {
            data = taoBaoApiService.convertTaobao(userinfo.getTbpid(), goodId);
            if (data == null || data.getString("uland_url") == null) {
                return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
            }
            uland_url = otherService.addQrCodeUrl(data.getString("uland_url"), uid);
            if (uland_url == null) {
                return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
            }
            data.put("qrcode", QINIUURL + uland_url);
        } else if (devId == 1) {
            data = pddApiService.convertPdd(userinfo.getPddpid(), goodId);
            if (data == null || data.getString("uland_url") == null) {
                return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
            }
            uland_url = otherService.addQrCodeUrl(data.getString("uland_url"), uid);
            if (uland_url == null) {
                return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
            }
            data.put("qrcode", QINIUURL + uland_url);
        } else if (devId == 2) {
            data = jdApiService.convertJd(userinfo.getJdpid(), jdurl, coupon);
            if (data == null || data.getString("uland_url") == null) {
                return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
            }
            uland_url = otherService.addQrCodeUrl(data.getString("uland_url"), uid);
            if (uland_url == null) {
                return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
            }
            data.put("qrcode", QINIUURL + uland_url);

        }
        redisUtil.set(key, data.toJSONString());
        redisUtil.expire(key, 200, TimeUnit.SECONDS);
        return WeikeResponseUtil.success(data);
    }

    /**
     * 生成邀请二维码
     *
     * @param request
     * @return
     */
    @LoginRequired
    @GetMapping("/createCode")
    public WeikeResponse createCode(HttpServletRequest request) throws IOException {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null)
            return WeikeResponseUtil.fail(ResponseCode.COMMON_USER_NOT_EXIST);
        Userinfo userinfo = userinfoMapper.selectByPrimaryKey(Long.valueOf(uid));
        if (userinfo == null || userinfo.getRoleId() == 3) {
            return WeikeResponseUtil.fail(ResponseCode.DELETE_ERROR);
        }
        Integer code = userinfoMapper.queryCodeId(userinfo.getId());
        ScoreBean scoreBean = new ScoreBean();
        scoreBean.setDataSrc(4);
        scoreBean.setUserId(userinfo.getId());
        scoreBean.setScoreType(1);
        String shareScore = otherService.querySetting("ShareScore").getConfigValue();
        scoreBean.setDay(EveryUtils.getNowday());
        scoreBean.setScore(Long.valueOf(shareScore));
        if (!scoreService.isShare(userinfo.getId())) {
            scoreDao.addScore(scoreBean);
            userinfo.setUserscore(Integer.valueOf(shareScore));
            scoreDao.updateUserScore(userinfo);
        }
        String codeUrl = otherService.addQrCodeUrlInv(QINIUURLLAST + ":" + port + "/user/index.html?code=" + code, uid);
        return WeikeResponseUtil.success(QINIUURL + codeUrl);
    }

    /**
     * 处理二维码进来的注册用户
     *
     * @param code
     * @return
     */
    @PostMapping("/createUser")
    public WeikeResponse createUser(@RequestParam(value = "phone") String userPhone,
                                    @RequestParam(value = "vaild") String vaild,
                                    @RequestParam(value = "code") String code) {
        Map phone = EverySign.isPhone(userPhone);
        Boolean flag = (Boolean) phone.get("flag");
        if (!flag) {
            return WeikeResponseUtil.fail("1000199", "请填写正确的手机号");
        }
        String key = Constants.SMS_LOGIN + userPhone;
        Integer isVaild = (Integer) redisTemplate.opsForValue().get(key);
        if (isVaild == null || !isVaild.toString().equals(vaild)) {
            return WeikeResponseUtil.fail("1000134", "验证码错误");
        }
        //检测注册手机号是否存在
        Userinfo userinfo = userApiService.queryUserByPhone(userPhone);
        if (userinfo != null) {
            return WeikeResponseUtil.fail("1000199", "手机号已注册");
        }
        Map<String, Object> param = new HashMap<>();
        param.put("userPhone", userPhone);
        param.put("code", code);
        Boolean invitation = userApiService.invitation(param);
        if (invitation) {
            return WeikeResponseUtil.success();
        }
        return WeikeResponseUtil.fail("1000699", "创建用户失败 请重试");

    }

    //每日爆款
    @PostMapping("/dayGoods")
    public WeikeResponse dayGoods(PageParam pageParam) {
        //查询列表数据
        String key = "dayGoods:" + pageParam.getPageNo();
        if (redisUtil.hasKey(key)) {
            return WeikeResponseUtil.success(JSONObject.parseObject(redisUtil.get(key)));
        }
        PageParam param = new PageParam(pageParam.getPageNo(), pageParam.getPageSize());
        JSONObject data = daygoodsService.queryList(param);
        redisUtil.set(key, data.toJSONString());
        redisUtil.expire(key, 50, TimeUnit.SECONDS);
        return WeikeResponseUtil.success(data);
    }

    //朋友圈图片
    @PostMapping("/friend")
    public WeikeResponse friend(PageParam pageParam) {
        //查询列表数据
        String key = "friend:" + pageParam.getPageNo();
        if (redisUtil.hasKey(key)) {
            return WeikeResponseUtil.success(JSONObject.parseObject(redisUtil.get(key)));
        }
        PageParam param = new PageParam(pageParam.getPageNo(), pageParam.getPageSize());
        JSONObject data = sysFriendDtoService.queryList(param);
        redisUtil.set(key, data.toJSONString());
        redisUtil.expire(key, 50, TimeUnit.SECONDS);
        return WeikeResponseUtil.success(data);
    }

    /**
     * 查询订单通知
     */
    @LoginRequired
    @PostMapping("/oderAdvice")
    public WeikeResponse querySysAdviceOder(HttpServletRequest request, PageParam pageParam) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_USER_NOT_EXIST);
        }
        String key = "oderAdvice:" + uid + pageParam.getPageNo();
        if (redisUtil.hasKey(key)) {
            return WeikeResponseUtil.success(JSONObject.parseObject(redisUtil.get(key)));
        }
        PageParam param = new PageParam(pageParam.getPageNo(), pageParam.getPageSize());
        List<SysJhAdviceOder> total = adviceService.queryListOderAdvice(Long.valueOf(uid), param);
        Integer sum = adviceService.countListOderAdvice(Long.valueOf(uid));
        JSONObject data = new JSONObject();
        data.put("pageData", total);
        data.put("pageCount", sum);
        int expire = 6;
        Config wxAccount = otherService.querySetting("OrderAdvice");
        if (wxAccount != null) {
            expire = Integer.parseInt(wxAccount.getConfigValue());
        }
        redisUtil.set(key, data.toJSONString());
        redisUtil.expire(key, expire, TimeUnit.SECONDS);
        return WeikeResponseUtil.success(data);
    }

    /**
     * 首页轮播图
     */
    @GetMapping("/banner")
    public WeikeResponse banner() {
        List<String> data = settingDao.queryBanner();
        return WeikeResponseUtil.success(data);
    }

    /**
     * 对外接口
     * show
     */
    @GetMapping("/show/{name}")
    public WeikeResponse show(@PathVariable("name") Integer id) {
        String sufix = "show:" + id;
        if (redisUtil.hasKey(sufix)) {
            return WeikeResponseUtil.success(redisUtil.get(sufix));
        }
        JSONObject data = memberService.queryMemberDetail(Long.valueOf(id));
        redisUtil.set(sufix, data.toString());
        redisUtil.expire(sufix, 10, TimeUnit.SECONDS);
        return WeikeResponseUtil.success(data);
    }

    /**
     * 对外接口
     * money
     */
    @GetMapping("/money/{name}")
    public WeikeResponse money(@PathVariable("name") Integer id) {
        String sufix = "money:" + id;
        if (redisUtil.hasKey(sufix)) {
            return WeikeResponseUtil.success(redisUtil.get(sufix));
        }
        JSONObject data = memberService.getMyMoney(Long.valueOf(id));
        redisUtil.set(sufix, data.toString());
        redisUtil.expire(sufix, 6, TimeUnit.SECONDS);
        return WeikeResponseUtil.success(data);
    }

}
