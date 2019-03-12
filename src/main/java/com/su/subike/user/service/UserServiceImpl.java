package com.su.subike.user.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.su.subike.cache.CommonCacheUtil;
import com.su.subike.common.constants.Constants;
import com.su.subike.common.exception.SuBikeException;
import com.su.subike.common.utils.QiniuFileUploadUtil;
import com.su.subike.common.utils.RandomNumberCode;
import com.su.subike.jms.SmsProcessor;
import com.su.subike.security.AESUtil;
import com.su.subike.security.Base64Util;
import com.su.subike.security.MD5Util;
import com.su.subike.security.RSAUtil;
import com.su.subike.user.dao.UserMapper;
import com.su.subike.user.entity.User;
import com.su.subike.user.entity.UserElement;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.jms.Destination;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private static final String SMS_QUEUE = "sms.queue";
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommonCacheUtil cacheUtil;

    @Autowired
    private SmsProcessor smsProcessor;

    private static final String VERIFYCODE_PREFIX = "verify.code.";
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
            ue.setMobile(mobile);
            ue.setUserId(user.getId());
            ue.setToken(token);
            ue.setPlatform(platform);

            cacheUtil.putTokenWhenLogin(ue);

        }catch (Exception e) {
            log.error("Fail to decrypt data",e);
            e.printStackTrace();
            throw new SuBikeException("数据解析异常");
        }
        return token;
    }

    /***
     * 昵称修改
     * @param user
     * @throws SuBikeException
     */
    @Override
    public void modifyNikeName(User user) throws SuBikeException {
        userMapper.updateByPrimaryKeySelective(user);
    }

    /***
     * 发送验证码
     * @param mobile
     * @param ip
     * @throws SuBikeException
     */
    @Override
    public void sendVercode(String mobile, String ip) throws SuBikeException{

        String verCode = RandomNumberCode.verCode();
        int result = cacheUtil.CacheForVerificationCode(VERIFYCODE_PREFIX+mobile,verCode,"reg",60,ip);
        if (result == 1) {
            log.info("当前验证码未过期，请稍后重试");
            throw new SuBikeException("当前验证码未过期，请稍后重试");
        } else if (result == 2) {
            log.info("超过当日验证码次数上线");
            throw new SuBikeException("超过当日验证码次数上限");
        } else if (result == 3) {
            log.info("超过当日验证码次数上限 {}", ip);
            throw new SuBikeException(ip + "超过当日验证码次数上限");
        }

        //校验通过 发送短信  发消息到队列
        Destination destination = new ActiveMQQueue(SMS_QUEUE);
        Map<String,String> smsParam = new HashMap<>();
        smsParam.put("mobile",mobile);
        smsParam.put("tplId", Constants.MDSMS_VERCODE_TPLID);
        smsParam.put("vercode",verCode);
        String message = JSON.toJSONString(smsParam);
        smsProcessor.sendSmsToQueue(destination,message);
    }

    @Override
    public String uploadHeadImg(MultipartFile file, Long userId) throws SuBikeException {
        try {
            User user = userMapper.selectByPrimaryKey(userId);
            String imgUrlName = QiniuFileUploadUtil.uploadHeadImg(file);
            user.setHeadImg(imgUrlName);
            userMapper.updateByPrimaryKeySelective(user);

            return Constants.QINIU_HEAD_IMG_BUCKET_URL+"/"+Constants.QINIU_HEAD_IMG_BUCKET_NAME+"/"+imgUrlName;

        }catch (Exception e){
            log.error(e.getMessage(),e);
            throw new SuBikeException("头像上传失败");
        }

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
