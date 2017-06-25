package org.moshe.arad.kafka.consumers.events;

import java.io.IOException;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.entities.backgammon.instrument.BackgammonDice;
import org.moshe.arad.entities.backgammon.instrument.json.BoardItemJson;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.events.TurnNotPassedUserMadeMoveEvent;
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
public class TurnNotPassedUserMadeMoveEventConsumer extends SimpleEventsConsumer {
	
	@Autowired
	private GameView gameView;
	
	@Autowired
	private ApplicationContext context;
	
	private ConsumerToProducerQueue consumerToProducerQueue;
	
	Logger logger = LoggerFactory.getLogger(TurnNotPassedUserMadeMoveEventConsumer.class);
	
	public TurnNotPassedUserMadeMoveEventConsumer() {
	}
	
	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		TurnNotPassedUserMadeMoveEvent turnNotPassedUserMadeMoveEvent = convertJsonBlobIntoEvent(record.value());
		GameViewChanges gameViewChanges = context.getBean(GameViewChanges.class);
		List<BoardItemJson> boardItemJsons = turnNotPassedUserMadeMoveEvent.getBackgammonBoardJson().getBackgammonItems();
		
		try{
			if(turnNotPassedUserMadeMoveEvent.isWhite()){
				gameViewChanges.setMessageToWhite("White You made a move, from column = " + turnNotPassedUserMadeMoveEvent.getFrom() + ", to column = "+ turnNotPassedUserMadeMoveEvent.getTo() +", turn will keep yours because black does not have play options.");
				gameViewChanges.setMessageToBlack("White Player made a move, from column = " + turnNotPassedUserMadeMoveEvent.getFrom() + ", to column = " + turnNotPassedUserMadeMoveEvent.getTo() +", White will keep his turn because you do not have play options.");
				
				gameViewChanges.setIsToShowRollDiceBtnToWhite(true);
				gameViewChanges.setIsToShowRollDiceBtnToBlack(false);
				
				gameViewChanges.setIsWhiteTurn(true);
				gameViewChanges.setIsBlackTurn(false);
			}
			else if(!turnNotPassedUserMadeMoveEvent.isWhite()){
				gameViewChanges.setMessageToWhite("Black Player made a move, from column = " + turnNotPassedUserMadeMoveEvent.getFrom() + ", to column = " + turnNotPassedUserMadeMoveEvent.getTo() +". Black will keep his turn because you do not have play options.");
				gameViewChanges.setMessageToBlack("Black You made a move, from column = " + turnNotPassedUserMadeMoveEvent.getFrom() + ", to column = "+ turnNotPassedUserMadeMoveEvent.getTo() +". turn will keep yours because white does not have play options.");
				
				gameViewChanges.setIsToShowRollDiceBtnToWhite(false);
				gameViewChanges.setIsToShowRollDiceBtnToBlack(true);
				
				gameViewChanges.setIsWhiteTurn(false);
				gameViewChanges.setIsBlackTurn(true);
			}
			
			gameViewChanges.setIsToApplyMove(true);
			gameViewChanges.setFrom(turnNotPassedUserMadeMoveEvent.getFrom());
			gameViewChanges.setTo(turnNotPassedUserMadeMoveEvent.getTo());
			gameViewChanges.setColumnSizeOnFrom(boardItemJsons.get(turnNotPassedUserMadeMoveEvent.getFrom()).getCount() + 1);
			gameViewChanges.setColumnSizeOnTo(boardItemJsons.get(turnNotPassedUserMadeMoveEvent.getTo()).getCount() - 1);
			gameView.markNeedToUpdateGroupUsers(gameViewChanges, turnNotPassedUserMadeMoveEvent.getGameRoomName());
		}
		catch(Exception e){
			logger.error("Failed to add new game room to view...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private TurnNotPassedUserMadeMoveEvent convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, TurnNotPassedUserMadeMoveEvent.class);
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
