package com.superman.superman.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * Created by liujupeng on 2018/12/20.
 */
@Controller
public class HtmlController {
    /**
     * 二维码处理识别
     * @return
     */
    @RequestMapping("/queryCodeUrl")
    public String queryUserUrl() {
        return "index";
    }

}
