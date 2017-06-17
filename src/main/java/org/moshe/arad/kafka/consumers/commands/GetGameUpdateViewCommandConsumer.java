package org.moshe.arad.kafka.consumers.commands;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.commands.GetGameUpdateViewCommand;
import org.moshe.arad.kafka.events.GetGameUpdateViewAckEvent;
import org.moshe.arad.view.utils.GameView;
import org.moshe.arad.view.utils.GameViewChanges;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Scope("prototype")
public class GetGameUpdateViewCommandConsumer extends SimpleCommandsConsumer {

	private ConsumerToProducerQueue consumerToProducerQueue;
	private Logger logger = LoggerFactory.getLogger(GetGameUpdateViewCommandConsumer.class);
	
	@Autowired
	private GameView gameView;
	
	@Autowired
	private ApplicationContext context;
	
	public GetGameUpdateViewCommandConsumer() {
	}

	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		GetGameUpdateViewAckEvent getGameUpdateViewAckEvent = context.getBean(GetGameUpdateViewAckEvent.class);
		GetGameUpdateViewCommand getGameUpdateViewCommand = convertJsonBlobIntoEvent(record.value());
		GameViewChanges gameViewChanges = null;
		
		logger.info("Get Lobby Update View Command record recieved, " + record.value());
		
		
		if(getGameUpdateViewCommand.isAllLevel()){
			gameViewChanges = gameView.getNeedToUpdateAllUsers();
		}
		else if(getGameUpdateViewCommand.isGroupLevel()){
			gameViewChanges = gameView.getNeedToUpdateGroupUsers(getGameUpdateViewCommand.getGroup());
		}
		else if(getGameUpdateViewCommand.isUserLevel()){
			gameViewChanges = gameView.getNeedToUpdateUser(getGameUpdateViewCommand.getUser());
		}
		else throw new RuntimeException("levels are false...");
		
		getGameUpdateViewAckEvent.setUuid(getGameUpdateViewCommand.getUuid());
		getGameUpdateViewAckEvent.setGameViewChange(gameViewChanges);
		
    	logger.info("passing get Lobby Update View Ack Event to producer...");
    	consumerToProducerQueue.getEventsQueue().put(getGameUpdateViewAckEvent);
    	logger.info("Event passed to producer...");		
	}

	public ConsumerToProducerQueue getConsumerToProducerQueue() {
		return consumerToProducerQueue;
	}

	public void setConsumerToProducerQueue(ConsumerToProducerQueue consumerToProducerQueue) {
		this.consumerToProducerQueue = consumerToProducerQueue;
	}
	
	private GetGameUpdateViewCommand convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, GetGameUpdateViewCommand.class);
		} catch (IOException e) {
			logger.error("Falied to convert Json blob into Event...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}




	