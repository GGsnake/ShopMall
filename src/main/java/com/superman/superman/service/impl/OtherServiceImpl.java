package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.superman.superman.dao.SysAdviceDao;
import com.superman.superman.model.SysJhAdviceDev;
import com.superman.superman.service.OtherService;
import com.superman.superman.utils.EveryUtils;
import com.superman.superman.utils.PageParam;
import lombok.extern.java.Log;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liujupeng on 2018/11/20.
 */
@Log
@Service("otherService")
public class OtherServiceImpl implements OtherService {
    @Autowired
    private SysAdviceDao sysAdviceDao;

    @Value("${juanhuang.logo}")
    private String logo;
    @Override
    public ByteArrayOutputStream crateQRCode(String content) {

        String resultImage = "";
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
        List<SysJhAdviceDev> sysJhAdviceDevs = sysAdviceDao.queryAdviceDev(pageParam.getStartRow(), pageParam.getPageSize());
        JSONArray data=new JSONArray();

        for (SysJhAdviceDev sy:sysJhAdviceDevs)
        {
            JSONObject var=new JSONObject();
            var.put("title",sy.getTitile());
            var.put("content",sy.getContent());
            if (sy.getImage()==null){
                var.put("image",logo);
            }
            else {
                var.put("image",sy.getImage());
            }
            var.put("contentImage",sy.getContentImage());
            var.put("createtime",sy.getCreatetime().getTime() / 1000);
            data.add(var);
        }
        return data;
    }

    @Override
    public String addQrCodeUrl(String data, String uid) {
        ByteArrayOutputStream stream = null;
        String codeImgUrl = null;
        try {
            stream = crateQRCode(data);
            codeImgUrl = EveryUtils.upload(stream.toByteArray(), "qrcode/" + uid + "/", ".png");
        }
        catch (Exception e){
            log.warning(e.getMessage());
            return null;
        }
        return codeImgUrl;
    }
    @Override
    public String addQrCodeUrlInv(String data, String uid) {
        ByteArrayOutputStream stream = null;
        String codeImgUrl = null;
        try {
            stream = crateQRCode(data);
            codeImgUrl = EveryUtils.upload(stream.toByteArray(), "invcode/" + uid + "/", ".png");
        }
        catch (Exception e){
            log.warning(e.getMessage());
            return null;
        }
        return codeImgUrl;
    }
}
