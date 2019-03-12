package com.su.subike.user.service;

import com.su.subike.common.exception.SuBikeException;
import com.su.subike.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    String login(String data, String key) throws SuBikeException;

    void modifyNikeName(User user) throws SuBikeException;

    void sendVercode(String mobile, String ip) throws SuBikeException;

    String uploadHeadImg(MultipartFile file, Long userId) throws SuBikeException;
}
