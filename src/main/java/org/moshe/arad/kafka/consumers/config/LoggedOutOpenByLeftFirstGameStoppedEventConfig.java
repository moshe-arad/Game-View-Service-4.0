package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class LoggedOutOpenByLeftFirstGameStoppedEventConfig extends SimpleConsumerConfig{

	public LoggedOutOpenByLeftFirstGameStoppedEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.LOGGED_OUT_OPENBY_LEFT_FIRST_GAME_STOPPED_EVENT_GROUP);
	}
}
