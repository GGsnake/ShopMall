package com.superman.superman.manager;

import com.superman.superman.dao.OderMapper;
import com.superman.superman.model.Oder;
import lombok.NonNull;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liujupeng on 2018/11/21.
 */
@Service("oderManager")
public class OderManager {
    @Autowired
    private OderMapper oderMapper;

    public void getAllOder(@NonNull Oder oder){

        var pddOder =oderMapper.selectAll(oder);

    }

}
