package com.su.subike.bike.service;

import com.alibaba.fastjson.JSONObject;
import com.su.subike.bike.dao.BikeMapper;
import com.su.subike.bike.entity.Bike;
import com.su.subike.bike.entity.BikeNoGen;
import com.su.subike.common.exception.SuBikeException;
import com.su.subike.user.dao.UserMapper;
import com.su.subike.user.entity.User;
import com.su.subike.user.entity.UserElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

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
    private UserMapper userMapper;


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
    /**
     *@Description  解锁单车 准备骑行
     */
    @Override
    public void unLockBike(UserElement currentUser, Long bikeNo) throws SuBikeException {
        //校验用户是否实名认证，是否有押金

        //检查用户是否有正在进行的骑行记录

        //检查用户钱包余额是否大于一元

        //推送给单车，进行解锁

        //修改Mongodb单车状态


        //


    }




}
