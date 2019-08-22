package com.superman.superman.config;

import com.superman.superman.utils.ErrorBean;
import com.superman.superman.utils.MyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by liujupeng on 2018/11/19.
 */
@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public ErrorBean<String> jsonErrorHandler(HttpServletRequest req, RuntimeException e) {
        ErrorBean<String> r = new ErrorBean<>();
        r.setMessage(e.getMessage());
        r.setCode(ErrorBean.ERROR);
        r.setData("Some Data");
        r.setUrl(req.getRequestURL().toString());
        return r;
    }
}
