package com.ia.core.algorithms.astar;

import java.util.LinkedList;
import java.util.List;

import com.ia.core.heuristic.Heuristics;
import com.ia.core.model.GameBoard;


public class AStar {
	/**
	 * List of nodes that are still open
	 */
	private List<AStarNode> openList;
	/**
	 * List of nodes that are already closed
	 */
	private List<AStarNode> closedList;
	/**
	 * List of solution nodes
	 */
	private List<GameBoard> solution;
	/**
	 * Number of steps
	 */
	private Integer steps;

	/**
	 * Default constructor
	 */
	public AStar() {
		openList = new LinkedList<AStarNode>();
		closedList = new LinkedList<AStarNode>();
		solution = new LinkedList<GameBoard>();
		steps = 0;
	}

	public void starSearch(GameBoard begin) {
		Heuristics h = new Heuristics(begin);
		h.start();
		try {
			h.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Integer he = h.getManhattan();
		
		AStarNode initialNode = new AStarNode(begin, null, 0, he);
		AStarNode actualNode = null;
		AStarNode finalNode = null;
		AStarNode savedNode = null;

		// Inserts the list open the starting node
		openList.add(initialNode);

		while (!openList.isEmpty()) {
			//get the node with the smallest heuristic value of the open list
			actualNode = minOpenList();

			// checks whether the current node is not the solution
			if (actualNode.getGameBoard().isSolution())
				break;

			// inserts the current node in the closed list and remove the open list
			closedList.add(actualNode);
			openList.remove(actualNode);

			/*
			 * For all nodes successors of the current node, 
			 * it checks if the list is open or closed. If any of them, 
			 * there is the cost is less than the cost of previously 
			 * purchased, if true removes this node from the closed 
			 * list, and if the list is open, it updates its cost. 
			 * In the case where this node is not in any means that 
			 * the lists is a new node, then this is added the open list.
			 * */
			
			for (GameBoard gameBoard : actualNode.getGameBoard().getGameBoardSuccessors()) {
				Heuristics heu = new Heuristics(gameBoard);
				heu.start();
				try {
					heu.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Integer heuristic = heu.getManhattan();
				if (actualNode.getPrevious() != null) {
					// Eliminates repeated nodes
					if (!actualNode.getPrevious().getGameBoard().equals(gameBoard)) {
						// Checks if the list is closed
						if ((savedNode = findGameBoardClosedList(gameBoard)) != null) {
							// if cost is lower than the cost previously found
							if (heuristic + actualNode.getNumMovements() < savedNode
									.getCost() + savedNode.getNumMovements()) {
								closedList.remove(savedNode);
								
								// Enters the state on the open list, 
								// if it is not on the open list
								if(findGameBoardInOpenList(gameBoard) == null) {
									finalNode = new AStarNode(gameBoard, actualNode,
											actualNode.getNumMovements() + 1,
											heuristic);

									openList.add(finalNode);					
								}
							}
						}
						// This verifies that the open list
						else if ((savedNode = findGameBoardInOpenList(gameBoard)) != null) {
							// if cost is lower than the cost previously found
							if (heuristic + actualNode.getNumMovements() < savedNode
									.getCost() + savedNode.getNumMovements()) {
								openList.remove(savedNode);

								finalNode = new AStarNode(gameBoard, actualNode,
										actualNode.getNumMovements() + 1,
										heuristic);

								openList.add(finalNode);
								}
						}
						// Node has not yet visited the same part in the open list
						else {
							finalNode = new AStarNode(gameBoard, actualNode,
									actualNode.getNumMovements() + 1, heuristic);

							openList.add(finalNode);
						}
					}
				}
				// As the previous node of the current is null, 
				// then this is the starting node, so all nodes 
				// are successors we have not yet visited. 
				// Inserts these nodes on the open list.
				else {
					finalNode = new AStarNode(gameBoard, actualNode,
							actualNode.getNumMovements() + 1, heuristic);

					openList.add(finalNode);
				}
			}
			
		}
		if (actualNode.getGameBoard().isSolution()) {
			AStarNode temp = actualNode;

			steps = actualNode.getNumMovements();

			while (temp.getPrevious() != null) {
				solution.add(temp.getGameBoard());
				temp = temp.getPrevious();
			}
		}
	}

	/**
	 * Find the node with the minimum cost
	 */
	public AStarNode minOpenList() {
		AStarNode min = null;

		for (AStarNode node  : openList) {
			if (min == null)
				min = node;
			else {
				if (min.getCost() > node.getCost())
					min = node;
			}
		}

		return min;
	}

	/**
	 * Find the node with the minimum cost at two levels
	 */
	public AStarNode minOpenList2Levels() {
		AStarNode min = null;
		GameBoard minNivel2 = null;

		for (AStarNode no : openList) {
			if (min == null) {
				min = no;
				minNivel2 = sucessorMenorCusto(min.getGameBoard());
			} else {
				GameBoard minTemp = sucessorMenorCusto(no.getGameBoard());
				Heuristics heu = new Heuristics(minTemp);
				heu.start();
				try {
					heu.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Integer heuristic = heu.getManhattan();
				Heuristics heu2 = new Heuristics(minNivel2);
				heu2.start();
				try {
					heu2.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Integer heuristic2 = heu2.getManhattan();
				if (min.getCost() + heuristic2 > no.getCost()
						+ heuristic) {
					min = no;
					minNivel2 = minTemp;
				}
			}
		}

		return min;
	}

	/**
	 * Find the lowest cost successor board
	 * @param gameBoard
	 */
	public GameBoard sucessorMenorCusto(GameBoard gameBoard) {
		GameBoard min = gameBoard;
		Heuristics heu = new Heuristics(gameBoard);
		heu.start();
		try {
			heu.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Integer heuristic = heu.getManhattan();

		for (GameBoard board : gameBoard.getGameBoardSuccessors()) {
			Heuristics heu2 = new Heuristics(board);
			heu2.start();
			try {
				heu2.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Integer heuristic2 = heu2.getManhattan();
			if (heuristic2 < heuristic)
				min = gameBoard;
		}

		return min;
	}

	/**
	 * Looking to open a node in the list no longer 
	 * has the state passed parameter.
	 * @param gameBoard
	 * 
	 */
	public AStarNode findGameBoardInOpenList(GameBoard gameBoard) {
		for (AStarNode n : openList) {
			if (n.getGameBoard().equals(gameBoard))
				return n;
		}

		return null;
	}

	/**
	 * Looking up a node in the closed list no 
	 * longer has the state passed parameter.
	 * @param gameBoard
	 */
	public AStarNode findGameBoardClosedList(GameBoard gameBoard) {
		for (AStarNode n : closedList) {
			if (n.getGameBoard().equals(gameBoard))
				return n;
		}

		return null;
	}

	/**
	 * @return
	 */
	public List<GameBoard> getSolution() {
		return solution;
	}

	/**
	 * @return
	 */
	public Integer getSteps() {
		return steps;
	}

	/**
	 * @param steps
	 */
	public void setSteps(Integer steps) {
		this.steps = steps;
	}
}