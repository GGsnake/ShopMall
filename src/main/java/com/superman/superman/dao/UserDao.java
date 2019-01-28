package com.superman.superman.dao;

import com.superman.superman.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by liujupeng on 2018/11/5.
 */
@Mapper
public interface UserDao {

    User selectByName(@Param("userPhone") String userPhone);
    User queryUser(@Param("user") User user);
    Integer createUser(@Param("user") User user);
    User selectByPhone(@Param("userPhone") String userPhone);
    Integer updateUser(@Param("user") User user);
    Integer updateUserScore(@Param("user") User user);
    @Insert("insert into pidtb(pid)value(#{pid})")
    Integer sqltest(String pid);

}
