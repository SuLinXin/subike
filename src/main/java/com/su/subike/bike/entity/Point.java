package com.su.subike.bike.entity;

import lombok.Data;

/**
 * @ClassName Point
 * @Description TODO
 * @Author 434945072@qq.com
 * Data 2019/3/16 14:11
 * Version 1.0
 **/
@Data
public class Point {


    public Point() {
    }
    public Point(Double[] loc){
        this.longitude = loc[0];
        this.latitude = loc[1];
    }
    public Point(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    private double longitude;

    private double latitude;
}
