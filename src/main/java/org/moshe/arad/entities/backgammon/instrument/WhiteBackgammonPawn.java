package org.moshe.arad.entities.backgammon.instrument;

import org.moshe.arad.entities.backgammon.move.BackgammonBoardLocation;
import org.moshe.arad.entities.backgammon.move.Move;

public class WhiteBackgammonPawn extends BackgammonPawn{

	@Override
	public boolean isAbleToDo(Move move) throws Exception {
		if(move == null) throw new Exception("move is null.");
		return (((BackgammonBoardLocation)move.getFrom()).getIndex() - ((BackgammonBoardLocation)move.getTo()).getIndex() > 0);
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof WhiteBackgammonPawn;
	}
}
