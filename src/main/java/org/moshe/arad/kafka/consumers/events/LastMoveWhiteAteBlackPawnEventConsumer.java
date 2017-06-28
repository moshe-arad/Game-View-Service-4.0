package org.moshe.arad.kafka.consumers.events;

import java.io.IOException;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.entities.backgammon.instrument.json.BoardItemJson;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.events.LastMoveWhiteAteBlackPawnEvent;
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
public class LastMoveWhiteAteBlackPawnEventConsumer extends SimpleEventsConsumer {
	
	@Autowired
	private GameView gameView;
	
	@Autowired
	private ApplicationContext context;
	
	private ConsumerToProducerQueue consumerToProducerQueue;
	
	Logger logger = LoggerFactory.getLogger(LastMoveWhiteAteBlackPawnEventConsumer.class);
	
	public LastMoveWhiteAteBlackPawnEventConsumer() {
	}
	
	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		LastMoveWhiteAteBlackPawnEvent lastMoveWhiteAteBlackPawnEvent = convertJsonBlobIntoEvent(record.value());
		GameViewChanges gameViewChanges = context.getBean(GameViewChanges.class);
		List<BoardItemJson> boardItemJsons = lastMoveWhiteAteBlackPawnEvent.getBackgammonBoardJson().getBackgammonItems();
		
		try{
			gameViewChanges.setMessageToWhite("White Player you successfuly ate one of black's pawns. turn will pass to next player.");
			gameViewChanges.setMessageToBlack("Bad news Black player. White player ate one of your pawns. turn will pass to you.");
			gameViewChanges.setMessageToWatcher("White Player has successfuly ate one of black's pawns. turn will pass to next player.");
			
			gameViewChanges.setIsToShowRollDiceBtnToWhite(false);
			gameViewChanges.setIsToShowRollDiceBtnToBlack(true);
			
			gameViewChanges.setIsWhiteTurn(false);
			gameViewChanges.setIsBlackTurn(true);
			
			gameViewChanges.setIsWhiteAteBlack(true);
			
			gameViewChanges.setIsToApplyMove(true);
			gameViewChanges.setFrom(lastMoveWhiteAteBlackPawnEvent.getFrom());
			gameViewChanges.setTo(lastMoveWhiteAteBlackPawnEvent.getTo());
			gameViewChanges.setColumnSizeOnFrom(boardItemJsons.get(lastMoveWhiteAteBlackPawnEvent.getFrom()).getCount() + 1);
			gameViewChanges.setColumnSizeOnTo(boardItemJsons.get(lastMoveWhiteAteBlackPawnEvent.getTo()).getCount());
			gameView.markNeedToUpdateGroupUsers(gameViewChanges, lastMoveWhiteAteBlackPawnEvent.getGameRoomName());
		}
		catch(Exception e){
			logger.error("Failed to add new game room to view...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private LastMoveWhiteAteBlackPawnEvent convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, LastMoveWhiteAteBlackPawnEvent.class);
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
