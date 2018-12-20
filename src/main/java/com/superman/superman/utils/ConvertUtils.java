package com.superman.superman.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liujupeng on 2018/12/19.
 */
public class ConvertUtils {
    public static List getStatus(Integer devId, Integer status) {
        List<Integer> statusList = new ArrayList<>();
        switch (status) {
            case 0:
                if (devId == 0) {
                    statusList.add(1);
                    statusList.add(2);
                    statusList.add(3);
                    statusList.add(5);
                }
                if (devId == 1) {
                    statusList.add(3);
                    statusList.add(12);
                    statusList.add(14);
                }
                if (devId == 2) {

                }
                return statusList;
            case 1:
                if (devId == 0) {
                    statusList.add(1);
                    statusList.add(2);
                    statusList.add(3);

                }
                if (devId == 1) {
                    statusList.add(12);
                    statusList.add(14);

                }
                if (devId == 2) {

                }
                return statusList;
            case 2:
                if (devId == 0) {
                    statusList.add(5);
                }
                if (devId == 1) {
                    statusList.add(3);

                }
                if (devId == 2) {

                }
            case 3:
                if (devId == 0) {

                }
                if (devId == 1) {

                }
                if (devId == 2) {

                }
                return statusList;

        }

        return statusList;
    }
}
