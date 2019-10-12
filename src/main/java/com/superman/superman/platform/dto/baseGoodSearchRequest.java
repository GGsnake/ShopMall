package com.superman.superman.platform.dto;

public abstract class baseGoodSearchRequest {
    private String goodName;
    private String pageSize;
    private String pageNo;

    public String getGoodName() {
        return goodName;
    }

    public baseGoodSearchRequest setGoodName(String goodName) {
        this.goodName = goodName;
        return this;
    }

    public String getPageSize() {
        return pageSize;
    }

    public baseGoodSearchRequest setPageSize(String pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public String getPageNo() {
        return pageNo;
    }

    public baseGoodSearchRequest setPageNo(String pageNo) {
        this.pageNo = pageNo;
        return this;
    }
}
