package com.su.subike.fee.dao;

import com.su.subike.fee.entity.RideFee;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface RideFeeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RideFee record);

    int insertSelective(RideFee record);

    RideFee selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RideFee record);

    int updateByPrimaryKey(RideFee record);

    RideFee selectBikeTypeFee(Byte type);
}