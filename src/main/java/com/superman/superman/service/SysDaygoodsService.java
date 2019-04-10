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
	
	SysDaygoods queryObject(Integer id);
	
	JSONObject queryList(PageParam pageParam);
	
	int queryTotal();
	
	void save(SysDaygoods daygoods);
	
	void update(SysDaygoods daygoods);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);

}
