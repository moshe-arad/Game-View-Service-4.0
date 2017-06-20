package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class WhiteAteBlackPawnEventConfig extends SimpleConsumerConfig{

	public WhiteAteBlackPawnEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.WHITE_ATE_BLACK_PAWN_EVENT_GROUP);
	}
}
