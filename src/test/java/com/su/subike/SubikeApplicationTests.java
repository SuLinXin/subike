package com.su.subike;

import com.su.subike.bike.entity.Point;
import com.su.subike.bike.service.BikeGeoService;
import com.su.subike.bike.service.BikeService;
import com.su.subike.bike.service.BikeServiceImpl;
import com.su.subike.common.exception.SuBikeException;
import com.su.subike.record.service.RideRecordService;
import com.su.subike.user.entity.UserElement;
import com.su.subike.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

//@MapperScan("com.su.subike.bike.dao")
@MapperScan("com.su.subike.record.dao")

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = SubikeApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SubikeApplicationTests {


	@Autowired
	private TestRestTemplate restTemplate;
	@LocalServerPort
	private int port;
	@Autowired
	private BikeGeoService bikeGeoService;

	@Autowired
	@Qualifier("bikeServiceImpl")
	private BikeService bikeService;
	@Autowired
	@Qualifier("rideRecordServiceImpl")
	private RideRecordService rideRecordService;
	@Autowired
	private MongoTemplate mongoTemplate;
//	@Test
//	public void contextLoads() {
//		String result = testRestTemplate.getForObject("/user/hello", String.class);
//		System.out.println(result);
//	}
//	@Test
//	public void geoTest() throws SuBikeException {
////		geoService.geoNearSphere("bike-position","location",new Point(116.312763,39.985416),0,100,null,null,10);
//		geoService.geoNear("bike-position",null,new Point(116.312763, 39.985416),10,50);
//	}

	@Test
	public void unlockTest() throws SuBikeException{
		UserElement ue = new UserElement();
		ue.setUserId(1l);
		ue.setPushChannelId("12345");
		ue.setPlatform("android");
		bikeService.unLockBike(ue,28000001l);
	}

}

