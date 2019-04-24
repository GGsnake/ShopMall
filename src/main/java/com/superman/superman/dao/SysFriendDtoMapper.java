package com.superman.superman.dao;

import com.superman.superman.model.SysJhTaobaoAll;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import com.superman.superman.dto.SysFriendDto;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SysFriendDtoMapper {
    /**
     *
     * @param page
     * @param pageSize
     * @return
     */
    @Select("select * from jh_friend_dto order by createTime desc limit #{page}, #{pageSize}")
    List<SysFriendDto> queryListGod(@Param("page")Integer page, @Param("pageSize")Integer pageSize);
    /**
     *随机选中商品
     * @return
     */
    @Select("select numiid from jh_taobao_all order by rand() limit 3")
    List<Long> random();
    /**
     *
     * @param page
     * @param pageSize
     * @return
     */
    @Select("select * from jh_friend_dto order by createTime desc limit #{page}, #{pageSize}")
    List<SysFriendDto> queryListFriend(@Param("page")Integer page, @Param("pageSize")Integer pageSize);
    /**
     *
     * @return
     */
    @Select("select count(*) from jh_friend_dto")
    Integer count();
    /**
     *
     * @return
     */
    @Select("select image from jh_friend_image where day=#{id}")
    List<String> getImages(Integer id);
    @Update("update  jh_friend_dto set goodIds=#{content}  where id=#{id}")
    void updateRandom(@Param("id") Integer id,@Param("content") String content);

}