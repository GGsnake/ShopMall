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
	@Select("select id,commissionRate from jh_taobao_hot order by commissionRate asc")
    List<SysJhTaobaoHot> queryAllCommissionRate();
	@Select("select id,coupon from jh_taobao_hot order by coupon asc")
    List<SysJhTaobaoHot> queryAllcoupon();
	@Select("select id,volume from jh_taobao_hot order by volume asc")
    List<SysJhTaobaoHot> queryAllvolume();
	@Update("update jh_taobao_hot set order_commiss=#{orderCommiss} where id=#{id}")
    void  updateCommissionRate(SysJhTaobaoHot map);
	@Update("update jh_taobao_hot set order_volume=#{orderVolume} where id=#{id}")
    void  updateorderVolume(Map<String,Object> map);
	@Update("update jh_taobao_hot set order_coupon=#{orderCoupon} where id=#{id}")
    void  updateorderCoupon(Map<String,Object> map);

    void saveopt(Map<String, Object> map);
    void saveJd(Map<String, Object> map);
    /**
     * 上百券
     * @return
     */

    List<SysJhJdHot> queryPageJd(Map<String,Object> map);
    /**
     * 上百券
     * @return
     */

    List<SysJhTaobaoHot> queryMaxGood(Map<String,Object> map);
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


    List<SysJhTaobaoHot>  queryPage(Map<String,Object> map);
    List<SysJhTaobaoHot>  queryPageTmall(Map<String,Object> map);
    List<SysJhTaobaoHot>  queryForGod(Map<String,Object> map);
    List<SysJhTaobaoHot>  queryForBao(Map<String,Object> map);
    List<SysJhTaobaoHot>  queryForJu(Map<String,Object> map);
}
