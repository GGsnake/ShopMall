package com.superman.superman.dao;

import com.superman.superman.model.ScoreBean;
import com.superman.superman.model.Userinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by snake on 2018/11/15.
 */
@Mapper
public interface ScoreDao {
    /**
     * 查询是否领取过每日商品浏览积分
     *
     * @param user
     * @return
     */
    ScoreBean isExit(@Param("score") ScoreBean user);

    /**
     * 增加积分
     *
     * @param user
     * @return
     */
    Integer addScore(@Param("score") ScoreBean user);

    /**
     * 统计积分
     *
     * @param uid
     * @return
     */
    @Select("select userScore from userinfo where id=#{uid} and status=0")
    Integer countScore(Long uid);

    /**
     * 积分提现
     * @param user
     * @return
     */
    @Update("update userinfo set cash=cash+ #{cash} where id=#{id}")
    Integer updateCash(Userinfo user);

    /**
     * 积分减少0
     * @param user
     * @return
     */
    @Update("update userinfo set userScore=0 where id=#{id}")
    Integer updateScoreZero(Userinfo user);

    /**
     * 增加用户积分
     * @param user
     * @return
     */
    @Update("update userinfo set userScore=userScore+#{userscore} where id=#{id}")
    Integer updateUserScore(Userinfo user);
}
