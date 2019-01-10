package com.superman.superman.dao;

import org.apache.ibatis.annotations.Select;

public interface HotUserMapper {
    @Select("select pid from pidtb limit 1")
    Long createTbPid();
    @Select("select pid from pidpdd limit 1")
    String createPddPid();
    @Select("delete from pidtb where pid=#{pid}")
    Integer deleteTbPid(Long pid);
    @Select("delete from pidpdd where pid=#{pid}")
    Integer deletePddPid(String pid);
}