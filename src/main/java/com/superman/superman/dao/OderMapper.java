package com.superman.superman.dao;

import com.superman.superman.dto.MemberDetail;
import com.superman.superman.model.Oder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface OderMapper {

    /**
     * 超级统计所有 平台未结算订单（只需要传入用户id 支持多用户id传入）
     *
     * @param list
     * @return
     */
    Long queryForAllOrderListToWait(@Param("list") List<Long> list, @Param("devId") Integer devId);

    /**
     * 超级统计所有 平台已结算订单（只需要传入用户id 支持多用户id传入）
     *
     * @param list
     * @param devId
     * @return
     */
    Long queryForAllOrderListToFinsh(@Param("list") List<Long> list, @Param("devId") Integer devId);

    @Select("  select SUM(s.money) as money,  SUM(s.counts) as sums FROM(" +
            "SELECT  IFNULL(SUM(tb.pub_share_pre_fee),0)*100 as money,COUNT(tb.id) as counts FROM tboder tb " +
            "left join userinfo u on tb.relation_id=u.rid\n" +
            "WHERE u.id=#{id} and tb.tk_status in ('3','12','14')  and tb.odercreate_time between #{tbstartTime} and #{tbendTime}  " +
            "UNION   \n" +
            "SELECT IFNULL(SUM(tb.promotion_amount),0) as money ,COUNT(tb.id) as counts FROM\n" +
            "oder tb" +
            " left join userinfo u on tb.p_id=u.pddPid WHERE u.id=#{id} and tb.order_create_time between #{pddstartTime} and #{pddendTime} and tb.order_status in (0,1,2,3,5)" +
            " UNION SELECT IFNULL(SUM(jd.estimateFee),0)*100 as money ,count(jd.id) as counts FROM  jdoder jd left join userinfo u on jd.positionId=u.jdPid WHERE u.id=#{id} and jd.orderTime between #{jdstartTime} and #{jdendTime} and jd.validCode in (16,17,18) ) as s")
    MemberDetail sumAllDevOderByOderCreateTimeForMb(@Param("id") Integer id, @Param("tbstartTime") String tbstartTime, @Param("tbendTime") String tbendTime, @Param("pddstartTime") Long pddstartTime, @Param("pddendTime") Long pddendTime, @Param("jdstartTime") Long jdstartTime, @Param("jdendTime") Long jdendTime);


    MemberDetail sumAllDevOderByOderCreateTimeForAgent(@Param("list") List list, @Param("tbstartTime") String tbstartTime, @Param("tbendTime") String tbendTime,
                                                       @Param("pddstartTime") Long pddstartTime, @Param("pddendTime") Long pddendTime,
                                                       @Param("jdstartTime") Long jdstartTime, @Param("jdendTime") Long jdendTime);

    MemberDetail sumAllDevOderByOderCreateTimeForAgentToSettle(@Param("list") List list, @Param("tbstartTime") String tbstartTime, @Param("tbendTime") String tbendTime,
                                                               @Param("pddstartTime") Long pddstartTime, @Param("pddendTime") Long pddendTime,
                                                               @Param("jdstartTime") Long jdstartTime, @Param("jdendTime") Long jdendTime);

    MemberDetail sumAllDevAllOder(@Param("list") List list);

    MemberDetail sumAllDevOderByOderCreateTimeForAgentGroup(@Param("list") List list, @Param("tbstartTime") String tbstartTime, @Param("tbendTime") String tbendTime,
                                                            @Param("pddstartTime") Long pddstartTime, @Param("pddendTime") Long pddendTime,
                                                            @Param("jdstartTime") Long jdstartTime, @Param("jdendTime") Long jdendTime);

    Integer countOpenOderForAgentGroupCreateTime(@Param("list") List list, @Param("tbstartTime") String tbstartTime, @Param("tbendTime") String tbendTime,
                                                 @Param("pddstartTime") Long pddstartTime, @Param("pddendTime") Long pddendTime,
                                                 @Param("jdstartTime") Long jdstartTime, @Param("jdendTime") Long jdendTime);


}