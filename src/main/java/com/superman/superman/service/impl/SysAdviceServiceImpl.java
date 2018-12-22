package com.superman.superman.service.impl;

import com.superman.superman.dao.SysAdviceDao;
import com.superman.superman.model.SysAdvice;
import com.superman.superman.service.SysAdviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service("adviceService")
public class SysAdviceServiceImpl implements SysAdviceService {

	@Autowired
	private SysAdviceDao adviceDao;
	
	@Override
	public SysAdvice queryObject(Integer id){
		return adviceDao.queryObject(id);
	}
	
	@Override
	public List<SysAdvice> queryList(Map<String, Object> map){
		return adviceDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return adviceDao.queryTotal(map);
	}
	
	@Override
	public void save(SysAdvice advice){
		adviceDao.save(advice);
	}
	
	@Override
	public void update(SysAdvice advice){
		adviceDao.update(advice);
	}
	
	@Override
	public void delete(Integer id){
		adviceDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		adviceDao.deleteBatch(ids);
	}
	
}
