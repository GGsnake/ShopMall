package com.superman.superman.utils.net;

import com.superman.superman.utils.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class HttpRequest {


	private static String encode = "UTF-8";

	/**
	 * 发送post请求
	 * @param url     请求地址
	 * @param formparams   formparams.add(new BasicNameValuePair(key, value))
	 * @throws IOException
	 */
	public static String sendPost(String url, List<org.apache.http.NameValuePair> formparams) throws IOException {
		String result=null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).build();//.setSocketTimeout(120000)
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setConfig(requestConfig);
			httpPost.setEntity(new UrlEncodedFormEntity(formparams, encode));
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				System.out.println(response.getStatusLine());
				org.apache.http.HttpEntity entity = response.getEntity();
				if(entity!=null){
					result = EntityUtils.toString(response.getEntity(),encode);
				}
				// do something useful with the response body
				// and ensure it is fully consumed
				EntityUtils.consume(entity);
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
		return StringUtil.replaceBlank(result);
	}
	/**
	 * 发送post请求
	 * @param url   请求地址
	 * @param params  请求参数Map<String, ? extends Object> params  Object可以为String或者File或者基本类型
	 * @throws IOException
	 */
	public static String sendPost(String url, Map<String, String> params) throws IOException {
		String result=null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).build();//.setSocketTimeout(120000)
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setConfig(requestConfig);
			List<org.apache.http.NameValuePair> formparams = new ArrayList<org.apache.http.NameValuePair>();
			if (params != null && params.size() > 0) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					String key = entry.getKey();
					String value = params.get(key);
					formparams.add(new BasicNameValuePair(key, value));
				}
			}
			httpPost.setEntity(new UrlEncodedFormEntity(formparams, encode));
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				org.apache.http.HttpEntity entity = response.getEntity();
				if(entity!=null){
					result = EntityUtils.toString(response.getEntity(),encode);
				}
				// do something useful with the response body
				// and ensure it is fully consumed
				EntityUtils.consume(entity);
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
		return StringUtil.replaceBlank(result);
	}
	public static String sendPost(String url, String param) throws IOException {
		String result=null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).build();//.setSocketTimeout(120000)
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setConfig(requestConfig);
			List<org.apache.http.NameValuePair> formparams = new ArrayList<NameValuePair>();
			if(!StringUtils.isBlank(param)){
				String[] pps = param.split("&");
				for (int i = 0; i < pps.length; i++) {
					int denghaoindex=pps[i].indexOf("=");
					formparams.add(new BasicNameValuePair(pps[i].substring(0,denghaoindex), pps[i].substring(denghaoindex+1)));
				}
			}
			httpPost.setEntity(new UrlEncodedFormEntity(formparams, encode));
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				System.out.println(response.getStatusLine());
				HttpEntity entity = response.getEntity();
				if(entity!=null){
//					result = EntityUtils.toString(response.getEntity(),encode);
					result = EntityUtils.toString(response.getEntity());
				}
				// do something useful with the response body
				// and ensure it is fully consumed
				EntityUtils.consume(entity);
			} finally {
				response.close();
			}
		}finally {
			httpclient.close();
		}
		return StringUtil.replaceBlank(result);
	}
    
  
}