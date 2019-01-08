package com.superman.superman.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.annotation.LoginRequired;
import com.superman.superman.dao.AgentDao;
import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.model.Agent;
import com.superman.superman.model.SysDaygoods;
import com.superman.superman.model.User;
import com.superman.superman.model.Userinfo;
import com.superman.superman.service.*;
import com.superman.superman.utils.*;
import io.swagger.models.Model;
import io.swagger.models.properties.Property;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * Created by liujupeng on 2018/12/17.
 */
@Log
@RestController
@RequestMapping("other")
public class OtherController {
    private static final String QINIUURL = "http://pjx55zb0m.bkt.clouddn.com/";
    @Autowired
    private TaoBaoApiService taoBaoApiService;
    @Autowired
    private OtherService otherService;
    @Autowired
    private AgentDao agentDao;
    @Autowired
    private PddApiService pddApiService;
    @Value("${domain.url}")
    private String DOMAINURL;
    @Value("${domain.codeurl}")
    private String QINIUURLLAST;
    @Value("${server.port}")
    private Integer port;
    @Autowired
    private JdApiService jdApiService;
    @Autowired
    private UserinfoMapper userinfoMapper;

    @Autowired
    private SysDaygoodsService daygoodsService;

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserApiService userApiService;

    @LoginRequired
    @PostMapping("/convert")
    public WeikeResponse convert(HttpServletRequest request, Long goodId, Integer devId) throws IOException, URISyntaxException {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null)
            return WeikeResponseUtil.fail(ResponseCode.COMMON_USER_NOT_EXIST);
        if (goodId == null)
            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        Userinfo userinfo = userApiService.queryByUid(Long.valueOf(uid));
        String pddpid = userinfo.getPddpid();
        Long tbpid = userinfo.getTbpid();
        String jdpid = userinfo.getJdpid();
        JSONObject data = new JSONObject();
        if (devId == 0) {
            data = taoBaoApiService.convertTaobao(tbpid, goodId);
            if (data == null || data.getString("uland_url") == null) {
                return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
            }
            String uland_url = otherService.addQrCodeUrl(data.getString("uland_url"), uid);
            if (uland_url == null) {
                return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
            }
            data.put("qrcode", QINIUURL + uland_url);

        }

        if (devId == 1) {
            data = pddApiService.convertPdd(pddpid, goodId);
            if (data == null || data.getString("uland_url") == null) {
                return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
            }
            String uland_url = otherService.addQrCodeUrl(data.getString("uland_url"), uid);
            if (uland_url == null) {
                return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
            }
            data.put("qrcode", QINIUURL + uland_url);
        }

        if (devId == 2)
            data = jdApiService.convertJd(goodId, Long.valueOf(tbpid));

        if (devId == 3)
            data = taoBaoApiService.convertTaobao(goodId, Long.valueOf(tbpid));
        return WeikeResponseUtil.success(data);
    }


    @GetMapping("/qrcode")
    public WeikeResponse qrcode(HttpServletResponse response, HttpServletRequest request) throws IOException {
        ByteArrayOutputStream img = otherService.crateQRCode("http://www.baidu.com");
        String upload = EveryUtils.upload(img.toByteArray(), "qrcode/", ".png");
        log.warning(upload);
        return null;

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
        Integer code = userinfoMapper.queryCodeId(Long.valueOf(uid));
        Long add = redisTemplate.opsForSet().add(Constants.INV_LOG, Constants.INV_LOG + EveryUtils.getNowday() + ":" + uid);
        String codeUrl = otherService.addQrCodeUrlInv(QINIUURLLAST + ":" + port + "/queryCodeUrl?code=" + code, uid);
        return WeikeResponseUtil.success(QINIUURL + codeUrl);
    }

    /**
     * 处理二维码
     *
     * @param user
     * @param code
     * @return
     */
    @PostMapping("/queryCode")
    public String queryCode(User user, Integer code) {
        String userPhone = user.getUserPhone();

        String loginPwd = user.getLoginPwd();
        if (loginPwd == null || userPhone == null) {
            return "addUserError";
        }
        Map phone = EverySign.isPhone(userPhone);
        Boolean flag = (Boolean) phone.get("flag");
        if (!flag) {
            return "addUserError";
        }
        Userinfo userinfo = new Userinfo();
        userinfo.setUserphone(userPhone);
        userinfo.setLoginpwd(loginPwd);
        Boolean userByPhone = userApiService.createUserByPhone(userinfo);
        if (userByPhone == false) {
            return "addUserError";
        }
        //获取用户Id
        Userinfo data = userinfoMapper.selectByPhone(userPhone);
        //根据邀请码查询代理的Id
        Integer agentId = userinfoMapper.queryUserCode(code.longValue());
        Agent agent = new Agent();
        agent.setUserId(data.getId().intValue());
        agent.setAgentId(agentId);
        agentDao.insert(agent);
        return "addUserSuccess";

    }

    //    @LoginRequired
    @GetMapping("/dayGoods")
    public WeikeResponse dayGoods(PageParam pageParam) {
        //查询列表数据
        PageParam param = new PageParam(pageParam.getPageNo(), pageParam.getPageSize());
        JSONArray daygoodsList = daygoodsService.queryList(param);
        Integer total = daygoodsService.queryTotal();
        JSONObject data = new JSONObject();
        data.put("pageData", daygoodsList);
        data.put("pageCount", total);
        return WeikeResponseUtil.success(data);
    }

}
