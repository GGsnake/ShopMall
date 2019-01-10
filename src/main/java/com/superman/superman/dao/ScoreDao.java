package com.superman.superman.dao;

import com.superman.superman.model.ScoreBean;
import com.superman.superman.model.Userinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by liujupeng on 2018/11/15.
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
    @Select("select ifNULL(sum(score),0) from score_user where userId=#{uid} and status=0")
    Integer countScore(Long uid);

    /**
     * 增加用户积分
     * @param user
     * @return
     */
    @Update("update userinfo set userScore=userScore+ #{userScore} where id=#{id}")
    Integer updateUserScore(Userinfo user);
}
