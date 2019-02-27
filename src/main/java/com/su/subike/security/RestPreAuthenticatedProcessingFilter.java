package com.su.subike.security;

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName RestPreAuthenticatedProcessingFilter
 * @Description TODO
 * @Author 434945072@qq.com
 * Data 2019/2/26 23:12
 * Version 1.0
 **/
public class RestPreAuthenticatedProcessingFilter extends AbstractPreAuthenticatedProcessingFilter {


    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest httpServletRequest) {
        return null;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest httpServletRequest) {
        return null;
    }
}
