package com.su.subike.sms;

/**
 * Created by JackWangon[www.coder520.com] 2017/8/5.
 */
public interface SmsSender {

     void sendSms(String phone, String tplId, String params);
}
