package com.bj58.dia.dm.gamechecker.entity;

public class ResultEntity {
    private int code;
    private String msg;
    private  double totalScore;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    @Override
    public String toString() {
        return "ResultEntity{" + "code=" + code + ", msg='" + msg + '\'' + ", totalScore=" + totalScore + '}';
    }
}
