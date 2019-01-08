package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.SysDaygoodsDao;
import com.superman.superman.model.SysDaygoods;
import com.superman.superman.service.SysDaygoodsService;
import com.superman.superman.utils.PageParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("daygoodsService")
public class SysDaygoodsServiceImpl implements SysDaygoodsService {

	@Autowired
	private SysDaygoodsDao daygoodsDao;
	
	@Override
	public SysDaygoods queryObject(Integer id){
		return daygoodsDao.queryObject(id);
	}
	
	@Override
	public JSONArray queryList(PageParam pageParam){
		List<SysDaygoods> sysDaygoods = daygoodsDao.queryListGod(pageParam.getStartRow(), pageParam.getPageSize());
		JSONArray data=new JSONArray();

		for (SysDaygoods sy:sysDaygoods){
			JSONObject jp=new JSONObject();
			List a=new ArrayList();
			a.add("xxxxxxx.jpg");
			a.add("xxxxxxx2222222.jpg");
			a.add("xxxxxxx33333333.jpg");
			jp.put("titile",sy.getTitile());
			jp.put("content",sy.getTitile());
			jp.put("image",sy.getTitile());
			jp.put("content_Images",a);
			jp.put("createtime",sy.getTitile());
			data.add(jp);
		}
		return data;
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
