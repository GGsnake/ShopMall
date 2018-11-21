//package com.superman.superman.utils;
//
//import javax.crypto.spec.SecretKeySpec;
//import java.security.Key;
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.jd.open.api.sdk.internal.JSON.JSON;
//import com.superman.superman.model.HotUser;
//
///**
// * Created by liujupeng on 2018/11/21.
// */
//public class JwtHelp {
//
//        private final static String base64Secret = "MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY=";
//        private final static int expiresSecond = 172800000;
//
//        public static Claims parseJWT(String jsonWebToken) {
//            try {
//                Claims claims = Jwts.parser()
//                        .setSigningKey(DatatypeConverter.parseBase64Binary(base64Secret))
//                        .parseClaimsJws(jsonWebToken).getBody();
//                return claims;
//            } catch (Exception ex) {
//                return null;
//            }
//        }
//
//        public static String createJWT(String username, String roles, String privileges) {
//            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
//
//            long nowMillis = System.currentTimeMillis();
//            Date now = new Date(nowMillis);
//
//            //生成签名密钥
//            byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Secret);
//            Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
//
//            //添加构成JWT的参数
//            JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
//                    .claim("user_name", username)
//                    .claim("user_role", roles)
//                    .claim("user_privilege", privileges)
//                    .signWith(signatureAlgorithm, signingKey);
//            //添加Token过期时间
//            if (expiresSecond >= 0) {
//                long expMillis = nowMillis + expiresSecond;
//                Date exp = new Date(expMillis);
//                builder.setExpiration(exp).setNotBefore(now);
//            }
//
//            //生成JWT
//            return builder.compact();
//        }
//
//}
