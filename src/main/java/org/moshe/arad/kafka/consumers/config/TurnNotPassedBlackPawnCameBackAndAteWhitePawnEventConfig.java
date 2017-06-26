package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class TurnNotPassedBlackPawnCameBackAndAteWhitePawnEventConfig extends SimpleConsumerConfig{

	public TurnNotPassedBlackPawnCameBackAndAteWhitePawnEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.TURN_NOT_PASSED_BLACK_PAWN_CAME_BACK_AND_ATE_WHITE_PAWN_EVENT_GROUP);
	}
}
