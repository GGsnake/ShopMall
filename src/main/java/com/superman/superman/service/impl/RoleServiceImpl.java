package com.superman.superman.service.impl;

import com.superman.superman.dao.PddDao;
import com.superman.superman.model.Role;
import com.superman.superman.model.Test;
import com.superman.superman.model.UserRole;
import com.superman.superman.service.RoleService;
import lombok.NonNull;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liujupeng on 2018/11/20.
 */
@Service("roleService")
public class RoleServiceImpl  implements RoleService {

    @Autowired
    private PddDao pddDao;


    @Override
    public UserRole getRole(@NonNull Long uid) {
       var role= pddDao.selectR(uid);
       var score=role.getScore();
       var userRole=new UserRole();
       userRole.setScore(score.intValue());
        if (score == 0) {
            userRole.setRoleId(3);
        }
        //佣金为100代表总代理 90%返佣
        if (score == 100) {
            userRole.setRoleId(1);
        }
        userRole.setRoleId(2);
        return userRole;
    }
}
