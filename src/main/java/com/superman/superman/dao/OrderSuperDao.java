package com.superman.superman.dao;

import com.superman.superman.model.Tboder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 专门统计各平台订单的dao
 */
public interface OrderSuperDao {
    /**
     * 批量统计所以平台订单佣金（预估）
     * @param list
     * @return
     */
    Integer queryAllDevOrder(@Param("list") List list, @Param("devId") Integer devId);

}