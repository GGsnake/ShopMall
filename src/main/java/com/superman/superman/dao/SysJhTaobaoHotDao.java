package com.superman.superman.dao;

import com.superman.superman.model.SysJhJdHot;
import com.superman.superman.model.SysJhTaobaoAll;
import com.superman.superman.model.SysJhTaobaoHot;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysJhTaobaoHotDao extends BaseDao<SysJhTaobaoHot> {

    /**
     * 京东本地搜索引擎
     * @return
     */

    List<SysJhJdHot> queryPageJd(Map<String, Object> map);

    /**
     * 淘宝本地搜索引擎
     * @param map
     * @return
     */

    List<SysJhTaobaoAll> queryLocalAllOpt(Map<String, Object> map);

    /**
     * 淘宝本地搜索引擎 统计数量
     * @param map
     * @return
     */
    Integer countLocalAllOpt(Map<String, Object> map);

    /**
     * 京东统计
     *
     * @return
     */
    Integer countMaxJd();

    /**
     * 京东统计(类目)
     *
     * @return
     */
    Integer countMaxJdCid(Integer cid);

    /**
     * 轮播图商品搜索
     *
     * @return
     */
    @Select("SELECT * FROM jh_taobao_all where numIid=#{goodId} and status=0 limit 1")
    SysJhTaobaoAll queryLocalSimple(Long goodId);

}
