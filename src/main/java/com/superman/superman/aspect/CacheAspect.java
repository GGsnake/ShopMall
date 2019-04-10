package com.superman.superman.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.annotation.FastCache;
import com.superman.superman.redis.RedisUtil;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Component
@Aspect
@Log
public class CacheAspect {
    @Autowired
    private RedisUtil redisUtil;

    @Pointcut("@annotation(com.superman.superman.annotation.FastCache)")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object aroundManagerLogPoint(ProceedingJoinPoint jp) throws Throwable {
        Object result = null;
        Method method = getMethod(jp);
        // 获取注解
        FastCache fastCache = method.getAnnotation(FastCache.class);
        //获得方法名
        String name = method.getName();
        // 判断是否使用缓存
        int timeOut = fastCache.timeOut();
        String key = name + ":" + generateKey(jp);
        Class returnType = ((MethodSignature) jp.getSignature()).getReturnType();

        result = redisUtil.get(key);
        if (result == null) {
            result = jp.proceed(jp.getArgs());
            redisUtil.setForTimeCustom(key, JSONObject.toJSONString(result), timeOut, TimeUnit.SECONDS);
            return result;
        }
        JSONObject jsonObject1 = JSONObject.parseObject(result.toString());
        Object jsonObject = JSON.toJavaObject(jsonObject1,returnType);
        return jsonObject;
    }

    // 生成缓存 key策略
    private String generateKey(ProceedingJoinPoint point) {
        StringBuffer key = new StringBuffer();
        key.append(StringUtils.join(point.getArgs(), "::"));
        return key.toString();
    }

    /**
     * @param joinPoint
     * @return
     * @Title: getMethod
     * @Description: 获取被拦截方法对象
     */
    protected Method getMethod(JoinPoint joinPoint) throws Exception {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        Method method = methodSignature.getMethod();

        return method;
    }

    public <T> T hget(JSON field, Class<T> clazz) {
        T result = (T) JSONObject.toJavaObject(field,clazz);
        return result;
    }
}

