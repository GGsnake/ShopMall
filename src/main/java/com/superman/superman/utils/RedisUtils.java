//package com.superman.superman.utils;
//
//import com.google.gson.Gson;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//
//import org.springframework.data.redis.core.*;
//import org.springframework.stereotype.Component;
//
//import java.util.Set;
//import java.util.concurrent.TimeUnit;
//
///**
// * @author heguoliang
// * @Description: TODO(Redis工具类)
// * @date 2017-6-23 15:07
// */
//@Component
//public class RedisUtils {
//
//    //是否开启redis缓存  true开启   false关闭
//    @Value("${spring.redis.open: #{false}}")
//    private boolean open;
//
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;
//    @Autowired
//    private ValueOperations<String, String> valueOperations;
//    @Autowired
//    private HashOperations<String, String, Object> hashOperations;
//    @Autowired
//    private ListOperations<String, Object> listOperations;
//    @Autowired
//    private SetOperations<String, Object> setOperations;
//    @Autowired
//    private ZSetOperations<String, Object> zSetOperations;
//
//    /**  默认过期时长，单位：秒 */
//    public final static long DEFAULT_EXPIRE = 60 * 60 * 24;
//
//    /**  不设置过期时长 */
//    public final static long NOT_EXPIRE = -1;
//
//    private final static Gson gson = new Gson();
//
//    public boolean exists(String key) {
//        if(!open){
//            return false;
//        }
//
//        return redisTemplate.hasKey(key);
//    }
//
//    public void set(String key, Object value, long expire){
//        if(!open){
//            return;
//        }
//
//        valueOperations.set(key, toJson(value));
//        if(expire != NOT_EXPIRE){
//            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
//        }
//    }
//
//    public void set(String key, Object value){
//        if(!open){
//            return;
//        }
//
//        set(key, value, DEFAULT_EXPIRE);
//    }
//
//    public <T> T get(String key, Class<T> clazz, long expire) {
//        if(!open){
//            return null;
//        }
//
//        String value = valueOperations.get(key);
//        if(expire != NOT_EXPIRE){
//            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
//        }
//        return value == null ? null : fromJson(value, clazz);
//    }
//
//    public <T> T get(String key, Class<T> clazz) {
//        if(!open){
//            return null;
//        }
//
//        return get(key, clazz, NOT_EXPIRE);
//    }
//
//    public String get(String key, long expire) {
//        if(!open){
//            return null;
//        }
//
//        String value = valueOperations.get(key);
//        if(expire != NOT_EXPIRE){
//            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
//        }
//        return value;
//    }
//
//    public String get(String key) {
//        if(!open){
//            return null;
//        }
//
//        return get(key, NOT_EXPIRE);
//    }
//
//    public void delete(String key) {
//        if(!open){
//            return;
//        }
//
//        if(exists(key)){
//            redisTemplate.delete(key);
//        }
//    }
//
//    public void delete(String... keys) {
//        if(!open){
//            return;
//        }
//
//        for (String key : keys) {
//            redisTemplate.delete(key);
//        }
//    }
//
//    public void deletePattern(String pattern) {
//        if(!open){
//            return;
//        }
//
//        Set<String> keys = redisTemplate.keys(pattern);
//        if (keys.size() > 0)
//            redisTemplate.delete(keys);
//    }
//
//    /**
//     * Object转成JSON数据
//     */
//    private String toJson(Object object){
//        if(!open){
//            return null;
//        }
//
//        if(object instanceof Integer || object instanceof Long || object instanceof Float ||
//                object instanceof Double || object instanceof Boolean || object instanceof String){
//            return String.valueOf(object);
//        }
//        return gson.toJson(object);
//    }
//
//    /**
//     * JSON数据，转成Object
//     */
//    private <T> T fromJson(String json, Class<T> clazz){
//        if(!open){
//            return null;
//        }
//
//        return gson.fromJson(json, clazz);
//    }
//
//}
