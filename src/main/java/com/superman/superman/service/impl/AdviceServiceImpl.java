package com.superman.superman.service.impl;

import com.superman.superman.dao.AgentDao;
import com.superman.superman.dao.SysAdviceDao;
import com.superman.superman.model.SysAdvice;
import com.superman.superman.model.SysJhAdviceOder;
import com.superman.superman.service.AdviceService;
import com.superman.superman.utils.PageParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("adviceService")
public class AdviceServiceImpl implements AdviceService {
	@Autowired
	private SysAdviceDao adviceDao;
	@Autowired
	private AgentDao agentDao;
	
	@Override
	public List<SysAdvice> queryList(Map<String, Object> map){
		return adviceDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return adviceDao.queryTotal(map);
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
