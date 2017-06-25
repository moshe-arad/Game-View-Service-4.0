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
public class BlackPawnTakenOutEventConsumer extends SimpleEventsConsumer {
	
	@Autowired
	private GameView gameView;
	
	@Autowired
	private ApplicationContext context;
	
	private ConsumerToProducerQueue consumerToProducerQueue;
	
	Logger logger = LoggerFactory.getLogger(BlackPawnTakenOutEventConsumer.class);
	
	public BlackPawnTakenOutEventConsumer() {
	}
	
	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		BlackPawnTakenOutEvent blackPawnTakenOutEvent = convertJsonBlobIntoEvent(record.value());
		GameViewChanges gameViewChanges = context.getBean(GameViewChanges.class);
		BackgammonDice firstDice = blackPawnTakenOutEvent.getFirstDice();
		BackgammonDice secondDice = blackPawnTakenOutEvent.getSecondDice();
		List<BoardItemJson> boardItemJsons = blackPawnTakenOutEvent.getBackgammonBoardJson().getBackgammonItems();
		
		try{
			gameViewChanges.setMessageToWhite("Black Player successfuly managed to take out his black pawn out of the game. He need to finish play this dice result " + firstDice.getValue() + ":" + secondDice.getValue() + ", wait for his move...");
			gameViewChanges.setMessageToBlack("Black you successfuly managed to take out your black pawn out of the game. Earlier you rolled " + firstDice.getValue() + ":" + secondDice.getValue() + ", make your move...");
			
			gameViewChanges.setIsToShowRollDiceBtnToWhite(false);
			gameViewChanges.setIsToShowRollDiceBtnToBlack(false);
			
			gameViewChanges.setIsWhiteTurn(false);
			gameViewChanges.setIsBlackTurn(true);
			
			gameViewChanges.setIsBlackTookOut(true);
			
			gameViewChanges.setIsToApplyMove(true);
			gameViewChanges.setFrom(blackPawnTakenOutEvent.getFrom());
			gameViewChanges.setTo(blackPawnTakenOutEvent.getTo());
			gameViewChanges.setColumnSizeOnFrom(boardItemJsons.get(blackPawnTakenOutEvent.getFrom()).getCount() + 1);
			
			gameView.markNeedToUpdateGroupUsers(gameViewChanges, blackPawnTakenOutEvent.getGameRoomName());
		}
		catch(Exception e){
			logger.error("Failed to add new game room to view...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private BlackPawnTakenOutEvent convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, BlackPawnTakenOutEvent.class);
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
