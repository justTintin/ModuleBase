package com.tintin.module.activity;

/**
 * Created by n003829 on 2016/4/21.
 */
public class GameScore  {

    private String playerName;
    private Integer score;
    private Boolean isPay;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    private String appId;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Boolean getIsPay() {
        return isPay;
    }

    public void setIsPay(Boolean isPay) {
        this. isPay = isPay;
    }

    @Override
    public String toString() {
        return "GameScore{" +
                "playerName='" + playerName + '\'' +
                ", score=" + score +
                ", isPay=" + isPay +
                ", appId='" + appId + '\'' +
                '}';
    }
}