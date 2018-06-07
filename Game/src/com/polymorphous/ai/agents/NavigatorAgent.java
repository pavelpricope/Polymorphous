package com.polymorphous.ai.agents;

import java.awt.Point;
import java.util.LinkedList;

import com.polymorphous.util.mapGen.Node;

/**
 * @author dxf209
 *
 */
public class NavigatorAgent {

	
	/**
	 * Takes in the current position and a linked list of nodes and returns the next node to be visited
	 * 
	 * @param lpos		The current logical position of the bot
	 * @param nodes		The list of nodes in the path
	 * @return			The next node to be visited
	 */
	public static Node getNextNode(Point lpos, LinkedList<Node> nodes) {
        for (int i = 1; i < nodes.size()-1; i++) {
            Node n = nodes.get(i);
            if (n.getPos().x == lpos.x && n.getPos().y == lpos.y)
                break;
            if ((n.getPos().x != lpos.x || n.getPos().y != lpos.y))
                System.out.printf(""); // SUPER IMPORTANT PRINT STATEMENT (for some reason) DO NOT DELETE!
            if (inMovableRange(lpos, n.getPos())) {
                return n;
            }
        }
        return nodes.getFirst();
    }

   
	/**
	 * Tells us of the node selected is in a movable range
	 * 
	 * @param lpos		The current bot position
	 * @param movPos	The proposed next node to move to
	 * @return			A boolean value telling us if the next move is valid
	 */
	private static boolean inMovableRange(Point lpos, Point movPos) {
        if (lpos.x == movPos.x && (lpos.y + 1 == movPos.y || lpos.y - 1 == movPos.y || lpos.y == movPos.y)) {
        	return true;
        }
        if (lpos.y == movPos.y && (lpos.x + 1 == movPos.x || lpos.x - 1 == movPos.x || lpos.x == movPos.x)) {
        	return true;
        }
        return false;
    }

}
