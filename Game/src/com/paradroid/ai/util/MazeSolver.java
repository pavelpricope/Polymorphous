package com.paradroid.ai.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;

import com.paradroid.util.mapGen.GenMap;
import com.paradroid.util.mapGen.MapCode;
import com.paradroid.util.mapGen.MapUtils;
import com.paradroid.util.mapGen.Node;

/**
 * @author dxf209
 *
 */
public class MazeSolver {
	private GenMap maze;
	private Node[][] graph;
	private int w;
	private int h;
	
	/**
	 * Maze Solver class that takes, and solves a GenMap
	 * @param maze	The maze to solve
	 */
	public MazeSolver(GenMap maze){
		this.maze = maze;
		this.graph = maze.getNodes();
		this.w = maze.getWidth();
		this.h = maze.getHeight();
	}
	
	
	/**
	 * The A* search algorithm implemented on the GenMap graph
	 * @return	A linked list of nodes which is the path to take
	 */
	public LinkedList<Node> aStarSearch() {
		//System.out.printf("A*");
		//SaveMaze.writeInitMaze(w, h, graph, "MazeSolution-AStar-init.png");
		
		ArrayList<Node> openList = new ArrayList<>();
		ArrayList<Node> closedList = new ArrayList<>();
		LinkedList<Node> path = new LinkedList<>();
		int nCount = 0;
		
		Node start = getStart();
		start.setCost(0.0f);
		Node goal = getGoal();
		
		long startTime = System.nanoTime();
		
		openList.add(start);
		
		while(!openList.isEmpty()){
			Node curNode = openList.remove(0);
			closedList.add(curNode);
			curNode.setTileType(MapCode.EXP);
			nCount ++;
			
			if(goal.equalTo(curNode)){
				curNode.setTileType(MapCode.PATH);
				// Write the path to the goal
				Optional<Node> parent = curNode.getParent();
				
				while(parent.isPresent()){
					//parent.get().setTileType(MapCode.PATH); // TODO: Strip the path out rather than change graph
					
					// Add the node to the path Queue
					path.add(parent.get());
					
					if(parent.get().getParent() == null){ break; }
					parent = parent.get().getParent();
				}
				
				System.out.printf(" Solved in : %d ns\n", (System.nanoTime() - startTime));
				
				start.setTileType(MapCode.START);
				goal.setTileType(MapCode.GOAL);
				//SaveMaze.writeInitMaze(w, h, graph, "MazeSolution-AStar.png");
				//System.out.println("Nodes Explored: " + nCount);
				break;
			}else{
				// Generate Successors
				ArrayList<Node> successors = MapUtils.generateSuccessor(curNode, maze);
				// FUTURE UPDATE successors = MazeUtils.filterNodes(successors, openList);
				successors = MapUtils.filterNodes(successors, closedList);
				
				for(Node n : successors){
					n.setParent(curNode);
					// Assign the cost to the parent node
					n.setCost(curNode.getCost());
					n.setTileType(MapCode.OPEN);
				}
				
				// Set the costs adding on the SLD
				//successors = AStarHeur.sldCost(successors, goal);
				
				successors = CostHeuristic.manhattanCost(successors, goal);
				
				openList.addAll(successors);
				openList.sort((m,n) -> m.compareNodes(m, n));
			}
			
		}
		return path;
	}
	
	
	/**
	 * Takes the mapcode and returns a node from the graph that has the mapcode type
	 * 
	 * @param code	The MapCode to search for
	 * @return		The Node of the graph that shares the mapcode
	 */
	private Node getTile(MapCode code){
		for(int y = 0; y < h; y++){ // was h-1
			for(int x = 0; x < w; x++){ // was w-1
				if(graph[x][y].getTileType() == code)
					return graph[x][y];
			}
		}
		// The dangerous null return!
		return null;
	}
	
	/**
	 * Gets the start from the graph
	 * 
	 * @return	The Node that is the Start
	 */
	private Node getStart(){
		return getTile(MapCode.START);
	}
	
	
	/**
	 * Gets the Goal from the graph
	 * 
	 * @return	The Node that is the Goal
	 */
	private Node getGoal(){
		return getTile(MapCode.GOAL);
	}
}
