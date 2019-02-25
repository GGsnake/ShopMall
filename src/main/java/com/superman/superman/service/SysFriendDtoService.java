package com.superman.superman.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dto.SysFriendDto;
import com.superman.superman.utils.PageParam;

public interface SysFriendDtoService{
    public JSONObject queryList(PageParam pageParam);
}
