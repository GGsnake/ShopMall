package com.superman.superman.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by liujupeng on 2018/11/9.
 */
public class EveryUtils {

    public static String HttpRestClient(String url, HttpMethod method, MultiValueMap<String, String> params) throws IOException {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10*1000);
        requestFactory.setReadTimeout(10*1000);
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        //  请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
        //  执行HTTP请求
        ResponseEntity<String> response = client.exchange(url, HttpMethod.GET, requestEntity, String.class);
        return response.getBody();
    }


    static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 佣金计算
     *
     * @param score
     * @return
     */
    public static Float getCommission(Float score) {
        Float bonus = null;
        //佣金为0
        if (score == 0) {
            bonus = 0f;
        }
        //佣金为100代表总代理 90%返佣
        if (score == 100) {
            bonus = 1f;
        }
        bonus = score;
        return bonus;
    }

    public static Long getDay() {
        Calendar calendar = Calendar.getInstance();

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Long today = c.getTimeInMillis() / 1000;
        return (today + 86400) - System.currentTimeMillis() / 1000;
    }

    //当天时间戳
    public static Long getToday() {
        Calendar calendar = Calendar.getInstance();

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Long today = c.getTimeInMillis() / 1000;
        return today;
    }

    //获取当月第一天的时间戳
    public static long getTimesMonthmorning() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Long nowtime1 = calendar.getTimeInMillis() / 1000;
        return nowtime1;
    }

    //获取当月最后一天的时间戳
    public static long getTimesMonthmorningLast() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.set(Calendar.MINUTE, 0);
        ca.set(Calendar.SECOND, 0);
        return ca.getTimeInMillis()/1000;
    } //获取当月最后一天的时间戳
    public static long getTopStar() {
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.MONTH, -1);
        ca.set(Calendar.DAY_OF_MONTH, 1);
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.set(Calendar.MINUTE, 0);
        ca.set(Calendar.SECOND, 0);
        return ca.getTimeInMillis()/1000;
    } //获取当月最后一天的时间戳
    public static long getEnd() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH,0);
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.set(Calendar.MINUTE, 0);
        ca.set(Calendar.SECOND, 0);
        return ca.getTimeInMillis()/1000;
    }


    /**
     * 上传图片或视频
     * @param file
     * @param prefix
     * @param suffix
     * @return
     * @throws IOException
     */
    public static String upload(byte[] file, String prefix, String suffix) throws IOException {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "Pj9hJWekBfxCfUgu4MuKlLvx6WQPX7HRbUcyDIXu";
        String secretKey = "dxoV_UMvQ_bOPLHnd_DEt1U4XfvX9Z7ELa1ke2wx";
        String bucket = "quanhuang";

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = prefix + String.valueOf(new Date().getTime()) + suffix;


        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(file);
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        Response response = uploadManager.put(byteInputStream, key, upToken, null, null);

        if (response.statusCode != 200){
            return "1";
        }
        //解析上传成功的结果
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        byteInputStream.close();
//        System.out.println(putRet.key);
        return putRet.key;
    }





    /**
     * 删除空间文件
     * @return
     */
    public static void delFile(String key) {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        //...其他参数参考类注释
        String accessKey = "Pj9hJWekBfxCfUgu4MuKlLvx6WQPX7HRbUcyDIXu";
        String secretKey = "dxoV_UMvQ_bOPLHnd_DEt1U4XfvX9Z7ELa1ke2wx";
        String bucket = "quanhuang";
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
//    	    System.err.println("code===="+ex.code());
//    	    System.err.println("response==="+ex.response.toString());
        }
    }



}
