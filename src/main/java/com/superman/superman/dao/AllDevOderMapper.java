package com.superman.superman.dao;

import com.superman.superman.annotation.FastCache;
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
 * Created by snake on 2018/12/19.
 */
@Mapper
public interface AllDevOderMapper {

    /**
     * 分页查询拼多多订单
     * @param status
     * @param id
     * @param star
     * @param end
     * @return
     */
    @FastCache(timeOut = 10)
    List<OderPdd> queryPddPageSize(@Param("status") List status, @Param("id") Long id, @Param("star") Integer star, @Param("end") Integer end);

    /**
     * 分页查询淘宝订单
     * @param status
     * @param id
     * @param star
     * @param end
     * @return
     */
    @FastCache(timeOut = 10)
    List<Tboder> queryTbPageSize(@Param("tk_status") List status, @Param("id") Long id, @Param("star") Integer star, @Param("end") Integer end);
    /**
     * 分页查询京东订单
     * @param status
     * @param id
     * @param star
     * @param end
     * @return
     */
    @FastCache(timeOut = 10)
    List<JdOder> queryJdPageSize(@Param("tk_status") List status, @Param("id") Long id, @Param("star") Integer star, @Param("end") Integer end);
    /**
     * 统计拼多多订单数量
     * @param status
     * @param id
     * @return
     */
    Integer queryPddPageSizeCount(@Param("status") List status,  @Param("id") Long id);
    /**
     * 统计淘宝订单数量
     * @param status
     * @param id
     * @return
     */
    Integer queryTbPageSizeCount(@Param("tk_status") List status,  @Param("id") Long id);
    /**
     * 统计京东单数量
     * @param map
     * @return
     */
    Integer queryJdPageSizeCount(Map<String,Object> map);



}
