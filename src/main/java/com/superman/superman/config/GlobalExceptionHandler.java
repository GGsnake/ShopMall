//package com.superman.superman.controller;
//
//import com.superman.superman.utils.ErrorBean;
//import com.superman.superman.utils.MyException;
//import lombok.var;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.ModelAndView;
//import javax.servlet.http.HttpServletRequest;
//
///**
// * Created by liujupeng on 2018/11/19.
// */
//@ControllerAdvice
//public class GlobalExceptionHandler {
//    public static final String DEFAULT_ERROR_VIEW = "error";
//
//
////    @ExceptionHandler(value = Exception.class)
////    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
////        var mav = new ModelAndView();
////        mav.addObject("exception", e);
////        mav.addObject("url", req.getRequestURL());
////        mav.setViewName(DEFAULT_ERROR_VIEW);
////        return mav;
////    }
//
//    @ExceptionHandler(value = MyException.class)
//    @ResponseBody
//    public ErrorBean<String> jsonErrorHandler(HttpServletRequest req, MyException e) throws Exception {
//        ErrorBean<String> r = new ErrorBean<>();
//        r.setMessage(e.getMessage());
//        r.setCode(ErrorBean.ERROR);
//        r.setData("Some Data");
//        r.setUrl(req.getRequestURL().toString());
//        return r;
//    }
//}
