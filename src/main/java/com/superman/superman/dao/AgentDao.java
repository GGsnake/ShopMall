package com.superman.superman.dao;

import com.superman.superman.model.Agent;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by liujupeng on 2018/11/23.
 */
public interface AgentDao {
    @Select("SELECT * FROM agent WHERE userId = #{id} and status=0")
    List<Agent> queryForUserId(Integer id);

    @Select("SELECT userId FROM agent WHERE agentId= #{id} and status=0 order by createTime limit #{star},#{end} ")
    List<Long> queryForUserIdLimt(@Param("id") Long id,@Param("star") Integer star,@Param("end") Integer end);

    @Select("SELECT userId,createTime FROM agent WHERE agentId= #{id} and status=0 order by createTime limit #{star},#{end} ")
    List<Agent> queryForUserIdAgentLimt(@Param("id") Long id,@Param("star") Integer star,@Param("end") Integer end);


    @Select("SELECT count(userId) FROM agent WHERE agentId= #{id} and status=0")
   Integer queryForUserIdCount(@Param("id") Long id);


    @Select("SELECT count(agentId) FROM agent WHERE agentId= #{id}")
    Integer countRecommd(@Param("id") Long id);

    @Select("SELECT userId,agentId,agentName,userName FROM agent WHERE agentId in (SELECT userId FROM agent WHERE agentId= #{id}) order by createTime limit #{star},#{end}")
    List<Agent> countRecommdToSum(@Param("id") Long id,@Param("star") Integer star,@Param("end") Integer end);


    @Select("SELECT userId,agentId FROM agent WHERE agentId in (SELECT userId FROM agent WHERE agentId= #{id}) order by createTime limit #{star},#{end}")
    List<Agent> countNoMyFans(@Param("id") Long id,@Param("star") Integer star,@Param("end") Integer end);

    @Select("SELECT count(userId) FROM agent WHERE agentId in (SELECT userId FROM agent WHERE agentId= #{id})  ")
    Integer countNoMyFansSum(@Param("id") Long id);



    @Select("SELECT count(userId)FROM agent WHERE agentId in (SELECT userId FROM agent WHERE agentId= #{id}) order by createTime limit #{star},#{end}")
     Integer countRecommdToIntCount(@Param("id") Long id);

    @Select("SELECT * FROM agent WHERE agentId = #{id} and status=0")
    List<Agent> queryForAgentList(Integer id);
    @Select("SELECT * FROM agent WHERE userId = #{id} and status=0")
    List<Agent> queryForUserList(Integer id);

    @Select("SELECT userId FROM agent WHERE agentId = #{id} and status=0")
    List<String> queryForAgentId(Integer id);

    @Select("SELECT userId FROM agent WHERE agentId = #{id} and status=0")
    List<Long> queryForAgentIdNew(Integer id);
    @Insert("INSERT INTO agent(agentId, userId,crateTime) VALUES(#{agentId}, #{userId},now()")
    int insert(Agent agent);
}
