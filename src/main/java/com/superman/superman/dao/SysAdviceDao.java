package com.superman.superman.dao;

import com.superman.superman.model.SysAdvice;
import com.superman.superman.model.SysDaygoods;
import com.superman.superman.model.SysJhAdviceDev;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysAdviceDao extends BaseDao<SysAdvice> {
    @Select("select * from jh_advice_dev limit #{offset}, #{limit} ")
    List<SysJhAdviceDev> queryAdviceDev(@Param("offset")Integer offset, @Param("limit")Integer limit);
    @Select("select count(*) from jh_advice_dev ")
    Integer tcountAdv();

}
