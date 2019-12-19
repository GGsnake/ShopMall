package com.superman.superman.utils;


public class ResponseUtil {

	/**
	 * 成功返回
	 * 
	 * @return
	 */
	public static Response<Object> success() {
		Response<Object> result = new Response<Object>();
		result.setSuccess(true);
		return result;
	}

	/**
	 * 成功返回
	 *
	 * @return
	 */
	public static Response<Object> success(Object o) {
		Response<Object> result = new Response<Object>();
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
	public static Response<Object> fail(ResponseCode responseCode) {
		Response<Object> result = new Response<Object>();
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
	public static Response<Object> fail(String code, String message) {
		Response<Object> result = new Response<Object>();
		result.setSuccess(false);
		result.setError(new ResponseCode(code, message));
		return result;
	}



}
