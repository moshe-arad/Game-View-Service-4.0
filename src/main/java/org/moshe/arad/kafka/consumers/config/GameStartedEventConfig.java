package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class GameStartedEventConfig extends SimpleConsumerConfig{

	public GameStartedEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.GAME_STARTED_EVENT_GROUP);
	}
}
