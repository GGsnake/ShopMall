//package com.superman.superman.utils.sms;
//
//import java.util.Random;
//
//import com.alibaba.fastjson.JSON;
//
//public class SmsSend {
//
//	public static final String charset = "utf-8";
//
//	// 请登录zz.253.com 获取创蓝API账号(非登录账号,示例:N1234567)
//	public static String account = "N451442_N0360303";
//
//	// 请登录zz.253.com 获取创蓝API密码(非登录密码)
//	public static String pswd = "beLtQz6w8d914f";
//
//	public static String getSms(String phone) {
//
//		String num = getRandNum(6);
//
//		// 短信发送的URL 请登录zz.253.com 获取完整的URL接口信息
//		String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
//
//		// 设置您要发送的内容：其中“【】”中括号为运营商签名符号，多签名内容前置添加提交
//		// String msg = "【253云通讯】你好,你的验证码是:"+num;
//		String msg = "此次登录验证码"+num+",验证码五分钟分钟过期" ;
//		String report = "true";
//		SmsSendRequest smsSingleRequest = new SmsSendRequest(account, pswd, msg, phone, report);
//		String requestJson = JSON.toJSONString(smsSingleRequest);
//		String response = ChuangLanSmsUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
//		SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
//		return num;
//	}
//
//	/**
//	 * 1、发给司机  车位使用过程中（未欠费）即将超时
//	 */
//	public static void issueDriver(String phone) {
//		// 短信发送的URL 请登录zz.253.com 获取完整的URL接口信息
//		String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
//
//		String msg = "尊敬的用户：您好！您使用的车位共享停车时间段即将结束，请尽快驶离车位，谢谢配合！";
//		// 状态报告
//		String report = "true";
//
//		SmsSendRequest smsSingleRequest = new SmsSendRequest(account, pswd, msg, phone, report);
//
//		String requestJson = JSON.toJSONString(smsSingleRequest);
//
//		String response = ChuangLanSmsUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
//
//		SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
//		System.out.println("---发送结果：" + smsSingleResponse.getErrorMsg());
//
//	}
//
//	/**
//	 * 2、发给车位主  车位使用过程中（未欠费）即将超时
//	 *
//	 * @param phone
//	 * @param con
//	 */
//	public static void issuePark(String phone, String con) {
//		// 短信发送的URL 请登录zz.253.com 获取完整的URL接口信息
//		String smsVariableRequestUrl = "http://smssh1.253.com/msg/variable/json";
//
//		String msg = "尊敬的车位主您好！您发布的{$var}车位共享时段即将结束，系统检测有用户正在使用中，请及时关注车位使用情况。谢谢！";
//
//		// 参数组
//		String params = phone + "," + con;
//		// 状态报告
//		String report = "true";
//
//		SmsVariableRequest smsVariableRequest = new SmsVariableRequest(account, pswd, msg, params, report);
//
//		String requestJson = JSON.toJSONString(smsVariableRequest);
//
//		System.out.println("before request string is: " + requestJson);
//
//		String response = ChuangLanSmsUtil.sendSmsByPost(smsVariableRequestUrl, requestJson);
//
//		System.out.println("response after request result is : " + response);
//
//		SmsVariableResponse smsVariableResponse = JSON.parseObject(response, SmsVariableResponse.class);
//
//		System.out.println("response  toString is : " + smsVariableResponse);
//	}
//
//	/**
//	 * 超时达半小时，短信提醒
//	 *
//	 * @param phone
//	 */
//	public static void timeout(String phone) {
//
//		String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
//		String msg = "尊敬的用户：您好！您已超时停车30分钟，请立即将车挪走，可能会面临被锁车、拖车、罚款、贴条等处罚，谢谢配合！";
//		// 状态报告
//		String report = "true";
//
//		SmsSendRequest smsSingleRequest = new SmsSendRequest(account, pswd, msg, phone, report);
//
//		String requestJson = JSON.toJSONString(smsSingleRequest);
//
//		String response = ChuangLanSmsUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
//
//		SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
//		System.out.println("---发送结果：" + smsSingleResponse.getErrorMsg());
//	}
//
//	/**
//	 * 车位正常使用过程中（未超时）费用不足短信提醒：
//	 *
//	 * @param phone
//	 */
//	public static void moneyLack(String phone) {
//
//		String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
//		String msg = "尊敬的用户：您好！系统检测到您的绿色车位账户余额不足支付一个计费周期停车费，请尽快充值！避免违约停车给您带来不便，敬请谅解！";
//		// 状态报告
//		String report = "true";
//
//		SmsSendRequest smsSingleRequest = new SmsSendRequest(account, pswd, msg, phone, report);
//
//		String requestJson = JSON.toJSONString(smsSingleRequest);
//
//		String response = ChuangLanSmsUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
//
//		SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
//		System.out.println("---发送结果：" + smsSingleResponse.getErrorMsg());
//	}
//
//	/**
//	 * 还在正常使用停车位（未超时）但已经欠费达到20元时 短信提醒
//	 *
//	 * @param phone
//	 */
//	public static void arrear(String phone) {
//
//		String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
//		String msg = "尊敬的用户您好！系统检测到您的绿色车位账户余额已欠费，此时已属于违约停车，请尽快充值！有可能降低信用或锁车等风险！请及时处理！";
//		// 状态报告
//		String report = "true";
//
//		SmsSendRequest smsSingleRequest = new SmsSendRequest(account, pswd, msg, phone, report);
//
//		String requestJson = JSON.toJSONString(smsSingleRequest);
//
//		String response = ChuangLanSmsUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
//
//		SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
//		System.out.println("---发送结果：" + smsSingleResponse.getErrorMsg());
//	}
//
//	/**
//	 * 已经欠费达50元或者超时达1小时 人工介入，短信提醒
//	 *
//	 * @param phone
//	 * @param con
//	 * @param tag
//	 *            0,欠费50元，1.超时1小时
//	 */
//	public static void intervene(String phone, String con, String name, int tag) {
//		// 短信发送的URL 请登录zz.253.com 获取完整的URL接口信息
//		String smsVariableRequestUrl = "http://smssh1.253.com/msg/variable/json";
//
//		String msg = "";
//
//		if (tag == 0) {
//
//			msg = "{$var}用户在{$var}号车位,{$var}上产生欠费50元。 ";
//		} else {
//
//			msg = "{$var}用户在{$var}号车位,{$var}上产生超时一个小时 ";
//
//		}
//
//		// 参数组
//		String params = "18688740158," + phone + "," + con + "," + name;
//		//String params = "15217844795," + phone + "," + con + "," + name;
//		// 状态报告
//		String report = "true";
//
//		SmsVariableRequest smsVariableRequest = new SmsVariableRequest(account, pswd, msg, params, report);
//
//		String requestJson = JSON.toJSONString(smsVariableRequest);
//
//		System.out.println("before request string is: " + requestJson);
//
//		String response = ChuangLanSmsUtil.sendSmsByPost(smsVariableRequestUrl, requestJson);
//
//		System.out.println("response after request result is : " + response);
//
//		SmsVariableResponse smsVariableResponse = JSON.parseObject(response, SmsVariableResponse.class);
//
//		System.out.println("response  toString is : " + smsVariableResponse);
//	}
//
//	/**
//	 * 欠费50元发给司机
//	 *
//	 * @param phone
//	 * @param money
//	 */
//	public static void arrearage(String phone, String money) {
//
//		String smsVariableRequestUrl = "http://smssh1.253.com/msg/variable/json";
//		String msg = "尊敬的用户您好！检测到您的账户欠费已达{$var}元。您已涉嫌故意停车占用他人车位，请立即充值！将会面临锁车、拖车、降低信用值等系列风险！";
//
//		// 参数组
//		String params = phone + "," + money;
//		// 状态报告
//		String report = "true";
//
//		SmsVariableRequest smsVariableRequest = new SmsVariableRequest(account, pswd, msg, params, report);
//
//		String requestJson = JSON.toJSONString(smsVariableRequest);
//
//		System.out.println("before request string is: " + requestJson);
//
//		String response = ChuangLanSmsUtil.sendSmsByPost(smsVariableRequestUrl, requestJson);
//
//		System.out.println("response after request result is : " + response);
//
//		SmsVariableResponse smsVariableResponse = JSON.parseObject(response, SmsVariableResponse.class);
//
//		System.out.println("response  toString is : " + smsVariableResponse);
//
//	}
//
//	/**
//	 * 超时1小时 发给司机
//	 *
//	 * @param phone
//	 * @param money
//	 */
//	public static void oneHour(String phone) {
//
//		String smsVariableRequestUrl = "http://smssh1.253.com/msg/send/json";
//		String msg = "尊敬的用户您好！检测到您停车超时一个小时。您已涉嫌故意停车占用他人车位，请立即挪车！可能将会面临锁车、拖车、降低信用值等系列风险！";
//
//		// 参数组
//		String params = phone;
//		// 状态报告
//		String report = "true";
//
//		SmsVariableRequest smsVariableRequest = new SmsVariableRequest(account, pswd, msg, params, report);
//
//		String requestJson = JSON.toJSONString(smsVariableRequest);
//
//		System.out.println("before request string is: " + requestJson);
//
//		String response = ChuangLanSmsUtil.sendSmsByPost(smsVariableRequestUrl, requestJson);
//
//		System.out.println("response after request result is : " + response);
//
//		SmsVariableResponse smsVariableResponse = JSON.parseObject(response, SmsVariableResponse.class);
//
//		System.out.println("response  toString is : " + smsVariableResponse);
//
//	}
//
//	/**
//	 * 欠费50元给车位主
//	 * @param phone
//	 * @param con
//	 * @param money
//	 */
//	public static void arrearageCarPlace(String phone, String con,String money) {
//
//		String smsVariableRequestUrl = "http://smssh1.253.com/msg/variable/json";
//		String msg = "车位主您好！使用{$var}车位的用户欠费已达{$var}元，您将会面临用户故意逃费风险，系统马上会停止计费！欠费追缴由您承担，请联系用户充值！";
//
//		// 参数组
//		String params = phone+","+con+","+money;
//		// 状态报告
//		String report = "true";
//
//		SmsVariableRequest smsVariableRequest = new SmsVariableRequest(account, pswd, msg, params, report);
//
//		String requestJson = JSON.toJSONString(smsVariableRequest);
//
//		System.out.println("before request string is: " + requestJson);
//
//		String response = ChuangLanSmsUtil.sendSmsByPost(smsVariableRequestUrl, requestJson);
//
//		System.out.println("response after request result is : " + response);
//
//		SmsVariableResponse smsVariableResponse = JSON.parseObject(response, SmsVariableResponse.class);
//
//		System.out.println("response  toString is : " + smsVariableResponse);
//
//	}
//
//	/**
//	 * 费用不足
//	 *
//	 * @param phone
//	 * @param plate
//	 */
//	/*public static void smsLack(String phone, String plate) {
//
//		// 短信发送的URL 请登录zz.253.com 获取完整的URL接口信息
//		String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
//
//		// 设置您要发送的内容：其中“【】”中括号为运营商签名符号，多签名内容前置添加提交
//		// String msg = "【253云通讯】你好,你的验证码是:"+num;
//		String msg = "【253云通讯】车牌号为" + plate + "，用车余额不足，请尽快充值。";
//		// 状态报告
//		String report = "true";
//
//		SmsSendRequest smsSingleRequest = new SmsSendRequest(account, pswd, msg, phone, report);
//
//		String requestJson = JSON.toJSONString(smsSingleRequest);
//
//		String response = ChuangLanSmsUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
//
//		SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
//
//		System.out.println("response  toString is :" + smsSingleResponse);
//
//	}*/
//
//	/**
//	 * 超时
//	 *
//	 * @param phone
//	 * @param plate
//	 */
///*	public static void smsTimeout(String phone, String plate) {
//
//		// 短信发送的URL 请登录zz.253.com 获取完整的URL接口信息
//		String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
//
//		// 设置您要发送的内容：其中“【】”中括号为运营商签名符号，多签名内容前置添加提交
//		// String msg = "【253云通讯】你好,你的验证码是:"+num;
//		String msg = "【253云通讯】车牌号为" + plate + "，您的停车已经超时，麻烦尽快处理！";
//		// 状态报告
//		String report = "true";
//
//		SmsSendRequest smsSingleRequest = new SmsSendRequest(account, pswd, msg, phone, report);
//
//		String requestJson = JSON.toJSONString(smsSingleRequest);
//
//		String response = ChuangLanSmsUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
//
//		SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
//
//		System.out.println("response  toString is :" + smsSingleResponse);
//
//	}*/
//
//	public static String getRandNum(int number) {
//		Random random = new Random();
//		String verify = "";
//		for (int i = 0; i < number; i++) {
//			int n = random.nextInt(10);
//			verify = verify + n;
//		}
//		return verify;
//	}
//
//}
