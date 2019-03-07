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
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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

    @RequestMapping("/sendVercode")
    public ApiResult sendVercode(@RequestBody User user){
        ApiResult resp = new ApiResult();
        try{


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

    /**
     *@Author
     *@Date 2017/8/5 0:59
     *@Description 修改头像
     */
//    @ApiOperation(value="上传头像",notes = "用户上传头像 file" ,httpMethod = "POST")
//    @RequestMapping(value = "/uploadHeadImg", method = RequestMethod.POST)
//    public ApiResult<String> uploadHeadImg(HttpServletRequest req, @RequestParam(required=false ) MultipartFile file) {
//
//        ApiResult<String> resp = new ApiResult<>();
//        try {
//            UserElement ue = getCurrentUser();
//            userService.uploadHeadImg(file,ue.getUserId());
//            resp.setMessage("上传成功");
//        } catch (SuBikeException e) {
//            resp.setCode(e.getStatusCode());
//            resp.setMessage(e.getMessage());
//        } catch (SuBikeException e) {
//            log.error("Fail to update user info", e);
//            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
//            resp.setMessage("内部错误");
//        }
//        return resp;
//    }

}
