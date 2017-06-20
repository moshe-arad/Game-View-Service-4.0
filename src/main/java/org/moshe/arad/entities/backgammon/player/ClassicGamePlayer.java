package org.moshe.arad.entities.backgammon.player;

import org.moshe.arad.entities.backgammon.instrument.BackgammonPawn;
import org.moshe.arad.entities.backgammon.move.Move;
import org.moshe.arad.entities.backgammon.turn.Turn;

public abstract class ClassicGamePlayer implements Player{

	public abstract void makePlayed(Move move) throws Exception;
	
	public abstract Turn getTurn();

	public abstract void setTurn(Turn turn);
	
	/**
	 * specific for backgammon
	 */
	public abstract void rollDices(); 
	/**
	 * specific for backgammon
	 * @throws Exception 
	 */
	public abstract boolean isCanPlayWith(BackgammonPawn pawn) throws Exception;
}
