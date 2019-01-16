package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.SysDaygoodsDao;
import com.superman.superman.dto.SysDayGoodDto;
import com.superman.superman.model.SysDaygoods;
import com.superman.superman.model.SysJhImage;
import com.superman.superman.service.SysDaygoodsService;
import com.superman.superman.utils.PageParam;
import org.springframework.beans.BeanUtils;
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
	public JSONObject queryList(PageParam pageParam){
		PageParam temp =new PageParam(pageParam.getPageNo(),pageParam.getPageSize());
		List<SysDaygoods> sysDaygoods = daygoodsDao.queryListGod(temp.getStartRow(), temp.getPageSize());
		Integer count = daygoodsDao.countDayGoods();
		JSONObject var=new JSONObject();
		JSONArray data=new JSONArray();
		if (count==null||count==0){
			var.put("pageCount",0);
			var.put("pageaData",data);
			return var;
		}
		for (SysDaygoods sy:sysDaygoods){
			SysDayGoodDto dto=new SysDayGoodDto();
			BeanUtils.copyProperties(sy,dto);
			List<String> images = daygoodsDao.getImages(sy.getId());
			dto.setContent_Images(images);
			data.add(dto);
		}
		var.put("pageCount",count);
		var.put("pageaData",data);
		return var;
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
