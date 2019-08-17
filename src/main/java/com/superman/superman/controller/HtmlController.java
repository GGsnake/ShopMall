package com.superman.superman.controller;

import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.model.Userinfo;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by liujupeng on 2018/12/20.
 */
@Controller
@Log
public class HtmlController {
    private static final String URL = "https://api.open.21ds.cn/jd_api_v1/";

    /**
     * 二维码处理识别
     *
     * @return
     */
    @RequestMapping("/queryCodeUrl")
    public String queryUserUrl() {
        return "index";
    }

    @Autowired
    private UserinfoMapper userinfoMapper;

    /**
     * 淘宝账户授权回调
     */
    @RequestMapping("tbAuth")
    public String money(Integer relation_id, Integer custompar, String account_name, String desc) {
        Userinfo relation = userinfoMapper.selectByPrimaryKey(custompar.longValue());
        if (relation == null || relation.getRid() != null) {
            return "redirect:http://www.quanhuangmaoyi.com/authError.html";
        }
        Integer hasRegister = userinfoMapper.relationIdExits(relation_id.toString());
        if (hasRegister != null) {
            return "redirect:http://www.quanhuangmaoyi.com/authError.html";
        }
        Userinfo userinfo = new Userinfo();
        userinfo.setId(custompar.longValue());
        userinfo.setRid(relation_id.toString());
        userinfoMapper.updateUserForRid(userinfo);
        return "redirect:http://www.quanhuangmaoyi.com/authSuccess.html";


    }

}
