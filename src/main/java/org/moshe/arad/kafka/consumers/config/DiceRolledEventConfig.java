package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class DiceRolledEventConfig extends SimpleConsumerConfig{

	public DiceRolledEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.DICE_ROLLED_EVENT_GROUP);
	}
}
