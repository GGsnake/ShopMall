package com.superman.superman.service;

/**
 * Created by liujupeng on 2018/11/6.
 */
public interface PddApiService {

    /**
     * 获得推广位订单明细
     * @param pid 拼多多推广位ID
     * @param page
     * @param pagesize
     * @return
     */
    String getBillList(String pid, Integer page, String pagesize);

    /**
     * 生成拼多多推广位
     * @param number 需要生成的pid推广位数量
     * @return
     */
    String newBillSingle(Integer number);

    /**
     *
     * @param pid
     * @param goodIdList
     * @return
     */
    String newPromotion(String pid,Long[] goodIdList);
    String getPddGoodList(Long uid);

}
