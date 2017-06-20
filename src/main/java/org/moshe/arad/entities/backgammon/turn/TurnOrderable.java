package org.moshe.arad.entities.backgammon.turn;

import org.moshe.arad.entities.backgammon.player.Player;

public interface TurnOrderable <T extends Player> {

	public T howHasTurn();
	
	public boolean passTurn();
	
	public T howIsNextInTurn();
}
