package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class TurnNotPassedWhitePawnTakenOutEventConfig extends SimpleConsumerConfig{

	public TurnNotPassedWhitePawnTakenOutEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.LAST_MOVE_WHITE_PAWN_TAKEN_OUT_EVENT_GROUP);
	}
}
