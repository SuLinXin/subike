package com.su.subike.security;

import com.su.subike.cache.CommonCacheUtil;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;
import javax.sound.midi.SoundbankResource;
import java.util.List;

/**
 * @ClassName RestPreAuthenticatedProcessingFilter
 * @Description TODO
 * @Author 434945072@qq.com
 * Data 2019/2/26 23:12
 * Version 1.0
 **/
public class RestPreAuthenticatedProcessingFilter extends AbstractPreAuthenticatedProcessingFilter {


    private List<String> noneSecurityPath;

    private CommonCacheUtil commonCacheUtil;

    public RestPreAuthenticatedProcessingFilter(List<String> noneSecurityPath, CommonCacheUtil commonCacheUtil) {
        this.noneSecurityPath = noneSecurityPath;
        this.commonCacheUtil = commonCacheUtil;
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest httpServletRequest) {

        System.out.println(111111);
        return null;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest httpServletRequest) {
        return null;
    }
}
