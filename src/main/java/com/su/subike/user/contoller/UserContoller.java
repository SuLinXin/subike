package com.su.subike.user.contoller;

import com.alibaba.druid.util.StringUtils;
import com.su.subike.common.constants.Constants;
import com.su.subike.common.exception.SuBikeException;
import com.su.subike.common.resp.ApiResult;
import com.su.subike.common.rest.BaseController;
import com.su.subike.user.dao.UserMapper;
import com.su.subike.user.entity.LoginInfo;
import com.su.subike.user.entity.User;
import com.su.subike.user.entity.UserElement;
import com.su.subike.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

@Slf4j
@RestController
@RequestMapping("user")
public class UserContoller extends BaseController {


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

    @RequestMapping(value = "/login",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult<String> login(@RequestBody LoginInfo loginInfo){

        ApiResult<String> resp = new ApiResult<>();

        try{
            String data = loginInfo.getData();
            String key = loginInfo.getKey();
            if(StringUtils.isEmpty(data) || StringUtils.isEmpty(key)){
                throw new SuBikeException("参数校准失败");
            }


            String token = userService.login(data,key);
            resp.setData(token);
        }catch (SuBikeException e){
                resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
                resp.setMessage(e.getMessage());
        }catch (Exception e){
            log.error("Fail to login",e);
            resp.setCode(Constants.RESP_STATUS_BADREQUEST);
            resp.setMessage("login内部错误");
        }


        return resp;
    }

    /**
     * 修改用户昵称
     * @param user
     * @return
     */
    @RequestMapping("/modifyNickName")
    public ApiResult modifyNickName(@RequestBody User user){
        ApiResult resp = new ApiResult();

        try{

            UserElement ue = getCurrentUser();
            user.setId(ue.getUserId());
            userService.modifyNikeName(user);
            resp.setMessage("昵称修改成功");
        }catch (SuBikeException e){
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage(e.getMessage());
        }catch (Exception e){
            log.error("Fail to modify nicename",e);
            resp.setCode(Constants.RESP_STATUS_BADREQUEST);
            resp.setMessage("修改昵称失败");
        }

        return resp;
    }

}
