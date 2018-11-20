package com.superman.superman.service;

import com.superman.superman.model.CollectBean;

import java.util.List;

/**
 * Created by liujupeng on 2018/11/20.
 */
public interface CollectService  {
    /**
     *
     * @param uid
     * @return
     */
    List<CollectBean> queryCollect(Long uid);

    /**
     *
     * @param colId
     * @return
     */
    Boolean deleteCollect(Integer colId,Long uid);

    /**
     *
     * @param collectBean
     * @return
     */
    Boolean addCollect(CollectBean collectBean);
}
