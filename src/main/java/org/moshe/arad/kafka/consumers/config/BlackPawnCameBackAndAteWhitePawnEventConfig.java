package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class BlackPawnCameBackAndAteWhitePawnEventConfig extends SimpleConsumerConfig{

	public BlackPawnCameBackAndAteWhitePawnEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.BLACK_PAWN_CAME_BACK_AND_ATE_WHITE_PAWN_EVENT_GROUP);
	}
}
