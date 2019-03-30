package com.su.subike.bike.service;

import com.su.subike.bike.entity.BikeLocation;
import com.su.subike.common.exception.SuBikeException;
import com.su.subike.user.entity.UserElement;

/**
 * @ClassName BikeService
 * @Description TODO
 * @Author 434945072@qq.com
 * Data 2019/3/15 22:16
 * Version 1.0
 **/
public interface BikeService {
    void generateBike() throws SuBikeException;
    void unLockBike(UserElement currentUser, Long bikeNo) throws SuBikeException;
    void lockBike( BikeLocation bikeLocation)throws SuBikeException;
    void reportLocation(BikeLocation bikeLocation)throws SuBikeException;
}
