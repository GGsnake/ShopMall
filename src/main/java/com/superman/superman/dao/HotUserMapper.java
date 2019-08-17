package com.superman.superman.dao;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface HotUserMapper {

    /**
     * 申请拼多多推广位
     * @return
     */
    @Select("select pid from jh_pid_pdd  where status=0 limit 1")
    String createPddPid();
    /**
     * 申请京东推广位
     * @return
     */
    @Select("select pid from jh_pid_jd  where status=0 limit 1")
    String createJdPid();
    /**
     * 删除拼多多推广位
     * @return
     */
    @Update("update  jh_pid_pdd set status=1  where pid=#{pid}")
    Integer deletePddPid(String pid);
    /**
     * 删除京东推广位
     * @return
     */
    @Update("update  jh_pid_jd set status=1  where pid=#{pid}")
    Integer deleteJdPid(String pid);
}