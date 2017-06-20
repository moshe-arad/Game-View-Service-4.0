package org.moshe.arad.entities.backgammon.instrument;

import org.moshe.arad.entities.backgammon.move.Move;

public interface Pawn {

	public boolean isAbleToDo(Move move) throws Exception;
}
