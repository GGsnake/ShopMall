package com.superman.superman.utils;

/**
 * 系统返回码
 *
 * @author Yaphis 2015年9月8日 下午2:08:13
 */
public class ResponseCode {

    // 系统公用返回码
    public static final ResponseCode COMMON_SUCCESS = new ResponseCode("000000", "成功");
    public static final ResponseCode COMMON_SYSTEM_ERROR = new ResponseCode("000001", "系统异常");

    // 业务公用返回码
    public static final ResponseCode COMMON_PARAMS_MISSING = new ResponseCode("100000", "请求参数不全");
    public static final ResponseCode COMMON_AUTHORITY_ERROR = new ResponseCode("100001", "无权限");
    public static final ResponseCode COMMON_USER_NOT_EXIST = new ResponseCode("100002", "用户不存在");
    public static final ResponseCode COMMON_USER_INFO_IMPERFECT = new ResponseCode("100003", "用户信息不完整");
    public static final ResponseCode COMMON_USER_NOT_LOGIN = new ResponseCode("100004", "用户未登录");
    public static final ResponseCode COMMON_PARAMS_ILLEGAL = new ResponseCode("100005", "请求参数非法");
    public static final ResponseCode COMMON_REQUEST_FAIL = new ResponseCode("100006", "请求失败");
    public static final ResponseCode COMMON_STOCK_FAIL = new ResponseCode("100007", "库存不足");
    public static final ResponseCode COMMON_STOCK_OVER = new ResponseCode("100008", "购买超限");
    public static final ResponseCode COMMON_STOCK_ZERO = new ResponseCode("100009", "库存为0");
    public static final ResponseCode COMMON_GOODS_NULL = new ResponseCode("100010", "商品不存在或已下架");
    public static final ResponseCode ADD_GOODS_TIMES = new ResponseCode("100011", "发布商品次数超限");
    public static final ResponseCode NOT_STORE_PEOPLE = new ResponseCode("100012", "不是该店的人员");
    public static final ResponseCode ADD_GOODS_ERROR = new ResponseCode("100013", "新增商品异常");
    public static final ResponseCode ADD_STYLE_ERROR = new ResponseCode("100014", "新增店铺商品布局异常");
    public static final ResponseCode ADD_ADDRESS_ERROR = new ResponseCode("100015", "收货地址最多10条");
    public static final ResponseCode TOKEN_ERROR = new ResponseCode("100016", "token无效");
    public static final ResponseCode TOKEN_NULL = new ResponseCode("100017", "缺少token参数");
    public static final ResponseCode TAG_EXIST = new ResponseCode("100018", "标签已存在");
    public static final ResponseCode GET_ACESS_TOKEN_ERROR = new ResponseCode("100019", "获取acess_token失败");
    public static final ResponseCode CREATE_CODE_ERROR = new ResponseCode("100020", "生成店铺小程序码异常");
    public static final ResponseCode STORE_NOT_EXISTS = new ResponseCode("100021", "店铺不存在");
    public static final ResponseCode TAG_OUTSIZE = new ResponseCode("100022", "可创建的标签已到上限");
    public static final ResponseCode LOGIN_ERROR = new ResponseCode("100023", "登录失败");
    public static final ResponseCode EDIT_USERINFO_ERROR = new ResponseCode("100024", "更新用户信息异常");
    public static final ResponseCode ADD_RECORD_ERROR = new ResponseCode("100025", "添加关注失败");
    public static final ResponseCode APPLY_SUCCESS = new ResponseCode("100026", "提交成功");
    public static final ResponseCode APPLY_FAIL = new ResponseCode("100027", "提交失败或者已经提交过");
    public static final ResponseCode COMMON_USER_PASSWORD_ERROR = new ResponseCode("100028", "密码错误");
    public static final ResponseCode DELETE_ERROR = new ResponseCode("100033", "删除错误");
    public static final ResponseCode PERMISSIONS_ERROR = new ResponseCode("100053", "无权限");

    private String errCode;
    private String errMsg;

    public ResponseCode(String errCode, String errMsg) {
        super();
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public String getErrCode() {
        return errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    @Override
    public String toString() {
        return "[errCode=" + errCode + ", errMsg=" + errMsg + "]";
    }

}
