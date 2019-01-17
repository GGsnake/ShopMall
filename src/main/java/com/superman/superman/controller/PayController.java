package com.superman.superman.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.superman.superman.annotation.LoginRequired;
import com.superman.superman.service.OtherService;
import com.superman.superman.utils.*;
import lombok.extern.java.Log;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by liujupeng on 2019/1/11.
 */
@Log
@RestController
@RequestMapping("/wx")
public class PayController {
    @Autowired
    private OtherService otherService;
    /**
     * 微信预支付（技能开通支付）
     *
     * @param response
     * @param request
     * @throws IOException
     * @throws DocumentException
     */
    @LoginRequired
    @RequestMapping("/wechatOrderPay")
    public WeikeResponse wechatOrderPay(HttpServletResponse response, HttpServletRequest request) throws IOException, DocumentException {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid != null) {
            /* 设置格式为text/html */
            request.setCharacterEncoding("utf-8");
            String s = otherService.payMoney(uid, request.getRemoteAddr());


        }
        return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);

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
        log.warning("回调ACTION:paySuccessSSSSSSSSSSSSSSS");
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        log.warning("~~~~~~~~~~~~~~~~付款成功~~~~~~~~~");
        outSteam.close();
        inStream.close();
        String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息

        Map<String, String> resultMap = XmlUtil.doXMLParse(result);
        String out_trade_no = resultMap.get("out_trade_no");
        String return_code = resultMap.get("return_code");
        String result_code = resultMap.get("result_code");
        String attach = resultMap.get("attach");
//
//
//        System.out.println("----resultMap----" + resultMap);
//
//        System.out.println("======return_code:" + return_code + "=========result_code:" + result_code);

        if (result_code.equalsIgnoreCase("SUCCESS")) {

            if (return_code.equalsIgnoreCase("SUCCESS")) {

                System.out.println("-----------支付成功----------------");

                System.out.println("---------附加参数-----" + attach);
                System.out.println("-------订单号---" + out_trade_no);


            } else {
                System.out.println("--------------------支付后验签失败，请检查-------------------");
            }


            String ty = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
            out.write(ty);
            out.flush();
            out.close();
        } else {
            System.out.println("--------------------支付后验签失败，请检查-------------------");
        }


    }
}
