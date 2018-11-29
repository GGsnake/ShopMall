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
JSONObject getMyTeam(Long uid, Integer i, Integer i1);

    JSONObject getMyTeam(long l, PageParam pageParam);
    JSONObject getMyNoFans(long l, PageParam pageParam);
}
