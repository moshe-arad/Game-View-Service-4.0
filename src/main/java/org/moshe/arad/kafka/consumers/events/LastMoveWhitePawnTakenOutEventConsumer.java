package org.moshe.arad.kafka.consumers.events;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.entities.backgammon.instrument.BackgammonDice;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.events.LastMoveWhitePawnTakenOutEvent;
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
public class LastMoveWhitePawnTakenOutEventConsumer extends SimpleEventsConsumer {
	
	@Autowired
	private GameView gameView;
	
	@Autowired
	private ApplicationContext context;
	
	private ConsumerToProducerQueue consumerToProducerQueue;
	
	Logger logger = LoggerFactory.getLogger(LastMoveWhitePawnTakenOutEventConsumer.class);
	
	public LastMoveWhitePawnTakenOutEventConsumer() {
	}
	
	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		LastMoveWhitePawnTakenOutEvent lastMoveWhitePawnTakenOutEvent = convertJsonBlobIntoEvent(record.value());
		GameViewChanges gameViewChanges = context.getBean(GameViewChanges.class);
		
		try{
			gameViewChanges.setMessageToWhite("White you successfuly managed to take out your white pawn out of the game. turn will pass to next player.");
			gameViewChanges.setMessageToBlack("White Player successfuly managed to take out his white pawn out of the game. turn will pass to you.");
			
			gameViewChanges.setIsToShowRollDiceBtnToWhite(false);
			gameViewChanges.setIsToShowRollDiceBtnToBlack(true);
			
			gameViewChanges.setIsWhiteTurn(false);
			gameViewChanges.setIsBlackTurn(true);
			
			gameViewChanges.setIsWhiteTookOut(true);
			
			gameViewChanges.setIsToApplyMove(true);
			
			gameView.markNeedToUpdateGroupUsers(gameViewChanges, lastMoveWhitePawnTakenOutEvent.getGameRoomName());
		}
		catch(Exception e){
			logger.error("Failed to add new game room to view...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private LastMoveWhitePawnTakenOutEvent convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, LastMoveWhitePawnTakenOutEvent.class);
		} catch (IOException e) {
			logger.error("Falied to convert Json blob into Event...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void setConsumerToProducerQueue(ConsumerToProducerQueue consumerToProducerQueue) {
		this.consumerToProducerQueue = consumerToProducerQueue;
	}

}
