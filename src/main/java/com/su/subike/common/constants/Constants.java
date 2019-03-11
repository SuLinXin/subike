package com.su.subike.common.constants;

public class Constants {


    /**自定义状态码 start**/
    public static final int RESP_STATUS_OK = 200;

    public static final int RESP_STATUS_NOAUTH = 401;

    public static final int RESP_STATUS_INTERNAL_ERROR = 500;

    public static final int RESP_STATUS_BADREQUEST = 400;
    /**自定义状态码 end**/

    public static final String REQUEST_TOKEN_KEY = "user-token";

    public static final String REQUEST_VERSION_KEY = "version";


    /**短信验证接口 start**/
    public static final String MDSMS_ACCOUNT_SID = "80030672388947c08cb94b04de534af2";
    public static final String MDSMS_AUTH_TOKEN = "131b2f4ed56a4b04ae044b729455d306";
    public static final String MDSMS_REST_URL = "https://api.miaodiyun.com/20150822";
    public static final String MDSMS_VERCODE_TPLID= "1454862394";

    /**短信验证接口 end**/
}
