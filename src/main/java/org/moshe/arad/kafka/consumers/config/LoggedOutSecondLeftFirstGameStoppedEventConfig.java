package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class LoggedOutSecondLeftFirstGameStoppedEventConfig extends SimpleConsumerConfig{

	public LoggedOutSecondLeftFirstGameStoppedEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.LOGGED_OUT_SECOND_LEFT_FIRST_GAME_STOPPED_EVENT_GROUP);
	}
}
