package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class TurnNotPassedBlackAteWhitePawnEventConfig extends SimpleConsumerConfig{

	public TurnNotPassedBlackAteWhitePawnEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.TURN_NOT_PASSED_BLACK_ATE_WHITE_PAWN_EVENT_GROUP);
	}
}
