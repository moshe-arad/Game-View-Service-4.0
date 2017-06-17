package org.moshe.arad.view.utils;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GameViewUpdate {

	private RedisTemplate<String, String> redisTemplate;
	
	public static final String NEED_TO_UPDATE = "NeedToUpdate";
	public static final String ALL = "All";
	public static final String GROUP = "Group";
	public static final String USER = "User";
	
	private Object allLocker = new Object();
	private Object groupLocker = new Object();
	private Object userLocker = new Object();
	
	@Autowired
	private ApplicationContext context;
	
	private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
	
	@Autowired
    public GameViewUpdate(final RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
	
	public void markNeedToUpdateAllUsers(GameViewChanges lobbyViewChanges){
		
		synchronized(allLocker){
			GameViewChanges existingLobbyViewChanges = getLobbyViewChangesFromRedis(NEED_TO_UPDATE + ":" + ALL);
			
			unionViews(lobbyViewChanges, existingLobbyViewChanges);
			
			ObjectMapper objectMapper = new ObjectMapper();
			String lobbyViewChangesJson = null;
			try {
				lobbyViewChangesJson = objectMapper.writeValueAsString(lobbyViewChanges);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			
			if(!redisTemplate.hasKey(ALL)) redisTemplate.opsForValue().set(ALL, "1");
			else redisTemplate.opsForValue().increment(ALL, 1);
			
			redisTemplate.opsForValue().set(NEED_TO_UPDATE + ":" + ALL, lobbyViewChangesJson);
			allLocker.notifyAll();
		}		
	}	
	
	public  void markNeedToUpdateGroupUsers(GameViewChanges lobbyViewChanges, String group){
		synchronized(groupLocker){
			GameViewChanges existingLobbyViewChanges = getLobbyViewChangesFromRedis(NEED_TO_UPDATE + ":" + GROUP + ":" + group);
			
			unionViews(lobbyViewChanges, existingLobbyViewChanges);
			
			ObjectMapper objectMapper = new ObjectMapper();
			String lobbyViewChangesJson = null;
			try {
				lobbyViewChangesJson = objectMapper.writeValueAsString(lobbyViewChanges);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			
			if(!redisTemplate.opsForHash().hasKey(GROUP, group)) redisTemplate.opsForHash().put(GROUP, group, "1");
			else redisTemplate.opsForHash().increment(GROUP, group, 1);
			
			redisTemplate.opsForValue().set(NEED_TO_UPDATE + ":" + GROUP + ":" + group, lobbyViewChangesJson);
			groupLocker.notifyAll();
		}		
	}
	
	public  void markNeedToUpdateSingleUser(GameViewChanges lobbyViewChanges, String username){
		
		synchronized(userLocker){
			GameViewChanges existingLobbyViewChanges = getLobbyViewChangesFromRedis(NEED_TO_UPDATE + ":" + USER + ":" + username);
			
			unionViews(lobbyViewChanges, existingLobbyViewChanges);
			
			ObjectMapper objectMapper = new ObjectMapper();
			String lobbyViewChangesJson = null;
			try {
				lobbyViewChangesJson = objectMapper.writeValueAsString(lobbyViewChanges);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			
			if(!redisTemplate.opsForHash().hasKey(USER, username)) redisTemplate.opsForHash().put(USER, username, "1");
			else redisTemplate.opsForHash().increment(USER, username, 1);
			
			redisTemplate.opsForValue().set(NEED_TO_UPDATE + ":" + USER + ":" + username, lobbyViewChangesJson);
			userLocker.notifyAll();
		}
	}
	
	private GameViewChanges getLobbyViewChangesFromRedis(String key){
		if(!redisTemplate.hasKey(key)) return null;
		else{
			String lobbyViewChangesJson = redisTemplate.opsForValue().get(key);
			ObjectMapper objectMapper = new ObjectMapper();
			GameViewChanges lobbyViewChanges = null;
			try {
				lobbyViewChanges = objectMapper.readValue(lobbyViewChangesJson, GameViewChanges.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return lobbyViewChanges;
		}
	}
	
	private void unionViews(GameViewChanges lobbyViewChanges, GameViewChanges existingLobbyViewChanges) {
		if(existingLobbyViewChanges != null && lobbyViewChanges != null){
			//TODO implement this
		}
	}
	
	public GameViewChanges getNeedToUpdateAllUsers(){
		synchronized (allLocker) {
			GameViewChanges result = null;
			ObjectMapper objectMapper = new ObjectMapper();
			String lobbyViewChangesJson = null;
			
			if(!redisTemplate.hasKey(NEED_TO_UPDATE + ":" + ALL)){
				try {
					allLocker.wait(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			try {
				lobbyViewChangesJson = redisTemplate.opsForValue().get(NEED_TO_UPDATE + ":" + ALL);
				if(lobbyViewChangesJson != null){
					result = objectMapper.readValue(lobbyViewChangesJson,GameViewChanges.class);
					checkInnerCounterOf(ALL, NEED_TO_UPDATE + ":" + ALL);
				}				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return result;
		}		
	}
	
	public GameViewChanges getNeedToUpdateGroupUsers(String group){
		synchronized (groupLocker) {
			GameViewChanges result = null;
			ObjectMapper objectMapper = new ObjectMapper();
			String lobbyViewChangesJson = null;
			
			if(!redisTemplate.hasKey(NEED_TO_UPDATE + ":" + GROUP + ":" + group)){
				try {
					groupLocker.wait(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			try {
				lobbyViewChangesJson = redisTemplate.opsForValue().get(NEED_TO_UPDATE + ":" + GROUP + ":" + group);
				if(lobbyViewChangesJson != null){
					result = objectMapper.readValue(lobbyViewChangesJson,GameViewChanges.class);
					hashCheckInnerCounterOf(GROUP, group,  NEED_TO_UPDATE + ":" + GROUP + ":" + group);
				}				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return result;
		}
	}
	
	public GameViewChanges getNeedToUpdateUser(String username){
		synchronized (userLocker) {
			GameViewChanges result = null;
			ObjectMapper objectMapper = new ObjectMapper();
			String lobbyViewChangesJson = null;
			
			if(!redisTemplate.hasKey(NEED_TO_UPDATE + ":" + USER + ":" + username)){
				try {
					userLocker.wait(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			try {
				lobbyViewChangesJson = redisTemplate.opsForValue().get(NEED_TO_UPDATE + ":" + USER + ":" + username);
				if(lobbyViewChangesJson != null){
					result = objectMapper.readValue(lobbyViewChangesJson,GameViewChanges.class);
					hashCheckInnerCounterOf(USER, username,  NEED_TO_UPDATE + ":" + USER + ":" + username);
				}					
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return result;
		}		
	}
	
	private void checkInnerCounterOf(String key, String needToUpdateKey){
		Integer counter = Integer.valueOf(redisTemplate.opsForValue().get(key));
		counter--;
		if(counter.equals(0)) redisTemplate.delete(needToUpdateKey);
		redisTemplate.opsForValue().set(key, counter.toString());
	}
	
	private void hashCheckInnerCounterOf(String key, String hkey, String needToUpdateKey){
		Integer counter = Integer.valueOf(redisTemplate.opsForHash().get(key, hkey).toString());
		counter--;
		if(counter.equals(0)) redisTemplate.delete(needToUpdateKey);
		redisTemplate.opsForHash().put(key, hkey, counter.toString());
	}
}
