//package com.superman.superman.utils;
//
//import com.alibaba.fastjson.JSONObject;
//
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//
///**
// * 后台系统返回
// *
// * @author Yaphis 2015年6月26日 下午6:06:32
// */
//public class ResponseResult<T> {
//
//    public ResponseResult() {
//        this.success();
//    }
//
//    /**
//     * 成功返回
//     */
//    public void success() {
//        createResponseResult(ResponseCode.COMMON_SUCCESS);
//    }
//
//    /**
//     * 成功返回(带附加信息)
//     *
//     * @param t
//     */
//    public void success(T t) {
//        createResponseResult(ResponseCode.COMMON_SUCCESS);
//        putOBJ(t);
//    }
//
//    /**
//     * 系统错误
//     */
//    public void systemError() {
//        createResponseResult(ResponseCode.COMMON_SYSTEM_ERROR);
//    }
//
//    /**
//     * 失败返回
//     *
//     * @param responseCode
//     */
//    public void fail(ResponseCode responseCode) {
//        createResponseResult(responseCode);
//    }
//
//    /**
//     * 异常返回
//     *
//     * @param exception
//     */
//    public void exception(Throwable exception) {
//        if (exception instanceof CommonException) {
//            CommonException comEx = (CommonException) exception;
//            createResponseResult(comEx.getErrCode(), comEx.getErrMsg());
//        } else {
//            createResponseResult(ResponseCode.COMMON_SYSTEM_ERROR.getErrCode(), ResponseCode.COMMON_SYSTEM_ERROR.getErrMsg() + "【" + exception.getMessage() + "】");
//        }
//    }
//
//    /**
//     * 获取json返回
//     *
//     * @return
//     */
//    public String jsonResult() {
//        String resJson = JSONObject.toJSONString(resultMap);
//        return resJson;
//    }
//
//    /**
//     * 获取map返回
//     *
//     * @return
//     */
//    public Map<String, String> mapReulst() {
//        Map<String, String> resMap = new HashMap<String, String>();
//        Iterator<String> resultMapIt = resultMap.keySet().iterator();
//        while (resultMapIt.hasNext()) {
//            String key = resultMapIt.next();
//            String value = resultMap.get(key).toString();
//            resMap.put(key, value);
//        }
//        return resMap;
//    }
//
//    protected void createResponseResult(String resCode, String resMsg) {
//        resultMap.put(RESCODE, resCode);
//        resultMap.put(RESMSG, resMsg);
//    }
//
//    protected void createResponseResult(ResponseCode responseCode) {
//        resultMap.put(RESCODE, responseCode.getErrCode());
//        resultMap.put(RESMSG, responseCode.getErrMsg());
//    }
//
//    /**
//     * 添加附加信息
//     *
//     * @param t
//     */
//    protected void putOBJ(T t) {
//        resultMap.put(OBJ, t);
//    }
//
//    /**
//     * 是否成功
//     *
//     * @return
//     */
//    public boolean isSuccess() {
//        return this.resultMap.get(RESCODE).equals(ResponseCode.COMMON_SUCCESS.getErrCode());
//    }
//
//    /**
//     * 获取返回信息
//     *
//     * @return
//     */
//    public String getMessage() {
//        return this.resultMap.get(RESMSG).toString();
//    }
//
//    /**
//     * 返回码
//     */
//    private static final String RESCODE = "RESCODE";
//    /**
//     * 返回信息
//     */
//    private static final String RESMSG = "RESMSG";
//
//    /**
//     * 附加信息
//     */
//    private static final String OBJ = "OBJECT";
//
//    /**
//     * 返回参数
//     */
//    private Map<String, Object> resultMap = new HashMap<String, Object>();
//}
