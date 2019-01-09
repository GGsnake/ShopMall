package com.superman.superman.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.superman.superman.annotation.LoginRequired;
import com.superman.superman.dao.SysAdviceDao;
import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.model.ApplyCash;
import com.superman.superman.model.Oder;
import com.superman.superman.model.Userinfo;
import com.superman.superman.service.MemberService;
import com.superman.superman.service.MoneyService;
import com.superman.superman.service.PddApiService;
import com.superman.superman.service.UserApiService;
import com.superman.superman.utils.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by liujupeng on 2018/11/9.
 */
@Log
@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private UserinfoMapper userinfoMapper;
    //    @Autowired
//    PddApiService pddApiService;
    @Autowired
    MemberService memberService;
    @Autowired
    UserApiService userApiService;
    @Autowired
    MoneyService moneyService;
    @Autowired
    SysAdviceDao sysAdviceDao;
    @Value("${domain.codeurl}")
    private String URL;

    @ApiOperation(value = "我的个人页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", required = true, dataType = "String", paramType = "/me"),
    })
    @LoginRequired
    @PostMapping("/me")
    public WeikeResponse myIndex(HttpServletRequest request) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_USER_NOT_EXIST);
        }
        JSONObject data = memberService.getMyMoney(Long.valueOf(uid));
        return WeikeResponseUtil.success(data);
    }

    /**
     * 个人佣金提现接口
     *
     * @param request
     * @return
     */
    @LoginRequired
    @PostMapping("/cash")
    public WeikeResponse getCash(HttpServletRequest request) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_USER_NOT_EXIST);
        }
        JSONObject data = new JSONObject();
        Userinfo user = userinfoMapper.selectByPrimaryKey(Long.valueOf(uid));
        Long waitMoney = moneyService.queryCashMoney(Long.valueOf(uid), 0, user);
        Long finishMoney = moneyService.queryCashMoney(Long.valueOf(uid), 1, user);
        Long cash = 0l;
        //TODO
        data.put("waitMoney", waitMoney);
        data.put("finishMoney", finishMoney);
        data.put("cash", cash);
        return WeikeResponseUtil.success(data);
    }

   /**
     * 个人佣金提现申请接口
     *
     * @param request
     * @return
     */
    @LoginRequired
    @GetMapping("/apply")
    public WeikeResponse apply(HttpServletRequest request,Long money,String account,String name) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null||money==null||account==null||name==null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        }
        if (money<0||money>99999){
            return WeikeResponseUtil.fail(ResponseCode.MONEY_MAX);
        }
        Userinfo user = userinfoMapper.selectByPrimaryKey(Long.valueOf(uid));
        if (user==null){
            return WeikeResponseUtil.fail(ResponseCode.COMMON_USER_NOT_EXIST);
        }
        ApplyCash applyCash=new ApplyCash();
        applyCash.setUserid(user.getId().intValue());
        applyCash.setMoney(money);
        applyCash.setAccount(account);
        applyCash.setName(name);
        Integer temp = sysAdviceDao.applyCash(applyCash);
        if (temp==1){
            return WeikeResponseUtil.success();
        }
        log.warning("用户提现失败-UID="+uid);
        return WeikeResponseUtil.fail("100063","申请提现失败请重试");

    }

   /**
     * 个人佣金提现申请查询 分页
     *
     * @param request
     * @return
     */
    @LoginRequired
    @GetMapping("/queryApply")
    public WeikeResponse queryApply(HttpServletRequest request,PageParam pageParam) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        }
        PageParam param = new PageParam(pageParam.getPageNo(), pageParam.getPageSize());
        List<ApplyCash> temp = sysAdviceDao.queryApplyCash(Integer.valueOf(uid),param.getStartRow(),pageParam.getPageSize());
        return WeikeResponseUtil.success(temp);
    }

    /**
     * 查看会员详情
     *
     * @param request
     * @param id
     * @return
     */
    @LoginRequired
    @PostMapping("/child")
    public WeikeResponse getChild(HttpServletRequest request, Long id) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_USER_NOT_EXIST);
        }
        Userinfo userinfo = userinfoMapper.selectByPrimaryKey(Long.valueOf(uid));
        Integer roleId = userinfo.getRoleId();

        if (roleId == 3) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_PARAMS_MISSING);
        }
        JSONObject var = new JSONObject();
        var = memberService.queryMemberDetail(id, userinfo.getId().intValue());
        return WeikeResponseUtil.success(var);
    }   //

    /**
     * 升级代理接口
     *
     * @param request
     * @param id      要升级的用户id
     * @param score   佣金比率
     * @return
     */
    @LoginRequired
    @PostMapping("/upAgent")
    public WeikeResponse upAgent(HttpServletRequest request, Integer id, Integer score) {
        String uid = (String) request.getAttribute(Constants.CURRENT_USER_ID);
        if (uid == null) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_USER_NOT_EXIST);
        }
        if (score < 0 || score > 100) {
            return WeikeResponseUtil.fail(ResponseCode.INT_CUSY);
        }
        Boolean var = userApiService.upAgent(id, Integer.valueOf(uid), score);
        if (var == false) {
            return WeikeResponseUtil.fail(ResponseCode.COMMON_AUTHORITY_ERROR);
        }
        return WeikeResponseUtil.success(var);
    }

    /**
     * 微信预支付（技能开通支付）
     * @param response
     * @param request
     * @throws IOException
     * @throws DocumentException
     */
    @RequestMapping("/wechatOrderPay")
    public void wechatOrderPay(HttpServletResponse response,HttpServletRequest request) throws IOException, DocumentException{
        /* 设置格式为text/html */
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();

//        微信支付商户号 1521764621
//        应用APPID wxc7df701f4d4f1eab
//        API秘钥：hzshop12345678912345678912345678
        String url2 = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        String appid = "wxc7df701f4d4f1eab";
        String body = "华众有个顶顶顶顶";
        String partnerid = "1521764621";
        String noncestr=Util.getRandomString(30);
        String notifyurl = URL+":8080/member/wechatBySuccess";//回调地址wechatBySuccess

        double money=10;

        String ip= request.getRemoteAddr();


        int totalfee = (int) (100*money);

        String attach=1231+"";//附加参数:用户id


        String tradetype = "APP";
        String key = "hzshop12345678912345678912345678";

        // 时间戳
        Long times = System.currentTimeMillis();
        String outtradeno="hj"+times+""+1231;

        String prepayid;
        String timestamp = String.valueOf(times/1000);
        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        parameters.put("appid", appid);//应用ID
        parameters.put("mch_id", partnerid);//商户号
        parameters.put("nonce_str", noncestr);//随机字符串
        parameters.put("body", body);//商品描述
        parameters.put("key", key);//秘钥
        parameters.put("trade_type", tradetype);//交易类型
        parameters.put("out_trade_no", outtradeno);//商户订单号
        parameters.put("total_fee", totalfee);//总金额
        parameters.put("spbill_create_ip", ip);//终端IP
        parameters.put("notify_url", notifyurl);//回调地址
        parameters.put("attach",attach);//附加参数
        String sign = MD5Util.createSign("utf-8", parameters);
        String params = String.format("<xml>" + "<appid>%s</appid>"
                        +"<attach>%s</attach>"
                        + "<body>%s</body>" + "<mch_id>%s</mch_id>"
                        + "<nonce_str>%s</nonce_str>"
                        + "<notify_url>%s</notify_url>"
                        + "<out_trade_no>%s</out_trade_no>"
                        + "<spbill_create_ip>%s</spbill_create_ip>"
                        + "<total_fee>%s</total_fee>"
                        + "<trade_type>%s</trade_type>" + "<sign>%s</sign>"
                        + "</xml>", appid,attach,body, partnerid, noncestr,
                notifyurl, outtradeno, ip, totalfee, tradetype,
                sign);

        String result = HttpUtil.doPost(url2, params);

/*		System.out.println("---------------result---------------"+result);
		String newStr = new String(result.getBytes(), "UTF-8");
		System.out.println("---------------newStr---------------"+newStr);*/

        //二次签名
        Map<String, String> keyval = XmlUtil.treeWalkStart(result);
        noncestr = keyval.get("nonce_str");
        String packageValue = "Sign=WXPay";
        prepayid = keyval.get("prepay_id");

        String stringA = "appid=%s&noncestr=%s&package=%s&partnerid=%s&prepayid=%s&timestamp=%s&key=%s";
        String stringSignTemp = String.format(stringA, appid,
                noncestr, packageValue, partnerid, prepayid,
                timestamp, key);
        sign = MD5.md5(stringSignTemp).toUpperCase();


        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appid", appid);
        map.put("partnerid", partnerid);
        map.put("prepayid", prepayid);
        map.put("packageValue", packageValue);
        map.put("noncestr", noncestr);
        map.put("timestamp", timestamp);
        map.put("sign", sign);
        map.put("ordersNo", outtradeno);
        map.put("attach", attach);
        Gson gson = new Gson();
        String gsonStr = gson.toJson(map);
        System.out.println("预支付数据：" + gsonStr);
        out.write(gsonStr);
        out.flush();
        out.close();


    }






    /**
     * 微信付款成功！（开通技能服务）
     * @throws IOException
     * @throws DocumentException

     */
    @RequestMapping("/wechatBySuccess")
    public void wechatBySuccess(HttpServletResponse response,HttpServletRequest request) throws IOException, DocumentException{

        // 设置格式为text/html
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        System.out.println("回调ACTION:paySuccess");
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        System.out.println("~~~~~~~~~~~~~~~~付款成功~~~~~~~~~");
        outSteam.close();
        inStream.close();
        String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息

        Map<String, String> resultMap = XmlUtil.doXMLParse(result);
        String out_trade_no = resultMap.get("out_trade_no");
        String return_code = resultMap.get("return_code");
        String result_code = resultMap.get("result_code");
        String attach = resultMap.get("attach");


        System.out.println("----resultMap----"+resultMap);

        System.out.println("======return_code:"+return_code+"=========result_code:"+result_code);

        if (result_code.equalsIgnoreCase("SUCCESS")) {

            if(return_code.equalsIgnoreCase("SUCCESS")){

                System.out.println("-----------支付成功----------------");

                System.out.println("---------附加参数-----"+attach);
                System.out.println("-------订单号---"+out_trade_no);




            }else{
                System.out.println("--------------------支付后验签失败，请检查-------------------");
            }


            String ty="<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
            out.write(ty);
            out.flush();
            out.close();
        }else{
            System.out.println("--------------------支付后验签失败，请检查-------------------");
        }


    }








}
