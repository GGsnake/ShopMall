package com.superman.superman.service;

import com.alibaba.fastjson.JSONArray;
import com.superman.superman.model.CollectBean;
import com.superman.superman.utils.PageParam;

/**
 * Created by liujupeng on 2018/11/20.
 */
public interface CollectService  {
    /**
     *
     * @param uid
     * @return
     */
    JSONArray queryCollect(Long uid, PageParam pageParam);

    /**
     *
     * @param colId
     * @return
     */
    Boolean deleteCollect(Integer colId,Long uid);
    /**
     *
     * @param uid
     * @return
     */
    Integer countCollect(Long uid);

    /**
     *
     * @param collectBean
     * @return
     */
    Boolean addCollect(CollectBean collectBean);
}
