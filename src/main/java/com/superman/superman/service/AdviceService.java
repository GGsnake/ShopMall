package com.superman.superman.service;

import com.superman.superman.model.SysAdvice;
import com.superman.superman.model.SysJhAdviceOder;
import com.superman.superman.utils.PageParam;

import java.util.List;
import java.util.Map;

/**
 * @author heguoliang
 * @Description: TODO(通知表)
 * @date 2018-12-22 18:05:59
 */
public interface AdviceService {
	/**
	 * 批量查询官方通知
	 * @param map
	 * @return
	 */
	List<SysAdvice> queryList(Map<String, Object> map);

	/**
	 * 统计官方通知数量
	 * @param map
	 * @return
	 */
	int queryTotal(Map<String, Object> map);


	/**
	 * 查询用户订单通知(分页)
	 * @param uid
	 * @return
	 */
	List<SysJhAdviceOder> queryListOderAdvice(Long uid,PageParam pageParam);
	/**
	 * 统计用户订单通知数量
	 * @param uid
	 * @return
	 */
	Integer countListOderAdvice(Long uid);

}
