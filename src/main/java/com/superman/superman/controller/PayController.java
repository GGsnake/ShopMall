package com.superman.superman.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.superman.superman.annotation.LoginRequired;
import com.superman.superman.utils.*;
import lombok.extern.java.Log;
import org.dom4j.DocumentException;
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
//        微信支付商户号 1521764621
//        应用APPID wxc7df701f4d4f1eab
//        API秘钥：hzshop12345678912345678912345678
        String url2 = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        String appid = "wxc7df701f4d4f1eab";
        String body = "ssssssssssssssssss";
        String partnerid = "1521764621";
        String noncestr = Util.getRandomString(30);
        String notifyurl = "http://120.79.62.89:8080/wx/wechatBySuccess";//回调地址wechatBySuccess

        double money = 1;

        String ip = request.getRemoteAddr();


        int totalfee = (int) (100 * money);
        String attach = uid ;//附加参数:用户id
        String tradetype = "APP";
        String key = "hzshop12345678912345678912345678";

        // 时间戳
        Long times = System.currentTimeMillis();
        String outtradeno = "hj" + times + "" + attach;

        String prepayid;
        String timestamp = String.valueOf(times / 1000);
        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        parameters.put("appid", appid);//应用ID
        parameters.put("mch_id", partnerid);//商户号
        parameters.put("nonce_str", noncestr);//随机字符串
        parameters.put("body", body);//商品描述
        parameters.put("key", key);//秘钥
        parameters.put("trade_type", tradetype);//交易类型
        parameters.put("out_trade_no", outtradeno);//商户订单号
        parameters.put("total_fee", totalfee);//总金额
        parameters.put("spbill_create_ip", ip);//终端IP
        parameters.put("notify_url", notifyurl);//回调地址
        parameters.put("attach", attach);//附加参数
        String sign = MD5Util.createSign("utf-8", parameters);
        String params = String.format("<xml>" + "<appid>%s</appid>"
                        + "<attach>%s</attach>"
                        + "<body>%s</body>" + "<mch_id>%s</mch_id>"
                        + "<nonce_str>%s</nonce_str>"
                        + "<notify_url>%s</notify_url>"
                        + "<out_trade_no>%s</out_trade_no>"
                        + "<spbill_create_ip>%s</spbill_create_ip>"
                        + "<total_fee>%s</total_fee>"
                        + "<trade_type>%s</trade_type>" + "<sign>%s</sign>"
                        + "</xml>", appid, attach, body, partnerid, noncestr,
                notifyurl, outtradeno, ip, totalfee, tradetype,
                sign);

        String result = HttpUtil.doPost(url2, params);

/*		System.out.println("---------------result---------------"+result);
		String newStr = new String(result.getBytes(), "UTF-8");
		System.out.println("---------------newStr---------------"+newStr);*/

        //二次签名
        Map<String, String> keyval = XmlUtil.treeWalkStart(result);
        noncestr = keyval.get("nonce_str");
        String packageValue = "Sign=WXPay";
        prepayid = keyval.get("prepay_id");

        String stringA = "appid=%s&noncestr=%s&package=%s&partnerid=%s&prepayid=%s&timestamp=%s&key=%s";
        String stringSignTemp = String.format(stringA, appid,
                noncestr, packageValue, partnerid, prepayid,
                timestamp, key);
        sign = MD5.md5(stringSignTemp).toUpperCase();


        JSONObject map = new JSONObject();
        map.put("appid", appid);
        map.put("partnerid", partnerid);
        map.put("prepayid", prepayid);
        map.put("packageValue", packageValue);
        map.put("noncestr", noncestr);
        map.put("timestamp", timestamp);
        map.put("sign", sign);
        map.put("ordersNo", outtradeno);
        map.put("attach", attach);
        return WeikeResponseUtil.success(map);
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


        System.out.println("----resultMap----" + resultMap);

        System.out.println("======return_code:" + return_code + "=========result_code:" + result_code);

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
