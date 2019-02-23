package com.su.subike.user.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.su.subike.cache.CommonCacheUtil;
import com.su.subike.common.exception.SuBikeException;
import com.su.subike.security.AESUtil;
import com.su.subike.security.Base64Util;
import com.su.subike.security.MD5Util;
import com.su.subike.security.RSAUtil;
import com.su.subike.user.dao.UserMapper;
import com.su.subike.user.entity.User;
import com.su.subike.user.entity.UserElement;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.generator.internal.util.StringUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sun.security.provider.MD5;

import java.io.UnsupportedEncodingException;
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommonCacheUtil cacheUtil;

    @Override
    public String login(String data, String key) throws SuBikeException {
        String token = null;
        String decryptData = null;
        try{
            byte[] aesKey = RSAUtil.decryptByPrivateKey(Base64Util.decode(key));
            decryptData = AESUtil.decrypt(data,new String(aesKey,"UTF-8"));
            if (decryptData == null){
                throw new Exception();
            }

            JSONObject jsonObject = JSON.parseObject(decryptData);
            String mobile = jsonObject.getString("mobile");
            String code = jsonObject.getString("code");
            String platform = jsonObject.getString("platform");
            if(StringUtils.isEmpty(mobile)|| StringUtils.isEmpty(code)){
                throw new Exception();
            }
            //去redis去取验证码 匹配
            String verCode =  cacheUtil.getCacheValue(mobile);
            User user;
            if(code.equals(verCode)){
                //手机匹配
                user = userMapper.selectByMobile(mobile);
                if(user == null){
                    user = new User();
                    user.setMobile(mobile);
                    user.setNickname(mobile);
                    userMapper.insertSelective(user);
                }
            }else {
                throw new SuBikeException("手机验证码错误，请检查");
            }

            //判断用户是否在数据库，没有的话帮他注册，插入数据库，生成token存在redis
            try {
                token = generateToken(user);
            }catch (Exception e){
                throw new SuBikeException("获取token失败");
            }


            UserElement ue = new UserElement();
            ue.setMobile(user.getMobile());
            ue.setUserId(user.getId());
            ue.setToken(token);
            ue.setPlatform(platform);

            cacheUtil.putTokenWhenLogin(ue);

        }catch (Exception e) {
            log.error("Fail to decrypt data",e);
            e.printStackTrace();
            throw new SuBikeException("数据解析异常");
        }
        return null;
    }

    /**
     * 生成token方法
     * @param user
     * @return
     */
    private String generateToken(User user) {
        String source = user.getId()+":"+user.getMobile()+":"+System.currentTimeMillis();
        return MD5Util.getMD5(source);
    }


}
