package com.superman.superman.controller;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.SysAdviceDao;
import com.superman.superman.model.SysAdvice;
import com.superman.superman.model.SysDaygoods;
import com.superman.superman.model.SysJhAdviceDev;
import com.superman.superman.service.OtherService;
import com.superman.superman.service.SysAdviceService;
import com.superman.superman.service.SysDaygoodsService;
import com.superman.superman.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author heguoliang
 * @Description: TODO(通知表)
 * @date 2018-12-22 18:05:59
 */
@RestController
@RequestMapping("/advice")
public class SysAdviceController{

	@Autowired
	private SysAdviceService adviceService;
	@Autowired
	private SysAdviceDao sysAdviceDao;
	@Autowired
	private OtherService otherService;
	/**
	 * 列表
	 */
	@GetMapping("/list")
	public WeikeResponse list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<SysAdvice> adviceList = adviceService.queryList(query);
		int total = adviceService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(adviceList, total, query.getLimit(), query.getPage());
		JSONObject var1=new JSONObject();
		var1.put("page", pageUtil);
		return WeikeResponseUtil.success(var1);
	}

//	/**
//	 * 列表
//	 */
//	@GetMapping("/adv")
//	public WeikeResponse advList(@RequestParam Map<String, Object> params){
//		//查询列表数据
//        Query query = new Query(params);
//		List<SysAdvice> adviceList = adviceService.queryList(query);
//		int total = adviceService.queryTotal(query);
//
//		PageUtils pageUtil = new PageUtils(adviceList, total, query.getLimit(), query.getPage());
//		JSONObject var1=new JSONObject();
//		var1.put("page", pageUtil);
//
//		return WeikeResponseUtil.success(var1);
//	}
	/**
	 * 官方通知
	 */
	@GetMapping("/adv")
	public WeikeResponse queryAdviceForDev(PageParam pageParam){
			//查询列表数据
		PageParam param = new PageParam(pageParam.getPageNo(), pageParam.getPageSize());
		JSONArray adviceList = otherService.queryAdviceForDev(param);
		int total = sysAdviceDao.tcountAdv();
		JSONObject var1=new JSONObject();
		var1.put("pageData", adviceList);
		var1.put("pageCount", total);
		return WeikeResponseUtil.success(var1);
	}



	
}
