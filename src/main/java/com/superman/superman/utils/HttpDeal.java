package com.superman.superman.utils;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 在java中处理http请求.
 * @author ljp
 *
 */
public class HttpDeal {
    /**
     * 处理get请求.
     * @return  json
     */
    public static  String get(String urls) throws MalformedURLException, URISyntaxException {
        //实例化httpclient
        CloseableHttpClient httpclient = HttpClients.createDefault();
        //实例化get方法
        URL url = new URL(urls);
        URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
        HttpGet httpget = new HttpGet(uri);

        //请求结果
        CloseableHttpResponse response = null;
        String content ="";
        try {
            //执行get方法
            response = httpclient.execute(httpget);
            if(response.getStatusLine().getStatusCode()==200){
                content = EntityUtils.toString(response.getEntity(),"utf-8");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                httpclient.close();//释放资源
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }
    /**
     * 处理post请求.
     * @param url  请求路径
     * @param params  参数
     * @return  json
     */
    public String post(String url,Map<String, String> params){
        //实例化httpClient
        CloseableHttpClient httpclient = HttpClients.createDefault();
        //实例化post方法
        HttpPost httpPost = new HttpPost(url);
        //处理参数
        List<NameValuePair> nvps = new ArrayList <NameValuePair>();
        Set<String> keySet = params.keySet();
        for(String key : keySet) {
            nvps.add(new BasicNameValuePair(key, params.get(key)));
        }
        //结果
        CloseableHttpResponse response = null;
        String content="";
        try {
            //提交的参数
            UrlEncodedFormEntity uefEntity  = new UrlEncodedFormEntity(nvps, "UTF-8");
            //将参数给post方法
            httpPost.setEntity(uefEntity);
            //执行post方法
            response = httpclient.execute(httpPost);
            if(response.getStatusLine().getStatusCode()==200){
                content = EntityUtils.toString(response.getEntity(),"utf-8");
                System.out.println(content);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
    public static InputStream getInputStream(String ursl){
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(ursl);//创建的URL
            if (url!=null) {
                httpURLConnection = (HttpURLConnection) url.openConnection();//打开链接
                httpURLConnection.setConnectTimeout(3000);//设置网络链接超时时间，3秒，链接失败后重新链接
                httpURLConnection.setDoInput(true);//打开输入流
                httpURLConnection.setRequestMethod("GET");//表示本次Http请求是GET方式
                int responseCode = httpURLConnection.getResponseCode();//获取返回码
                if (responseCode == 200) {//成功为200
                    //从服务器获得一个输入流
                    inputStream = httpURLConnection.getInputStream();
                }
            }


        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return inputStream;

    }
    public static void saveImageToDisk(String url,Long id) {
        InputStream inputStream = null;
        inputStream = getInputStream(url);//调用getInputStream()函数。
        byte[] data = new byte[1024];
        int len = 0;

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream("D:\\12231.png");//初始化一个FileOutputStream对象。
            while ((len = inputStream.read(data))!=-1) {//循环读取inputStream流中的数据，存入文件流fileOutputStream
                fileOutputStream.write(data,0,len);
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{//finally函数，不管有没有异常发生，都要调用这个函数下的代码
            if(fileOutputStream!=null){
                try {
                    fileOutputStream.close();//记得及时关闭文件流
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if(inputStream!=null){
                try {
                    inputStream.close();//关闭输入流
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }



    }

}
