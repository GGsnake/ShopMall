package com.superman.superman.controller;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.superman.superman.model.SysAdvice;
import com.superman.superman.service.SysAdviceService;
import com.superman.superman.utils.PageUtils;
import com.superman.superman.utils.Query;
import com.superman.superman.utils.WeikeResponse;
import com.superman.superman.utils.WeikeResponseUtil;
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
	

	

	
}
