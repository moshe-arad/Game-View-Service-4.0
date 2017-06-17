package org.moshe.arad.view.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class GameViewSimple {

	private RedisTemplate<String, String> redisTemplate;
	
	private Logger logger = LoggerFactory.getLogger(GameViewSimple.class);
	
	@Autowired
    public GameViewSimple(final RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
