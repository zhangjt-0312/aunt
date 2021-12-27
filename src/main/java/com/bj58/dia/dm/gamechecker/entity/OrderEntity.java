package com.bj58.dia.dm.gamechecker.entity;

import java.util.Date;

public class OrderEntity {
    private String id;

    private Date serviceBeginTime;
    private Integer serviceUnitTime;

    private Double x;
    private Double y;
    private Double serviceScore;
    private Double distanceScore;
    private Double timeScore;

    public Double getServiceScore() {
        return serviceScore;
    }

    public void setServiceScore(Double serviceScore) {
        this.serviceScore = serviceScore;
    }

    public Double getDistanceScore() {
        return distanceScore;
    }

    public void setDistanceScore(Double distanceScore) {
        this.distanceScore = distanceScore;
    }

    public Double getTimeScore() {
        return timeScore;
    }

    public void setTimeScore(Double timeScore) {
        this.timeScore = timeScore;
    }

    private AuntEntity auntEntity;

    public AuntEntity getAuntEntity() {
        return auntEntity;
    }

    public void setAuntEntity(AuntEntity auntEntity) {
        this.auntEntity = auntEntity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getServiceBeginTime() {
        return serviceBeginTime;
    }

    public void setServiceBeginTime(Date serviceBeginTime) {
        this.serviceBeginTime = serviceBeginTime;
    }

    public Integer getServiceUnitTime() {
        return serviceUnitTime;
    }

    public void setServiceUnitTime(Integer serviceUnitTime) {
        this.serviceUnitTime = serviceUnitTime;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }
}
