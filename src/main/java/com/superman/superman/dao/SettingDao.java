package com.superman.superman.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface SettingDao {
    //轮播图
    @Select("select url from jh_banner_img order by id desc limit 3")
    List<String> queryBanner();
}
