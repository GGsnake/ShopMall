package com.superman.superman.model;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.time.Instant;

/**
 * Created by snake on 2018/11/5.
 */
public class BaseBean implements Serializable {
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    public Instant createTime;

    @TableField(value = "update_Time", fill = FieldFill.INSERT_UPDATE)
    public Instant updateTime;

    @TableField(value = "status", fill = FieldFill.INSERT)
    public Boolean status;

    public Instant getCreateTime() {
        return createTime;
    }

    public BaseBean setCreateTime(Instant createTime) {
        this.createTime = createTime;
        return this;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public BaseBean setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Boolean getStatus() {
        return status;
    }

    public BaseBean setStatus(Boolean status) {
        this.status = status;
        return this;
    }
}
