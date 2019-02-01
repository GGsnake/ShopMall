package com.superman.superman.controller;

import com.superman.superman.service.UserApiService;
import com.superman.superman.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
