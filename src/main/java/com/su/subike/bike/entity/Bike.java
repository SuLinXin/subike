package com.su.subike.bike.entity;

import lombok.Data;

@Data

public class Bike {
    private Long id;

    private Long number;

    private Byte type;

    private Byte enableFlag;

}