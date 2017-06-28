package org.moshe.arad.kafka.consumers.events;

import java.io.IOException;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.entities.backgammon.instrument.json.BoardItemJson;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.events.BlackAteWhitePawnEvent;
import org.moshe.arad.kafka.events.LastMoveBlackAteWhitePawnEvent;
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
public class LastMoveBlackAteWhitePawnEventConsumer extends SimpleEventsConsumer {
	
	@Autowired
	private GameView gameView;
	
	@Autowired
	private ApplicationContext context;
	
	private ConsumerToProducerQueue consumerToProducerQueue;
	
	Logger logger = LoggerFactory.getLogger(LastMoveBlackAteWhitePawnEventConsumer.class);
	
	public LastMoveBlackAteWhitePawnEventConsumer() {
	}
	
	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		LastMoveBlackAteWhitePawnEvent lastMoveBlackAteWhitePawnEvent = convertJsonBlobIntoEvent(record.value());
		GameViewChanges gameViewChanges = context.getBean(GameViewChanges.class);
		List<BoardItemJson> boardItemJsons = lastMoveBlackAteWhitePawnEvent.getBackgammonBoardJson().getBackgammonItems();
		
		try{
			gameViewChanges.setMessageToWhite("Bad news White player. Black player ate one of your pawns.  turn will pass to you.");
			gameViewChanges.setMessageToBlack("Black Player you successfuly ate one of white's pawns. turn will pass to next player.");
			gameViewChanges.setMessageToWatcher("Black Player has successfuly ate one of white's pawns. turn will pass to next player.");
			
			gameViewChanges.setIsToShowRollDiceBtnToWhite(true);
			gameViewChanges.setIsToShowRollDiceBtnToBlack(false);
			
			gameViewChanges.setIsWhiteTurn(true);
			gameViewChanges.setIsBlackTurn(false);
			
			gameViewChanges.setIsBlackAteWhite(true);
			
			gameViewChanges.setIsToApplyMove(true);
			gameViewChanges.setFrom(lastMoveBlackAteWhitePawnEvent.getFrom());
			gameViewChanges.setTo(lastMoveBlackAteWhitePawnEvent.getTo());
			gameViewChanges.setColumnSizeOnFrom(boardItemJsons.get(lastMoveBlackAteWhitePawnEvent.getFrom()).getCount() + 1);
			gameViewChanges.setColumnSizeOnTo(boardItemJsons.get(lastMoveBlackAteWhitePawnEvent.getTo()).getCount());
			gameView.markNeedToUpdateGroupUsers(gameViewChanges, lastMoveBlackAteWhitePawnEvent.getGameRoomName());
		}
		catch(Exception e){
			logger.error("Failed to add new game room to view...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private LastMoveBlackAteWhitePawnEvent convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, LastMoveBlackAteWhitePawnEvent.class);
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
