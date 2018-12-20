package com.superman.superman.controller;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.annotation.LoginRequired;
import com.superman.superman.dao.AgentDao;
import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.model.Agent;
import com.superman.superman.model.User;
import com.superman.superman.model.Userinfo;
import com.superman.superman.service.*;
import com.superman.superman.utils.*;
import io.swagger.models.Model;
import io.swagger.models.properties.Property;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Map;

/**
 * Created by liujupeng on 2018/12/17.
 */
@Log
@RestController
@RequestMapping("other")
public class OtherController {
    private static final String QINIUURL = "http://pjx55zb0m.bkt.clouddn.com/";
    private static final String QINIUURLLAST = "http://pjx55zb0m.bkt.clouddn.com";
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
    @Autowired
    private JdApiService jdApiService;
    @Autowired
    private UserinfoMapper userinfoMapper;

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

    @LoginRequired
    @GetMapping("/createCode")
    public WeikeResponse createCode(HttpServletResponse response, HttpServletRequest request) throws IOException {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null)
            return WeikeResponseUtil.fail(ResponseCode.COMMON_USER_NOT_EXIST);
        Integer code = userinfoMapper.queryCodeId(Long.valueOf(uid));
        String imgUrl = otherService.addQrCodeUrlInv(QINIUURLLAST + "?code=" + code, uid);
        return WeikeResponseUtil.success(QINIUURL + imgUrl);
    }

    //    @LoginRequired
    @PostMapping("/queryCode")
    public void queryCode(HttpServletResponse response, HttpServletRequest request, User user, Integer code) throws IOException {
//        return WeikeResponseUtil.success(QINIUURL+agentId);
        String userPhone = user.getUserPhone();
        String loginPwd = user.getLoginPwd();
        Userinfo userinfo=new Userinfo();
        userinfo.setUserphone(userPhone);
        userinfo.setLoginpwd(loginPwd);
        Boolean userByPhone = userApiService.createUserByPhone(userinfo);
        if (userByPhone){
            Userinfo data = userinfoMapper.selectByPhone(userPhone);
            Integer agentId = userinfoMapper.queryUserCode(code.longValue());
            Agent agent=new Agent();
            agent.setUserId(data.getId().intValue());
            agent.setAgentId(agentId);
            agentDao.insert(agent);
        }

    }
//    @LoginRequired

}
