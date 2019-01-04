package com.superman.superman.service;

import com.superman.superman.model.SysDaygoods;
import java.util.List;
import java.util.Map;

/**
 * @author heguoliang
 * @Description: TODO(每日爆款)
 * @date 2019-01-04 20:47:50
 */
public interface SysDaygoodsService {
	
	SysDaygoods queryObject(Integer id);
	
	List<SysDaygoods> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(SysDaygoods daygoods);
	
	void update(SysDaygoods daygoods);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);

}
