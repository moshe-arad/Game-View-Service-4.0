package org.moshe.arad.kafka.consumers.events;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.entities.GameRoom;
import org.moshe.arad.entities.backgammon.instrument.BackgammonDice;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.events.DiceRolledEvent;
import org.moshe.arad.kafka.events.UserMadeInvalidMoveEvent;
import org.moshe.arad.kafka.events.WhitePawnCameBackEvent;
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
public class WhitePawnCameBackEventConsumer extends SimpleEventsConsumer {
	
	@Autowired
	private GameView gameView;
	
	@Autowired
	private ApplicationContext context;
	
	private ConsumerToProducerQueue consumerToProducerQueue;
	
	Logger logger = LoggerFactory.getLogger(WhitePawnCameBackEventConsumer.class);
	
	public WhitePawnCameBackEventConsumer() {
	}
	
	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		WhitePawnCameBackEvent whitePawnCameBackEvent = convertJsonBlobIntoEvent(record.value());
		GameViewChanges gameViewChanges = context.getBean(GameViewChanges.class);
		BackgammonDice firstDice = whitePawnCameBackEvent.getFirstDice();
		BackgammonDice secondDice = whitePawnCameBackEvent.getSecondDice();
		
		try{
			gameViewChanges.setMessageToWhite("White you successfuly managed to return your white pawn back into the game. Earlier you rolled " + firstDice.getValue() + ":" + secondDice.getValue() + ", make your move...");
			gameViewChanges.setMessageToBlack("White Player successfuly managed to return your white pawn back into the game. He need to finish play this dice result " + firstDice.getValue() + ":" + secondDice.getValue() + ", wait for his move...");
			
			gameViewChanges.setIsToShowRollDiceBtnToWhite(false);
			gameViewChanges.setIsToShowRollDiceBtnToBlack(false);
			
			gameViewChanges.setIsWhiteTurn(true);
			gameViewChanges.setIsBlackTurn(false);
			
			gameViewChanges.setIsWhiteReturned(true);
			
			gameViewChanges.setIsToApplyMove(true);
			
			gameView.markNeedToUpdateGroupUsers(gameViewChanges, whitePawnCameBackEvent.getGameRoomName());
		}
		catch(Exception e){
			logger.error("Failed to add new game room to view...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private WhitePawnCameBackEvent convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, WhitePawnCameBackEvent.class);
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
