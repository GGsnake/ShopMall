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

    @Select("SELECT count(agentId) FROM agent WHERE agentId= #{id}")
    Integer countRecommd(@Param("id") Long id);

    @Select("SELECT userId,agentId FROM agent WHERE agentId in (SELECT userId FROM agent WHERE agentId= #{id}) order by createTime limit #{star},#{end}")
    List<Agent> countRecommdToSum(@Param("id") Long id,@Param("star") Integer star,@Param("end") Integer end);

    @Select("SELECT userId FROM agent WHERE agentId = #{id} and status=0")
    List<String> queryForAgentId(Integer id);
    @Insert("INSERT INTO agent(agentId, userId,crateTime) VALUES(#{agentId}, #{userId},now()")
    int insert(Agent agent);
}
