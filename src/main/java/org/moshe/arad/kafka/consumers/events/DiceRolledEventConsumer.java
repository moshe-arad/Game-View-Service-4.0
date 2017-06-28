package org.moshe.arad.kafka.consumers.events;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.entities.GameRoom;
import org.moshe.arad.entities.backgammon.instrument.BackgammonDice;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
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
public class DiceRolledEventConsumer extends SimpleEventsConsumer {
	
	@Autowired
	private GameView gameView;
	
	@Autowired
	private ApplicationContext context;
	
	private ConsumerToProducerQueue consumerToProducerQueue;
	
	Logger logger = LoggerFactory.getLogger(DiceRolledEventConsumer.class);
	
	public DiceRolledEventConsumer() {
	}
	
	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		DiceRolledEvent diceRolledEvent = convertJsonBlobIntoEvent(record.value());
		GameViewChanges gameViewChanges = context.getBean(GameViewChanges.class);
		
		try{
			GameRoom room = diceRolledEvent.getGameRoom();
			BackgammonDice firstDice = diceRolledEvent.getFirstDice();
			BackgammonDice secondDice = diceRolledEvent.getSecondDice();
			
			if(room.getOpenBy().equals(diceRolledEvent.getUsername())){
				gameViewChanges.setMessageToWhite(room.getOpenBy() + " You rolled the dice with result of " + firstDice.getValue() + ":" + secondDice.getValue() + ", make your move...");
				gameViewChanges.setMessageToBlack(room.getOpenBy() + " Player has rolled the dice with result of " + firstDice.getValue() + ":" + secondDice.getValue() + ", wait for his move...");
				gameViewChanges.setMessageToWatcher(room.getOpenBy() + " Player has rolled the dice with result of " + firstDice.getValue() + ":" + secondDice.getValue() + ", wait for his move...");
				
				gameViewChanges.setIsToShowRollDiceBtnToWhite(false);
				gameViewChanges.setIsToShowRollDiceBtnToBlack(false);
				
				gameViewChanges.setIsWhiteTurn(true);
				gameViewChanges.setIsBlackTurn(false);
			}
			else if(room.getSecondPlayer().equals(diceRolledEvent.getUsername())){
				gameViewChanges.setMessageToWhite(room.getSecondPlayer() + " Player has rolled the dice with result of " + firstDice.getValue() + ":" + secondDice.getValue() + ", wait for his move...");
				gameViewChanges.setMessageToBlack(room.getSecondPlayer() + " You rolled the dice with result of " + firstDice.getValue() + ":" + secondDice.getValue() + ", make your move...");
				gameViewChanges.setMessageToWatcher(room.getSecondPlayer() + " Player has rolled the dice with result of " + firstDice.getValue() + ":" + secondDice.getValue() + ", wait for his move...");
						
				gameViewChanges.setIsToShowRollDiceBtnToWhite(false);
				gameViewChanges.setIsToShowRollDiceBtnToBlack(false);
				
				gameViewChanges.setIsWhiteTurn(false);
				gameViewChanges.setIsBlackTurn(true);
			}
			
			gameView.markNeedToUpdateGroupUsers(gameViewChanges, diceRolledEvent.getGameRoom().getName());
		}
		catch(Exception e){
			logger.error("Failed to add new game room to view...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private DiceRolledEvent convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, DiceRolledEvent.class);
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
