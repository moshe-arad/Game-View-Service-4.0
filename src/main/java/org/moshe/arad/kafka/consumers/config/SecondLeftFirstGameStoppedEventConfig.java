package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class SecondLeftFirstGameStoppedEventConfig extends SimpleConsumerConfig{

	public SecondLeftFirstGameStoppedEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.SECOND_LEFT_FIRST_GAME_STOPPED_EVENT_GROUP);
	}
}
