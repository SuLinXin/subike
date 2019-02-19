package com.su.subike.user.contoller;

import com.su.subike.common.constants.Constants;
import com.su.subike.common.resp.ApiResult;
import com.su.subike.user.dao.UserMapper;
import com.su.subike.user.entity.LoginInfo;
import com.su.subike.user.entity.User;
import com.su.subike.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
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
    public ApiResult<String> login(@ResponseBody LoginInfo loginInfo){

        ApiResult<String> resp = new ApiResult<>();

        try{
            String token = userService.login();
            resp.setData(token);
        }catch (Exception e){
            log.error("Fail to login",e);
            resp.setCode(Constants.RESP_STATUS_BADREQUEST);
            resp.setMessage("login内部错误");
        }


        return resp;
    }

}
