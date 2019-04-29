package com.superman.superman.service;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.model.Userinfo;
import com.superman.superman.utils.PageParam;

/**
 * Created by liujupeng on 2018/11/8.
 */
public interface MemberService {
    /**
     * 获取预估收入
     * @param uid
     * @return
     */
    JSONObject getMyMoney(Long uid);

    /**
     * 获取我的团队直属会员和直属代理
     *
     * @param userId
     * @param pageParam 分页参数
     * @return
     */
    JSONObject getMyTeam(Long userId, PageParam pageParam);

    /**
     * 代理的会员下级（总代专属）
     * @param userId
     * @param pageParam
     * @return
     */
    JSONObject getMyNoFans(Long userId, PageParam pageParam);

    /**查询代理的业绩情况(总代理专属)
     * @param userId
     * @param myId
     * @return
     */
    JSONObject queryMemberDetail(Long userId, Integer myId);

    /**查询自己的的业绩情况
     * @param userId
     * @return
     */
    JSONObject queryMemberDetail(Long userId);


}
