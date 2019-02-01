package com.superman.superman.utils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.utils.sms.ChuangLanSmsUtil;
import com.superman.superman.utils.sms.SmsSendRequest;
import com.superman.superman.utils.sms.SmsSendResponse;


/**
 *
 * @author tianyh
 * @Description:普通短信发送
 */
public class SmsSendDemo {
    public static final String charset = "utf-8";
    // 请登录zz.253.com 获取创蓝API账号(非登录账号,示例:N1234567)
    // 请登录zz.253.com 获取创蓝API账号(非登录账号,示例:N1234567)
    public static String account = "N451442_N0360303";

    // 请登录zz.253.com 获取创蓝API密码(非登录密码)
    public static String pswd = "beLtQz6w8d914f";

    public static SmsSendResponse getSms(String phone, String code) {
        // 短信发送的URL 请登录zz.253.com 获取完整的URL接口信息
        String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";

        // 设置您要发送的内容：其中“【】”中括号为运营商签名符号，多签名内容前置添加提交
        // String msg = "【253云通讯】你好,你的验证码是:"+num;
        String msg = "此次登录验证码"+code+",验证码两分钟过期" ;
        String report = "true";
        SmsSendRequest smsSingleRequest = new SmsSendRequest(account, pswd, msg, phone, report);
        String requestJson = JSON.toJSONString(smsSingleRequest);
        String response = ChuangLanSmsUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
        SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
        return smsSingleResponse;
    }
}
