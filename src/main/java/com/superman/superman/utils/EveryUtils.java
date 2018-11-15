package com.superman.superman.utils;

import java.util.Calendar;

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

    public static Long getDay(){
        Calendar calendar = Calendar.getInstance();

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Long today=c.getTimeInMillis()/1000;
        return (today+86400)-System.currentTimeMillis()/1000;
    }
}
