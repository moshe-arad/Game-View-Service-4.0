package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class WhitePawnCameBackAndAteBlackPawnEventConfig extends SimpleConsumerConfig{

	public WhitePawnCameBackAndAteBlackPawnEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.WHITE_PAWN_CAME_BACK_AND_ATE_BLACK_PAWN_EVENT_GROUP);
	}
}
