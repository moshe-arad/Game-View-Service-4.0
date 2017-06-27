package org.moshe.arad.kafka.consumers.events;

import java.io.IOException;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.entities.backgammon.instrument.BackgammonDice;
import org.moshe.arad.entities.backgammon.instrument.json.BoardItemJson;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.events.UserMadeInvalidMoveEvent;
import org.moshe.arad.kafka.events.UserMadeMoveEvent;
import org.moshe.arad.kafka.events.WinnerMoveMadeEvent;
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
public class WinnerMoveMadeEventConsumer extends SimpleEventsConsumer {
	
	@Autowired
	private GameView gameView;
	
	@Autowired
	private ApplicationContext context;
	
	private ConsumerToProducerQueue consumerToProducerQueue;
	
	Logger logger = LoggerFactory.getLogger(WinnerMoveMadeEventConsumer.class);
	
	public WinnerMoveMadeEventConsumer() {
	}
	
	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		WinnerMoveMadeEvent winnerMoveMadeEvent = convertJsonBlobIntoEvent(record.value());
		GameViewChanges gameViewChanges = context.getBean(GameViewChanges.class);
		BackgammonDice firstDice = winnerMoveMadeEvent.getFirstDice();
		BackgammonDice secondDice = winnerMoveMadeEvent.getSecondDice();
		List<BoardItemJson> boardItemJsons = winnerMoveMadeEvent.getBackgammonBoardJson().getBackgammonItems();
		
		try{
			if(winnerMoveMadeEvent.isWhite()){
				gameViewChanges.setMessageToWhite("White You made a winner move, You are the Winner!!!");
				gameViewChanges.setMessageToBlack("White Player made a winner move, he's the Winner, You Lost!!!");
				
				gameViewChanges.setIsToShowRollDiceBtnToWhite(false);
				gameViewChanges.setIsToShowRollDiceBtnToBlack(false);
				
				gameViewChanges.setIsWhiteTurn(false);
				gameViewChanges.setIsBlackTurn(false);
			}
			else if(!winnerMoveMadeEvent.isWhite()){
				gameViewChanges.setMessageToWhite("Black Player made a winner move, he's the Winner, You Lost!!!");
				gameViewChanges.setMessageToBlack("Black You made a winner move, You are the Winner!!!");
				
				gameViewChanges.setIsToShowRollDiceBtnToWhite(false);
				gameViewChanges.setIsToShowRollDiceBtnToBlack(false);
				
				gameViewChanges.setIsWhiteTurn(false);
				gameViewChanges.setIsBlackTurn(false);
			}
			
			gameViewChanges.setIsToApplyMove(true);
			gameViewChanges.setFrom(winnerMoveMadeEvent.getFrom());
			gameViewChanges.setTo(winnerMoveMadeEvent.getTo());
			gameViewChanges.setColumnSizeOnFrom(boardItemJsons.get(winnerMoveMadeEvent.getFrom()).getCount() + 1);

			gameView.markNeedToUpdateGroupUsers(gameViewChanges, winnerMoveMadeEvent.getGameRoomName());
		}
		catch(Exception e){
			logger.error("Failed to add new game room to view...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private WinnerMoveMadeEvent convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, WinnerMoveMadeEvent.class);
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
