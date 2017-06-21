package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class TurnNotPassedWhitePawnCameBackEventConfig extends SimpleConsumerConfig{

	public TurnNotPassedWhitePawnCameBackEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.TURN_NOT_PASSED_WHITE_PAWN_CAME_BACK_EVENT_GROUP);
	}
}
