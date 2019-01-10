package com.superman.superman.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SmsUtil {
	public static final String accounts ="uucj";
	public static final String password ="huazhongno1";
	public static final String productId = "887362";

	public static int sendSms(String phone,String con){
		int result = 0;
		try {
			String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String Url = "http://www.ztsms.cn/sendNSms.do?productid=887362";
			String pwd= DigestUtils.md5Hex(DigestUtils.md5Hex("huazhongno1")+date);
			HttpClient client =new HttpClient();
			PostMethod post = new PostMethod(Url);
			post.setRequestHeader("Content-type",
					"application/x-www-form-urlencoded;charset=utf-8");
			NameValuePair userid = new NameValuePair("username", "uucj");
			NameValuePair account = new NameValuePair("password", pwd);
			NameValuePair password = new NameValuePair("tkey",date );
			NameValuePair mobile = new NameValuePair("mobile", phone);
			NameValuePair content = new NameValuePair("content", con);
			NameValuePair sendTime = new NameValuePair("xh", "");
			post.setRequestBody(new NameValuePair[] { userid, account, password,mobile, content, sendTime });
			result= client.executeMethod(post);//发送请求
			/*System.out.println("----------发送结果-----------"+result);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	public static int sendSmsLogin(String phone,String con){
		int result = 0;
		try {
			String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String Url = "http://www.ztsms.cn/sendNSms.do?productid=676767";
			String pwd= DigestUtils.md5Hex(DigestUtils.md5Hex("huazhongno1")+date);
			HttpClient client =new HttpClient();
			PostMethod post = new PostMethod(Url);
			post.setRequestHeader("Content-type",
					"application/x-www-form-urlencoded;charset=utf-8");
			NameValuePair userid = new NameValuePair("username", "uucj");
			NameValuePair account = new NameValuePair("password", pwd);
			NameValuePair password = new NameValuePair("tkey",date );
			NameValuePair mobile = new NameValuePair("mobile", phone);
			NameValuePair content = new NameValuePair("content", con);
			NameValuePair sendTime = new NameValuePair("xh", "");
			post.setRequestBody(new NameValuePair[] { userid, account, password,mobile, content, sendTime });
			result= client.executeMethod(post);//发送请求
			/*System.out.println("----------发送结果-----------"+result);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 微信端登录发送短信验证码
	 * @param mobile
	 * @return
	 */
	public static JSONObject sendLoginSmsVcode(String mobile){
		JSONObject json = new JSONObject();
		int code = (int)((Math.random()*9+1)*100000);
		/*System.out.println(code);*/
		String content = "此次登录验证码"+code+"，验证码五分钟过期【优优车检】";
		int result = sendSmsLogin(mobile, content);
		json.put("sendResult", result);
		json.put("code", code);
		return json;
	}
	/**
	 * 向车检站发送下单短信通知
	 * @param mobile
	 * @return
	 */
	public static void inform(String mobile,String nickName,String phone,String plateNumber,String yyTime){

//		String content = "【优优车检】新订单通知,"
//				+ "下单用户-"+nickName+",联系方式-"+phone+",车牌号-"+plateNumber+",预约时间-"+yyTime;
		String content ="【优优车检】新订单通知\r\n" +
				"下单用户："+nickName+"\r\n" +
				"联系方式："+phone+"\r\n" +
				"车牌号：   "+plateNumber+"\r\n" +
				"预约时间："+yyTime;

		int result = sendSms(mobile, content);

	}


}
