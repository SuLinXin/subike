package com.su.subike.security;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by JackWangon[www.coder520.com] 2017/8/3.
 */
public class BadCredentialException extends AuthenticationException {
    public BadCredentialException(String msg) {
        super(msg);
    }
}
