package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class LastMoveBlackAteWhitePawnEventConfig extends SimpleConsumerConfig{

	public LastMoveBlackAteWhitePawnEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.LAST_MOVE_BLACK_ATE_WHITE_PAWN_EVENT_GROUP);
	}
}
