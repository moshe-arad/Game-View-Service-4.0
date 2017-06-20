package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class BlackAteWhitePawnEventConfig extends SimpleConsumerConfig{

	public BlackAteWhitePawnEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.BLACK_ATE_WHITE_PAWN_EVENT_GROUP);
	}
}
