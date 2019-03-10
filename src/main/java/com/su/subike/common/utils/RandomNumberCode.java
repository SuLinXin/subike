package com.su.subike.common.utils;


import org.apache.commons.lang3.StringUtils;

import java.util.Random;

/**
 * @ClassName RandomNumberCode
 * @Description TODO
 * @Author 434945072@qq.com
 * Data 2019/3/10 14:38
 * Version 1.0
 **/
public class RandomNumberCode {


    public static String verCode(){
        Random random = new Random();
        return StringUtils.substring(String.valueOf(random.nextInt()),2,8);

    }

}
