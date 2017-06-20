package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class BlackPawnCameBackEventConfig extends SimpleConsumerConfig{

	public BlackPawnCameBackEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.BLACK_PAWN_CAME_BACK_EVENT_GROUP);
	}
}
