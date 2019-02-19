package com.su.subike.user.contoller;

import com.su.subike.user.dao.UserMapper;
import com.su.subike.user.entity.User;
import com.su.subike.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserContoller {


//    @Autowired
//    private UserMapper userMapper;
//    @RequestMapping("/hello")
//    public User hello(){
//        User user = userMapper.selectByPrimaryKey(1L);
//        return user;
//    }

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    @RequestMapping("/login")
    public User login(){
        String token = userService.login();
        return token;
    }

}
