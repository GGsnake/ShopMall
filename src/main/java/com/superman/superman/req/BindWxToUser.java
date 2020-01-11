package com.superman.superman.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by snake on 2018/12/20.
 */
@Setter
@Getter
@ToString
public class BindWxToUser {
    private String wx;
    private String nickname;
    private String headimgurl;
    private String token;
    private String phone;

    public Boolean isNone(){
        if (this.wx==null||this.nickname==null||this.headimgurl==null||this.token==null){
            return true;
        }
        return false;
    }
}
