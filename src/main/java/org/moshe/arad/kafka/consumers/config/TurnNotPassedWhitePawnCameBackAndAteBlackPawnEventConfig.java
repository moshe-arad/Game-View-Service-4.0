package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class TurnNotPassedWhitePawnCameBackAndAteBlackPawnEventConfig extends SimpleConsumerConfig{

	public TurnNotPassedWhitePawnCameBackAndAteBlackPawnEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.TURN_NOT_PASSED_WHITE_PAWN_CAME_BACK_AND_ATE_BLACK_PAWN_EVENT_GROUP);
	}
}
