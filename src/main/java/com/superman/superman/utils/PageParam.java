package com.superman.superman.utils;

import java.io.Serializable;

/**
 * 
 * @author daiwenhua
 *
 */
public class PageParam implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6712010429290414297L;

	int pageNo;
	
	int pageSize;
	
	int startRow;
	
	int endRow;
	
	public PageParam(){}
	
	public PageParam(int pageNo, int pageSize) {
		super();
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.startRow = (pageNo - 1) * pageSize;
		this.endRow = pageNo * pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public int getEndRow() {
		return endRow;
	}

	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}
	
	
}
