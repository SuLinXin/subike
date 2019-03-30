package com.su.subike.bike.controller;

import com.su.subike.bike.entity.Bike;
import com.su.subike.bike.entity.BikeLocation;
import com.su.subike.bike.entity.Point;
import com.su.subike.bike.service.BikeGeoService;
import com.su.subike.bike.service.BikeService;
import com.su.subike.common.constants.Constants;
import com.su.subike.common.exception.SuBikeException;
import com.su.subike.common.resp.ApiResult;
import com.su.subike.common.rest.BaseController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @ClassName BikeController
 * @Description TODO
 * @Author 434945072@qq.com
 * Data 2019/3/15 23:03
 * Version 1.0
 **/
@Slf4j

@RestController
@RequestMapping("bike")
public class BikeController extends BaseController {

    @Autowired
    @Qualifier("bikeServiceImpl")
    private BikeService bikeService;

    @Autowired
    private BikeGeoService bikeGeoService;




    /**
     *@Author JackWang [www.coder520.com]
     *@Date 2017/8/6 17:29
     *@Description 生成单车
     */
    @ApiIgnore
    @RequestMapping("/generateBike")
    public ApiResult generateBike(){

        ApiResult<String> resp = new ApiResult<>();
        try {

            bikeService.generateBike();
            resp.setMessage("创建单车成功");
        } catch (SuBikeException e) {
            resp.setCode(e.getStatusCode());
            resp.setMessage(e.getMessage());
        } catch (Exception e) {
            log.error("Fail to update bike info", e);
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage("内部错误");
        }

        return resp;
    }

    /**
     *@Author JackWang [www.coder520.com]
     *@Date 2017/8/8 21:38
     *@Description 查找某坐标附近单车
     */

    @ApiOperation(value="查找附近单车",notes = "根据用户APP定位坐标来查找附近单车",httpMethod = "POST")
    @ApiImplicitParam(name = "point",value = "用户定位坐标",required = true,dataType = "Point")
    @RequestMapping("/findAroundBike")
    public ApiResult findAroundBike(@RequestBody Point point ){

        ApiResult<List<BikeLocation>> resp = new ApiResult<>();
        try {
            List<BikeLocation> bikeList = bikeGeoService.geoNear("bike-position",null,point,10,50);
            resp.setMessage("查询附近单车成功");
            resp.setData(bikeList);
        } catch (SuBikeException e) {
            resp.setCode(e.getStatusCode());
            resp.setMessage(e.getMessage());
        } catch (Exception e) {
            log.error("Fail to find around bike info", e);
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage("内部错误");
        }

        return resp;
    }

    /**
     *@Author JackWang [www.coder520.com]
     *@Date 2017/8/21 12:13
     *@Description  解锁单车 准备骑行
     */
    @ApiOperation(value="解锁单车",notes = "根据单车编号解锁单车",httpMethod = "POST")
    @ApiImplicitParam(name = "bike",value = "单车编号",required = true,dataType = "Bike")
    @RequestMapping("/unLockBike")
    public ApiResult unLockBike(@RequestBody Bike bike){

        ApiResult<List<BikeLocation>> resp = new ApiResult<>();
        try {
            bikeService.unLockBike(getCurrentUser(),bike.getNumber());
            resp.setMessage("等待单车解锁");
        } catch (SuBikeException e) {
            resp.setCode(e.getStatusCode());
            resp.setMessage(e.getMessage());
        } catch (Exception e) {
            log.error("Fail to unlock bike ", e);
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage("内部错误");
        }

        return resp;
    }


    /**
     *@Author JackWang [www.coder520.com]
     *@Date 2017/8/21 12:14
     *@Description  锁车 骑行结束
     */
    @ApiOperation(value="锁定单车",notes = "骑行结束锁定单车（需要上传锁定时候定位坐标）",httpMethod = "POST")
    @ApiImplicitParam(name = "bikeLocation",value = "单车编号",required = true,dataType = "BikeLocation")
    @RequestMapping("/lockBike")
    public ApiResult lockBike(@RequestBody BikeLocation bikeLocation){

        ApiResult<List<BikeLocation>> resp = new ApiResult<>();
        try {
            bikeService.lockBike(bikeLocation);
            resp.setMessage("锁车成功");
        } catch (SuBikeException e) {
            resp.setCode(e.getStatusCode());
            resp.setMessage(e.getMessage());
        } catch (Exception e) {
            log.error("Fail to lock bike", e);
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage("内部错误");
        }

        return resp;
    }

    /**
     *@Author JackWang [www.coder520.com]
     *@Date 2017/8/25 23:49
     *@Description  单车上报坐标
     */
    @ApiOperation(value="骑行轨迹上报",notes = "骑行中上报单车位置 轨迹手机卡",httpMethod = "POST")
    @ApiImplicitParam(name = "bikeLocation",value = "单车编号",required = true,dataType = "BikeLocation")
    @RequestMapping("/reportLocation")
    public ApiResult reportLocation(@RequestBody BikeLocation bikeLocation){

        ApiResult<List<BikeLocation>> resp = new ApiResult<>();
        try {
            bikeService.reportLocation(bikeLocation);
            resp.setMessage("上报坐标成功");
        } catch (SuBikeException e) {
            resp.setCode(e.getStatusCode());
            resp.setMessage(e.getMessage());
        } catch (Exception e) {
            log.error("Fail to report location", e);
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage("内部错误");
        }

        return resp;
    }


}
