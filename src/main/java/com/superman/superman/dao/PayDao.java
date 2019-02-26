package com.superman.superman.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface PayDao {
    @Insert("insert into jh_pay_log(userId,orderSn,accept,createTime) value (#{id},#{sn},0,now())")
    Integer addPayLog(Map<String,Object> map);


    @Insert("insert into jh_taobao_hot(pictUrl,shopTitle,title,commissionRate,coupon,zkFinalPrice,volume,numIid,createTime,status) values (#{pictUrl},#{shopTitle},#{title},#{commissionRate}" +
            ",#{coupon},#{zkFinalPrice},#{volume},#{numIid}," +
            ",now(),0)")
    Integer test();

    @Select("select accept from jh_pay_log where userId=#{id} limit 1")
    Integer queryAccept(Integer id);



}
