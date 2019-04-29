package com.superman.superman.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Data
public class Userinfo {

    private Long id;


    private String username;

    private String loginname;


    private String loginpwd;


    private Long loginsecret;


    private Integer usersex;


    private Integer userscore;
    private Integer roleId;

    private String userphoto;

    private Integer userstatus;


    private Long usertotalscore;


    private String jdpid;


    private String pddpid;


    private Long tbpid;


    private String wphpid;


    private Date createtime;


    private Date updatetime;

    private Byte status;

    private String userphone;

    private Integer score;

    private String wxopenid;
    private Double cash;
    private String rid;
    private String spid;


}