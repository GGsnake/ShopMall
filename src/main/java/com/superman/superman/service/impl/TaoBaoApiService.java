package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.NTbkItem;
import com.taobao.api.request.TbkItemGetRequest;
import com.taobao.api.response.TbkItemGetResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liujupeng on 2018/12/4.
 */
@Service
public class TaoBaoApiService implements com.superman.superman.service.TaoBaoApiService {
    final String url = "http://gw.api.taobao.com/router/rest";
    final String appkey = "25338125";
    final String secret = "c4d36be247e477a9d88704f022e1c514";

    @Override
    public JSONObject serachGoods(String Keywords, String cat, Boolean isTmall, Long page_no, Long page_size, String sort, String itemloc) {

        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        TbkItemGetRequest req = new TbkItemGetRequest();
        req.setFields("num_iid,title,pict_url,reserve_price,zk_final_price,item_url,volume");
//        req.setFields("num_iid,title,pict_url,small_images,reserve_price,zk_final_price,user_type,provcity,item_url,seller_id,volume,nick");
        if (Keywords != null) {
            req.setQ(Keywords);
        }
        if (cat != null) {
            req.setCat(cat);
        }
        if (isTmall != null) {
            req.setIsTmall(isTmall);
        }
        if (sort != null) {
            req.setSort(sort);
        }
        if (itemloc != null) {
            req.setItemloc(itemloc);
        }
        JSONObject datta=new JSONObject();
        JSONArray data=new JSONArray();
        req.setPageNo(page_no);
        req.setPageSize(page_size);

        TbkItemGetResponse rsp = null;
        try {
            rsp = client.execute(req);
            List<NTbkItem> results = rsp.getResults();
            if ( results== null) {

                datta.put("data",data);
                datta.put("sum",0);
                return datta;
            }
            for (NTbkItem nk:results){
                Float pri= Float.valueOf(nk.getReservePrice());
                Float zkFinalPrice = Float.valueOf(nk.getZkFinalPrice());
                JSONObject nktb=new JSONObject();
                nktb.put("goodsId",nk.getNumIid());
                nktb.put("volume",nk.getVolume());
                nktb.put("imgUrl",nk.getPictUrl());
                nktb.put("price",nk.getReservePrice());
                nktb.put("priceAfter",nk.getZkFinalPrice());
                nktb.put("coupe",pri-zkFinalPrice);
                data.add(nktb);
            }
            datta.put("data",data);
            datta.put("sum",rsp.getTotalResults());
            return datta;
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }
}
