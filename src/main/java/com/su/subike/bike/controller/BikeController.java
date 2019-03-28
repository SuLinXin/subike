package com.su.subike.bike.controller;

import com.su.subike.bike.service.BikeGeoService;
import com.su.subike.bike.service.BikeService;
import com.su.subike.common.rest.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName BikeController
 * @Description TODO
 * @Author 434945072@qq.com
 * Data 2019/3/15 23:03
 * Version 1.0
 **/

@RestController
@RequestMapping("bike")
public class BikeController extends BaseController {

    @Autowired
    @Qualifier("BikeServiceImpl")
    private BikeService bikeService;
    @Autowired
    private BikeGeoService bikeGeoService;

}
