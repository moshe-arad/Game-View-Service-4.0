package org.moshe.arad.kafka.consumers.events;

import java.io.IOException;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.entities.backgammon.instrument.BackgammonDice;
import org.moshe.arad.entities.backgammon.instrument.json.BoardItemJson;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.events.UserMadeInvalidMoveEvent;
import org.moshe.arad.kafka.events.UserMadeLastMoveEvent;
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
public class UserMadeLastMoveEventConsumer extends SimpleEventsConsumer {
	
	@Autowired
	private GameView gameView;
	
	@Autowired
	private ApplicationContext context;
	
	private ConsumerToProducerQueue consumerToProducerQueue;
	
	Logger logger = LoggerFactory.getLogger(UserMadeLastMoveEventConsumer.class);
	
	public UserMadeLastMoveEventConsumer() {
	}
	
	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		UserMadeLastMoveEvent userMadeLastMoveEvent = convertJsonBlobIntoEvent(record.value());
		GameViewChanges gameViewChanges = context.getBean(GameViewChanges.class);
		List<BoardItemJson> boardItemJsons = userMadeLastMoveEvent.getBackgammonBoardJson().getBackgammonItems();
		
		try{
			if(userMadeLastMoveEvent.isWhite()){
				gameViewChanges.setMessageToWhite("White You made a move, from column = " + userMadeLastMoveEvent.getFrom() + ", to column = "+ userMadeLastMoveEvent.getTo() +", turn will pass to next player.");
				gameViewChanges.setMessageToBlack("White Player made a move, from column = " + userMadeLastMoveEvent.getFrom() + ", to column = " + userMadeLastMoveEvent.getTo() +", turn will pass to you.");
				
				gameViewChanges.setIsToShowRollDiceBtnToWhite(false);
				gameViewChanges.setIsToShowRollDiceBtnToBlack(true);
				
				gameViewChanges.setIsWhiteTurn(false);
				gameViewChanges.setIsBlackTurn(true);
			}
			else if(!userMadeLastMoveEvent.isWhite()){
				gameViewChanges.setMessageToWhite("Black Player made a move, from column = " + userMadeLastMoveEvent.getFrom() + ", to column = " + userMadeLastMoveEvent.getTo() +". turn will pass to you.");
				gameViewChanges.setMessageToBlack("Black You made a move, from column = " + userMadeLastMoveEvent.getFrom() + ", to column = "+ userMadeLastMoveEvent.getTo() +". turn will pass to next player.");
				
				gameViewChanges.setIsToShowRollDiceBtnToWhite(true);
				gameViewChanges.setIsToShowRollDiceBtnToBlack(false);
				
				gameViewChanges.setIsWhiteTurn(true);
				gameViewChanges.setIsBlackTurn(false);
			}
			
			gameViewChanges.setFrom(userMadeLastMoveEvent.getFrom());
			gameViewChanges.setTo(userMadeLastMoveEvent.getTo());
			gameViewChanges.setIsToApplyMove(true);
			gameViewChanges.setColumnSizeOnFrom(boardItemJsons.get(userMadeLastMoveEvent.getFrom()).getCount() + 1);
			gameViewChanges.setColumnSizeOnTo(boardItemJsons.get(userMadeLastMoveEvent.getTo()).getCount() - 1);
			gameView.markNeedToUpdateGroupUsers(gameViewChanges, userMadeLastMoveEvent.getGameRoomName());
		}
		catch(Exception e){
			logger.error("Failed to add new game room to view...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private UserMadeLastMoveEvent convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, UserMadeLastMoveEvent.class);
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
