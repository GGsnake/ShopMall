package com.superman.superman.dao;

import com.superman.superman.req.UpdateWxOpenId;
import com.superman.superman.model.Userinfo;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserinfoMapper {
    /**
     * @param id
     * @return
     */
    Userinfo selectByPrimaryKey(Long id);

    Optional<Userinfo> selectByPhone(String userPhone);

    @Select("select id,userPhone,rid,spid from userinfo where wxOpenId=#{id} limit 1")
    Userinfo queryUserWxOpenId(String id);


    @Select("select roleId from userinfo where id=#{id} limit 1")
    Integer queryMyRole(Integer id);

    /**
     * 手机号码绑定用户微信Id
     *
     * @param uu
     * @return
     */
    @Update("update userinfo set wxOpenId=#{id},userName=#{name},userPhoto=#{photo} where userPhone=#{phone}")
    Integer updateUserWxOpenId(UpdateWxOpenId uu);

    /**
     * 渠道更新
     *
     * @param userinfo
     * @return
     */
    @Update("update userinfo set rid=#{rid}  where id=#{id}")
    Integer updateUserForRid(Userinfo userinfo);

    /**
     * 更新PID
     *
     * @param userinfo
     * @return
     */
    @Update("update userinfo set pid=#{pid},tree=#{tree} where id=#{id}")
    Integer updatePid(Userinfo userinfo);

    /**
     * 渠道检查
     *
     * @param rid
     * @return
     */
    @Select("select id from userinfo where rid=#{rid} limit 1")
    Integer relationIdExits(String rid);

    @Select("select id,userName,createTime,userPhone,roleId from userinfo where id in (SELECT userId FROM agent WHERE agentId= #{agentId} and status=0) and status=0 ORDER by roleId asc  limit #{star},#{end}")
    List<Userinfo> selectInUserInfoForAgentId(@Param("agentId") Long agentId, @Param("star") Integer star, @Param("end") Integer end);


    @Insert("insert into invcode(userId,createTime) values (#{id},now())")
    Optional<Integer> insertCode(Long id);

    @Select("select ifnull(id,0) from invcode where userId=#{id}")
    Integer queryCodeId(Long id);

    @Select("select userId from invcode where id=#{id}")
    Integer queryUserCode(Long id);

    @Select("select id from invcode where userId=#{id}")
    Integer queryInvCodeId(Long id);


    @Select("select pid from userinfo where id=#{id} limit 1")
    Integer querypid(Integer id);


    Optional<Userinfo> queryUserInfoSingle(Userinfo userinfo);

}