package org.moshe.arad.initializer;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.consumers.ISimpleConsumer;
import org.moshe.arad.kafka.consumers.commands.GetGameUpdateViewCommandConsumer;
import org.moshe.arad.kafka.consumers.config.BlackAteWhitePawnEventConfig;
import org.moshe.arad.kafka.consumers.config.BlackPawnCameBackAndAteWhitePawnEventConfig;
import org.moshe.arad.kafka.consumers.config.BlackPawnCameBackEventConfig;
import org.moshe.arad.kafka.consumers.config.BlackPawnTakenOutEventConfig;
import org.moshe.arad.kafka.consumers.config.DiceRolledCanNotPlayEventConfig;
import org.moshe.arad.kafka.consumers.config.DiceRolledEventConfig;
import org.moshe.arad.kafka.consumers.config.GameStartedEventConfig;
import org.moshe.arad.kafka.consumers.config.GetGameUpdateViewCommandConfig;
import org.moshe.arad.kafka.consumers.config.LastMoveBlackAteWhitePawnEventConfig;
import org.moshe.arad.kafka.consumers.config.LastMoveBlackPawnCameBackAndAteWhitePawnEventConfig;
import org.moshe.arad.kafka.consumers.config.LastMoveBlackPawnCameBackEventConfig;
import org.moshe.arad.kafka.consumers.config.LastMoveBlackPawnTakenOutEventConfig;
import org.moshe.arad.kafka.consumers.config.LastMoveWhiteAteBlackPawnEventConfig;
import org.moshe.arad.kafka.consumers.config.LastMoveWhitePawnCameBackAndAteBlackPawnEventConfig;
import org.moshe.arad.kafka.consumers.config.LastMoveWhitePawnCameBackEventConfig;
import org.moshe.arad.kafka.consumers.config.LastMoveWhitePawnTakenOutEventConfig;
import org.moshe.arad.kafka.consumers.config.LoggedOutOpenByLeftFirstGameStoppedEventConfig;
import org.moshe.arad.kafka.consumers.config.LoggedOutSecondLeftFirstGameStoppedEventConfig;
import org.moshe.arad.kafka.consumers.config.OpenByLeftFirstGameStoppedEventConfig;
import org.moshe.arad.kafka.consumers.config.SecondLeftFirstGameStoppedEventConfig;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.moshe.arad.kafka.consumers.config.TurnNotPassedBlackAteWhitePawnEventConfig;
import org.moshe.arad.kafka.consumers.config.TurnNotPassedBlackPawnCameBackAndAteWhitePawnEventConfig;
import org.moshe.arad.kafka.consumers.config.TurnNotPassedBlackPawnCameBackEventConfig;
import org.moshe.arad.kafka.consumers.config.TurnNotPassedBlackPawnTakenOutEventConfig;
import org.moshe.arad.kafka.consumers.config.TurnNotPassedUserMadeMoveEventConfig;
import org.moshe.arad.kafka.consumers.config.TurnNotPassedWhiteAteBlackPawnEventConfig;
import org.moshe.arad.kafka.consumers.config.TurnNotPassedWhitePawnCameBackAndAteBlackPawnEventConfig;
import org.moshe.arad.kafka.consumers.config.TurnNotPassedWhitePawnCameBackEventConfig;
import org.moshe.arad.kafka.consumers.config.TurnNotPassedWhitePawnTakenOutEventConfig;
import org.moshe.arad.kafka.consumers.config.UserMadeInvalidMoveEventConfig;
import org.moshe.arad.kafka.consumers.config.UserMadeLastMoveEventConfig;
import org.moshe.arad.kafka.consumers.config.UserMadeMoveEventConfig;
import org.moshe.arad.kafka.consumers.config.WhiteAteBlackPawnEventConfig;
import org.moshe.arad.kafka.consumers.config.WhitePawnCameBackAndAteBlackPawnEventConfig;
import org.moshe.arad.kafka.consumers.config.WhitePawnCameBackEventConfig;
import org.moshe.arad.kafka.consumers.config.WhitePawnTakenOutEventConfig;
import org.moshe.arad.kafka.consumers.config.WinnerMoveMadeEventConfig;
import org.moshe.arad.kafka.consumers.events.BlackAteWhitePawnEventConsumer;
import org.moshe.arad.kafka.consumers.events.BlackPawnCameBackAndAteWhitePawnEventConsumer;
import org.moshe.arad.kafka.consumers.events.BlackPawnCameBackEventConsumer;
import org.moshe.arad.kafka.consumers.events.BlackPawnTakenOutEventConsumer;
import org.moshe.arad.kafka.consumers.events.DiceRolledCanNotPlayEventConsumer;
import org.moshe.arad.kafka.consumers.events.DiceRolledEventConsumer;
import org.moshe.arad.kafka.consumers.events.GameStartedEventConsumer;
import org.moshe.arad.kafka.consumers.events.LastMoveBlackAteWhitePawnEventConsumer;
import org.moshe.arad.kafka.consumers.events.LastMoveBlackPawnCameBackAndAteWhitePawnEventConsumer;
import org.moshe.arad.kafka.consumers.events.LastMoveBlackPawnCameBackEventConsumer;
import org.moshe.arad.kafka.consumers.events.LastMoveBlackPawnTakenOutEventConsumer;
import org.moshe.arad.kafka.consumers.events.LastMoveWhiteAteBlackPawnEventConsumer;
import org.moshe.arad.kafka.consumers.events.LastMoveWhitePawnCameBackAndAteBlackPawnEventConsumer;
import org.moshe.arad.kafka.consumers.events.LastMoveWhitePawnCameBackEventConsumer;
import org.moshe.arad.kafka.consumers.events.LastMoveWhitePawnTakenOutEventConsumer;
import org.moshe.arad.kafka.consumers.events.LoggedOutOpenByLeftFirstGameStoppedEventConsumer;
import org.moshe.arad.kafka.consumers.events.LoggedOutSecondLeftFirstGameStoppedEventConsumer;
import org.moshe.arad.kafka.consumers.events.OpenByLeftFirstGameStoppedEventConsumer;
import org.moshe.arad.kafka.consumers.events.SecondLeftFirstGameStoppedEventConsumer;
import org.moshe.arad.kafka.consumers.events.TurnNotPassedBlackAteWhitePawnEventConsumer;
import org.moshe.arad.kafka.consumers.events.TurnNotPassedBlackPawnCameBackAndAteWhitePawnEventConsumer;
import org.moshe.arad.kafka.consumers.events.TurnNotPassedBlackPawnCameBackEventConsumer;
import org.moshe.arad.kafka.consumers.events.TurnNotPassedBlackPawnTakenOutEventConsumer;
import org.moshe.arad.kafka.consumers.events.TurnNotPassedUserMadeMoveEventConsumer;
import org.moshe.arad.kafka.consumers.events.TurnNotPassedWhiteAteBlackPawnEventConsumer;
import org.moshe.arad.kafka.consumers.events.TurnNotPassedWhitePawnCameBackAndAteBlackPawnEventConsumer;
import org.moshe.arad.kafka.consumers.events.TurnNotPassedWhitePawnCameBackEventConsumer;
import org.moshe.arad.kafka.consumers.events.TurnNotPassedWhitePawnTakenOutEventConsumer;
import org.moshe.arad.kafka.consumers.events.UserMadeInvalidMoveEventConsumer;
import org.moshe.arad.kafka.consumers.events.UserMadeLastMoveEventConsumer;
import org.moshe.arad.kafka.consumers.events.UserMadeMoveEventConsumer;
import org.moshe.arad.kafka.consumers.events.WhiteAteBlackPawnEventConsumer;
import org.moshe.arad.kafka.consumers.events.WhitePawnCameBackAndAteBlackPawnEventConsumer;
import org.moshe.arad.kafka.consumers.events.WhitePawnCameBackEventConsumer;
import org.moshe.arad.kafka.consumers.events.WhitePawnTakenOutEventConsumer;
import org.moshe.arad.kafka.consumers.events.WinnerMoveMadeEventConsumer;
import org.moshe.arad.kafka.events.GetGameUpdateViewAckEvent;
import org.moshe.arad.kafka.events.InitGameRoomCompletedEvent;
import org.moshe.arad.kafka.producers.ISimpleProducer;
import org.moshe.arad.kafka.producers.events.SimpleEventsProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class AppInit implements ApplicationContextAware, IAppInitializer {	
	
	private GameStartedEventConsumer gameStartedEventConsumer;
	
	@Autowired
	private GameStartedEventConfig gameStartedEventConfig;
	
	private GetGameUpdateViewCommandConsumer getGameUpdateViewCommandConsumer;
	
	@Autowired
	private GetGameUpdateViewCommandConfig getGameUpdateViewCommandConfig;
	
	@Autowired
	private SimpleEventsProducer<GetGameUpdateViewAckEvent> getGameUpdateViewAckEventProducer;
	
	private DiceRolledEventConsumer diceRolledEventConsumer;
	
	@Autowired
	private DiceRolledEventConfig diceRolledEventConfig;
	
	private UserMadeInvalidMoveEventConsumer userMadeInvalidMoveEventConsumer;
	
	@Autowired
	private UserMadeInvalidMoveEventConfig userMadeInvalidMoveEventConfig;
	
	private WhitePawnCameBackEventConsumer whitePawnCameBackEventConsumer;
	
	@Autowired
	private WhitePawnCameBackEventConfig whitePawnCameBackEventConfig;
	
	private BlackPawnCameBackEventConsumer blackPawnCameBackEventConsumer;
	
	@Autowired
	private BlackPawnCameBackEventConfig blackPawnCameBackEventConfig;
	
	private WhitePawnTakenOutEventConsumer whitePawnTakenOutEventConsumer;
	
	@Autowired
	private WhitePawnTakenOutEventConfig whitePawnTakenOutEventConfig;
	
	private BlackPawnTakenOutEventConsumer blackPawnTakenOutEventConsumer;
	
	@Autowired
	private BlackPawnTakenOutEventConfig blackPawnTakenOutEventConfig;
	
	private BlackAteWhitePawnEventConsumer blackAteWhitePawnEventConsumer;
	
	@Autowired
	private BlackAteWhitePawnEventConfig blackAteWhitePawnEventConfig;
	
	private WhiteAteBlackPawnEventConsumer whiteAteBlackPawnEventConsumer;
	
	@Autowired
	private WhiteAteBlackPawnEventConfig whiteAteBlackPawnEventConfig;
	
	private UserMadeMoveEventConsumer userMadeMoveEventConsumer;
	
	@Autowired
	private UserMadeMoveEventConfig userMadeMoveEventConfig;
	
	private LastMoveWhitePawnCameBackEventConsumer lastMoveWhitePawnCameBackEventConsumer;
	
	@Autowired
	private LastMoveWhitePawnCameBackEventConfig lastMoveWhitePawnCameBackEventConfig;
	
	private TurnNotPassedWhitePawnCameBackEventConsumer turnNotPassedWhitePawnCameBackEventConsumer;
	
	@Autowired
	private TurnNotPassedWhitePawnCameBackEventConfig turnNotPassedWhitePawnCameBackEventConfig;
	
	private LastMoveBlackPawnCameBackEventConsumer lastMoveBlackPawnCameBackEventConsumer;
	
	@Autowired
	private LastMoveBlackPawnCameBackEventConfig lastMoveBlackPawnCameBackEventConfig;
	
	private TurnNotPassedBlackPawnCameBackEventConsumer turnNotPassedBlackPawnCameBackEventConsumer;
	
	@Autowired
	private TurnNotPassedBlackPawnCameBackEventConfig turnNotPassedBlackPawnCameBackEventConfig;
	
	private LastMoveWhitePawnTakenOutEventConsumer lastMoveWhitePawnTakenOutEventConsumer;
	
	@Autowired
	private LastMoveWhitePawnTakenOutEventConfig lastMoveWhitePawnTakenOutEventConfig;
	
	private TurnNotPassedWhitePawnTakenOutEventConsumer turnNotPassedWhitePawnTakenOutEventConsumer;
	
	@Autowired
	private TurnNotPassedWhitePawnTakenOutEventConfig turnNotPassedWhitePawnTakenOutEventConfig;
	
	private LastMoveBlackPawnTakenOutEventConsumer lastMoveBlackPawnTakenOutEventConsumer;
	
	@Autowired
	private LastMoveBlackPawnTakenOutEventConfig lastMoveBlackPawnTakenOutEventConfig;
	
	private TurnNotPassedBlackPawnTakenOutEventConsumer turnNotPassedBlackPawnTakenOutEventConsumer;
	
	@Autowired
	private TurnNotPassedBlackPawnTakenOutEventConfig turnNotPassedBlackPawnTakenOutEventConfig;
	
	private LastMoveBlackAteWhitePawnEventConsumer lastMoveBlackAteWhitePawnEventConsumer;
	
	@Autowired
	private LastMoveBlackAteWhitePawnEventConfig lastMoveBlackAteWhitePawnEventConfig;
	
	private TurnNotPassedBlackAteWhitePawnEventConsumer turnNotPassedBlackAteWhitePawnEventConsumer;
	
	@Autowired
	private TurnNotPassedBlackAteWhitePawnEventConfig turnNotPassedBlackAteWhitePawnEventConfig;
	
	private LastMoveWhiteAteBlackPawnEventConsumer lastMoveWhiteAteBlackPawnEventConsumer;
	
	@Autowired
	private LastMoveWhiteAteBlackPawnEventConfig lastMoveWhiteAteBlackPawnEventConfig;
	
	private TurnNotPassedWhiteAteBlackPawnEventConsumer turnNotPassedWhiteAteBlackPawnEventConsumer;
	
	@Autowired
	private TurnNotPassedWhiteAteBlackPawnEventConfig turnNotPassedWhiteAteBlackPawnEventConfig;
	
	private UserMadeLastMoveEventConsumer userMadeLastMoveEventConsumer;
	
	@Autowired
	private UserMadeLastMoveEventConfig userMadeLastMoveEventConfig;
	
	private TurnNotPassedUserMadeMoveEventConsumer turnNotPassedUserMadeMoveEventConsumer;
	
	@Autowired
	private TurnNotPassedUserMadeMoveEventConfig turnNotPassedUserMadeMoveEventConfig;
	
	private WhitePawnCameBackAndAteBlackPawnEventConsumer whitePawnCameBackAndAteBlackPawnEventConsumer;
	
	@Autowired
	private WhitePawnCameBackAndAteBlackPawnEventConfig whitePawnCameBackAndAteBlackPawnEventConfig;
	
	private LastMoveWhitePawnCameBackAndAteBlackPawnEventConsumer lastMoveWhitePawnCameBackAndAteBlackPawnEventConsumer;
	
	@Autowired
	private LastMoveWhitePawnCameBackAndAteBlackPawnEventConfig lastMoveWhitePawnCameBackAndAteBlackPawnEventConfig;
	
	private TurnNotPassedWhitePawnCameBackAndAteBlackPawnEventConsumer turnNotPassedWhitePawnCameBackAndAteBlackPawnEventConsumer;
	
	@Autowired
	private TurnNotPassedWhitePawnCameBackAndAteBlackPawnEventConfig turnNotPassedWhitePawnCameBackAndAteBlackPawnEventConfig;
	
	private BlackPawnCameBackAndAteWhitePawnEventConsumer blackPawnCameBackAndAteWhitePawnEventConsumer;
	
	@Autowired
	private BlackPawnCameBackAndAteWhitePawnEventConfig blackPawnCameBackAndAteWhitePawnEventConfig;
	
	private LastMoveBlackPawnCameBackAndAteWhitePawnEventConsumer lastMoveBlackPawnCameBackAndAteWhitePawnEventConsumer;
	
	@Autowired
	private LastMoveBlackPawnCameBackAndAteWhitePawnEventConfig lastMoveBlackPawnCameBackAndAteWhitePawnEventConfig;
	
	private TurnNotPassedBlackPawnCameBackAndAteWhitePawnEventConsumer turnNotPassedBlackPawnCameBackAndAteWhitePawnEventConsumer;
	
	@Autowired
	private TurnNotPassedBlackPawnCameBackAndAteWhitePawnEventConfig turnNotPassedBlackPawnCameBackAndAteWhitePawnEventConfig;
	
	private DiceRolledCanNotPlayEventConsumer diceRolledCanNotPlayEventConsumer;
	
	@Autowired
	private DiceRolledCanNotPlayEventConfig diceRolledCanNotPlayEventConfig;
	
	private WinnerMoveMadeEventConsumer winnerMoveMadeEventConsumer;
	
	@Autowired
	private WinnerMoveMadeEventConfig winnerMoveMadeEventConfig;
	
	private LoggedOutOpenByLeftFirstGameStoppedEventConsumer loggedOutOpenByLeftFirstGameStoppedEventConsumer;
	
	@Autowired
	private LoggedOutOpenByLeftFirstGameStoppedEventConfig loggedOutOpenByLeftFirstGameStoppedEventConfig;
	
	private LoggedOutSecondLeftFirstGameStoppedEventConsumer loggedOutSecondLeftFirstGameStoppedEventConsumer;
	
	@Autowired
	private LoggedOutSecondLeftFirstGameStoppedEventConfig loggedOutSecondLeftFirstGameStoppedEventConfig;
	
	private OpenByLeftFirstGameStoppedEventConsumer openByLeftFirstGameStoppedEventConsumer;
	
	@Autowired
	private OpenByLeftFirstGameStoppedEventConfig openByLeftFirstGameStoppedEventConfig;
	
	private SecondLeftFirstGameStoppedEventConsumer secondLeftFirstGameStoppedEventConsumer;
	
	@Autowired
	private SecondLeftFirstGameStoppedEventConfig secondLeftFirstGameStoppedEventConfig;
	
	private ConsumerToProducerQueue getGameUpdateViewQueue;
	
	private ExecutorService executor = Executors.newFixedThreadPool(6);
	
	private Logger logger = LoggerFactory.getLogger(AppInit.class);
	
	@Autowired
	private ApplicationContext context;
	
	public static final int NUM_CONSUMERS = 3;
	
	@Override
	public void initKafkaCommandsConsumers() {
		getGameUpdateViewQueue = context.getBean(ConsumerToProducerQueue.class);
		
		for(int i=0; i<NUM_CONSUMERS; i++){
			getGameUpdateViewCommandConsumer = context.getBean(GetGameUpdateViewCommandConsumer.class);
			initSingleConsumer(getGameUpdateViewCommandConsumer, KafkaUtils.GET_GAME_UPDATE_VIEW_COMMAND_TOPIC, getGameUpdateViewCommandConfig, getGameUpdateViewQueue);
			
			executeProducersAndConsumers(Arrays.asList(getGameUpdateViewCommandConsumer));
		}
	}

	@Override
	public void initKafkaEventsConsumers() {
		for(int i=0; i<NUM_CONSUMERS; i++){
			gameStartedEventConsumer = context.getBean(GameStartedEventConsumer.class);
			initSingleConsumer(gameStartedEventConsumer, KafkaUtils.GAME_STARTED_EVENT_TOPIC, gameStartedEventConfig, null);
			
			diceRolledEventConsumer = context.getBean(DiceRolledEventConsumer.class);
			initSingleConsumer(diceRolledEventConsumer, KafkaUtils.DICE_ROLLED_EVENT_TOPIC, diceRolledEventConfig, null);
			
			userMadeInvalidMoveEventConsumer = context.getBean(UserMadeInvalidMoveEventConsumer.class);
			initSingleConsumer(userMadeInvalidMoveEventConsumer, KafkaUtils.USER_MADE_INVALID_MOVE_EVENT_TOPIC, userMadeInvalidMoveEventConfig, null);
			
			whitePawnCameBackEventConsumer = context.getBean(WhitePawnCameBackEventConsumer.class);
			initSingleConsumer(whitePawnCameBackEventConsumer, KafkaUtils.WHITE_PAWN_CAME_BACK_EVENT_TOPIC, whitePawnCameBackEventConfig, null);
			
			blackPawnCameBackEventConsumer = context.getBean(BlackPawnCameBackEventConsumer.class);
			initSingleConsumer(blackPawnCameBackEventConsumer, KafkaUtils.BLACK_PAWN_CAME_BACK_EVENT_TOPIC, blackPawnCameBackEventConfig, null);
			
			whitePawnTakenOutEventConsumer = context.getBean(WhitePawnTakenOutEventConsumer.class);
			initSingleConsumer(whitePawnTakenOutEventConsumer, KafkaUtils.WHITE_PAWN_TAKEN_OUT_EVENT_TOPIC, whitePawnTakenOutEventConfig, null);
			
			blackPawnTakenOutEventConsumer = context.getBean(BlackPawnTakenOutEventConsumer.class);
			initSingleConsumer(blackPawnTakenOutEventConsumer, KafkaUtils.BLACK_PAWN_TAKEN_OUT_EVENT_TOPIC, blackPawnTakenOutEventConfig, null);
			
			blackAteWhitePawnEventConsumer = context.getBean(BlackAteWhitePawnEventConsumer.class);
			initSingleConsumer(blackAteWhitePawnEventConsumer, KafkaUtils.BLACK_ATE_WHITE_PAWN_EVENT_TOPIC, blackAteWhitePawnEventConfig, null);
			
			whiteAteBlackPawnEventConsumer = context.getBean(WhiteAteBlackPawnEventConsumer.class);
			initSingleConsumer(whiteAteBlackPawnEventConsumer, KafkaUtils.WHITE_ATE_BLACK_PAWN_EVENT_TOPIC, whiteAteBlackPawnEventConfig, null);
			
			userMadeMoveEventConsumer = context.getBean(UserMadeMoveEventConsumer.class);
			initSingleConsumer(userMadeMoveEventConsumer, KafkaUtils.USER_MADE_MOVE_EVENT_TOPIC, userMadeMoveEventConfig, null);
			
			lastMoveWhitePawnCameBackEventConsumer = context.getBean(LastMoveWhitePawnCameBackEventConsumer.class);
			initSingleConsumer(lastMoveWhitePawnCameBackEventConsumer, KafkaUtils.LAST_MOVE_WHITE_PAWN_CAME_BACK_EVENT_TOPIC, lastMoveWhitePawnCameBackEventConfig, null);
			
			turnNotPassedWhitePawnCameBackEventConsumer = context.getBean(TurnNotPassedWhitePawnCameBackEventConsumer.class);
			initSingleConsumer(turnNotPassedWhitePawnCameBackEventConsumer, KafkaUtils.TURN_NOT_PASSED_WHITE_PAWN_CAME_BACK_EVENT_TOPIC, turnNotPassedWhitePawnCameBackEventConfig, null);
			
			lastMoveBlackPawnCameBackEventConsumer = context.getBean(LastMoveBlackPawnCameBackEventConsumer.class);
			initSingleConsumer(lastMoveBlackPawnCameBackEventConsumer, KafkaUtils.LAST_MOVE_BLACK_PAWN_CAME_BACK_EVENT_TOPIC, lastMoveBlackPawnCameBackEventConfig, null);
			
			turnNotPassedBlackPawnCameBackEventConsumer = context.getBean(TurnNotPassedBlackPawnCameBackEventConsumer.class);
			initSingleConsumer(turnNotPassedBlackPawnCameBackEventConsumer, KafkaUtils.TURN_NOT_PASSED_BLACK_PAWN_CAME_BACK_EVENT_TOPIC, turnNotPassedBlackPawnCameBackEventConfig, null);
			
			lastMoveWhitePawnTakenOutEventConsumer = context.getBean(LastMoveWhitePawnTakenOutEventConsumer.class);
			initSingleConsumer(lastMoveWhitePawnTakenOutEventConsumer, KafkaUtils.LAST_MOVE_WHITE_PAWN_TAKEN_OUT_EVENT_TOPIC, lastMoveWhitePawnTakenOutEventConfig, null);
			
			turnNotPassedWhitePawnTakenOutEventConsumer = context.getBean(TurnNotPassedWhitePawnTakenOutEventConsumer.class);
			initSingleConsumer(turnNotPassedWhitePawnTakenOutEventConsumer, KafkaUtils.TURN_NOT_PASSED_WHITE_PAWN_TAKEN_OUT_EVENT_TOPIC, turnNotPassedWhitePawnTakenOutEventConfig, null);
			
			lastMoveBlackPawnTakenOutEventConsumer = context.getBean(LastMoveBlackPawnTakenOutEventConsumer.class);
			initSingleConsumer(lastMoveBlackPawnTakenOutEventConsumer, KafkaUtils.LAST_MOVE_BLACK_PAWN_TAKEN_OUT_EVENT_TOPIC, lastMoveBlackPawnTakenOutEventConfig, null);
			
			turnNotPassedBlackPawnTakenOutEventConsumer = context.getBean(TurnNotPassedBlackPawnTakenOutEventConsumer.class);
			initSingleConsumer(turnNotPassedBlackPawnTakenOutEventConsumer, KafkaUtils.TURN_NOT_PASSED_BLACK_PAWN_TAKEN_OUT_EVENT_TOPIC, turnNotPassedBlackPawnTakenOutEventConfig, null);
			
			lastMoveBlackAteWhitePawnEventConsumer = context.getBean(LastMoveBlackAteWhitePawnEventConsumer.class);
			initSingleConsumer(lastMoveBlackAteWhitePawnEventConsumer, KafkaUtils.LAST_MOVE_BLACK_ATE_WHITE_PAWN_EVENT_TOPIC, lastMoveBlackAteWhitePawnEventConfig, null);
			
			turnNotPassedBlackAteWhitePawnEventConsumer = context.getBean(TurnNotPassedBlackAteWhitePawnEventConsumer.class);
			initSingleConsumer(turnNotPassedBlackAteWhitePawnEventConsumer, KafkaUtils.TURN_NOT_PASSED_BLACK_ATE_WHITE_PAWN_EVENT_TOPIC, turnNotPassedBlackAteWhitePawnEventConfig, null);
			
			lastMoveWhiteAteBlackPawnEventConsumer = context.getBean(LastMoveWhiteAteBlackPawnEventConsumer.class);
			initSingleConsumer(lastMoveWhiteAteBlackPawnEventConsumer, KafkaUtils.LAST_MOVE_WHITE_ATE_BLACK_PAWN_EVENT_TOPIC, lastMoveWhiteAteBlackPawnEventConfig, null);
			
			turnNotPassedWhiteAteBlackPawnEventConsumer = context.getBean(TurnNotPassedWhiteAteBlackPawnEventConsumer.class);
			initSingleConsumer(turnNotPassedWhiteAteBlackPawnEventConsumer, KafkaUtils.TURN_NOT_PASSED_WHITE_ATE_BLACK_PAWN_EVENT_TOPIC, turnNotPassedWhiteAteBlackPawnEventConfig, null);
			
			userMadeLastMoveEventConsumer = context.getBean(UserMadeLastMoveEventConsumer.class);
			initSingleConsumer(userMadeLastMoveEventConsumer, KafkaUtils.USER_MADE_LAST_MOVE_EVENT_TOPIC, userMadeLastMoveEventConfig, null);
			
			turnNotPassedUserMadeMoveEventConsumer = context.getBean(TurnNotPassedUserMadeMoveEventConsumer.class);
			initSingleConsumer(turnNotPassedUserMadeMoveEventConsumer, KafkaUtils.TURN_NOT_PASSED_USER_MADE_MOVE_EVENT_TOPIC, turnNotPassedUserMadeMoveEventConfig, null);
			
			whitePawnCameBackAndAteBlackPawnEventConsumer = context.getBean(WhitePawnCameBackAndAteBlackPawnEventConsumer.class);
			initSingleConsumer(whitePawnCameBackAndAteBlackPawnEventConsumer, KafkaUtils.WHITE_PAWN_CAME_BACK_AND_ATE_BLACK_PAWN_EVENT_TOPIC, whitePawnCameBackAndAteBlackPawnEventConfig, null);
			
			lastMoveWhitePawnCameBackAndAteBlackPawnEventConsumer = context.getBean(LastMoveWhitePawnCameBackAndAteBlackPawnEventConsumer.class);
			initSingleConsumer(lastMoveWhitePawnCameBackAndAteBlackPawnEventConsumer, KafkaUtils.LAST_MOVE_WHITE_PAWN_CAME_BACK_AND_ATE_BLACK_PAWN_EVENT_TOPIC, lastMoveWhitePawnCameBackAndAteBlackPawnEventConfig, null);
			
			turnNotPassedWhitePawnCameBackAndAteBlackPawnEventConsumer = context.getBean(TurnNotPassedWhitePawnCameBackAndAteBlackPawnEventConsumer.class);
			initSingleConsumer(turnNotPassedWhitePawnCameBackAndAteBlackPawnEventConsumer, KafkaUtils.TURN_NOT_PASSED_WHITE_PAWN_CAME_BACK_AND_ATE_BLACK_PAWN_EVENT_TOPIC, turnNotPassedWhitePawnCameBackAndAteBlackPawnEventConfig, null);
			
			blackPawnCameBackAndAteWhitePawnEventConsumer = context.getBean(BlackPawnCameBackAndAteWhitePawnEventConsumer.class);
			initSingleConsumer(blackPawnCameBackAndAteWhitePawnEventConsumer, KafkaUtils.BLACK_PAWN_CAME_BACK_AND_ATE_WHITE_PAWN_EVENT_TOPIC, blackPawnCameBackAndAteWhitePawnEventConfig, null);
			
			lastMoveBlackPawnCameBackAndAteWhitePawnEventConsumer = context.getBean(LastMoveBlackPawnCameBackAndAteWhitePawnEventConsumer.class);
			initSingleConsumer(lastMoveBlackPawnCameBackAndAteWhitePawnEventConsumer, KafkaUtils.LAST_MOVE_BLACK_PAWN_CAME_BACK_AND_ATE_WHITE_PAWN_EVENT_TOPIC, lastMoveBlackPawnCameBackAndAteWhitePawnEventConfig, null);
			
			turnNotPassedBlackPawnCameBackAndAteWhitePawnEventConsumer = context.getBean(TurnNotPassedBlackPawnCameBackAndAteWhitePawnEventConsumer.class);
			initSingleConsumer(turnNotPassedBlackPawnCameBackAndAteWhitePawnEventConsumer, KafkaUtils.TURN_NOT_PASSED_BLACK_PAWN_CAME_BACK_AND_ATE_WHITE_PAWN_EVENT_TOPIC, turnNotPassedBlackPawnCameBackAndAteWhitePawnEventConfig, null);
			
			diceRolledCanNotPlayEventConsumer = context.getBean(DiceRolledCanNotPlayEventConsumer.class);
			initSingleConsumer(diceRolledCanNotPlayEventConsumer, KafkaUtils.DICE_ROLLED_CAN_NOT_PLAY_EVENT_TOPIC, diceRolledCanNotPlayEventConfig, null);
			
			winnerMoveMadeEventConsumer = context.getBean(WinnerMoveMadeEventConsumer.class);
			initSingleConsumer(winnerMoveMadeEventConsumer, KafkaUtils.WINNER_MOVE_MADE_EVENT_TOPIC, winnerMoveMadeEventConfig, null);
			
			loggedOutOpenByLeftFirstGameStoppedEventConsumer = context.getBean(LoggedOutOpenByLeftFirstGameStoppedEventConsumer.class);
			initSingleConsumer(loggedOutOpenByLeftFirstGameStoppedEventConsumer, KafkaUtils.LOGGED_OUT_OPENBY_LEFT_FIRST_GAME_STOPPED_EVENT_TOPIC, loggedOutOpenByLeftFirstGameStoppedEventConfig, null);
			
			loggedOutSecondLeftFirstGameStoppedEventConsumer = context.getBean(LoggedOutSecondLeftFirstGameStoppedEventConsumer.class);
			initSingleConsumer(loggedOutSecondLeftFirstGameStoppedEventConsumer, KafkaUtils.LOGGED_OUT_SECOND_LEFT_FIRST_GAME_STOPPED_EVENT_TOPIC, loggedOutSecondLeftFirstGameStoppedEventConfig, null);
			
			openByLeftFirstGameStoppedEventConsumer = context.getBean(OpenByLeftFirstGameStoppedEventConsumer.class);
			initSingleConsumer(openByLeftFirstGameStoppedEventConsumer, KafkaUtils.OPENBY_LEFT_FIRST_GAME_STOPPED_EVENT_TOPIC, openByLeftFirstGameStoppedEventConfig, null);
			
			secondLeftFirstGameStoppedEventConsumer = context.getBean(SecondLeftFirstGameStoppedEventConsumer.class);
			initSingleConsumer(secondLeftFirstGameStoppedEventConsumer, KafkaUtils.SECOND_LEFT_FIRST_GAME_STOPPED_EVENT_TOPIC, secondLeftFirstGameStoppedEventConfig, null);
			
			executeProducersAndConsumers(Arrays.asList(gameStartedEventConsumer,
					diceRolledEventConsumer,
					userMadeInvalidMoveEventConsumer,
					whitePawnCameBackEventConsumer,
					blackPawnCameBackEventConsumer,
					whitePawnTakenOutEventConsumer,
					blackPawnTakenOutEventConsumer,
					blackAteWhitePawnEventConsumer,
					whiteAteBlackPawnEventConsumer,
					userMadeMoveEventConsumer,
					lastMoveWhitePawnCameBackEventConsumer,
					turnNotPassedWhitePawnCameBackEventConsumer,
					lastMoveBlackPawnCameBackEventConsumer,
					turnNotPassedBlackPawnCameBackEventConsumer,
					lastMoveWhitePawnTakenOutEventConsumer,
					turnNotPassedWhitePawnTakenOutEventConsumer,
					lastMoveBlackPawnTakenOutEventConsumer,
					turnNotPassedBlackPawnTakenOutEventConsumer,
					lastMoveBlackAteWhitePawnEventConsumer,
					turnNotPassedBlackAteWhitePawnEventConsumer,
					lastMoveWhiteAteBlackPawnEventConsumer,
					turnNotPassedWhiteAteBlackPawnEventConsumer,
					userMadeLastMoveEventConsumer,
					turnNotPassedUserMadeMoveEventConsumer,
					whitePawnCameBackAndAteBlackPawnEventConsumer,
					lastMoveWhitePawnCameBackAndAteBlackPawnEventConsumer,
					turnNotPassedWhitePawnCameBackAndAteBlackPawnEventConsumer,
					blackPawnCameBackAndAteWhitePawnEventConsumer,
					lastMoveBlackPawnCameBackAndAteWhitePawnEventConsumer,
					turnNotPassedBlackPawnCameBackAndAteWhitePawnEventConsumer,
					diceRolledCanNotPlayEventConsumer,
					winnerMoveMadeEventConsumer,
					loggedOutOpenByLeftFirstGameStoppedEventConsumer,
					loggedOutSecondLeftFirstGameStoppedEventConsumer,
					openByLeftFirstGameStoppedEventConsumer,
					secondLeftFirstGameStoppedEventConsumer));
		}
	}

	@Override
	public void initKafkaCommandsProducers() {
		
	}

	@Override
	public void initKafkaEventsProducers() {
		initSingleProducer(getGameUpdateViewAckEventProducer, KafkaUtils.GET_GAME_UPDATE_VIEW_ACK_EVENT_TOPIC, getGameUpdateViewQueue);
		
		executeProducersAndConsumers(Arrays.asList(getGameUpdateViewAckEventProducer));
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}

	@Override
	public void engineShutdown() {
		logger.info("about to do shutdown.");	
//		shutdownSingleProducer(userEmailAvailabilityCheckedEventProducer);
		selfShutdown();
		logger.info("shutdown compeleted.");
	}	
	
	private void initSingleConsumer(ISimpleConsumer consumer, String topic, SimpleConsumerConfig consumerConfig, ConsumerToProducerQueue queue) {
		consumer.setTopic(topic);
		consumer.setSimpleConsumerConfig(consumerConfig);
		consumer.initConsumer();	
		consumer.setConsumerToProducerQueue(queue);
	}
	
	private void initSingleProducer(ISimpleProducer producer, String topic, ConsumerToProducerQueue queue) {
		producer.setTopic(topic);	
		producer.setConsumerToProducerQueue(queue);
	}
	
	private void shutdownSingleConsumer(ISimpleConsumer consumer) {
		consumer.setRunning(false);
		consumer.getScheduledExecutor().shutdown();	
	}
	
	private void shutdownSingleProducer(ISimpleProducer producer) {
		producer.setRunning(false);
		producer.getScheduledExecutor().shutdown();	
	}
	
	private void selfShutdown(){
		this.executor.shutdown();
	}
	
	private void executeProducersAndConsumers(List<Runnable> jobs){
		for(Runnable job:jobs)
			executor.execute(job);
	}
}
