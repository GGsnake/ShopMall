package com.superman.superman.controller;

import com.superman.superman.utils.Constants;
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
    @RequestMapping("/queryCodeUrl")
    public String queryUserUrl(HttpServletResponse response, HttpServletRequest request, Integer code) throws IOException {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
//        if (uid == null)
//            return WeikeResponseUtil.fail(ResponseCode.COMMON_USER_NOT_EXIST);
//        Integer agentId = userinfoMapper.queryUserCode(code.longValue());
//        modelAttribute
        return "ind";
    }
}
