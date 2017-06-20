package org.moshe.arad.entities.backgammon.move;

import org.springframework.stereotype.Component;

@Component
public class BackgammonBoardLocation implements BoardLocation{

	private Integer index;

	public BackgammonBoardLocation() {
	}
	
	public BackgammonBoardLocation(Integer index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return "BackgammonBoardLocation [index=" + index + "]";
	}
}
