package com.superman.superman.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by liujupeng on 2018/11/20.
 */
public interface OtherService {

        ByteArrayOutputStream crateQRCode(String content);
        String addQrCodeUrl(String data,String uid) throws IOException;
}
