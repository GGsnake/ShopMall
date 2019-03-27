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
     * 上百券
     * @return
     */

    List<SysJhJdHot> queryPageJd(Map<String,Object> map);
    /**
     * 本地搜索引擎
     * @return
     */

    List<SysJhTaobaoAll> queryLocalAllOpt(Map<String,Object> map);
    /**
     * 本地搜索引擎
     * @return
     */

    Integer countLocalAllOpt(Map<String,Object> map);
    /**
     * 上百券
     * @return
     */
    Integer countMaxGood();

    Integer countJu();
    /**
     * 京东统计
     * @return
     */
    Integer countMaxJd();
    /**
     * 京东统计
     * @return
     */
    Integer countMaxJdCid(Integer cid);


    List<SysJhTaobaoHot>  queryForJu(Map<String,Object> map);

    /**
     * 轮播图商品搜索
     * @return
     */
    @Select("SELECT * FROM jh_taobao_all where numIid=#{goodId} and status=0 limit 1")
    SysJhTaobaoAll queryLocalSimple(Long goodId);

}
