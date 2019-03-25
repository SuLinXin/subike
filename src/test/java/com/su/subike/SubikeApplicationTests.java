package com.su.subike;

import com.su.subike.bike.entity.Point;
import com.su.subike.bike.service.BikeGeoService;
import com.su.subike.common.exception.SuBikeException;
import com.su.subike.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = SubikeApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SubikeApplicationTests {


	@Autowired
	private TestRestTemplate testRestTemplate;
	@LocalServerPort
	private int port;

	@Autowired
	private UserService userService;

	@Autowired
	private BikeGeoService geoService;

//	@Test
//	public void contextLoads() {
//		String result = testRestTemplate.getForObject("/user/hello", String.class);
//		System.out.println(result);
//	}
	@Test
	public void geoTest() throws SuBikeException {
//		geoService.geoNearSphere("bike-position","location",new Point(116.312763,39.985416),0,100,null,null,10);
		geoService.geoNear("bike-position",null,new Point(116.312763, 39.985416),10,50);
	}
//	@Test
//	public void test() {
//		try {
//			userService.login(data, key);
//		} catch (Exception e) {
//			log.error("出错了666", e);
//		}
//	}

}

