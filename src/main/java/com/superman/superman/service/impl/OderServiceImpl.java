package com.superman.superman.service.impl;

import com.superman.superman.dao.OderMapper;
import com.superman.superman.model.Oder;
import com.superman.superman.service.OderService;
import lombok.NonNull;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liujupeng on 2018/11/24.
 */
@Service("oderService")
public class OderServiceImpl implements OderService {

    @Autowired
    private OderMapper oderMapper;

    @Override
    public void queryAllOder(String id) {

    }

    @Override
    public void saveOder(String id) {

    }

    @Override
    public void queryJdOder(String id) {

    }

    @Override
    public List<Oder> queryPddOderListToId(@NonNull String id,@NonNull Integer status,@NonNull Integer sort) {
        if (sort==0){
            var pddoder=oderMapper.queryPddOderList(id,status,"asc");
        }
        if (sort==1){
            var pddoder=oderMapper.queryPddOderList(id,status,"desc");
        }
        return null;
    }

    @Override
    public Integer  countPddOderForId(String id) {
        return oderMapper.selectPid(id);
    }

    @Override
    public Integer countPddOderForIdList(List list) {
        return oderMapper.selectPidIn(list);
    }

    @Override
    public void queryTbOder(@NonNull String id) {


    }
}
