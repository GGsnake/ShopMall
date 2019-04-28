package com.superman.superman.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.model.Config;
import com.superman.superman.model.Userinfo;
import com.superman.superman.utils.PageParam;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by liujupeng on 2018/11/20.
 */
public interface OtherService {
    /**
     * 创建二维码图片返回 ByteArrayOutputStream流
     *
     * @param content
     * @return
     */
    ByteArrayOutputStream crateQRCode(String content);
    /**
     * 创建二维码图片返回 ByteArrayOutputStream流
     *
     * @param pageParam
     * @ret
     */
    JSONArray queryAdviceForDev(PageParam pageParam);

    String addQrCodeUrl(String data, String uid) throws IOException;

    /**
     * 生成分享APP邀请二维码的图片URL
     *
     * @param data
     * @param uid
     * @return
     * @throws IOException
     */
    String addQrCodeUrlInv(String data, String uid) throws IOException;

    /**
     * 微信预付款
     *
     * @param uid
     * @param ip
     * @return
     */
    JSONObject payMoney(String uid, String ip);
    /**
     * 更新朋友圈的商品
     *
     * @return
     */
    void updateFrientGoods();

    /**
     * 生成邀请二维码
     *
     * @param userinfo
     * @return
     */
    String  builderInviteCodeUrl(Userinfo userinfo);
}
