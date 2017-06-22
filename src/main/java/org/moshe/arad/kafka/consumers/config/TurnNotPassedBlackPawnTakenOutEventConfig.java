package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class TurnNotPassedBlackPawnTakenOutEventConfig extends SimpleConsumerConfig{

	public TurnNotPassedBlackPawnTakenOutEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.TURN_NOT_PASSED_BLACK_PAWN_TAKEN_OUT_EVENT_GROUP);
	}
}
