package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class LastMoveWhitePawnCameBackEventConfig extends SimpleConsumerConfig{

	public LastMoveWhitePawnCameBackEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.LAST_MOVE_WHITE_PAWN_CAME_BACK_EVENT_GROUP);
	}
}