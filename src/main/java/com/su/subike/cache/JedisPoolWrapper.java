package com.su.subike.cache;

import com.su.subike.common.constants.Parameters;
import com.su.subike.common.exception.SuBikeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class JedisPoolWrapper {
    private JedisPool jedisPool = null;

    @Autowired
    private Parameters parameters;


    @PostConstruct
    public void init() throws SuBikeException {

        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxWaitMillis(parameters.getRedisMaxWaitMillis());
            config.setMaxIdle(parameters.getRedisMaxIdle());
            config.setMaxTotal(parameters.getRedisMaxTotal());

            jedisPool = new JedisPool(config,parameters.getRedisHost(),parameters.getRedisPort(),2000);
        }catch (Exception e){
            log.error("Fail to initialize redis pool",e);
            throw new SuBikeException("初始化redis失败");
        }

    }


    public JedisPool getJedisPool(){
        return jedisPool;
    }
}
