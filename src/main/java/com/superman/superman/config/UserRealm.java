package com.superman.superman.config;

import com.superman.superman.model.Userinfo;
import com.superman.superman.service.UserApiService;
import com.superman.superman.utils.ResponseCode;
import com.superman.superman.utils.TokenState;
import com.superman.superman.utils.WeikeResponseUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

/**
 * Created by liujupeng on 2018/11/22.
 */
public class UserRealm  extends AuthorizingRealm {
    @Autowired
    private UserApiService userApiService;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }
    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        //
        String userPhone= (String) authenticationToken.getPrincipal();
        String credentials = (String) authenticationToken.getCredentials();
        Userinfo userinfo = userApiService.queryUserByPhone(userPhone);

        if (userinfo==null){
            throw  new UnknownAccountException("账户不存在");

        }
        if (!DigestUtils.md5DigestAsHex(credentials.getBytes()).equals(userinfo.getLoginpwd())) {
            throw  new UnknownAccountException("密码错误");
        }
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(userPhone, credentials, getName());

        return info;
    }
}
