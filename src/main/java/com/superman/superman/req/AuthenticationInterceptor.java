package com.superman.superman.req;

import com.superman.superman.annotation.LoginRequired;
import com.superman.superman.model.TokenModel;
import com.superman.superman.service.TokenService;
import com.superman.superman.utils.Constants;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Created by liujupeng on 2018/11/21.
 */
@Log
public class AuthenticationInterceptor implements HandlerInterceptor {
    //    @Autowired
//    private RedisTemplate redisTemplate;
    @Autowired
    private TokenService manager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            response.sendError(401, "请登录");
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        LoginRequired methdAnno = method.getAnnotation(LoginRequired.class);
        if (methdAnno == null) {
            return true;
        }
        //从header中得到token
        String authorization = request.getHeader(Constants.AUTHORIZATION);
        //验证token
        TokenModel model = manager.getToken(authorization);
        if (manager.checkToken(model)) {
            //如果token验证成功，将token对应的用户id存在request中，便于之后注入
            request.setAttribute(Constants.CURRENT_USER_ID, model.getUserId());
            return true;
        }
        //如果验证token失败，并且方法注明了Authorization，返回401错误
        if (method.getAnnotation(LoginRequired.class) != null) {
            response.sendError(401, "token失效，请重新登录");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;

//        //从header中得到token
//        String authorization = request.getHeader(Constants.AUTHORIZATION);
//        //验证token
//        TokenModel model = manager.getToken(authorization);
//        if (manager.checkToken(model)) {
//            //如果token验证成功，将token对应的用户id存在request中，便于之后注入
//            request.setAttribute(Constants.CURRENT_USER_ID, model.getUserId());
//            return true;
//        }
//        //如果验证token失败，并且方法注明了Authorization，返回401错误
//        response.sendError(401, "请登录");
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
