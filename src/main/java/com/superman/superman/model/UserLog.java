package com.superman.superman.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.superman.superman.model.enums.LogOperationEnum;

import java.time.Instant;

/**
 * Created by sanke on 2018/11/24.
 */
@TableName
public class UserLog  {
    @TableId
    private Long id;
    private Long userId;
    private LogOperationEnum operationEnum;
    private String ip;
    public Instant createTime=Instant.now();

    public Long getId() {
        return id;
    }

    public UserLog setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public UserLog setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public LogOperationEnum getOperationEnum() {
        return operationEnum;
    }

    public UserLog setOperationEnum(LogOperationEnum operationEnum) {
        this.operationEnum = operationEnum;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public UserLog setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public UserLog setCreateTime(Instant createTime) {
        this.createTime = createTime;
        return this;
    }
}
