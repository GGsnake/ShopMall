package com.superman.superman.dao;

import java.util.List;
import java.util.Map;

/**
 * @author heguoliang
 * @Description: TODO(还需在XML文件里，有对应的SQL语句)
 * @date 2017-6-23 15:07
 */
public interface BaseDao<T> {
	
	void save(T t);
	
	void save(Map<String, Object> map);
	
	void saveBatch(List<T> list);
	
	int update(T t);
	
	int update(Map<String, Object> map);
	
	int delete(Object id);
	
	int delete(Map<String, Object> map);
	
	int deleteBatch(Object[] id);

	T queryObject(Object id);
	
	List<T> queryList(Map<String, Object> map);
	
	List<T> queryList(Object id);
	
	int queryTotal(Map<String, Object> map);

	int queryTotal();
	
	//导出数据
	List<T> exportData(Map<String, Object> map);
}
