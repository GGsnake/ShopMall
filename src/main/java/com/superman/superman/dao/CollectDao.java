package com.superman.superman.dao;

import com.superman.superman.model.CollectBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by liujupeng on 2018/11/20.
 */
@Mapper
public interface CollectDao {

    List<CollectBean> query(@Param("id") Long uid,@Param("star") Integer star,@Param("end") Integer end);

    Integer delete(@Param("id") Long colId);

    @Select("select userId from collect where id=#{colId} limit 0,1" )
    CollectBean querySimple(Integer colId);

    @Select("select count(id) from collect where userId=#{userId}")
    Integer count(Long userId);


    Integer addCollect(@Param("col") CollectBean  collectBean);



}
