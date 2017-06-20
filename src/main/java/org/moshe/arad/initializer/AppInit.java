package org.moshe.arad.initializer;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.consumers.ISimpleConsumer;
import org.moshe.arad.kafka.consumers.commands.GetGameUpdateViewCommandConsumer;
import org.moshe.arad.kafka.consumers.config.BlackPawnCameBackEventConfig;
import org.moshe.arad.kafka.consumers.config.DiceRolledEventConfig;
import org.moshe.arad.kafka.consumers.config.GameStartedEventConfig;
import org.moshe.arad.kafka.consumers.config.GetGameUpdateViewCommandConfig;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.moshe.arad.kafka.consumers.config.UserMadeInvalidMoveEventConfig;
import org.moshe.arad.kafka.consumers.config.WhitePawnCameBackEventConfig;
import org.moshe.arad.kafka.consumers.config.WhitePawnTakenOutEventConfig;
import org.moshe.arad.kafka.consumers.events.BlackPawnCameBackEventConsumer;
import org.moshe.arad.kafka.consumers.events.DiceRolledEventConsumer;
import org.moshe.arad.kafka.consumers.events.GameStartedEventConsumer;
import org.moshe.arad.kafka.consumers.events.UserMadeInvalidMoveEventConsumer;
import org.moshe.arad.kafka.consumers.events.WhitePawnCameBackEventConsumer;
import org.moshe.arad.kafka.consumers.events.WhitePawnTakenOutEventConsumer;
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
			
			executeProducersAndConsumers(Arrays.asList(gameStartedEventConsumer,
					diceRolledEventConsumer,
					userMadeInvalidMoveEventConsumer,
					whitePawnCameBackEventConsumer,
					blackPawnCameBackEventConsumer,
					whitePawnTakenOutEventConsumer));
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
