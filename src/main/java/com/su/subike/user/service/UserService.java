package com.su.subike.user.service;

import com.su.subike.common.exception.SuBikeException;
import com.su.subike.user.entity.User;

public interface UserService {
    String login(String data, String key) throws SuBikeException;

    void modifyNikeName(User user) throws SuBikeException;
}
