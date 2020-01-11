package com.superman.superman.dao;

import com.superman.superman.model.Agent;
import com.superman.superman.model.Userinfo;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by snake on 2018/11/23.
 */
public interface AgentDao {
    @Select("SELECT * FROM agent WHERE userId = #{id} and status=0")
    List<Agent> queryForUserId(Integer id);


    /**
     * 统计我的一级粉丝或者代理
     *
     * @param id
     * @return
     */
    @Select("SELECT ifnull(count(userId),0) FROM agent WHERE agentId= #{id} and status=0")
    Integer queryForUserIdCount(@Param("id") Long id);

    /**
     * 统计我的一级粉丝或者代理
     *
     * @param id
     * @return
     */
    @Select("SELECT ifnull(count(userId),0) FROM agent WHERE agentId= #{id} and status=0 and  to_days(createTime) = to_days(now())")
    Integer queryForUserIdCountToday(@Param("id") Long id);


    @Select("SELECT count(agentId) FROM agent WHERE agentId= #{id}")
    Integer countRecommd(@Param("id") Long id);

    @Select("SELECT userId,agentId,agentName,userName FROM agent WHERE agentId in (SELECT userId FROM agent WHERE agentId= #{id}) order by createTime limit #{star},#{end}")
    List<Agent> countRecommdToSum(@Param("id") Long id, @Param("star") Integer star, @Param("end") Integer end);


    @Select("SELECT userId,agentId FROM agent WHERE agentId IN (SELECT userId FROM agent WHERE agentId= #{id}) " +
            "ORDER BY createTime limit #{star},#{end}")
    List<Agent> countNoMyFans(@Param("id") Long id, @Param("star") Integer star, @Param("end") Integer end);

    /**
     * 统计我的非直属粉丝数量
     *
     * @param id
     * @return
     */
    @Select("SELECT IFNULL(COUNT(userId),0) FROM agent WHERE agentId in (SELECT userId FROM agent WHERE agentId= #{id})  ")
    Integer countNoMyFansSum(@Param("id") Long id);

    /**
     * 统计我的非直属粉丝(按时间)
     *
     * @param id
     * @return
     */
    @Select("SELECT IFNULL(COUNT(userId),0) FROM agent WHERE agentId in (SELECT userId FROM agent WHERE agentId= #{id}) and  to_days(createTime) = to_days(now())")
    Integer countNoMyFansSumToday(@Param("id") Long id);

    /**
     * 查询我的粉丝信息集合
     *
     * @param id
     * @return
     */
    @Select("Select u.Id,u.roleId,u.score from agent a left join userinfo u on a.userId=u.id and a.status=0 WHERE a.agentId=#{id}  and a.status=0")
    List<Userinfo> superQueryFansUserInfo(Integer id);

    /**
     * 根据代理id查询粉丝id
     *
     * @param id
     * @return
     */
    @Select("SELECT userId FROM agent WHERE agentId = #{id} and status=0")
    List<Long> queryForAgentIdNew(Integer id);


    /**
     * 建立代理关系
     *
     * @param agent
     * @return
     */
    @Insert("INSERT INTO agent(agentId, userId,createTime) VALUES(#{agentId}, #{userId},now())")
    Optional<Integer> insert(Agent agent);

    /**
     * 给用户升级成代理
     *
     * @param score
     * @param uid
     * @return
     */
    @Update("update userinfo set roleId=2 ,score=#{score},updateTime=now() where id=#{uid}")
    Integer upAgent(@Param("score") Integer score, @Param("uid") Integer uid);

    /**
     * 更新代理升级时间
     *
     * @param uid
     * @return
     */
    @Update("update agent set updateTime=now() where userId=#{uid}")
    Integer upAgentTime(@Param("uid") Integer uid);


    @Select("SELECT score FROM userinfo WHERE pddPid=#{id}")
    Integer queryUserScore(String id);

    @Select("SELECT score FROM userinfo WHERE rid=#{id}")
    Integer queryUserScoreTb(String id);

    @Select("SELECT score FROM userinfo WHERE jdPid=#{id}")
    Integer queryUserScoreJd(Long id);
}
