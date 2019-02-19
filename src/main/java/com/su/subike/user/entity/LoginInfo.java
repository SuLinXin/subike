package com.su.subike.user.entity;

import lombok.Data;

@Data
public class LoginInfo {

    /**登录信息密文***/
    private String data;


    /**RSA加密的AESkey***/
    private String key;

}
