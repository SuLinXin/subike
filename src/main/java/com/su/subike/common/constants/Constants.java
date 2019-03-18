package com.su.subike.common.constants;

import org.omg.CORBA.PUBLIC_MEMBER;

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

    /**短信验证接口 start**/
        public static final String QINIU_ACCESS_KEY = "YguhwoOdbMTQFT-L2KmgAO_FweShvMjb79WfkSl7";
        public static final String QINIU_SECRET_KEY = "g-7nySThM73Utcs79PWDr8cMvcQVkt5x-M-sTVAN";
        public static final String QINIU_HEAD_IMG_BUCKET_NAME="subike";
        public static final String QINIU_HEAD_IMG_BUCKET_URL="pocvhytm4.bkt.clouddn.com";
    /**短信验证接口 end**/



    /**百度云推送 start**/
    public static final String BAIDU_YUN_PUSH_API_KEY="Gw8S8sVXe1tEWWA5PeLTUGaw";

    public static final String BAIDU_YUN_PUSH_SECRET_KEY="cl9AspNY7wZDAZ0h7WDTxsRINnrXKuhZ";

    public static final String CHANNEL_REST_URL = "api.push.baidu.com";
    /**百度云推送end**/
}
