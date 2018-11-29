package com.superman.superman.service;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.utils.PageParam;

/**
 * Created by liujupeng on 2018/11/8.
 */
public interface MemberService {
String aaaa();
String bbb();
String ccc();
    JSONObject getMyMoney(Long uid);

    JSONObject getMyTeam(Long userId, PageParam pageParam);
    JSONObject getMyNoFans(Long userId, PageParam pageParam);
}
