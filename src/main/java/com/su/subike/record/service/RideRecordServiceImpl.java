package com.su.subike.record.service;

import com.su.subike.common.exception.SuBikeException;
import com.su.subike.record.dao.RideRecordMapper;
import com.su.subike.record.entity.RideRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by JackWangon[www.coder520.com] 2017/8/25.
 */
@Service
@Slf4j
public class RideRecordServiceImpl implements RideRecordService{

    @Autowired
    private RideRecordMapper rideRecordMapper;

    @Override
    public List<RideRecord> listRideRecord(long userId, Long lastId) throws SuBikeException {

        List<RideRecord> list = rideRecordMapper.selectRideRecordPage(userId,lastId);

        return list;
    }
}
