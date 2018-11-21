package com.superman.superman.req;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.superman.superman.annotation.LoginRequired;
import com.superman.superman.model.HotUser;
import lombok.extern.java.Log;
import lombok.var;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * Created by liujupeng on 2018/11/21.
 */
@Log
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        log.warning("sssssssssssssssssssssssssssssssssssssss");
        HandlerMethod handlerMethod= (HandlerMethod) handler;
        Method method=handlerMethod.getMethod();

        LoginRequired methdAnno=method.getAnnotation(LoginRequired.class);
        if (methdAnno==null){
            return true;
        }

        String token = request.getHeader("token");
        if(StringUtils.isBlank(token)){
           return false;
        }
        String json = JWT.decode(token).getAudience().get(0);
        if (json==null){
            return false;
        }

        HotUser user = JSON.parseObject(json, HotUser.class);
        JWTVerifier jwtVerifier=null;
        if (user!=null){
           jwtVerifier = JWT.require(Algorithm.HMAC256(user.getLoginpwd())).build();
            jwtVerifier.verify(token).
        }
        if (){

        }
        HashOperations hashOperations = redisTemplate.opsForHash();

        JSONObject res = new JSONObject();
        res.put("success","false");
        res.put("msg","xxxx");
        try  {
            PrintWriter writer = httpServletResponse.getWriter();
            writer.append(res.toJSONString());
        }
        catch (Exception e){
            e.printStackTrace();
            httpServletResponse.sendError(500);
            return false;
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
