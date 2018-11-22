//package com.superman.superman.utils;
//
//import com.alibaba.fastjson.JSONObject;
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.jd.open.api.sdk.internal.JSON.JSON;
//import com.superman.superman.model.HotUser;
//
///**
// * Created by liujupeng on 2018/11/21.
// */
//public class JWTtoken {
//    public String getToken(HotUser user) {
//        String token="";
//        token= JWT.create().withAudience(JSON.toString(user))
//                .sign(Algorithm.HMAC256(user.getLoginpwd()));
//        return token;
//    }
//}
