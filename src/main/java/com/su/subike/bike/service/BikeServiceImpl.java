package com.su.subike.bike.service;

import com.su.subike.bike.dao.BikeMapper;
import com.su.subike.bike.entity.Bike;
import com.su.subike.bike.entity.BikeNoGen;
import com.su.subike.common.exception.SuBikeException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName BikeServiceImpl
 * @Description TODO
 * @Author 434945072@qq.com
 * Data 2019/3/15 22:24
 * Version 1.0
 **/

public class BikeServiceImpl implements BikeService {


    @Autowired
    private BikeMapper bikeMapper;


    @Override
    public void generateBike() throws SuBikeException {
        //单车编号生成sql
        BikeNoGen bikeNoGen = new BikeNoGen();
        bikeMapper.generateBikeNo(bikeNoGen);
        //生成单车
        Bike bike = new Bike();
        bike.setType((byte)2);
        bike.setNumber(bikeNoGen.getAutoIncNo());
        bikeMapper.insertSelective(bike);
    }
}
