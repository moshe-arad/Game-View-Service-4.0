package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class UserMadeLastMoveEventConfig extends SimpleConsumerConfig{

	public UserMadeLastMoveEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.USER_MADE_MOVE_EVENT_GROUP);
	}
}
