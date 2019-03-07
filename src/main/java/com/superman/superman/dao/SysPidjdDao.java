package com.superman.superman.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysPidjdDao  {
    @Insert("insert ignore jh_pid_jd(pid) value(#{pid})")
    Integer addPidJd(Long pid);
    @Insert("insert ignore jh_pid_pdd(pid) value(#{pid})")
    Integer addPidPdd(String pid);
    @Insert("insert ignore jh_pid_tb(pid) value(#{pid})")
    Integer addPidTb(Long pid);

}
