package com.su.subike.common.rest;

import com.su.subike.cache.CommonCacheUtil;
import com.su.subike.common.constants.Constants;
import com.su.subike.user.entity.UserElement;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.logging.Log_$logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
@Slf4j
public class BaseController {

    @Autowired
    private CommonCacheUtil cacheUtil;

    protected UserElement getCurrentUser(){

        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        String token = request.getHeader(Constants.REQUEST_TOKEN_KEY);
        if (!StringUtils.isEmpty(token)){
            try{
                UserElement ue = cacheUtil.getUserByToken(token);
                return ue;
            }catch (Exception e){
                log.error("fail get user by token",e);
                throw e;
            }

        }
        return null;
    }


    protected String getIpFromRequest(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
    }

}
