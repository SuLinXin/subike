package com.su.subike.bike.service;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.su.subike.bike.dao.BikeMapper;
import com.su.subike.bike.entity.Bike;
import com.su.subike.bike.entity.BikeLocation;
import com.su.subike.bike.entity.BikeNoGen;
import com.su.subike.common.exception.SuBikeException;
import com.su.subike.common.utils.DateUtil;
import com.su.subike.common.utils.RandomNumberCode;
import com.su.subike.fee.dao.RideFeeMapper;
import com.su.subike.fee.entity.RideFee;
import com.su.subike.record.dao.RideRecordMapper;
import com.su.subike.record.entity.RideRecord;
import com.su.subike.user.dao.UserMapper;
import com.su.subike.user.entity.User;
import com.su.subike.user.entity.UserElement;
import com.su.subike.wallet.dao.WalletMapper;
import com.su.subike.wallet.entity.Wallet;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.logging.Log_$logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Slf4j
@Service
/**
 * @ClassName BikeServiceImpl
 * @Description TODO
 * @Author 434945072@qq.com
 * Data 2019/3/15 22:24
 * Version 1.0
 **/

public class BikeServiceImpl implements BikeService {

    private static final Byte NOT_VERYFY = 1;//未认证
    private static final Object BIKE_UNLOCK = 2; //单车解锁
    private static final Object BIKE_LOCK = 1;//单车锁定

    private static final Byte RIDE_END = 2;//骑行结束


    @Autowired
    private BikeMapper bikeMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RideRecordMapper rideRecordMapper;
    @Autowired
    private WalletMapper walletMapper;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private RideFeeMapper feeMapper;

    @Override
    @Transactional
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
    @Transactional
    public void unLockBike(UserElement currentUser, Long bikeNo) throws SuBikeException {

        try {
            //校验用户是否实名认证，是否有押金
            User user = userMapper.selectByPrimaryKey(currentUser.getUserId());
            if (user.getVerifyFlag() == NOT_VERYFY){
                throw new SuBikeException("用户尚未认证");
            }
            //检查用户是否有正在进行的骑行记录
            RideRecord record = rideRecordMapper.selectRecordNotClosed(currentUser.getUserId());
            if (record != null) {
                throw new SuBikeException("存在未关闭骑行订单");
            }
            //检查用户钱包余额是否大于一元
            Wallet wallet = walletMapper.selectByUserId(currentUser.getUserId());
            if (wallet.getRemainSum().compareTo(new BigDecimal(1)) < 0) {
                throw new SuBikeException("余额不足");
            }
            //推送给单车，进行解锁

            //修改Mongodb单车状态
            Query query = Query.query(Criteria.where("bike_no").is(bikeNo));
            Update update =Update.update("status",BIKE_UNLOCK);
            mongoTemplate.updateFirst(query,update,"bike-position");

            //建立订单  记录开始骑行时间  同时骑行轨迹开始上报(另一个接口)
            RideRecord rideRecord = new RideRecord();
            rideRecord.setBikeNo(bikeNo);
            String recordNo = new Date().toString() + System.currentTimeMillis() + RandomNumberCode.randomNo();
            rideRecord.setRecordNo(recordNo);
            rideRecord.setStartTime(new Date());
            rideRecord.setUserid(currentUser.getUserId());
            rideRecordMapper.insertSelective(rideRecord);

        }catch (Exception e){
            log.error("fail to un lock bike", e);
            throw new SuBikeException("解锁单车失败");
        }


    }

    @Override
    @Transactional
    public void lockBike(BikeLocation bikeLocation) throws SuBikeException {
        try {

            //结束订单 计算骑行时间存订单
            RideRecord record = rideRecordMapper.selectBikeRecordOnGoing(bikeLocation.getBikeNumber());
            if(record==null){
                throw new SuBikeException("骑行记录不存在");
            }
            Long userid = record.getUserid();
            //查询单车类型 查询计价信息
            Bike bike = bikeMapper.selectByBikeNo(bikeLocation.getBikeNumber());
            if(bike==null){
                throw new SuBikeException("单车不存在");
            }
            RideFee fee =feeMapper.selectBikeTypeFee(bike.getType());
            if(fee==null){
                throw new SuBikeException("计费信息异常");
            }
            BigDecimal cost = BigDecimal.ZERO;
            record.setEndTime(new Date());
            record.setStatus(RIDE_END);
            Long min = DateUtil.getBetweenMin(new Date(),record.getStartTime());
            record.setRideTime(min.intValue());
            int minUnit =fee.getMinUnit();
            int intMin = min.intValue();
            if(intMin/minUnit==0){
                //不足一个时间单位 按照一个时间单位算
                cost = fee.getFee();
            }else if(intMin%minUnit==0){
                //整除了时间单位 直接计费
                cost = fee.getFee().multiply(new BigDecimal(intMin/minUnit));
            }else if(intMin%minUnit!=0){
                //不整除 +1 补足一个时间单位
                cost = fee.getFee().multiply(new BigDecimal((intMin/minUnit)+1));
            }
            record.setRideCost(cost);
            rideRecordMapper.updateByPrimaryKeySelective(record);
            //钱包扣费
            Wallet wallet = walletMapper.selectByUserId(userid);
            wallet.setRemainSum(wallet.getRemainSum().subtract(cost));
            walletMapper.updateByPrimaryKeySelective(wallet);

            //修改mongoDB中单车状态为锁定
            Query query = Query.query(Criteria.where("bike_no").is(bikeLocation.getBikeNumber()));
            Update update = Update.update("status",BIKE_LOCK)
                    .set("location.coordinates",bikeLocation.getCoordinates());
            mongoTemplate.updateFirst(query,update,"bike-position");
        } catch (Exception e) {
            log.error("fail to lock bike", e);
            throw new SuBikeException("锁定单车失败");
        }
    }

    @Override
    @Transactional
    public void reportLocation(BikeLocation bikeLocation) throws SuBikeException {
        //数据库中查询该单车尚未完结的订单
        RideRecord record = rideRecordMapper.selectBikeRecordOnGoing(bikeLocation.getBikeNumber());
        if(record==null){
            throw new SuBikeException("骑行记录不存在");
        }
        // 查询mongo中是否已经有骑行的坐标记录数据
        DBObject obj = mongoTemplate.getCollection("ride_contrail")
                .findOne(new BasicDBObject("record_no",record.getRecordNo()));
        //没有   插入 //已经存在 添加坐标
        if(obj==null){
            List<BasicDBObject> list = new ArrayList();
            BasicDBObject temp = new BasicDBObject("loc",bikeLocation.getCoordinates());
            list.add(temp);
            BasicDBObject insertObj = new BasicDBObject("record_no",record.getRecordNo())
                    .append("bike_no",record.getBikeNo())
                    .append("contrail",list);
            mongoTemplate.insert(insertObj,"ride_contrail");
        }else {
            Query query = new Query( Criteria.where("record_no").is(record.getRecordNo()));
            Update update = new Update().push("contrail", new BasicDBObject("loc",bikeLocation.getCoordinates()));
            mongoTemplate.updateFirst(query,update,"ride_contrail");
        }

    }


}
