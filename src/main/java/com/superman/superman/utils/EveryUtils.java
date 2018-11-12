package com.superman.superman.utils;

/**
 * Created by liujupeng on 2018/11/9.
 */
public class EveryUtils {
    /**
     * 佣金计算
     * @param score
     * @return
     */
    public static Float getCommission(Float score){
        Float bonus = null;
        //佣金为0
        if (score == 0) {
            bonus = 0f;
        }
        //佣金为100代表总代理 90%返佣
        if (score == 100) {
            bonus = 1f;
        }
        bonus =score;
        return bonus;
    }

}
