package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class LastMoveWhiteAteBlackPawnEventConfig extends SimpleConsumerConfig{

	public LastMoveWhiteAteBlackPawnEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.LAST_MOVE_WHITE_ATE_BLACK_PAWN_EVENT_GROUP);
	}
}
