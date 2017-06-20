package org.moshe.arad.entities.backgammon.instrument;

import org.moshe.arad.entities.backgammon.move.BoardLocation;
import org.moshe.arad.entities.backgammon.move.Move;
import org.moshe.arad.entities.backgammon.player.Player;

public interface Board {

	public void initBoard();
	
	public void clearBoard();
	
	public void display();
	
	public boolean isHasMoreMoves(Player player) throws Exception;
	
	public boolean isValidMove(Player player, Move move) throws Exception;
	
	public void executeMove(Player player, Move move) throws Exception;
	
	public boolean setPawn(Pawn pawn, BoardLocation location);
	
	public boolean isWinner(Player player) throws Exception;
}
