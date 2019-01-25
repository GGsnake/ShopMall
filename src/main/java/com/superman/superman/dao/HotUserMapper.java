package com.superman.superman.dao;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface HotUserMapper {
    @Select("select pid from jh_pid_tb limit 1")
    Long createTbPid();
    @Select("select pid from jh_pid_pdd limit 1")
    String createPddPid();
    @Update("update jh_pid_tb set status=1 where pid=#{pid}")
    Integer deleteTbPid(Long pid);
    @Update("update  jh_pid_pdd set status=1  where pid=#{pid}")
    Integer deletePddPid(String pid);


    @Update("update  userinfo set score=0  where id=#{id}")
    Integer updateScore(Integer id);
}