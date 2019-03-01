package com.su.subike.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.midi.SoundbankResource;
import java.io.IOException;

/**
 * @ClassName RestAuthenticationEntryPoint
 * @Description TODO
 * @Author 434945072@qq.com
 * Data 2019/3/1 23:20
 * Version 1.0
 **/
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        System.out.println(44444);
    }
}
