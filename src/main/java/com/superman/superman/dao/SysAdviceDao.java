package com.superman.superman.dao;

import com.superman.superman.Dto.SysJhProblem;
import com.superman.superman.Dto.SysJhVideoTutorial;
import com.superman.superman.model.ApplyCash;
import com.superman.superman.model.SysAdvice;
import com.superman.superman.model.SysDaygoods;
import com.superman.superman.model.SysJhAdviceDev;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysAdviceDao extends BaseDao<SysAdvice> {
    @Select("select * from jh_advice_dev limit #{offset}, #{limit} ")
    List<SysJhAdviceDev> queryAdviceDev(@Param("offset") Integer offset, @Param("limit") Integer limit);

    @Select("select count(*) from jh_advice_dev ")
    Integer tcountAdv();

    //新增提现申请
    @Insert("insert into jh_cash_apply (`userId`, `money`,  `audit`,  `createtime` )value(#{userid},#{money}, 0,now()) ")
    Integer applyCash(ApplyCash applyCash);

    //查询提现申请记录
    @Select("select id,userId,money,audit,createTime from jh_cash_apply  where userId=#{uid} limit #{offset}, #{limit} ")
    List<ApplyCash> queryApplyCash(@Param("uid") Integer uid, @Param("offset") Integer offset, @Param("limit") Integer limit);

    //查询订单通知 代理或者粉丝
    @Select("select * from jh_cash_apply  where userId=#{uid} limit #{offset}, #{limit} ")
    List<ApplyCash> queryOderAdvice(@Param("uid") Integer uid, @Param("offset") Integer offset, @Param("limit") Integer limit);


    //查询常见问题
    @Select("select * from jh_problem   limit #{offset}, #{limit} ")
    List<SysJhProblem> querySysJhProblem(@Param("offset") Integer offset, @Param("limit") Integer limit);


    @Select("select count(*) from jh_problem ")
    Integer countProblem();


    @Select("select count(*) from jh_video_tutorial ")
    Integer countTutorial();


    //查询视频教程
    @Select("select * from jh_video_tutorial limit #{offset}, #{limit} ")
    List<SysJhVideoTutorial> querySysJhVideoTutorial(@Param("offset") Integer offset, @Param("limit") Integer limit);

}
