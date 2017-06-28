package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class OpenByLeftFirstGameStoppedEventConfig extends SimpleConsumerConfig{

	public OpenByLeftFirstGameStoppedEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.OPENBY_LEFT_FIRST_GAME_STOPPED_EVENT_GROUP);
	}
}
