package com.superman.superman.model;

/**
 * Created by liujupeng on 2018/11/8.
 */
public class Role {
    private  Long userId;
    @Override
    public String toString() {
        return "Test{" +
                "userId=" + userId +
                ", score=" + score +
                '}';
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    private  Long score;
}

