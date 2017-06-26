package org.moshe.arad.kafka.consumers.events;

import java.io.IOException;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.entities.backgammon.instrument.BackgammonDice;
import org.moshe.arad.entities.backgammon.instrument.json.BoardItemJson;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.events.BlackPawnCameBackAndAteWhitePawnEvent;
import org.moshe.arad.kafka.events.LastMoveBlackPawnCameBackAndAteWhitePawnEvent;
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
public class LastMoveBlackPawnCameBackAndAteWhitePawnEventConsumer extends SimpleEventsConsumer {
	
	@Autowired
	private GameView gameView;
	
	@Autowired
	private ApplicationContext context;
	
	private ConsumerToProducerQueue consumerToProducerQueue;
	
	Logger logger = LoggerFactory.getLogger(LastMoveBlackPawnCameBackAndAteWhitePawnEventConsumer.class);
	
	public LastMoveBlackPawnCameBackAndAteWhitePawnEventConsumer() {
	}
	
	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		LastMoveBlackPawnCameBackAndAteWhitePawnEvent lastMoveBlackPawnCameBackAndAteWhitePawnEvent = convertJsonBlobIntoEvent(record.value());
		GameViewChanges gameViewChanges = context.getBean(GameViewChanges.class);
		BackgammonDice firstDice = lastMoveBlackPawnCameBackAndAteWhitePawnEvent.getFirstDice();
		BackgammonDice secondDice = lastMoveBlackPawnCameBackAndAteWhitePawnEvent.getSecondDice();
		List<BoardItemJson> boardItemJsons = lastMoveBlackPawnCameBackAndAteWhitePawnEvent.getBackgammonBoardJson().getBackgammonItems();
		
		try{
			gameViewChanges.setMessageToWhite("Black you successfuly managed to return your black pawn back into the game. Earlier you rolled " + firstDice.getValue() + ":" + secondDice.getValue() + ", make your move...");
			gameViewChanges.setMessageToBlack("Black Player successfuly managed to return your black pawn back into the game. He need to finish play this dice result " + firstDice.getValue() + ":" + secondDice.getValue() + ", wait for his move...");
			
			gameViewChanges.setIsToShowRollDiceBtnToWhite(true);
			gameViewChanges.setIsToShowRollDiceBtnToBlack(false);
			
			gameViewChanges.setIsWhiteTurn(true);
			gameViewChanges.setIsBlackTurn(false);
			
			gameViewChanges.setIsBlackReturned(true);
			gameViewChanges.setIsBlackAteWhite(true);
			gameViewChanges.setIsToApplyMove(true);
			gameViewChanges.setFrom(lastMoveBlackPawnCameBackAndAteWhitePawnEvent.getFrom());
			gameViewChanges.setTo(lastMoveBlackPawnCameBackAndAteWhitePawnEvent.getTo());
			gameViewChanges.setColumnSizeOnTo(boardItemJsons.get(lastMoveBlackPawnCameBackAndAteWhitePawnEvent.getTo()).getCount());
			
			gameView.markNeedToUpdateGroupUsers(gameViewChanges, lastMoveBlackPawnCameBackAndAteWhitePawnEvent.getGameRoomName());
		}
		catch(Exception e){
			logger.error("Failed to add new game room to view...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private LastMoveBlackPawnCameBackAndAteWhitePawnEvent convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, LastMoveBlackPawnCameBackAndAteWhitePawnEvent.class);
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
