package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.superman.superman.annotation.FastCache;
import com.superman.superman.dao.*;
import com.superman.superman.dto.SysFriendDto;
import com.superman.superman.manager.ConfigQueryManager;
import com.superman.superman.model.*;
import com.superman.superman.redis.RedisUtil;
import com.superman.superman.service.OtherService;
import com.superman.superman.service.ScoreService;
import com.superman.superman.utils.*;
import com.superman.superman.utils.net.HttpUtil;
import com.superman.superman.utils.sign.MD5;
import com.superman.superman.utils.sign.MD5Util;
import lombok.extern.java.Log;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by snake on 2018/11/20.
 */
@Log
@Service("otherService")
public class OtherServiceImpl implements OtherService {
    @Autowired
    private SysAdviceDao sysAdviceDao;
    @Value("${domain.qnyurl}")
    private String QINIUURL;
    @Value("${server.port}")
    private Integer port;
    @Value("${domain.codeurl}")
    private String QINIUURLLAST;
    @Autowired
    private ScoreDao scoreDao;

    @Autowired
    private ScoreService scoreService;
    @Autowired
    private UserinfoMapper userinfoMapper;
    @Autowired
    private ConfigQueryManager configQueryManager;

    @Override
    public ByteArrayOutputStream crateQRCode(String content) {
        if (!StringUtils.isEmpty(content)) {
            ServletOutputStream stream = null;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            @SuppressWarnings("rawtypes")
            HashMap<EncodeHintType, Comparable> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); // 指定字符编码为“utf-8”
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M); // 指定二维码的纠错等级为中级
            hints.put(EncodeHintType.MARGIN, 2); // 设置图片的边距
            try {
                QRCodeWriter writer = new QRCodeWriter();
                BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 300, 300, hints);
                BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
                ImageIO.write(bufferedImage, "png", os);
                return os;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (stream != null) {
                    try {
                        stream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    @Override

    public JSONArray queryAdviceForDev(PageParam pageParam) {
        Map<String, Object> map = new HashMap<>();
        map.put("offset", pageParam.getStartRow());
        map.put("limit", pageParam.getPageSize());
        List<SysJhAdviceDev> sysJhAdviceDevs = sysAdviceDao.queryAdviceDev(map);
        JSONArray data = new JSONArray();
        String logo = configQueryManager.queryForKey("Logo");
        for (SysJhAdviceDev sy : sysJhAdviceDevs) {
            JSONObject var = new JSONObject();
            var.put("title", sy.getTitile());
            var.put("content", sy.getContent());
            if (sy.getImage() == null) {
                var.put("image", logo);
            } else {
                var.put("image", sy.getImage());
            }
            var.put("contentImage", sy.getContentImage());
            var.put("createtime", sy.getCreatetime().getTime() / 1000);
            data.add(var);
        }
        return data;
    }

    @Override
    @FastCache(timeOut = 600)
    public String addQrCodeUrl(String data, String uid) {
        ByteArrayOutputStream stream = null;
        String codeImgUrl = null;
        try {
            stream = crateQRCode(data);
            codeImgUrl = EveryUtils.upload(stream.toByteArray(), "qrcode/" + uid + "/", ".png");
        } catch (Exception e) {
            log.warning(e.getMessage());
            return null;
        }
        return codeImgUrl;
    }

    /**
     * 生成分享APP邀请二维码的图片URL
     *
     * @param data
     * @param uid
     * @return
     * @throws IOException
     */
    @FastCache(timeOut = 60)
    public String addQrCodeUrlInv(String data, String uid) {
        ByteArrayOutputStream stream = null;
        String codeImgUrl = null;
        try {
            stream = crateQRCode(data);
            codeImgUrl = EveryUtils.upload(stream.toByteArray(), "invcode/" + uid + "/", ".png");
        } catch (Exception e) {
            log.warning(e.getMessage());
            return null;
        }
        return codeImgUrl;
    }

    @Override
    public JSONObject payMoney(String uid, String ip) {
        String noncestr = Util.getRandomString(30);
        String body = "升级成为运营商";
        String url2 = configQueryManager.queryForKey("WxPayUrl");
        String appid = configQueryManager.queryForKey("WxPayAppId");
        String partnerid = configQueryManager.queryForKey("WxPartNerId");
        String notifyurl = configQueryManager.queryForKey("WxPayNotifUrl");
        Double money = Double.valueOf(configQueryManager.queryForKey("AgentMoney"));
        String key = configQueryManager.queryForKey("WxApplyKey");
        int totalfee = (int) (100 * money);
        String attach = uid;//附加参数:用户id
        String tradetype = "APP";
        String prepayid;
        // 时间戳
        Long times = System.currentTimeMillis();
        String outtradeno = "hj" + times + "" + attach;
        String timestamp = String.valueOf(times / 1000);
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
        parameters.put("attach", attach);//附加参数
        String sign = MD5Util.createSign("utf-8", parameters, key);
        String params = String.format("<xml>" + "<appid>%s</appid>"
                        + "<attach>%s</attach>"
                        + "<body>%s</body>" + "<mch_id>%s</mch_id>"
                        + "<nonce_str>%s</nonce_str>"
                        + "<notify_url>%s</notify_url>"
                        + "<out_trade_no>%s</out_trade_no>"
                        + "<spbill_create_ip>%s</spbill_create_ip>"
                        + "<total_fee>%s</total_fee>"
                        + "<trade_type>%s</trade_type>" + "<sign>%s</sign>"
                        + "</xml>", appid, attach, body, partnerid, noncestr,
                notifyurl, outtradeno, ip, totalfee, tradetype,
                sign);

        String result = HttpUtil.doPost(url2, params);
        //二次签名
        Map<String, String> keyval = null;
        try {
            keyval = XmlUtil.treeWalkStart(result);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        noncestr = keyval.get("nonce_str");
        String packageValue = "sign=WXPay";
        prepayid = keyval.get("prepay_id");
        String stringA = "appid=%s&noncestr=%s&package=%s&partnerid=%s&prepayid=%s&timestamp=%s&key=%s";
        String stringSignTemp = String.format(stringA, appid,
                noncestr, packageValue, partnerid, prepayid,
                timestamp, key);
        sign = MD5.md5(stringSignTemp).toUpperCase();
        JSONObject map = new JSONObject();
        map.put("appid", appid);
        map.put("partnerid", partnerid);
        map.put("prepayid", prepayid);
        map.put("packageValue", packageValue);
        map.put("noncestr", noncestr);
        map.put("timestamp", timestamp);
        map.put("sign", sign);
        map.put("ordersNo", outtradeno);
        map.put("attach", attach);
        return map;
    }


    @Autowired
    private SysFriendDtoMapper sysFriendDtoMapper;

    @Async
    @Override
    public void updateFrientGoods() {
        //先获取到所有的id
        List<SysFriendDto> sysFriendDtos = sysFriendDtoMapper.queryListFriend(0, 200);
        sysFriendDtos.forEach(bean -> {
            JSONObject jsonObject = new JSONObject();
            List<Long> goodIdList = sysFriendDtoMapper.random();
            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(goodIdList);
            jsonObject.put("data", jsonArray);
            String content = jsonObject.toJSONString();
            sysFriendDtoMapper.updateRandom(bean.getId(), content);
        });

    }

    @Override
    public String builderInviteCodeUrl(Userinfo userinfo) {
        Integer code = userinfoMapper.queryCodeId(userinfo.getId());
        if (null == code) {
            return null;
        }
        String shareScore = configQueryManager.queryForKey("ShareScore");
        if (shareScore == null) {
            return null;
        }
        ScoreBean scoreBean = new ScoreBean();
        //积分类型 为 邀请
        scoreBean.setDataSrc(4);
        scoreBean.setUserId(userinfo.getId());
        //积分收益为 收入
        scoreBean.setScoreType(1);
        scoreBean.setDay(EveryUtils.getNowday());
        scoreBean.setScore(Long.valueOf(shareScore));
        if (!scoreService.isShare(userinfo.getId())) {
            scoreDao.addScore(scoreBean);
            userinfo.setUserscore(Integer.valueOf(shareScore));
            scoreDao.updateUserScore(userinfo);
        }
        String codeUrl = QINIUURL + addQrCodeUrlInv(QINIUURLLAST + ":" + port + "/user/index.html?code=" + code, userinfo.getId().toString());
        return codeUrl;
    }
    //
//    @Override
//    @FastCache(timeOut = 18)
//    public String querySetting(String no) {
//        String config = configQueryManager.queryForKey(no);
//        return config;
//
//    }
}
