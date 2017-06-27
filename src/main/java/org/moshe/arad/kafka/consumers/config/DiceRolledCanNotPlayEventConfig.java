package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class DiceRolledCanNotPlayEventConfig extends SimpleConsumerConfig{

	public DiceRolledCanNotPlayEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.DICE_ROLLED_CAN_NOT_PLAY_EVENT_GROUP);
	}
}
