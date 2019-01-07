package com.superman.superman.dao;

import com.superman.superman.model.SysDaygoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysDaygoodsDao extends BaseDao<SysDaygoods> {

    List<SysDaygoods> queryListGod(@Param("page")Integer page, @Param("pageSize")Integer pageSize);
    List<SysDaygoods> getImages(List list);

}
