package org.moshe.arad.entities.backgammon.player;

import org.moshe.arad.entities.backgammon.instrument.BackgammonPawn;
import org.moshe.arad.entities.backgammon.move.Move;
import org.moshe.arad.entities.backgammon.turn.Turn;

/**
 * 
 * @author moshe-arad
 *
 */
public interface Player {
	
	public void makePlayed(Move move) throws Exception;
	
	public Turn getTurn();

	public void setTurn(Turn turn);
	
	/**
	 * specific for backgammon
	 */
	public void rollDices(); 
	/**
	 * specific for backgammon
	 * @throws Exception 
	 */
	public boolean isCanPlayWith(BackgammonPawn pawn) throws Exception;
}
