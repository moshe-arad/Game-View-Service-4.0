package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class UserMadeInvalidMoveEventConfig extends SimpleConsumerConfig{

	public UserMadeInvalidMoveEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.USER_MADE_INVALID_MOVE_EVENT_GROUP);
	}
}
