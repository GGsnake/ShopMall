package com.superman.superman.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dto.SysJhProblem;
import com.superman.superman.dto.SysJhVideoTutorial;
import com.superman.superman.dao.SysAdviceDao;
import com.superman.superman.model.Config;
import com.superman.superman.model.SysAdvice;
import com.superman.superman.redis.RedisUtil;
import com.superman.superman.service.OtherService;
import com.superman.superman.service.SysAdviceService;
import com.superman.superman.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * @author heguoliang
 * @Description:
 * @date 2018-12-22 18:05:59
 */
@RestController
@RequestMapping("/advice")
public class SysAdviceController{

	@Autowired
	private RedisUtil redisUtil;
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

	/**
	 * 官方通知
	 */
	@PostMapping("/adv")
	public WeikeResponse queryAdviceForDev(PageParam pageParam){
		String key = "adv:" + pageParam.getPageNo();
		if (redisUtil.hasKey(key)) {
			return WeikeResponseUtil.success(JSONObject.parseObject(redisUtil.get(key)));
		}
			//查询列表数据
		PageParam param = new PageParam(pageParam.getPageNo(), pageParam.getPageSize());
		JSONArray adviceList = otherService.queryAdviceForDev(param);
		int total = sysAdviceDao.countAdvSum();
		JSONObject var1=new JSONObject();
		var1.put("pageData", adviceList);
		var1.put("pageCount", total);
		redisUtil.set(key, var1.toJSONString());
		redisUtil.expire(key, 150, TimeUnit.SECONDS);
		return WeikeResponseUtil.success(var1);
	}


	/**
	 * 查询常见问题
	 */
	@PostMapping("/problem")
	public WeikeResponse querySysJhProblem(PageParam pageParam){
			//查询列表数据
		PageParam param = new PageParam(pageParam.getPageNo(), pageParam.getPageSize());
		List<SysJhProblem> total = sysAdviceDao.querySysJhProblem(param.getStartRow(),param.getPageSize());
		int sum = sysAdviceDao.countProblem();
		JSONObject var1=new JSONObject();
		var1.put("pageData", total);
		var1.put("pageCount", sum);
		return WeikeResponseUtil.success(var1);
	}


	/**
	 * 查询视频教程
	 */
	@PostMapping("/tutorial")
	public WeikeResponse querySysJhVideoTutorial(PageParam pageParam){
			//查询列表数据
		PageParam param = new PageParam(pageParam.getPageNo(), pageParam.getPageSize());
		List<SysJhVideoTutorial> total = sysAdviceDao.querySysJhVideoTutorial(param.getStartRow(),param.getPageSize());
		int sum = sysAdviceDao.countTutorial();
		JSONObject var1=new JSONObject();
		var1.put("pageData", total);
		var1.put("pageCount", sum);
		return WeikeResponseUtil.success(var1);
	}


	/**
	 * 版本号检测
	 */
	@GetMapping("/version")
	public WeikeResponse getVersion(){
		JSONObject data=new JSONObject();
		data.put("version", otherService.querySetting("Version").getConfigValue());
		data.put("ios", otherService.querySetting("Ios").getConfigValue());
		data.put("android",otherService.querySetting("Android").getConfigValue());
		return WeikeResponseUtil.success(data);
	}
	/**
	 * 客服接口
	 */
	@GetMapping("/customer")
	public WeikeResponse customer(){
		//查询列表数据
		JSONObject var1=new JSONObject();
		var1.put("image", otherService.querySetting("WxImage").getConfigValue());
		var1.put("account", otherService.querySetting("Account").getConfigValue());
		var1.put("name",otherService.querySetting("Name").getConfigValue());
		return WeikeResponseUtil.success(var1);
	}
	/**
	 * 联系我们
	 */
	@GetMapping("/contact")
	public WeikeResponse contactWe(){
		Config wxAccount = otherService.querySetting("WxImage");
		return WeikeResponseUtil.success(wxAccount.getConfigValue());
	}


}
