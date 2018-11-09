package com.superman.superman.dao;

import com.superman.superman.model.Role;
import com.superman.superman.model.Test;
import com.superman.superman.model.UserPdd;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by liujupeng on 2018/11/6.
 */
@Mapper
public interface PddDao {
    int insert(UserPdd userPdd);

    UserPdd selectUsers(@Param("userPid") Long userPid);
    Test selectT(@Param("test") Long userPid);
    Role selectR(@Param("uid") Long userPid);
    List<Test> selectRchid(@Param("test") Test t);
    List<Role> selectRchidlist(@Param("list") List ll);
    List<Test> selectTchidlist(@Param("list") List ll);
   Integer countAgentId(@Param("id") Integer id);
}
