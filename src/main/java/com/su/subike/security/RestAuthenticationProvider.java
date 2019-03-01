package com.su.subike.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * @ClassName RestAuthenticationProvider
 * @Description TODO
 * @Author 434945072@qq.com
 * Data 2019/3/1 23:15
 * Version 1.0
 **/
public class RestAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println(3333);
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        System.out.println(22222);
        return false;
    }
}
