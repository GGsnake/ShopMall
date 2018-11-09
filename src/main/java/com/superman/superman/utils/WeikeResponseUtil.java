package com.superman.superman.utils;



/**
 * @author 80132724
 * @date 2017年5月25日
 * @time 上午10:37:29
 */
public class WeikeResponseUtil {

	/**
	 * 成功返回
	 * 
	 * @return
	 */
	public static WeikeResponse<Object> success() {
		WeikeResponse<Object> result = new WeikeResponse<Object>();
		result.setSuccess(true);
		return result;
	}

	/**
	 * 成功返回
	 *
	 * @return
	 */
	public static WeikeResponse<Object> success(Object o) {
		WeikeResponse<Object> result = new WeikeResponse<Object>();
		result.setSuccess(true);
		result.setData(o);
		return result;
	}

	/**
	 * 失败返回
	 * 
	 * @param responseCode
	 * @return
	 */
	public static WeikeResponse<Object> fail(ResponseCode responseCode) {
		WeikeResponse<Object> result = new WeikeResponse<Object>();
		result.setSuccess(false);
		result.setError(responseCode);
		return result;
	}

	/**
	 * 失败返回
	 * @param code
	 * @param message
	 * @return
	 */
	public static WeikeResponse<Object> fail(String code, String message) {
		WeikeResponse<Object> result = new WeikeResponse<Object>();
		result.setSuccess(false);
		result.setError(new ResponseCode(code, message));
		return result;
	}



}
