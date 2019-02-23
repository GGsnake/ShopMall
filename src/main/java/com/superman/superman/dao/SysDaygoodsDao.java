package com.superman.superman.dao;

import com.superman.superman.model.SysDaygoods;
import com.superman.superman.model.SysJhImage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysDaygoodsDao extends BaseDao<SysDaygoods> {
    @Select("select * from jh_day_goods order by createtime desc  limit #{page}, #{pageSize} ")
    List<SysDaygoods> queryListGod(@Param("page")Integer page, @Param("pageSize")Integer pageSize);

    @Select("select IFNULL(count(*),0) from jh_day_goods")
   Integer countDayGoods();
    @Select("select image from jh_day_image where day=#{id}")
    List<String> getImages(Integer id);

}
