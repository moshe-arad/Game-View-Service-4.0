package org.moshe.arad.kafka.consumers.events;

import java.io.IOException;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.entities.GameRoom;
import org.moshe.arad.entities.backgammon.instrument.BackgammonDice;
import org.moshe.arad.entities.backgammon.instrument.json.BoardItemJson;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.events.DiceRolledEvent;
import org.moshe.arad.kafka.events.UserMadeInvalidMoveEvent;
import org.moshe.arad.kafka.events.WhitePawnCameBackAndAteBlackPawnEvent;
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
public class LastMoveWhitePawnCameBackAndAteBlackPawnEventConsumer extends SimpleEventsConsumer {
	
	@Autowired
	private GameView gameView;
	
	@Autowired
	private ApplicationContext context;
	
	private ConsumerToProducerQueue consumerToProducerQueue;
	
	Logger logger = LoggerFactory.getLogger(LastMoveWhitePawnCameBackAndAteBlackPawnEventConsumer.class);
	
	public LastMoveWhitePawnCameBackAndAteBlackPawnEventConsumer() {
	}
	
	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		WhitePawnCameBackAndAteBlackPawnEvent whitePawnCameBackAndAteBlackPawnEvent = convertJsonBlobIntoEvent(record.value());
		GameViewChanges gameViewChanges = context.getBean(GameViewChanges.class);
		BackgammonDice firstDice = whitePawnCameBackAndAteBlackPawnEvent.getFirstDice();
		BackgammonDice secondDice = whitePawnCameBackAndAteBlackPawnEvent.getSecondDice();
		List<BoardItemJson> boardItemJsons = whitePawnCameBackAndAteBlackPawnEvent.getBackgammonBoardJson().getBackgammonItems();
		
		try{
			gameViewChanges.setMessageToWhite("White you successfuly managed to return your white pawn back into the game. turn has passed to next player...");
			gameViewChanges.setMessageToBlack("White Player successfuly managed to return your white pawn back into the game. turn passed to you...");
			gameViewChanges.setMessageToWatcher("White has successfuly managed to return his white pawn back into the game. turn has passed to next player...");
			
			gameViewChanges.setIsToShowRollDiceBtnToWhite(false);
			gameViewChanges.setIsToShowRollDiceBtnToBlack(true);
			
			gameViewChanges.setIsWhiteTurn(false);
			gameViewChanges.setIsBlackTurn(true);
			
			gameViewChanges.setIsWhiteReturned(true);
			gameViewChanges.setIsWhiteAteBlack(true);
			gameViewChanges.setIsToApplyMove(true);
			gameViewChanges.setFrom(whitePawnCameBackAndAteBlackPawnEvent.getFrom());
			gameViewChanges.setTo(whitePawnCameBackAndAteBlackPawnEvent.getTo());
			gameViewChanges.setColumnSizeOnTo(boardItemJsons.get(whitePawnCameBackAndAteBlackPawnEvent.getTo()).getCount());
			gameView.markNeedToUpdateGroupUsers(gameViewChanges, whitePawnCameBackAndAteBlackPawnEvent.getGameRoomName());
		}
		catch(Exception e){
			logger.error("Failed to add new game room to view...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private WhitePawnCameBackAndAteBlackPawnEvent convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, WhitePawnCameBackAndAteBlackPawnEvent.class);
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
