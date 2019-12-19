package com.superman.superman.controller;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.annotation.LoginRequired;
import com.superman.superman.dao.PayDao;
import com.superman.superman.service.OtherService;
import com.superman.superman.utils.*;
import lombok.extern.java.Log;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liujupeng on 2019/1/11.
 */
@Log
@RestController
@RequestMapping("/wx")
public class PayController {
    @Autowired
    private OtherService otherService;
    @Autowired
    private PayDao payDao;
    /**
     * 微信预支付（技能开通支付）
     *
     * @param request
     * @throws IOException
     */
    @LoginRequired
    @RequestMapping("/wechatOrderPay")
    public Response wechatOrderPay(HttpServletRequest request) throws IOException {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid != null) {
            request.setCharacterEncoding("utf-8");
            JSONObject data = otherService.payMoney(uid, request.getRemoteAddr());
            return ResponseUtil.success(data);
        }
        return ResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
    }


    /**
     * 微信付款成功！（开通技能服务）
     *
     * @throws IOException
     * @throws DocumentException
     */
    @RequestMapping("/wechatBySuccess")
    public void wechatBySuccess(HttpServletResponse response, HttpServletRequest request) throws IOException, DocumentException {

        // 设置格式为text/html
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息

        Map<String, String> resultMap = XmlUtil.doXMLParse(result);
        String out_trade_no = resultMap.get("out_trade_no");
        String return_code = resultMap.get("return_code");
        String result_code = resultMap.get("result_code");
        String attach = resultMap.get("attach");
        if (result_code.equalsIgnoreCase("SUCCESS")) {
            if (return_code.equalsIgnoreCase("SUCCESS")) {
                Map map = new HashMap();
                map.put("id", attach);
                map.put("sn", out_trade_no);
                log.warning(EveryUtils.getNowday() + "用户付款uid为==" + attach);
                payDao.addPayLog(map);
            } else {
            }
            String ty = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
            out.write(ty);
            out.flush();
            out.close();
        } else {

        }


    }


}
