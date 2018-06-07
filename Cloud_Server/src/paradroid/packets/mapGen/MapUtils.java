package paradroid.packets.mapGen;

import java.util.ArrayList;

/**
 * @author dxf209
 *
 */
public class MapUtils {

    
	/**
	 * Generates successors based on a node and the graph passed
	 * 
	 * @param node		The node to generate successors from
	 * @param genMap	The GenMap to generate successors on
	 * @return			An ArrayList of successor nodes
	 */
	public static ArrayList<Node> generateSuccessor(Node node, GenMap genMap) {
        ArrayList<Node> succ = new ArrayList<>();

        int nodeX = node.getPos().x, nodeY = node.getPos().y;

        // Can alter this list to disallow generation of a genMap of more tile types

        if(genMap.getItem(nodeX+1, nodeY) != null && genMap.getItem(nodeX+1, nodeY).getTileType() != MapCode.WALL) {
            succ.add(genMap.getItem(nodeX+1, nodeY));
        }

        if(genMap.getItem(nodeX-1, nodeY) != null && genMap.getItem(nodeX-1, nodeY).getTileType() != MapCode.WALL) {
            succ.add(genMap.getItem(nodeX-1, nodeY));
        }

        if(genMap.getItem(nodeX, nodeY+1) != null && genMap.getItem(nodeX, nodeY+1).getTileType() != MapCode.WALL) {
            succ.add(genMap.getItem(nodeX, nodeY+1));
        }

        if(genMap.getItem(nodeX, nodeY-1) != null && genMap.getItem(nodeX, nodeY-1).getTileType() != MapCode.WALL) {
            succ.add(genMap.getItem(nodeX, nodeY-1));
        }

        return succ;
    }


	/**
	 * Gets the MapCode of a Node from a given GenMap
	 *
	 * @param n			The Node that we want the tile status of
	 * @param genMap	The GenMap to get the Node from
	 * @return			The MapCode of the given Node on the given GenMap
	 */
	public static MapCode getTileStatus(Node n, GenMap genMap) {
        Node[][] graph = genMap.getNodes();

        for (int y = 0; y < genMap.getHeight()-1; y++) {
            for (int x = 0; x < genMap.getWidth()-1; x++) {
                if(n.equalTo(graph[x][y]))
                    return n.getTileType();
            }
        }
        return null;
    }


	/**
	 * Filters one list of nodes against another
	 *
	 * @param tbf	The ArrayList of nodes to be filtered
	 * @param fl	The ArrayList of nodes that must not be in the resulting list
	 * @return		The ArrayList of filtered Nodes
	 */
	public static ArrayList<Node> filterNodes(ArrayList<Node> tbf, ArrayList<Node> fl) {
        ArrayList<Node> filtered = new ArrayList<>();

        boolean p = false;
        for(Node a : tbf) {
            for (Node b : fl) {
                if(a.equalTo(b)) {
                    p = true;
                    break;
                }
            }
            // Add to filtered if we exit the loop with no breaks
            if(!p)
                filtered.add(a);

            p = false;
        }
        return filtered;
    }
}
