package com.superman.superman.service;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.model.Userinfo;
import com.superman.superman.utils.PageParam;

/**
 * Created by liujupeng on 2018/11/8.
 */
public interface MemberService {
    /**
     *
     * @param uid
     * @return
     */
    JSONObject getMyMoney(Long uid);

    /**
     *
     * @param userId
     * @param pageParam
     * @return
     */
    JSONObject getMyTeam(Long userId, PageParam pageParam);

    /**
     *
     * @param userId
     * @param pageParam
     * @return
     */
    JSONObject getMyNoFans(Long userId, PageParam pageParam);

    /**
     *
     * @param userId
     * @return
     */
    JSONObject queryMemberDetail(Long userId);

    /**
     *
     * @param uid
     * @return 查询我的待结算
     */
    JSONObject getMyMoneyOf(Long uid);

}
