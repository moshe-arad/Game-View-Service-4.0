package org.moshe.arad.kafka.consumers.events;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.entities.backgammon.instrument.BackgammonDice;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.events.UserMadeInvalidMoveEvent;
import org.moshe.arad.kafka.events.UserMadeMoveEvent;
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
public class UserMadeMoveEventConsumer extends SimpleEventsConsumer {
	
	@Autowired
	private GameView gameView;
	
	@Autowired
	private ApplicationContext context;
	
	private ConsumerToProducerQueue consumerToProducerQueue;
	
	Logger logger = LoggerFactory.getLogger(UserMadeMoveEventConsumer.class);
	
	public UserMadeMoveEventConsumer() {
	}
	
	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		UserMadeMoveEvent userMadeMoveEvent = convertJsonBlobIntoEvent(record.value());
		GameViewChanges gameViewChanges = context.getBean(GameViewChanges.class);
		BackgammonDice firstDice = userMadeMoveEvent.getFirstDice();
		BackgammonDice secondDice = userMadeMoveEvent.getSecondDice();
		
		try{
			if(userMadeMoveEvent.isWhite()){
				gameViewChanges.setMessageToWhite("White You made a move, from column = " + userMadeMoveEvent.getFrom() + ", to column = "+ userMadeMoveEvent.getTo() +". Earlier you rolled " + firstDice.getValue() + ":" + secondDice.getValue() + ", make your move...");
				gameViewChanges.setMessageToBlack("White Player made a move, from column = " + userMadeMoveEvent.getFrom() + ", to column = " + userMadeMoveEvent.getTo() +". He still need to play this dice result " + firstDice.getValue() + ":" + secondDice.getValue() + ", wait for his move...");
				
				gameViewChanges.setIsToShowRollDiceBtnToWhite(false);
				gameViewChanges.setIsToShowRollDiceBtnToBlack(false);
				
				gameViewChanges.setIsWhiteTurn(true);
				gameViewChanges.setIsBlackTurn(false);
			}
			else if(!userMadeMoveEvent.isWhite()){
				gameViewChanges.setMessageToWhite("Black Player made a move, from column = " + userMadeMoveEvent.getFrom() + ", to column = " + userMadeMoveEvent.getTo() +". He still need to play this dice result " + firstDice.getValue() + ":" + secondDice.getValue() + ", wait for his move...");
				gameViewChanges.setMessageToBlack("Black You made a move, from column = " + userMadeMoveEvent.getFrom() + ", to column = "+ userMadeMoveEvent.getTo() +". Earlier you rolled " + firstDice.getValue() + ":" + secondDice.getValue() + ", make your move...");
				
				gameViewChanges.setIsToShowRollDiceBtnToWhite(false);
				gameViewChanges.setIsToShowRollDiceBtnToBlack(false);
				
				gameViewChanges.setIsWhiteTurn(false);
				gameViewChanges.setIsBlackTurn(true);
			}
			
			gameViewChanges.setIsToApplyMove(true);
			
			gameView.markNeedToUpdateGroupUsers(gameViewChanges, userMadeMoveEvent.getGameRoomName());
		}
		catch(Exception e){
			logger.error("Failed to add new game room to view...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private UserMadeMoveEvent convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, UserMadeMoveEvent.class);
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
