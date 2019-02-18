package com.su.subike.security;

import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadata;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class AESUtil {

    public static final String KEY_ALGORITHM = "AES";
    public static final String KEY_ALGORITHM_MODE = "AES/CBC/PKCS5Padding";

    /**
     * AES对称加密
     * @param data
     * @param key key需要16位
     * @return
     */
    public static String encrypt(String data,String key){
        try {
            SecretKeySpec spec = new SecretKeySpec(key.getBytes("UTF-8"),KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_MODE);
            cipher.init(Cipher.ENCRYPT_MODE , spec,new IvParameterSpec(new byte[cipher.getBlockSize()]));
            byte[] bs = cipher.doFinal(data.getBytes("UTF-8"));
            return Base64Util.encode(bs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }


    /**
     * AES对称解密 key需要16位
     * @param data
     * @param key
     * @return
     */
    public static String decrypt(String data, String key) {
        try {
            SecretKeySpec spec = new SecretKeySpec(key.getBytes("UTF-8"), KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_MODE);
            cipher.init(Cipher.DECRYPT_MODE , spec , new IvParameterSpec(new byte[cipher.getBlockSize()]));
            byte[] originBytes = Base64Util.decode(data);
            byte[] result = cipher.doFinal(originBytes);
            return new String(result,"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }


    public static void main(String[] args) throws Exception {
        /**AES加密数据**/
        String key = "qwertyuiopasd123";
        String message = "苏苏苏加解密测试";
        String enResult = encrypt(message,key);
        System.out.println(enResult);
        System.out.println(decrypt(enResult,key));

        /**RSA 加密AES的密钥**/
        byte[] enKey = RSAUtil.encryptByPublicKey(key.getBytes("UTF-8"),"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCaZ2Z1LAfQDMCgdl27KNxr HW+eZCTq24v/e9VwaFfzGPu4T2m3ufYpe47/Rf8F98Yft6PQvuDXte/7lgwG VpIsTPDHumx0Rfaw2/ArmgFof4oiAA563iEIQHU9lL80s39/9f0q1DEAv0Lq gt3ZbQfJCqgj+zCmP3iSd7PUaxA8fQIDAQAB");
        System.out.println(new String(enKey,"UTF-8"));
        String baseKey = Base64Util.encode(enKey);


        /**服务端RSA解密AES的key**/
        byte[] de = Base64Util.decode(baseKey);
        byte[] deKey = RSAUtil.decryptByPrivateKey(de);
        System.out.println(new String(deKey,"UTF-8"));
    }
}
