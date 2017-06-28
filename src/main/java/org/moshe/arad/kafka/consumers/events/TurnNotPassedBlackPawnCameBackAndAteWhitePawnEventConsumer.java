package org.moshe.arad.kafka.consumers.events;

import java.io.IOException;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.entities.backgammon.instrument.BackgammonDice;
import org.moshe.arad.entities.backgammon.instrument.json.BoardItemJson;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.events.BlackPawnCameBackAndAteWhitePawnEvent;
import org.moshe.arad.kafka.events.LastMoveBlackPawnCameBackAndAteWhitePawnEvent;
import org.moshe.arad.kafka.events.TurnNotPassedBlackPawnCameBackAndAteWhitePawnEvent;
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
public class TurnNotPassedBlackPawnCameBackAndAteWhitePawnEventConsumer extends SimpleEventsConsumer {
	
	@Autowired
	private GameView gameView;
	
	@Autowired
	private ApplicationContext context;
	
	private ConsumerToProducerQueue consumerToProducerQueue;
	
	Logger logger = LoggerFactory.getLogger(TurnNotPassedBlackPawnCameBackAndAteWhitePawnEventConsumer.class);
	
	public TurnNotPassedBlackPawnCameBackAndAteWhitePawnEventConsumer() {
	}
	
	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		TurnNotPassedBlackPawnCameBackAndAteWhitePawnEvent turnNotPassedBlackPawnCameBackAndAteWhitePawnEvent = convertJsonBlobIntoEvent(record.value());
		GameViewChanges gameViewChanges = context.getBean(GameViewChanges.class);
		BackgammonDice firstDice = turnNotPassedBlackPawnCameBackAndAteWhitePawnEvent.getFirstDice();
		BackgammonDice secondDice = turnNotPassedBlackPawnCameBackAndAteWhitePawnEvent.getSecondDice();
		List<BoardItemJson> boardItemJsons = turnNotPassedBlackPawnCameBackAndAteWhitePawnEvent.getBackgammonBoardJson().getBackgammonItems();
		
		try{
			gameViewChanges.setMessageToWhite("Black you successfuly managed to return your black pawn back into the game. Black will keep turn play, because white don't have any moves to make...");
			gameViewChanges.setMessageToBlack("Black Player successfuly managed to return your black pawn back into the game. Black will keep turn play, because white don't have any moves to make...");
			gameViewChanges.setMessageToWatcher("Black Player has successfuly managed to return his black pawn back into the game. Black will keep turn play, because white don't have any moves to make...");
			
			gameViewChanges.setIsToShowRollDiceBtnToWhite(false);
			gameViewChanges.setIsToShowRollDiceBtnToBlack(true);
			
			gameViewChanges.setIsWhiteTurn(false);
			gameViewChanges.setIsBlackTurn(true);
			
			gameViewChanges.setIsBlackReturned(true);
			gameViewChanges.setIsBlackAteWhite(true);
			gameViewChanges.setIsToApplyMove(true);
			gameViewChanges.setFrom(turnNotPassedBlackPawnCameBackAndAteWhitePawnEvent.getFrom());
			gameViewChanges.setTo(turnNotPassedBlackPawnCameBackAndAteWhitePawnEvent.getTo());
			gameViewChanges.setColumnSizeOnTo(boardItemJsons.get(turnNotPassedBlackPawnCameBackAndAteWhitePawnEvent.getTo()).getCount());
			
			gameView.markNeedToUpdateGroupUsers(gameViewChanges, turnNotPassedBlackPawnCameBackAndAteWhitePawnEvent.getGameRoomName());
		}
		catch(Exception e){
			logger.error("Failed to add new game room to view...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private TurnNotPassedBlackPawnCameBackAndAteWhitePawnEvent convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, TurnNotPassedBlackPawnCameBackAndAteWhitePawnEvent.class);
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
