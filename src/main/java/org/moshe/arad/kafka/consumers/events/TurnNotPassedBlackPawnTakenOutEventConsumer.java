package org.moshe.arad.kafka.consumers.events;

import java.io.IOException;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.entities.GameRoom;
import org.moshe.arad.entities.backgammon.instrument.BackgammonDice;
import org.moshe.arad.entities.backgammon.instrument.json.BoardItemJson;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.events.BlackPawnTakenOutEvent;
import org.moshe.arad.kafka.events.DiceRolledEvent;
import org.moshe.arad.kafka.events.LastMoveBlackPawnTakenOutEvent;
import org.moshe.arad.kafka.events.UserMadeInvalidMoveEvent;
import org.moshe.arad.kafka.events.WhitePawnCameBackEvent;
import org.moshe.arad.kafka.events.WhitePawnTakenOutEvent;
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
public class TurnNotPassedBlackPawnTakenOutEventConsumer extends SimpleEventsConsumer {
	
	@Autowired
	private GameView gameView;
	
	@Autowired
	private ApplicationContext context;
	
	private ConsumerToProducerQueue consumerToProducerQueue;
	
	Logger logger = LoggerFactory.getLogger(TurnNotPassedBlackPawnTakenOutEventConsumer.class);
	
	public TurnNotPassedBlackPawnTakenOutEventConsumer() {
	}
	
	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		LastMoveBlackPawnTakenOutEvent lastMoveBlackPawnTakenOutEvent = convertJsonBlobIntoEvent(record.value());
		GameViewChanges gameViewChanges = context.getBean(GameViewChanges.class);
		List<BoardItemJson> boardItemJsons = lastMoveBlackPawnTakenOutEvent.getBackgammonBoardJson().getBackgammonItems();
		
		try{
			gameViewChanges.setMessageToWhite("Black Player successfuly managed to take out his black pawn out of the game. Black will keep his turn because you do not have play options.");
			gameViewChanges.setMessageToBlack("Black you successfuly managed to take out your black pawn out of the game. turn will keep yours because white does not have play options.");
			gameViewChanges.setMessageToWatcher("Black has successfuly managed to take out his black pawn out of the game. turn will keep to be black's because white does not have play options.");
			
			gameViewChanges.setIsToShowRollDiceBtnToWhite(false);
			gameViewChanges.setIsToShowRollDiceBtnToBlack(true);
			
			gameViewChanges.setIsWhiteTurn(false);
			gameViewChanges.setIsBlackTurn(true);
			
			gameViewChanges.setIsBlackTookOut(true);
			
			gameViewChanges.setIsToApplyMove(true);
			gameViewChanges.setFrom(lastMoveBlackPawnTakenOutEvent.getFrom());
			gameViewChanges.setTo(lastMoveBlackPawnTakenOutEvent.getTo());
			gameViewChanges.setColumnSizeOnFrom(boardItemJsons.get(lastMoveBlackPawnTakenOutEvent.getFrom()).getCount() + 1);
			gameView.markNeedToUpdateGroupUsers(gameViewChanges, lastMoveBlackPawnTakenOutEvent.getGameRoomName());
		}
		catch(Exception e){
			logger.error("Failed to add new game room to view...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private LastMoveBlackPawnTakenOutEvent convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, LastMoveBlackPawnTakenOutEvent.class);
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
