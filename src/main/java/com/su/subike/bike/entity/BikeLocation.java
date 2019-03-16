package com.su.subike.bike.entity;

import lombok.Data;

/**
 * @ClassName BikeLocation
 * @Description TODO
 * @Author 434945072@qq.com
 * Data 2019/3/16 14:06
 * Version 1.0
 **/

@Data
public class BikeLocation {
    private String id;

    private Long bikeNumber;

    private int status;

    private Double[] coordinates;

    private Double distance;
}
