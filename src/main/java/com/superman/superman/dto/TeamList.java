package com.superman.superman.dto;

/**
 * Created by liujupeng on 2018/11/8.
 */
public class TeamList {
    private  Long userId;
    private  Long score;
    private  Boolean isDaili;
    private Integer member;

    @Override
    public String toString() {
        return "TeamList{" +
                "userId=" + userId +
                ", score=" + score +
                ", isDaili=" + isDaili +
                ", member=" + member +
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

    public Boolean getDaili() {
        return isDaili;
    }

    public void setDaili(Boolean daili) {
        isDaili = daili;
    }

    public Integer getMember() {
        return member;
    }

    public void setMember(Integer member) {
        this.member = member;
    }
}
