//package com.superman.superman.utils.sms;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.math.BigDecimal;
//import java.net.InetAddress;
//import java.net.Socket;
//import java.net.UnknownHostException;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.time.ZonedDateTime;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import com.google.gson.Gson;
//
//public class Test {
//
//	public static void main(String[] args) throws ParseException {
//		// TODO Auto-generated method stub
//
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		// Date d1 = new Date();
//		// System.out.println("当前时间：" + sdf.format(d1));
//		//
//		// Date d2 = new Date(d1 .getTime() + 900000);
//		// System.out.println("加后时间："+sdf.format(d2));
//		//// //前者大于后者 返回大于0 反之小于0。
//		// System.out.println(d2);
//		//
//		// String xx="";
//		//
//		// String[] split = xx.split(",");
//		// for (int i = 0; i < split.length; i++) {
//		// System.out.println("-----"+split[i]);
//		// }
//
//		/*
//		 * Date d1 = new Date(); Date d2= new Date(d1.getTime() + 1800000);
//		 * System.out.println("d1:"+sdf.format(d1));
//		 * System.out.println("d2:"+sdf.format(d2));
//		 * System.out.println("----"+compare_date(d2,d1));
//		 */
//
//		// System.out.println("======compare_date:"+compare_date(new Date(),
//		// sdf.parse("2018-06-19 16:00:00")));
//		// send_delivery_message("o4OMb5MVqo2pXKB5m8TJyyZFzCXk",
//		// "1530090818020");
//
//		// Msnmodel.send_recharge("o4OMb5MVqo2pXKB5m8TJyyZFzCXk",
//		// "wx271827473081312dcab8a60a3912963539", 0.2, 2,
//		// "2018-06-27","123456789");
//
//		// client();
//
//		/*  BigDecimal bigDecimal = new BigDecimal("20.00");
//		  BigDecimal c = new BigDecimal("30.00");
//
//	          int i = c.compareTo(bigDecimal);
//	          System.out.println(i);
//	          long second = differFromSecond(sdf.parse("2018-12-10 00:52:00"), sdf.parse("2018-12-10 00:48:55"));
//	          System.out.println("-----second:"+second);
//	          System.out.println("-----second:"+second/60);
//	          SmsSend.getSms("15217844795");*/
//
//		 SmsSend.intervene("15217844795", "测试","袁工", 0);
//	      //SmsSend.issuePark("15217844795", "袁木求鱼");
//
//		//SmsSend.issueDriver("15217844795");
//	//	BigDecimal u = new BigDecimal("-50");
//		//BigDecimal bd5 = new BigDecimal("-50.00");
//	//	int i5 = u.compareTo(bd5);
//          Date d=new Date();
//		SimpleDateFormat simpleDF = new SimpleDateFormat("yyyy-MM-dd");
//		  String eDate = simpleDF.format(new Date());
//		  Date parse= sdf.parse(eDate+" 11:33:00");
//		  System.out.println(parse);
//		  long l= differFromSecond(d, parse);
//		  System.out.println("----------l:"+l);
//			  if(l/60==15){
//
//				  System.out.println(l/60);
//			  }else{
//
//				  System.out.println("----------l:"+l/60);
//			  }
//
//		//System.out.println("is:"+i5);
//
//
//	}
//
//
//
//
//	/**
//	 * 计算日期
//	 *
//	 * @param date
//	 * @param day
//	 * @return
//	 */
//	public static Date getDate(Date date, long day){
//		long time = date.getTime(); // 得到指定日期的毫秒数
//		day = day * 24 * 60 * 60 * 1000; // 要加上的天数转换成毫秒数
//		time += day; // 相加得到新的毫秒数
//		return new Date(time); // 将毫秒数转换成日期
//	}
//
//
//
//	public static String xx() {
//
//		Map<String, Object> map = new HashMap<>();
//
//		try {
//			int i = 1 + 1;
//			System.out.println("----:" + i);
//
//			map.put("msg", 1);
//			try {
//				// 开始停车消息推送
//				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
//				Msnmodel.send_startStopCar("123", "123", "123", "123", df.format(new Date()), "123");
//			} catch (Exception e) {
//				System.out.println("----发送消息-----");
//				e.printStackTrace();
//			}
//
//		} catch (Exception e) {
//			map.put("msg", 0);
//			System.out.println("------程序错误--------");
//			e.printStackTrace();
//		}
//
//		Gson gson = new Gson();
//		String str = gson.toJson(map);
//		return str;
//
//	}
//
//	public static void client() {
//		Socket socket = null;
//		OutputStream os = null;
//		InputStream is = null;
//		try {
//			socket = new Socket(InetAddress.getByName("192.168.1.23"), 8888);
//			os = socket.getOutputStream();
//			String str = "我是客户端，请接收。";
//			System.out.println("------str" + str);
//			os.write(str.getBytes());
//			// shutdownOutput()：显示的告诉服务器端我已经输出完毕
//			socket.shutdownOutput();
//			is = socket.getInputStream();
//			byte[] b = new byte[100];
//			int len;
//			while ((len = is.read(b)) != -1) {
//				str = new String(b, 0, len);
//				System.out.println(str);
//			}
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (is != null) {
//				try {
//					is.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//			if (os != null) {
//				try {
//					os.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//			if (socket != null) {
//				try {
//					socket.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}
//
//	public static boolean sameDate(Date d1, Date d2) {
//		LocalDate localDate1 = ZonedDateTime.ofInstant(d1.toInstant(), ZoneId.systemDefault()).toLocalDate();
//		LocalDate localDate2 = ZonedDateTime.ofInstant(d2.toInstant(), ZoneId.systemDefault()).toLocalDate();
//		return localDate1.isEqual(localDate2);
//	}
//
//	// 计算两个时间差值（秒数）
//	public static long differFromSecond(Date date1, Date date2) {
//		long minutes = (date2.getTime() - date1.getTime()) / 1000;
//		return minutes;
//	}
//
//
//
//	/**
//	 * 比较两个时间大小
//	 *
//	 * @param beginDate
//	 *            开始时间
//	 * @param endDate
//	 *            结束时间
//	 * @return 1.开始时间大于结束时间，0.开始时间小于结束时间
//	 */
//	public static int compare_date(Date beginDate, Date endDate) {
//
//		if (beginDate.getTime() > endDate.getTime()) {
//			System.out.println("dt1 在dt2前");
//			return 1;
//		} else {
//			return 0;
//		}
//
//	}
//
//	/**
//	 * 秒转换成时间
//	 *
//	 * @param seconds
//	 * @return
//	 */
//	public static String secToTime(long seconds) {
//		String timeStr = null;
//
//		long hours = 0;// 小时
//		long mins = 0; // 分
//		long day = 0;// 天数
//		if (seconds < 60) {
//
//		} else if (seconds < 3600) {
//			mins = seconds / 60;
//			seconds = seconds % 60;
//		} else {
//			day = seconds / (60 * 60 * 24);
//			mins = seconds / 60;
//			seconds = seconds % 60;
//			hours = mins / 60;
//			mins = mins % 60;
//
//		}
//
//		hours = hours % 24;// 小时
//
//		if (day == 0) {
//			timeStr = formatTime(hours) + ':' + formatTime(mins) + ':' + formatTime(seconds);
//		} else {
//
//			timeStr = day + "天 " + formatTime(hours) + ':' + formatTime(mins) + ':' + formatTime(seconds);
//		}
//
//		return timeStr;
//	}
//
//	public static String formatTime(long i) {
//		String retStr = null;
//		if (i < 10)
//			retStr = "0" + i;
//		else
//			retStr = "" + i;
//		return retStr;
//	}
//
//	/**
//	 * 计算时间差
//	 *
//	 * @param beginDate
//	 *            开始时间
//	 * @param endDate
//	 *            结束时间（系统当前时间）
//	 * @return 返回天数 跟秒数
//	 */
//	public static Map<String, Object> getDatePoor(Date beginDate, Date endDate) {
//
//		Map<String, Object> map = new HashMap<>();
//
//		long nd = 1000 * 24 * 60 * 60;
//		long nh = 1000 * 60 * 60;
//		long nm = 1000 * 60;
//		long ns = 1000;
//		// 获得两个时间的毫秒时间差异
//		long diff = endDate.getTime() - beginDate.getTime();
//		// 计算差多少天
//		long day = diff / nd;
//		// 计算差多少小时
//		long hour = diff % nd / nh;
//		// 计算差多少分钟
//		long min = diff % nd % nh / nm;
//		// 计算差多少秒//输出结果
//		long sec = diff % nd % nh % nm / ns;
//		// 把小时跟分钟统统转换成秒
//		long ss = day * 24 * 60 * 60 + hour * 60 * 60 + min * 60 + sec;
//		map.put("day", day);// 天数
//		map.put("sec", ss);// 秒
//		return map;
//	}
//
//
//	public static long getDateMin(Date endDate, Date beginDate) {
//
//
//		long nd = 1000 * 24 * 60 * 60;
//		long nh = 1000 * 60 * 60;
//		long nm = 1000 * 60;
//		// 获得两个时间的毫秒时间差异
//		long diff = endDate.getTime() - beginDate.getTime();
//		// 计算差多少小时
//		long hour = diff % nd / nh;
//		// 计算差多少分钟
//		long min = diff % nd % nh / nm;
//		// 把小时转换成分钟
//		long ss =  hour * 60 + min;
//	    return ss;
//	}
//
//	public static void send_delivery_message(String openid, String formid) {
//
//		AccessToken token = TokenThread.accessToken;
//
//		System.out.println("-------" + token);
//		// https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=
//		String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=11_-5BPKYUXw4ZoZIYt_nO4egkyvKJP2RqOeSJNMKJ9ab_l56FLrCU3eJVOqMpt9x9xzBryCvEkcYVUJ6OMcA0K95Xp3VrDY8yVgpZHJx_4oDqNHPT_96wZTAqdq0ArUD6pstNi0t1vVEWFlRkSMSOaAFAHNJ";
//		WxTemplate temp = new WxTemplate();
//
//		temp.setTouser(openid);// 用户id
//		temp.setForm_id(formid);
//		temp.setTopcolor("#000000");
//		temp.setTemplate_id("yVCcChdMQ2AGfrCgAxJcPPn0KzuCrLOT9dq7mk3LKV0");
//		Map<String, TemplateData> m = new HashMap<String, TemplateData>();
//		/*
//		 * TemplateData first = new TemplateData(); first.setColor("#000000");
//		 * first.setValue(" ");
//		 */
//
//		TemplateData keyword1 = new TemplateData();// 订单类型
//		keyword1.setColor("#27408B");
//		keyword1.setValue("绿色车位");
//
//		TemplateData keyword2 = new TemplateData();// 订单编号
//		keyword2.setColor("#27408B");
//		keyword2.setValue("2号车位");
//
//		TemplateData keyword3 = new TemplateData();// 商品详情
//		keyword3.setColor("#27408B");
//		keyword3.setValue("2018年6月27日 17:07:00");
//
//		TemplateData keyword4 = new TemplateData();// 商品详情
//		keyword4.setColor("#27408B");
//		keyword4.setValue("好运来商务大厦");
//
//		TemplateData keyword5 = new TemplateData();// 商品详情
//		keyword5.setColor("#27408B");
//		keyword5.setValue("前15分钟免费");
//
//		m.put("keyword1", keyword1);
//		m.put("keyword2", keyword2);
//		m.put("keyword3", keyword3);
//		m.put("keyword4", keyword4);
//		m.put("keyword5", keyword5);
//
//		temp.setData(m);
//		String jsonString = JSONObject.fromObject(temp).toString();
//
//		JSONObject jsonObject = WeixinUtil.httpsRequest(url, "POST", jsonString);
//		int result = 0;
//		if (null != jsonObject) {
//			if (0 != jsonObject.getInt("errcode")) {
//				result = jsonObject.getInt("errcode");
//				System.out.println(
//						"错误 errcode:{} " + jsonObject.getInt("errcode") + "errmsg:{}" + jsonObject.getString("errmsg"));
//			}
//
//		}
//		System.out.println("模板消息发送结果：" + result);
//	}
//
//}
