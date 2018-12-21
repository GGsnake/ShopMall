package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.CollectDao;
import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.model.CollectBean;
import com.superman.superman.model.Userinfo;
import com.superman.superman.service.CollectService;
import com.superman.superman.utils.PageParam;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.NonNull;

import java.util.List;


/**
 * Created by liujupeng on 2018/11/20.
 */
@Log
@Service("collectService")
public class CollectServiceImpl implements CollectService {

    @Autowired
    private CollectDao collectDao;
    @Autowired
    private UserinfoMapper userinfoMapper;


    @Override
    public JSONArray queryCollect(@NonNull Long uid, @NonNull PageParam pageParam) {
        Userinfo userinfo = userinfoMapper.selectByPrimaryKey(uid);
        if (userinfo==null){
            return null;
        }
        Integer score = userinfo.getScore();
        List<CollectBean> query = collectDao.query(uid,pageParam.getStartRow(),pageParam.getPageSize());
        if (query==null||query.size()==0){
            return null;
        }
        JSONArray data=new JSONArray();
        for (CollectBean collectBean:query){

            JSONObject temp=new JSONObject();
            Long promotion_rate = collectBean.getPromotion_rate();

            temp.put("goodId",collectBean.getGoodId());
            temp.put("title",collectBean.getTitle());
            temp.put("image",collectBean.getImage());
            temp.put("id",collectBean.getId());
            temp.put("src",collectBean.getSrc());
            temp.put("price",collectBean.getPrice());
            temp.put("volume",collectBean.getVolume());
            temp.put("coupon",collectBean.getCoupon());
            // TODO
            temp.put("agent",promotion_rate);
            temp.put("coupon_price",collectBean.getCoupon_price());
            data.add(temp);
        }
        return data;
    }

    @Override
    public Boolean deleteCollect(@NonNull Integer colId,Long uid) {
        CollectBean collectBean = collectDao.querySimple(colId);
        if (collectBean==null||collectBean.getUserId()!=uid)
            return false;
        Integer flag =0;
        try {
            flag =collectDao.delete(Long.valueOf(colId));
        }
        catch (Exception e){
            log.warning(e.getMessage());
            return false;
        }
        return flag==0?false:true;
    }

    @Override
    public Integer countCollect(@NonNull Long uid) {
        Integer count = collectDao.count(uid);
        return count==null?0:count;
    }

    @Override
    public Boolean addCollect(@NonNull CollectBean collectBean) {
        Integer flag=0;
        try {
            flag = collectDao.addCollect(collectBean);
        }
        catch (Exception e){
            log.warning(e.getMessage());
            return false;
        }
        return flag==0?false:true;
    }
}
