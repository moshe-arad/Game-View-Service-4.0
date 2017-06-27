package org.moshe.arad.kafka.consumers.events;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.entities.GameRoom;
import org.moshe.arad.entities.backgammon.instrument.BackgammonDice;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.events.DiceRolledCanNotPlayEvent;
import org.moshe.arad.kafka.events.DiceRolledEvent;
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
public class DiceRolledCanNotPlayEventConsumer extends SimpleEventsConsumer {
	
	@Autowired
	private GameView gameView;
	
	@Autowired
	private ApplicationContext context;
	
	private ConsumerToProducerQueue consumerToProducerQueue;
	
	Logger logger = LoggerFactory.getLogger(DiceRolledCanNotPlayEventConsumer.class);
	
	public DiceRolledCanNotPlayEventConsumer() {
	}
	
	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		DiceRolledCanNotPlayEvent diceRolledCanNotPlayEvent = convertJsonBlobIntoEvent(record.value());
		GameViewChanges gameViewChanges = context.getBean(GameViewChanges.class);
		
		try{
			GameRoom room = diceRolledCanNotPlayEvent.getGameRoom();
			BackgammonDice firstDice = diceRolledCanNotPlayEvent.getFirstDice();
			BackgammonDice secondDice = diceRolledCanNotPlayEvent.getSecondDice();
			
			if(room.getOpenBy().equals(diceRolledCanNotPlayEvent.getUsername())){
				gameViewChanges.setMessageToWhite(room.getOpenBy() + " You rolled the dice with result of " + firstDice.getValue() + ":" + secondDice.getValue() + ", unfortunately you don't have any moves to make, turn will pass to other player...");
				gameViewChanges.setMessageToBlack(room.getOpenBy() + " Player has rolled the dice with result of " + firstDice.getValue() + ":" + secondDice.getValue() + ", White player don't have any moves to make, turn will pass to you...");
				
				gameViewChanges.setIsToShowRollDiceBtnToWhite(false);
				gameViewChanges.setIsToShowRollDiceBtnToBlack(true);
				
				gameViewChanges.setIsWhiteTurn(false);
				gameViewChanges.setIsBlackTurn(true);
			}
			else if(room.getSecondPlayer().equals(diceRolledCanNotPlayEvent.getUsername())){
				gameViewChanges.setMessageToWhite(room.getSecondPlayer() + " Player has rolled the dice with result of " + firstDice.getValue() + ":" + secondDice.getValue() + ", Black player don't have any moves to make, turn will pass to you...");
				gameViewChanges.setMessageToBlack(room.getSecondPlayer() + " You rolled the dice with result of " + firstDice.getValue() + ":" + secondDice.getValue() + ", unfortunately you don't have any moves to make, turn will pass to other player...");
				
				gameViewChanges.setIsToShowRollDiceBtnToWhite(true);
				gameViewChanges.setIsToShowRollDiceBtnToBlack(false);
				
				gameViewChanges.setIsWhiteTurn(true);
				gameViewChanges.setIsBlackTurn(false);
			}
			
			gameView.markNeedToUpdateGroupUsers(gameViewChanges, diceRolledCanNotPlayEvent.getGameRoom().getName());
		}
		catch(Exception e){
			logger.error("Failed to add new game room to view...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private DiceRolledCanNotPlayEvent convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, DiceRolledCanNotPlayEvent.class);
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
