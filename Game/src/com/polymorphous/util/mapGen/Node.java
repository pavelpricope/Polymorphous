package com.polymorphous.util.mapGen;

import java.awt.Point;
import java.util.Optional;


/**
 * @author dxf209
 *
 */
public class Node {

    private Point pos;
    private float cost;
    private MapCode tile;
    private Optional<Node> parent;

    
    /**
     * A Node with an x,y position, a cost, a MapCode and an optional parent node
     * 
     * @param pos	The position of the node in the graph
     * @param cost	The cost of the node
     * @param tile	The tile type of the node
     */
    public Node(Point pos, float cost, MapCode tile) {
        this.pos = pos;
        this.cost = cost;
        this.tile = tile;
    }

    
    /**
     * Gets the node position
     * 
     * @return	The Point of the node
     */
    public Point getPos() {
        return pos;
    }

    
    /**
     * Gets the cost of the node
     * @return	float value which is the node cost
     */
    public float getCost() {
        return cost;
    }

    
    /**
     * Sets the cost of the node
     * 
     * @param cost	The cost to set
     */
    public void setCost(float cost) {
        this.cost = cost;
    }

    
    /**
     * Gets the tile type of the Node
     * 
     * @return	The MapCode of the node
     */
    public MapCode getTileType() {
        return tile;
    }

    
    /**
     * Sets the tile type
     * 
     * @param type	The MapCode type to set
     */
    public void setTileType(MapCode type) {
        tile = type;
    }

    
    /**
     * Compares two nodes 
     * 
     * @param n1	The first node
     * @param n2	The second node
     * @return		The integer value of the comparison
     */
    public int compareNodes(Node n1, Node n2) {
        if(n1.getCost() > n2.getCost())
            return 1;
        else if (n1.getCost() < n2.getCost())
            return -1;

        return 0;
    }

    
    /**
     * Sets the parent of the node
     * 
     * @param n	The node to set as the parent node
     */
    public void setParent(Node n) {
        parent = Optional.of(n);
    }

    
    /**
     * Gets the parent of the node if one exists
     * 
     * @return	The optional parent node 
     */
    public Optional<Node> getParent() {
        return parent;
    }

    
    /**
     * Check if the current node is equal to node n
     * @param n	The node that must be compared to
     * @return	A boolean telling us if two nodes are equal
     */
    public boolean equalTo(Node n) {
        return n.getPos().getX() == getPos().getX() && n.getPos().getY() == getPos().getY();
    }
}
