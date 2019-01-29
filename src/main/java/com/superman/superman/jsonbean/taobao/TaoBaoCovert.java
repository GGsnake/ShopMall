package com.superman.superman.jsonbean.taobao;

public class TaoBaoCovert {
    private CovertResult result;
    private String request_id;
    private int code;
    public void setResult(CovertResult result) {
        this.result = result;
    }
    public CovertResult getResult() {
        return result;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }
    public String getRequest_id() {
        return request_id;
    }

    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }
}
