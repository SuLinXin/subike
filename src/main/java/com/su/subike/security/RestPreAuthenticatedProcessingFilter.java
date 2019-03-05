package com.su.subike.security;

import com.su.subike.cache.CommonCacheUtil;
import com.su.subike.common.constants.Constants;
import com.su.subike.user.entity.UserElement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.sound.midi.SoundbankResource;
import java.util.Arrays;
import java.util.List;
@Slf4j

/**
 * @ClassName RestPreAuthenticatedProcessingFilter
 * @Description TODO
 * @Author 434945072@qq.com
 * Data 2019/2/26 23:12
 * Version 1.0
 **/
public class RestPreAuthenticatedProcessingFilter extends AbstractPreAuthenticatedProcessingFilter {
    /**
     * spring的路径匹配器
     */
    private AntPathMatcher matcher = new AntPathMatcher();

    private List<String> noneSecurityList;

    private CommonCacheUtil commonCacheUtil;

    public RestPreAuthenticatedProcessingFilter(List<String> noneSecurityList, CommonCacheUtil commonCacheUtil) {
        this.noneSecurityList = noneSecurityList;
        this.commonCacheUtil = commonCacheUtil;
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        GrantedAuthority[] authorities = new GrantedAuthority[1];
        if(isNoneSecurity(request.getRequestURI().toString()) || "OPTIONS".equals(request.getMethod())){
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_SOMEONE");
            authorities[0] = authority;
            //无需权限的url直接发放token走Provider授权
            return new RestAuthenticationToken(Arrays.asList(authorities));
        }
        //检查APP版本
        String version = request.getHeader(Constants.REQUEST_VERSION_KEY);
        String token = request.getHeader(Constants.REQUEST_TOKEN_KEY);

        if (version == null) {
            request.setAttribute("header-error", 400);
        }
//        else if(false){
        //动态获取version参数 比较 如果要求的最低版本号大于传入的版本号  设置错误头
        // request.setAttribute("header-error", 400);
//        }

        //校验token
        if (request.getAttribute("header-error") == null) {
            try {
                if (token != null && !token.trim().isEmpty()) {
                    UserElement ue = commonCacheUtil.getUserByToken(token);

                    if (ue instanceof UserElement) {
                        //检查到token说明用户已经登录 授权给用户BIKE_CLIENT角色 允许访问
                        GrantedAuthority authority = new SimpleGrantedAuthority("BIKE_CLIENT");
                        authorities[0] = authority;
                        RestAuthenticationToken authToken = new RestAuthenticationToken(Arrays.asList(authorities));
                        authToken.setUser(ue);
                        return authToken;
                    } else {
                        //token不对
                        request.setAttribute("header-error", 401);
                    }
                } else {
                    log.warn("Got no token from request header");
                    //token不存在 告诉移动端 登录
                    request.setAttribute("header-error", 401);
                }
            } catch (Exception e) {
                log.error("Fail to authenticate user", e);
            }

        }

        if(request.getAttribute("header-error") != null){
            //请求头有错误  随便给个角色 让逻辑继续
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_NONE");
            authorities[0] = authority;
        }
        RestAuthenticationToken authToken = new RestAuthenticationToken(Arrays.asList(authorities));
        return authToken;
    }

    private boolean isNoneSecurity(String uri) {
        boolean result = false;
        if (this.noneSecurityList != null) {
            for (String pattern : this.noneSecurityList) {
                if (matcher.match(pattern, uri)) {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest httpServletRequest) {
        return null;
    }
}
