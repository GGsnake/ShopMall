package com.superman.superman.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.utils.PageParam;

public interface SysFriendDtoService{
     JSONObject queryList(PageParam pageParam);
     JSONArray queryListFriend(PageParam pageParam);
}
