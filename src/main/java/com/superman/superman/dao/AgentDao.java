package com.superman.superman.dao;

import com.superman.superman.model.Agent;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by liujupeng on 2018/11/23.
 */
public interface AgentDao {
    @Select("SELECT * FROM agent WHERE userId = #{id} and status=0")
    List<Agent> queryForUserId(Integer id);
    @Select("SELECT * FROM agent WHERE agentId = #{id} and status=0")
    List<Agent> queryForAgentId(Integer id);
    @Insert("INSERT INTO agent(agentId, userId,crateTime) VALUES(#{agentId}, #{userId},now()")
    int insert(Agent agent);
}
