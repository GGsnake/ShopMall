package com.superman.superman.utils;



/**
 * 标准返回
 * @author 80132724
 *
 * @param <T>
 */
public class Response<T> {
	private boolean success;
	private ResponseCode error;
	private T data;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public ResponseCode getError() {
		return error;
	}

	public void setError(ResponseCode error) {
		this.error = error;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "BaseReq [success=" + success + ", error=" + error + ", data=" + data + "]";
	}
}
