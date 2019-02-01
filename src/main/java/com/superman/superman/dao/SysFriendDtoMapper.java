package com.superman.superman.dao;

import com.superman.superman.model.SysDaygoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import com.superman.superman.dto.SysFriendDto;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysFriendDtoMapper {
    int insert(@Param("sysFriendDto") SysFriendDto sysFriendDto);

    int insertSelective(@Param("sysFriendDto") SysFriendDto sysFriendDto);

    int insertList(@Param("sysFriendDtos") List<SysFriendDto> sysFriendDtos);

    int updateByPrimaryKeySelective(@Param("sysFriendDto") SysFriendDto sysFriendDto);
    @Select("select * from jh_friend_dto limit #{page}, #{pageSize}")
    List<SysFriendDto> queryListGod(@Param("page")Integer page, @Param("pageSize")Integer pageSize);
    @Select("select count(*) from jh_friend_dto")
    Integer count();
    @Select("select image from jh_friend_image where day=#{id}")
    List<String> getImages(Integer id);

}
