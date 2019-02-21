package com.su.subike.user.service;

import com.su.subike.common.exception.SuBikeException;

public interface UserService {
    String login(String data, String key) throws SuBikeException;
}
