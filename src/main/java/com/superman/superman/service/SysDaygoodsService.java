package com.superman.superman.service;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.model.SysDaygoods;
import com.superman.superman.utils.PageParam;

/**
 * @author heguoliang
 * @Description: TODO(每日爆款)
 * @date 2019-01-04 20:47:50
 */
public interface SysDaygoodsService {
	/**
	 * 查询每日爆款
	 * @param pageParam
	 * @return
	 */
	JSONObject queryList(PageParam pageParam);
	/**
	 * 更新
	 * @param daygoods
	 */
	void update(SysDaygoods daygoods);
	/**
	 * 删除
	 * @param id
	 */
	void delete(Integer id);

}
