
package com.ia.core.heuristic;

import com.ia.core.model.Element;
import com.ia.core.model.GameBoard;

public class Heuristics extends Thread {
	
	
	private final GameBoard gameBoard;
	private Integer manhattan = 0;
	
	public Heuristics(GameBoard gameBoard) {
		this.gameBoard = gameBoard;

	}

	public Integer getManhattan() {
        return manhattan;
    }

	@Override
	public void run() {
		
		for (Element element : gameBoard.getElements()) {
			Integer temp = 0;

			if (element.getNumber() == null) {
				temp = Math.abs(element.getLine() - 2);
				temp += Math.abs(element.getColumn() - 2);
			} else if (element.getNumber() == 1) {
				temp = element.getLine();
				temp += element.getColumn();
			} else if (element.getNumber() == 2) {
				temp = element.getLine();
				temp += Math.abs(element.getColumn() - 1);
			} else if (element.getNumber() == 3) {
				temp = element.getLine();
				temp += Math.abs(element.getColumn() - 2);
			} else if (element.getNumber() == 4) {
				temp = Math.abs(element.getLine() - 1);
				temp += element.getColumn();
			} else if (element.getNumber() == 5) {
				temp = Math.abs(element.getLine() - 1);
				temp += Math.abs(element.getColumn() - 1);
			} else if (element.getNumber() == 6) {
				temp = Math.abs(element.getLine() - 1);
				temp += Math.abs(element.getColumn() - 2);
			} else if (element.getNumber() == 7) {
				temp = Math.abs(element.getLine() - 2);
				temp += element.getColumn();
			} else if (element.getNumber() == 8) {
				temp = Math.abs(element.getLine() - 2);
				temp += Math.abs(element.getColumn() - 1);
			}

			manhattan += temp;
			
		}
	}
	
	

}