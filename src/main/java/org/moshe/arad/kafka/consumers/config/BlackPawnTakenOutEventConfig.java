package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class BlackPawnTakenOutEventConfig extends SimpleConsumerConfig{

	public BlackPawnTakenOutEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.BLACK_PAWN_TAKEN_OUT_EVENT_GROUP);
	}
}
