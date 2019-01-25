package com.superman.superman.dao;

import com.superman.superman.model.JdOder;
import com.superman.superman.model.Oder;
import com.superman.superman.model.Tboder;
import com.superman.superman.model.Userinfo;
import com.superman.superman.req.OderPdd;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * Created by liujupeng on 2018/12/19.
 */
@Mapper
public interface AllDevOderMapper {
    //    @Select("select  goods_id,promotion_amount,goods_name,order_create_time,goods_thumbnail_url,order_sn" +
//            " from oder  where pid in and order_status=5")

    List<OderPdd> queryPddPageSize(@Param("status") List status, @Param("id") Long id, @Param("star") Integer star, @Param("end") Integer end);

    List<Tboder> queryTbPageSize(@Param("tk_status") List status, @Param("id") Long id, @Param("star") Integer star, @Param("end") Integer end);

    List<JdOder> queryJdPageSize(@Param("tk_status") List status, @Param("id") Long id, @Param("star") Integer star, @Param("end") Integer end);

    Integer queryPddPageSizeCount(@Param("status") List status,  @Param("id") Long id);
    Integer queryTbPageSizeCount(@Param("tk_status") List status,  @Param("id") Long id);
;
    Integer queryJdPageSizeCount(Map<String,Object> map);

//    List<Oder> queryPddPageSizeSupreMe(@Param("status") List status, @Param("id") Long id, @Param("star") Integer star, @Param("end") Integer end);

    @Select("Select pddPid from userinfo  WHERE id in (select userId from agent where agentId=#{id} and status=0) and status=0 union  Select pddPid from userinfo  WHERE id =#{id}")
    List<Userinfo> superQAllPidInfoForUserId(Long id);


}
