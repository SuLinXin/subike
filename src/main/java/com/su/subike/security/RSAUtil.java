package com.su.subike.security;



import javax.crypto.Cipher;


import java.io.InputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class RSAUtil {

    /**
     * 私钥字符串
     */
    private static String PRIVATE_KEY ="";
    /**
     * 公钥字符串
     */
    private static String PUBLIC_KEY ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCaZ2Z1LAfQDMCgdl27KNxr HW+eZCTq24v/e9VwaFfzGPu4T2m3ufYpe47/Rf8F98Yft6PQvuDXte/7lgwG VpIsTPDHumx0Rfaw2/ArmgFof4oiAA563iEIQHU9lL80s39/9f0q1DEAv0Lq gt3ZbQfJCqgj+zCmP3iSd7PUaxA8fQIDAQAB";

    public static final String KEY_ALGORITHM = "RSA";


    /**
     * 读取密钥字符串
     * @throws Exception
     */

    public static void convert() throws Exception {
        byte[] data = null;

        try {
            InputStream is = RSAUtil.class.getResourceAsStream("/enc_pri");
            int length = is.available();
            data = new byte[length];
            is.read(data);
        } catch (Exception e) {
        }

        String dataStr = new String(data);
        try {
            PRIVATE_KEY = dataStr;
        } catch (Exception e) {
        }

        if (PRIVATE_KEY == null) {
            throw new Exception("Fail to retrieve key");
        }
    }



    /**
     * 私钥解密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data) throws Exception {
        convert();
        byte[] keyBytes = Base64Util.decode(PRIVATE_KEY);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
//        Key privateKey = makePrivateKey(PRIVATE_KEY);
//        Cipher cipher = Cipher.getInstance("RSA");
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(data);
    }

    /**
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String key) throws Exception {
        byte[] keyBytes = Base64Util.decode(key);
        X509EncodedKeySpec pkcs8KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(pkcs8KeySpec);

        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
//        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     *  引入第三方密码工具包 处理编码
     * @param stored
     * @return
     * @throws GeneralSecurityException
     * @throws Exception
     */
//    public static PrivateKey makePrivateKey(String stored) throws GeneralSecurityException, Exception {
//        /*byte[] data = Base64.getDecoder().decode(stored);
//        PKCS8EncodedKeySpec spec = new  PKCS8EncodedKeySpec(data);
//        KeyFactory fact = KeyFactory.getInstance("RSA");
//        return fact.generatePrivate(spec);*/
//        byte[] data = Base64Util.decode(stored);
//        ASN1EncodableVector v = new ASN1EncodableVector();
//        v.add(new ASN1Integer(0));
//        ASN1EncodableVector v2 = new ASN1EncodableVector();
//        v2.add(new ASN1ObjectIdentifier(PKCSObjectIdentifiers.rsaEncryption.getId()));
//        v2.add(DERNull.INSTANCE);
//        v.add(new DERSequence(v2));
//        v.add(new DEROctetString(data));
//        ASN1Sequence seq = new DERSequence(v);
//        byte[] privKey = seq.getEncoded("DER");
//        PKCS8EncodedKeySpec spec = new  PKCS8EncodedKeySpec(privKey);
//        KeyFactory fact = KeyFactory.getInstance("RSA");
//        PrivateKey key = fact.generatePrivate(spec);
//
//        return key;
//
//    }




    public static void main(String[] args) throws Exception {
//        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
//        keyPairGenerator.initialize(1024);
//
//        KeyPair keyPair = keyPairGenerator.generateKeyPair();
//        PublicKey publicKey = keyPair.getPublic();
//        PrivateKey privateKey = keyPair.getPrivate();
//        System.out.println(Base64Util.encode(publicKey.getEncoded()));
//        System.out.println(Base64Util.encode(privateKey.getEncoded()));

        String data = "ceshi";
        byte[] enResult= encryptByPublicKey(data.getBytes("UTF-8"),PUBLIC_KEY);
        System.out.println(enResult.toString());
        byte[] deResult = decryptByPrivateKey(enResult);
        System.out.println(new String(deResult,"UTF-8"));
    }
}
