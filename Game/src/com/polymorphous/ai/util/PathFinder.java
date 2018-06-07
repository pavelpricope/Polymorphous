package com.polymorphous.ai.util;

import com.polymorphous.util.mapGen.GenMap;
import com.polymorphous.util.mapGen.MapUtils;
import com.polymorphous.util.mapGen.Node;

import java.util.*;

/**
 * Created by dxf209
 */
public class PathFinder {

    
	/**
	 * Finds the path between two nodes from a given GenMap
	 * 
	 * @param start	The Node to start from
	 * @param end	The goal Node
	 * @param map	The GenMap that must be traversed
	 * @return		A linked list of the path of nodes
	 */
	public static LinkedList<Node> pathFind(Node start, Node end, GenMap map) {
        // Open list to sort and remove the next element
        PriorityQueue<Node> openList = new PriorityQueue<>(10, (m,n) -> m.compareNodes(m, n));
        // Closed list which states which nodes have already been visited
        HashSet<Node> closedList = new HashSet<>();
        // Linked List of the path that has been taken from start to end nodes
        LinkedList<Node> path = new LinkedList<>();

        start.setCost(0.0f);
        //start.setParent(null); // FIXME: May not need the null! Null is bad!

        long startTime = System.nanoTime();

        openList.add(start);

        while(!openList.isEmpty()) {
            // Remove the next node from the open list and add it to the closed list
            Node currentNode = openList.poll();
            closedList.add(currentNode);

            if(end.equalTo(currentNode)) {
                Optional<Node> parent = currentNode.getParent();

                while(parent != null && parent.isPresent()) {
                    if(path.contains(parent.get()))
                        break;

                    path.add(parent.get());

                    if(parent.get().getParent() == null) //|| path.contains(parent.get().getParent()))
                        break;

                    parent = parent.get().getParent();
                    //System.out.println("Parent: " + parent.get().getPos().x);
                }
                break;

            } else {
                // Generate successors
                ArrayList<Node> successors = new ArrayList<>();
                successors = MapUtils.generateSuccessor(currentNode, map);

                // Filter out nodes that have already been visited
                for(Node n : successors) {
                    if(!closedList.contains(n)) {
                        n.setParent(currentNode);
                        n.setCost(currentNode.getCost());
                        n.setCost(CostHeuristic.manhattanCost(n, end));
                        openList.add(n);
                    }
                }
            }


        }
        
        path.addFirst(end);
        return path;
    }
}
