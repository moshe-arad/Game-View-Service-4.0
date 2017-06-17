package org.moshe.arad.view.utils;

import java.util.List;

import org.moshe.arad.entities.GameRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameView {

	@Autowired
	private GameViewUpdate gameViewUpdate;
	
	@Autowired
	private GameViewSimple gameViewSimple;
	
	public GameViewChanges getNeedToUpdateAllUsers(){
		return gameViewUpdate.getNeedToUpdateAllUsers();
	}
	
	public GameViewChanges getNeedToUpdateGroupUsers(String group){
		return gameViewUpdate.getNeedToUpdateGroupUsers(group);
	}
	
	public GameViewChanges getNeedToUpdateUser(String username){
		return gameViewUpdate.getNeedToUpdateUser(username);
	}
	
	public void markNeedToUpdateAllUsers(GameViewChanges lobbyViewChanges){
		gameViewUpdate.markNeedToUpdateAllUsers(lobbyViewChanges);
	}
	
	public void markNeedToUpdateGroupUsers(GameViewChanges lobbyViewChanges, String group){
		gameViewUpdate.markNeedToUpdateGroupUsers(lobbyViewChanges, group);
	}
	
	public void markNeedToUpdateSingleUser(GameViewChanges lobbyViewChanges, String username){
		gameViewUpdate.markNeedToUpdateSingleUser(lobbyViewChanges, username);
	}
}
