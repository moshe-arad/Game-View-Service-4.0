package org.moshe.arad.kafka.consumers.events;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.entities.GameRoom;
import org.moshe.arad.entities.backgammon.instrument.BackgammonDice;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.events.DiceRolledEvent;
import org.moshe.arad.kafka.events.UserMadeInvalidMoveEvent;
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
public class UserMadeInvalidMoveEventConsumer extends SimpleEventsConsumer {
	
	@Autowired
	private GameView gameView;
	
	@Autowired
	private ApplicationContext context;
	
	private ConsumerToProducerQueue consumerToProducerQueue;
	
	Logger logger = LoggerFactory.getLogger(UserMadeInvalidMoveEventConsumer.class);
	
	public UserMadeInvalidMoveEventConsumer() {
	}
	
	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		UserMadeInvalidMoveEvent userMadeInvalidMoveEvent = convertJsonBlobIntoEvent(record.value());
		GameViewChanges gameViewChanges = context.getBean(GameViewChanges.class);
		BackgammonDice firstDice = userMadeInvalidMoveEvent.getFirstDice();
		BackgammonDice secondDice = userMadeInvalidMoveEvent.getSecondDice();
		
		try{
			if(userMadeInvalidMoveEvent.isWhite()){
				gameViewChanges.setMessageToWhite("White You made an invalid move, Try again. Earlier you rolled " + firstDice.getValue() + ":" + secondDice.getValue() + ", make your move...");
				gameViewChanges.setMessageToBlack("White Player tried to made an invalid move. He still need to play this dice result " + firstDice.getValue() + ":" + secondDice.getValue() + ", wait for his move...");
				
				gameViewChanges.setIsToShowRollDiceBtnToWhite(false);
				gameViewChanges.setIsToShowRollDiceBtnToBlack(false);
				
				gameViewChanges.setIsWhiteTurn(true);
				gameViewChanges.setIsBlackTurn(false);
			}
			else if(!userMadeInvalidMoveEvent.isWhite()){
				gameViewChanges.setMessageToWhite("Black Player tried to made an invalid move. He still need to play this dice result " + firstDice.getValue() + ":" + secondDice.getValue() + ", wait for his move...");
				gameViewChanges.setMessageToBlack("Black You made an invalid move, Try again. Earlier you rolled " + firstDice.getValue() + ":" + secondDice.getValue() + ", make your move...");
				
				gameViewChanges.setIsToShowRollDiceBtnToWhite(false);
				gameViewChanges.setIsToShowRollDiceBtnToBlack(false);
				
				gameViewChanges.setIsWhiteTurn(false);
				gameViewChanges.setIsBlackTurn(true);
			}
			
			gameViewChanges.setMessageToWatcher("An invalid move was made, wait for retry...");
			
			gameViewChanges.setFrom(userMadeInvalidMoveEvent.getFrom());
			gameViewChanges.setTo(userMadeInvalidMoveEvent.getTo());
			gameViewChanges.setIsToApplyMove(false);
			
			gameView.markNeedToUpdateGroupUsers(gameViewChanges, userMadeInvalidMoveEvent.getGameRoomName());
		}
		catch(Exception e){
			logger.error("Failed to add new game room to view...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private UserMadeInvalidMoveEvent convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, UserMadeInvalidMoveEvent.class);
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
