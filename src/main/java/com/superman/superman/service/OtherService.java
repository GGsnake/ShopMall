package com.superman.superman.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.model.Config;
import com.superman.superman.model.Userinfo;
import com.superman.superman.utils.PageParam;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by snake on 2018/11/20.
 */
public interface OtherService {
    /**
     * 创建二维码图片返回 ByteArrayOutputStream流
     * @param content
     * @return
     */
    ByteArrayOutputStream crateQRCode(String content);
    /**
     * 查询官方通知
     * @param pageParam
     * @return
     */
    JSONArray queryAdviceForDev(PageParam pageParam);

    /**
     * 根据URL地址生成二维码图片
     * @param data url地址
     * @param uid
     * @return
     */
    String addQrCodeUrl(String data, String uid);

    /**
     * 生成分享APP邀请二维码的图片URL
     *
     * @param data
     * @param uid
     * @return
     * @throws IOException
     */
    String addQrCodeUrlInv(String data, String uid);

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
