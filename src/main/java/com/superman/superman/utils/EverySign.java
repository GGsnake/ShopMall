package com.superman.superman.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.DigestUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liujupeng on 2018/11/8.
 */
public class EverySign {
    public static Map isPhone(String phone) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        Map set=new HashMap();
        if (phone.length() != 11) {
            set.put("data","手机号应为11位数");
            set.put("flag",false);
            return set;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            if (!isMatch) {
                set.put("data","请填入正确的手机号");
                set.put("flag",false);
                return set;
            }
            set.put("flag",true);
            return set;
        }
    }


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
