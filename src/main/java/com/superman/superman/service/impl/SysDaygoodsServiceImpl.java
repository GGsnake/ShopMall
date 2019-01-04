package com.superman.superman.service.impl;

import com.superman.superman.dao.SysDaygoodsDao;
import com.superman.superman.model.SysDaygoods;
import com.superman.superman.service.SysDaygoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service("daygoodsService")
public class SysDaygoodsServiceImpl implements SysDaygoodsService {

	@Autowired
	private SysDaygoodsDao daygoodsDao;
	
	@Override
	public SysDaygoods queryObject(Integer id){
		return daygoodsDao.queryObject(id);
	}
	
	@Override
	public List<SysDaygoods> queryList(Map<String, Object> map){
		return daygoodsDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return daygoodsDao.queryTotal(map);
	}
	
	@Override
	public void save(SysDaygoods daygoods){
		daygoodsDao.save(daygoods);
	}
	
	@Override
	public void update(SysDaygoods daygoods){
		daygoodsDao.update(daygoods);
	}
	
	@Override
	public void delete(Integer id){
		daygoodsDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		daygoodsDao.deleteBatch(ids);
	}
	
}
