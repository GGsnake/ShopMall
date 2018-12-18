package com.superman.superman.controller;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.annotation.LoginRequired;
import com.superman.superman.model.Userinfo;
import com.superman.superman.service.*;
import com.superman.superman.utils.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private PddApiService pddApiService;

    @Autowired
    private JdApiService jdApiService;

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
            if (uland_url==null){
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
            if (uland_url==null){
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
    public void qrcode(HttpServletResponse response, HttpServletRequest request) throws IOException {
//        String realPath = request.getServletContext().getRealPath("/static/img");
//        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"/static/";
        ByteArrayOutputStream img = otherService.crateQRCode("http://www.baidu.com");
//        File writeName = new File(realPath+"/21321.jpg");
//        FileImageOutputStream fileImageOutputStream=new FileImageOutputStream(writeName);
//        ImageIO.write(img, "JPEG",fileImageOutputStream);
        String upload = EveryUtils.upload(img.toByteArray(), "qrcode/", ".png");
        log.warning(upload);

    }
}
