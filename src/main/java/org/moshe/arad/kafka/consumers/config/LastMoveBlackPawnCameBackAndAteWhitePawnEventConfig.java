package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class LastMoveBlackPawnCameBackAndAteWhitePawnEventConfig extends SimpleConsumerConfig{

	public LastMoveBlackPawnCameBackAndAteWhitePawnEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.LAST_MOVE_BLACK_PAWN_CAME_BACK_AND_ATE_WHITE_PAWN_EVENT_GROUP);
	}
}
