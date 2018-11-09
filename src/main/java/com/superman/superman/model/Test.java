package com.superman.superman.model;

/**
 * Created by liujupeng on 2018/11/8.
 */
public class Test {
    private Integer agentId;
    private Integer userId;

    @Override
    public String toString() {
        return "Test{" +
                "agentId=" + agentId +
                ", userId=" + userId +
                '}';
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }
}
