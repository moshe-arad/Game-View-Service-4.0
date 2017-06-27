package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class WinnerMoveMadeEventConfig extends SimpleConsumerConfig{

	public WinnerMoveMadeEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.WINNER_MOVE_MADE_EVENT_GROUP);
	}
}
