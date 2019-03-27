package com.superman.superman.dao;

import com.superman.superman.model.Tboder;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 专门统计各平台订单的dao
 */
public interface OrderSuperDao {

    /**
     * 批量统计淘宝订单佣金（预估）
     * @param list
     * @return
     */
    Integer queryJdOrder(List list);
    /**
     * 批量统计淘宝订单佣金（预估）
     * @param list
     * @return
     */
    Integer queryTaoBaoOrder(List list);
    /**
     * 批量统计淘宝订单佣金（预估）
     * @param list
     * @return
     */
    Integer queryPddOrder(List list);



}