package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class GetGameUpdateViewCommandConfig extends SimpleConsumerConfig{

	public GetGameUpdateViewCommandConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.GET_GAME_UPDATE_VIEW_GROUP);
	}
}
