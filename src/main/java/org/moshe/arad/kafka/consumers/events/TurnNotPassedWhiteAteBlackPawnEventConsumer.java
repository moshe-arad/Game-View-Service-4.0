package org.moshe.arad.kafka.consumers.events;

import java.io.IOException;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.entities.backgammon.instrument.BackgammonDice;
import org.moshe.arad.entities.backgammon.instrument.json.BoardItemJson;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.events.BlackAteWhitePawnEvent;
import org.moshe.arad.kafka.events.BlackPawnCameBackEvent;
import org.moshe.arad.kafka.events.TurnNotPassedWhiteAteBlackPawnEvent;
import org.moshe.arad.kafka.events.WhiteAteBlackPawnEvent;
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
public class TurnNotPassedWhiteAteBlackPawnEventConsumer extends SimpleEventsConsumer {
	
	@Autowired
	private GameView gameView;
	
	@Autowired
	private ApplicationContext context;
	
	private ConsumerToProducerQueue consumerToProducerQueue;
	
	Logger logger = LoggerFactory.getLogger(TurnNotPassedWhiteAteBlackPawnEventConsumer.class);
	
	public TurnNotPassedWhiteAteBlackPawnEventConsumer() {
	}
	
	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		TurnNotPassedWhiteAteBlackPawnEvent turnNotPassedWhiteAteBlackPawnEvent = convertJsonBlobIntoEvent(record.value());
		GameViewChanges gameViewChanges = context.getBean(GameViewChanges.class);
		List<BoardItemJson> boardItemJsons = turnNotPassedWhiteAteBlackPawnEvent.getBackgammonBoardJson().getBackgammonItems();
		
		try{
			gameViewChanges.setMessageToWhite("White Player you successfuly ate one of black's pawns. turn will keep yours because black does not have play options.");
			gameViewChanges.setMessageToBlack("Bad news Black player. White player ate one of your pawns. White will keep his turn because you do not have play options.");
			gameViewChanges.setMessageToWatcher("White Player has successfuly ate one of black's pawns. turn will keep to be white's because black does not have play options.");
			
			gameViewChanges.setIsToShowRollDiceBtnToWhite(true);
			gameViewChanges.setIsToShowRollDiceBtnToBlack(false);
			
			gameViewChanges.setIsWhiteTurn(true);
			gameViewChanges.setIsBlackTurn(false);
			
			gameViewChanges.setIsWhiteAteBlack(true);
			
			gameViewChanges.setIsToApplyMove(true);
			gameViewChanges.setFrom(turnNotPassedWhiteAteBlackPawnEvent.getFrom());
			gameViewChanges.setTo(turnNotPassedWhiteAteBlackPawnEvent.getTo());
			gameViewChanges.setColumnSizeOnFrom(boardItemJsons.get(turnNotPassedWhiteAteBlackPawnEvent.getFrom()).getCount() + 1);
			gameViewChanges.setColumnSizeOnTo(boardItemJsons.get(turnNotPassedWhiteAteBlackPawnEvent.getTo()).getCount());
			gameView.markNeedToUpdateGroupUsers(gameViewChanges, turnNotPassedWhiteAteBlackPawnEvent.getGameRoomName());
		}
		catch(Exception e){
			logger.error("Failed to add new game room to view...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private TurnNotPassedWhiteAteBlackPawnEvent convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, TurnNotPassedWhiteAteBlackPawnEvent.class);
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
