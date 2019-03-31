package com.su.subike.record.service;

import com.su.subike.record.entity.RideRecord;
import com.su.subike.common.exception.SuBikeException;

import java.util.List;

/**
 *
 */
public interface RideRecordService {
    List<RideRecord> listRideRecord(long userId, Long lastId) throws SuBikeException;
}
