package com.bj58.dia.dm.gamechecker.entity;

import java.util.List;

public class AuntEntity {
    private String id;
    private Double serviceScore;
    private Double x;
    private Double y;

    private List<OrderEntity> dispatOrderList;

    public List<OrderEntity> getDispatOrderList() {
        return dispatOrderList;
    }

    public void setDispatOrderList(List<OrderEntity> dispatOrderList) {
        this.dispatOrderList = dispatOrderList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getServiceScore() {
        return serviceScore;
    }

    public void setServiceScore(Double serviceScore) {
        this.serviceScore = serviceScore;
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
