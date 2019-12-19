package com.superman.superman.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.superman.superman.model.enums.UserLevel;

@TableName
public class User extends BaseBean {
    @TableId(type = IdType.ASSIGN_ID)
    private Long userId;
    private String name;
    private String avatarUrl;
    private String mobile;
    private String password;
    private UserLevel userlevel;
    private Long superUser;
    private Long inviterUser;
    private String userTree;
    private Integer treeLevel;
    private String inviterCode;

    public String getInviterCode() {
        return inviterCode;
    }

    public User setInviterCode(String inviterCode) {
        this.inviterCode = inviterCode;
        return this;
    }

    public Long getSuperUser() {
        return superUser;
    }

    public User setSuperUser(Long superUser) {
        this.superUser = superUser;
        return this;
    }

    public Long getInviterUser() {
        return inviterUser;
    }

    public User setInviterUser(Long inviterUser) {
        this.inviterUser = inviterUser;
        return this;
    }

    public String getUserTree() {
        return userTree;
    }

    public User setUserTree(String userTree) {
        this.userTree = userTree;
        return this;
    }

    public Integer getTreeLevel() {
        return treeLevel;
    }

    public User setTreeLevel(Integer treeLevel) {
        this.treeLevel = treeLevel;
        return this;
    }

    public UserLevel getUserlevel() {
        return userlevel;
    }

    public User setUserlevel(UserLevel userlevel) {
        this.userlevel = userlevel;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public User setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public User setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public String getMobile() {
        return mobile;
    }

    public User setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }
}
