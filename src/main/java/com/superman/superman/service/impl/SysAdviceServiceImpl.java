package com.superman.superman.service.impl;

import com.superman.superman.dao.AgentDao;
import com.superman.superman.dao.SysAdviceDao;
import com.superman.superman.model.SysAdvice;
import com.superman.superman.model.SysJhAdviceOder;
import com.superman.superman.service.SysAdviceService;
import com.superman.superman.utils.PageParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("adviceService")
public class SysAdviceServiceImpl implements SysAdviceService {

	@Autowired
	private SysAdviceDao adviceDao;
	@Autowired
	private AgentDao agentDao;
	
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

	@Override
	public List<SysJhAdviceOder> queryListOderAdvice(Long uid,PageParam pageParam) {
		List<Long> var=new ArrayList();
		var.add(uid);
		List<Long> ids = agentDao.queryForAgentIdNew(uid.intValue());
		if (ids!=null){
			var.addAll(ids);
		}
		List<SysJhAdviceOder> sysJhAdviceOders = adviceDao.querySysOderAdvice(var,pageParam.getStartRow(),pageParam.getPageSize());
		return sysJhAdviceOders;
	}
	@Override
	public Integer countListOderAdvice(Long uid) {
		List<Long> var=new ArrayList();
		var.add(uid);
		List<Long> ids = agentDao.queryForAgentIdNew(uid.intValue());
		if (ids!=null){
			var.addAll(ids);
		}
		Integer sum = adviceDao.countSysOderAdvice(var);
		return sum;
	}

}
