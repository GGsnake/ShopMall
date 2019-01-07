package com.superman.superman.service.impl;

import com.superman.superman.dao.SysDaygoodsDao;
import com.superman.superman.model.SysDaygoods;
import com.superman.superman.service.SysDaygoodsService;
import com.superman.superman.utils.PageParam;
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
	public List<SysDaygoods> queryList(PageParam pageParam){
		List<SysDaygoods> sysDaygoods = daygoodsDao.queryListGod(pageParam.getStartRow(), pageParam.getPageSize());

//		for (SysDaygoods sy:sysDaygoods){
//			sy.get
//		}
		return null;
	}
	
	@Override
	public int queryTotal(){
		return daygoodsDao.queryTotal();
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
