package com.superman.superman.dao;

import com.superman.superman.model.SysDaygoods;
import com.superman.superman.model.SysJhImage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysDaygoodsDao extends BaseDao<SysDaygoods> {
    /**
     * 每日爆款查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @Select("select * from jh_day_goods order by createtime desc  limit #{page}, #{pageSize} ")
    List<SysDaygoods> queryListGod(@Param("page") Integer page, @Param("pageSize") Integer pageSize);

    /**
     * 每日爆款统计
     * @return
     */
    @Select("select IFNULL(count(*),0) from jh_day_goods")
    Integer countDayGoods();

    /**
     * 获取每日爆款的图片
     * @param id
     * @return
     */
    @Select("select image from jh_day_image where day=#{id}")
    List<String> getImages(Integer id);

}
