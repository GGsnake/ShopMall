package com.superman.superman.utils;

import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * @author heguoliang
 * @Description: TODO(返回数据)
 * @date 2017-6-23 15:07
 */
public class Result extends HashMap<String, Object> {

	private final static int CODE_SUCCESS=0;
	
	public static Result error() {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
	}
	
	public static Result error(String msg) {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
	}
	
	public static Result error(int code, String msg) {
		Result r = new Result();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	public static Result ok() {
		return ok("操作成功");
	}

	public static Result ok(String msg) {
		Result r = new Result();
		r.put("code", CODE_SUCCESS);
		r.put("msg", msg);
		return r;
	}

	public Result put(Map<String, Object> map) {
		super.putAll(map);
		return this;
	}

	public Result put(String key, Object value) {
		super.put(key, value);
		return this;
	}

}
