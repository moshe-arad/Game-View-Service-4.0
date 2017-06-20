package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class WhitePawnCameBackEventConfig extends SimpleConsumerConfig{

	public WhitePawnCameBackEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.WHITE_PAWN_CAME_BACK_EVENT_GROUP);
	}
}
