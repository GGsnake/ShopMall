package com.superman.superman.service.impl;

import com.superman.superman.dao.CollectDao;
import com.superman.superman.model.CollectBean;
import com.superman.superman.service.CollectService;
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


    @Override
    public List<CollectBean> queryCollect(@NonNull Long uid) {
        List<CollectBean> query = collectDao.query(uid);
        return query;
    }

    @Override
    public Boolean deleteCollect(@NonNull Integer colId,Long uid) {
        //TODO uid权限校验
        Integer flag = collectDao.delete(Long.valueOf(colId));
        return flag==0?false:true;
    }

    @Override
    public Boolean addCollect(@NonNull CollectBean collectBean) {
        Integer flag = collectDao.addCollect(collectBean);
        return flag==0?false:true;
    }
}
