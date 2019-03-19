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
     *@Author JackWang [www.coder520.com]
     *@Date 2017/8/21 22:34
     *@Description  解锁单车 准备骑行
     */
    @Override
    @Transactional
    public void unLockBike(UserElement currentUser, Long bikeNo) throws SuBikeException {
        try {
            //检查用户是否已经认证（实名认证没  押金交了没 ）
            User user = userMapper.selectByPrimaryKey(currentUser.getUserId());
            if (user.getVerifyFlag() == NOT_VERYFY) {
                throw new SuBikeException("用户尚未认证");
            }
            //检查用户有没有未关闭的骑行记录
            RideRecord record = rideRecordMapper.selectRecordNotClosed(currentUser.getUserId());
            if (record != null) {
                throw new SuBikeException("存在未关闭骑行订单");
            }
            //检查用户钱包余额是否足够（大于一元）
            Wallet wallet = walletMapper.selectByUserId(currentUser.getUserId());
            if (wallet.getRemainSum().compareTo(new BigDecimal(1)) < 0) {
                throw new SuBikeException("余额不足");
            }
            //推送单车进行解锁
            JSONObject notification = new JSONObject();
            notification.put("unlock", "unlock");
            BaiduPushUtil.pushMsgToSingleDevice(currentUser,"{\"title\":\"TEST\",\"description\":\"Hello Baidu push!\"}");
            //推送如果可靠性比较差 可以采用单车端开锁后 主动ACK服务器 再修改相关状态的方式
            // 修改mongoDB中单车状态
            Query query = Query.query(Criteria.where("bike_no").is(bikeNo));
            Update update = Update.update("status", BIKE_UNLOCK);
            mongoTemplate.updateFirst(query, update, "bike-position");
            //建立订单  记录开始骑行时间  同时骑行轨迹开始上报(另一个接口)
            RideRecord rideRecord = new RideRecord();
            rideRecord.setBikeNo(bikeNo);
            String recordNo = new Date().toString() + System.currentTimeMillis() + RandomNumberCode.randomNo();
            rideRecord.setRecordNo(recordNo);
            rideRecord.setStartTime(new Date());
            rideRecord.setUserid(currentUser.getUserId());
            rideRecordMapper.insertSelective(rideRecord);
        } catch (Exception e) {
            log.error("fail to un lock bike", e);
            throw new SuBikeException("解锁单车失败");
        }
    }




}
