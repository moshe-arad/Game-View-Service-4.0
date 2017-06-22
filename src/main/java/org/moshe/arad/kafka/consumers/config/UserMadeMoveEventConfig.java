package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class UserMadeMoveEventConfig extends SimpleConsumerConfig{

	public UserMadeMoveEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.USER_MADE_LAST_MOVE_EVENT_GROUP);
	}
}
