package com.superman.superman.service;

import com.alibaba.fastjson.JSONArray;
import com.superman.superman.utils.PageParam;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by liujupeng on 2018/11/20.
 */
public interface OtherService {

        ByteArrayOutputStream crateQRCode(String content);
       JSONArray queryAdviceForDev(PageParam pageParam);
        String addQrCodeUrl(String data,String uid) throws IOException;
        String addQrCodeUrlInv(String data,String uid) throws IOException;
}
