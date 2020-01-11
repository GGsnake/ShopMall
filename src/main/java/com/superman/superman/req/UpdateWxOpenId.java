package com.superman.superman.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by snake on 2019/1/9.
 */
@ToString
@Setter
@Getter
public class UpdateWxOpenId {
    private String id;
    private String name;
    private String photo;
    private String phone;

}
