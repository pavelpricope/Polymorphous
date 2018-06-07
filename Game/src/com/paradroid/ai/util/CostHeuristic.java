package com.paradroid.ai.util;

import com.paradroid.util.mapGen.Node;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author dxf209
 *
 */
public class CostHeuristic {

    /**
     * Straight line distance cost calculated between the successor nodes and the goal node
     * 
     * @param successors	ArrayList of Nodes of all successors from the current state
     * @param goal			The target goal from the map 
     * @return				An ArrayList of new nodes with the updated cost
     */
    public static ArrayList<Node> sldCost(ArrayList<Node> successors, Node goal) {
        ArrayList<Node> newNodes = new ArrayList<> ();

        int goalX = goal.getPos().x, goalY = goal.getPos().y;

        for(Node n : successors) {
            double cost = Math.sqrt(Math.pow((goalX - n.getPos().getX()), 2.0d)+Math.pow((goalY - n.getPos().getY()), 2.0d));
            n.setCost((float)cost + n.getCost());
            newNodes.add(n);
        }
        return newNodes;
    }

    
    /**
     * Straight line distance cost between a single node and the goal node
     * 
     * @param start	The node to calculate the SLD from 
     * @param goal	The node to calculate the SLD to
     * @return		The float value, which is the SLD from the start to the goal
     */
    public static float sldCost(Node start, Node goal) {
        int startX = start.getPos().x, startY = start.getPos().y;
        int goalX = goal.getPos().x, goalY = goal.getPos().y;

        return (float)Math.sqrt(Math.pow((goalX - startX), 2.0d)+Math.pow((goalY - startY), 2.0d));
    }

    
    /**
     * Calculate the manhattan cost between a list of successor nodes and the goal node
     * @param sucessors	The list of successor nodes from the graph
     * @param goal		The goal node to get to
     * @return			A list of nodes, with the updated cost
     */
    public static ArrayList<Node> manhattanCost(ArrayList<Node> sucessors, Node goal) {
        ArrayList<Node> newNodes = new ArrayList<>();

        int goalX = goal.getPos().x, goalY = goal.getPos().y;

        for(Node n : sucessors) {
            float cost = Math.abs(n.getPos().x - goalX) + Math.abs(n.getPos().y - goalY); // f(x)

            //n.setCost(cost + n.getCost());
            n.setCost(cost);
            newNodes.add(n);
        }
        return newNodes;
    }

    
    /**
     * Manhattan cost between a single start node and the goal node
     * @param successor	The start node to calculate the manhattan distance from
     * @param goal		The goal node to calculate the manhattan distance to
     * @return			The float value of the manhattan distance calculated
     */
    public static float manhattanCost(Node successor, Node goal) {
        int goalX = goal.getPos().x, goalY = goal.getPos().y;
        return Math.abs(successor.getPos().x - goalX) + Math.abs(successor.getPos().y - goalY);
    }

    
    /**
     * Cost calculation determined by path size
     * @param path	A linked list of path nodes
     * @return		The size of the path
     */
    public static float calculateCost(LinkedList<Node> path) {
        return (float)path.size();
    }

    
    /**
     * A distance magnitude between two points
     * @param a	Point 1
     * @param b	Point 2
     * @return	The absolute distance magnitude
     */
    public static int distanceMagnitude(Point a, Point b) {
        return Math.abs(a.x - b.x + a.y - b.y);
    }
}
