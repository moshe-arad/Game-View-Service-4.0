package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class WhitePawnTakenOutEventConfig extends SimpleConsumerConfig{

	public WhitePawnTakenOutEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.WHITE_PAWN_TAKEN_OUT_EVENT_GROUP);
	}
}
