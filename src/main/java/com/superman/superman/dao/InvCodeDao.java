package com.superman.superman.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface InvCodeDao {
    /**
     * 新增邀请码
     * @param id
     * @return
     */
    @Insert("insert into invcode(userId,createTime) values (#{id},now())")
    Integer insertCode(Long id);
    /**
     * 查询邀请码是否存在
     * @param id
     * @return
     */
    @Select("select ifnull(id,0) from invcode where userId=#{id}")
    Integer queryCodeId(Long id);

    /**
     * 根据邀请码查询
     * @param id
     * @return
     */
    @Select("select userId from invcode where id=#{id}")
    Integer queryUserCode(Long id);

    @Select("select id from invcode where userId=#{id}")
    Integer queryInvCodeId(Long id);

}
