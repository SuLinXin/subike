package com.su.subike.common.exception;

import com.su.subike.common.constants.Constants;

public class SuBikeException extends Exception{

    public SuBikeException(String message){
        super(message);
    }

    public int getStatusCode(){
        return Constants.RESP_STATUS_INTERNAL_ERROR;
    }
}
