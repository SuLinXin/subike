package com.su.subike.bike.service;

import com.mongodb.DBObject;
import com.su.subike.bike.entity.BikeLocation;
import com.su.subike.bike.entity.Point;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName BikeGeoService
 * @Description TODO
 * @Author 434945072@qq.com
 * Data 2019/3/16 14:17
 * Version 1.0
 **/

@Component
@Slf4j

public class BikeGeoService {



    @Autowired
    private MongoTemplate mongoTemplate;

    public List<BikeLocation> geoNearSphere(String collection, String locationField, Point point, long minDistance, long maxDistance, DBObject query, DBObject fields, int limit){

        return null;
    }
}
