package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class TurnNotPassedWhiteAteBlackPawnEventConfig extends SimpleConsumerConfig{

	public TurnNotPassedWhiteAteBlackPawnEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.TURN_NOT_PASSED_WHITE_ATE_BLACK_PAWN_EVENT_GROUP);
	}
}
