package com.superman.superman.utils;

import org.springframework.util.DigestUtils;

import java.util.*;

/**
 * Created by liujupeng on 2018/11/8.
 */
public class EverySign {
    public  static String pddSign(SortedMap urlSign, String SECRET){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SECRET);
        Set<Map.Entry<String, String>> entry = urlSign.entrySet();
        //通过迭代器取出map中的键值关系，迭代器接收的泛型参数应和Set接收的一致
        Iterator<Map.Entry<String, String>> it = entry.iterator();
        while (it.hasNext()) {
            //将键值关系取出存入Map.Entry这个映射关系集合接口中
            Map.Entry<String, String> me = it.next();
            //使用Map.Entry中的方法获取键和值
            String param = me.getKey() + me.getValue();
            stringBuilder.append(param);
        }
        stringBuilder.append(SECRET);
        String hex = DigestUtils.md5DigestAsHex(stringBuilder.toString().getBytes());
        return hex.toUpperCase();
    }
}
