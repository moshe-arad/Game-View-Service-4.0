package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class TurnNotPassedUserMadeMoveEventConfig extends SimpleConsumerConfig{

	public TurnNotPassedUserMadeMoveEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.TURN_NOT_PASSED_USER_MADE_MOVE_EVENT_GROUP);
	}
}
