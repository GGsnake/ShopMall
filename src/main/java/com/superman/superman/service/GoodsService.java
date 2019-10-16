package com.superman.superman.service;

import com.superman.superman.model.enums.Platform;
import com.superman.superman.utils.PageParam;

import java.util.List;

/**
 * Created by liujupeng on 2018/11/14.
 */
public interface GoodsService {
    /**
     * 查询商品
     *
     * @param pageParam
     * @return
     */
    Object searchGoods(Platform platform, PageParam pageParam);


}
