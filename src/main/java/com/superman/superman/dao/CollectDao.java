package com.superman.superman.dao;

import com.superman.superman.model.CollectBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by liujupeng on 2018/11/20.
 */
@Mapper
public interface CollectDao {
    List<CollectBean> query(@Param("id") Long uid);
    Integer delete(@Param("id") Long colId);
    Integer addCollect(@Param("col") CollectBean  collectBean);



}
