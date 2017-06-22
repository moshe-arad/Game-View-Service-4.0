package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class LastMoveBlackPawnTakenOutEventConfig extends SimpleConsumerConfig{

	public LastMoveBlackPawnTakenOutEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.LAST_MOVE_BLACK_PAWN_TAKEN_OUT_EVENT_GROUP);
	}
}
