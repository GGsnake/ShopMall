package com.superman.superman.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by liujupeng on 2018/11/9.
 */
public class EveryUtils {
    static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 佣金计算
     *
     * @param score
     * @return
     */
    public static Float getCommission(Float score) {
        Float bonus = null;
        //佣金为0
        if (score == 0) {
            bonus = 0f;
        }
        //佣金为100代表总代理 90%返佣
        if (score == 100) {
            bonus = 1f;
        }
        bonus = score;
        return bonus;
    }

    public static Long getDay() {
        Calendar calendar = Calendar.getInstance();

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Long today = c.getTimeInMillis() / 1000;
        return (today + 86400) - System.currentTimeMillis() / 1000;
    }

    //当天时间戳
    public static Long getToday() {
        Calendar calendar = Calendar.getInstance();

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Long today = c.getTimeInMillis() / 1000;
        return today;
    }

    //获取当月第一天的时间戳
    public static long getTimesMonthmorning() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Long nowtime1 = calendar.getTimeInMillis() / 1000;
        return nowtime1;
    }

    //获取当月最后一天的时间戳
    public static long getTimesMonthmorningLast() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.set(Calendar.MINUTE, 0);
        ca.set(Calendar.SECOND, 0);
        return ca.getTimeInMillis()/1000;
    } //获取当月最后一天的时间戳
    public static long getTopStar() {
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.MONTH, -1);
        ca.set(Calendar.DAY_OF_MONTH, 1);
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.set(Calendar.MINUTE, 0);
        ca.set(Calendar.SECOND, 0);
        return ca.getTimeInMillis()/1000;
    } //获取当月最后一天的时间戳
    public static long getEnd() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH,0);
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.set(Calendar.MINUTE, 0);
        ca.set(Calendar.SECOND, 0);
        return ca.getTimeInMillis()/1000;
    }
}
