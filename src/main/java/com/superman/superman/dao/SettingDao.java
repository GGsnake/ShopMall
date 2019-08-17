package com.superman.superman.dao;

import com.superman.superman.model.Config;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface SettingDao {

    /**
     * 配置查找
     * @return
     */
    @Select("select * from jh_config where ConfigNo=#{no}")
    Config querySetting(String no);


}
