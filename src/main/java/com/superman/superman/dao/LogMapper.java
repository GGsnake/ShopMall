package com.superman.superman.dao;

import com.superman.superman.model.UserLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by liujupeng on 2018/11/24.
 */
public interface LogMapper {

    @Insert("insert into userlog(userId,operation,ip,createTime) value(#{userId},#{operation},#{ip},now())")
    int addUserLogin(UserLog userLog);
}
