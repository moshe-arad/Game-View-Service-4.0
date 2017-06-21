package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class LastMoveBlackPawnCameBackEventConfig extends SimpleConsumerConfig{

	public LastMoveBlackPawnCameBackEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.LAST_MOVE_BLACK_PAWN_CAME_BACK_EVENT_GROUP);
	}
}
