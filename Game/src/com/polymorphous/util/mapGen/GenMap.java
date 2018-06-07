package com.polymorphous.util.mapGen;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * @author dxf209
 *
 */
public class GenMap {

    private Node[][] map;
    private int sX, sY;
    private ArrayList<Node> spawns = new ArrayList<>();

    /**
     * Generate a GenMap of specified size
     *  
     * @param sizeX	The width of the map
     * @param sizeY	The height of the map
     */
    public GenMap(int sizeX, int sizeY) {
        this.sX = sizeX;
        this.sY = sizeY;

        map = new Node[sX][sY];
        initMap();
        
        long seed = System.currentTimeMillis();
        
        generateMap(sX, sY, seed);
    }
    
    
    /**
     * Generates a GenMap of specified size, with a specified seed
     * 
     * @param sizeX	The width of the map
     * @param sizeY	The height of the map
     * @param seed	The seed of the map
     */
    public GenMap(int sizeX, int sizeY, long seed) {
    	this.sX = sizeX;
        this.sY = sizeY;

        map = new Node[sX][sY];
        initMap();
        generateMap(sX, sY, seed);
    }

   
    /**
     * Generates a GenMap from a loaded png image
     * 
     * @param img	The png image to be used as the map
     */
    public GenMap(BufferedImage img) {
        // Takes an image and generates a map
        this.sX = img.getWidth();
        this.sY = img.getHeight();

        map = new Node[sX][sY];

        for(int y = 0; y < sY; y++){
            for (int x = 0; x < sX; x++){
                map[x][y] = new Node(new Point(x,y), 0.0f, tileToMapCode(img.getRGB(x, y)));
            }
        }
//         drawMap();
    }


    /**
     * Generates the GenMap from the parameters passed from the constructor
     *  
     * @param sX	The width of the map
     * @param sY	The height of the map
     * @param seed	The map seed
     */
    private void generateMap(int sX, int sY, long seed) {
        ArrayList<Node> nodeList = new ArrayList<>();
        
        Random rand = new Random();
        rand.setSeed(seed);

        // Use the system time as the random seed
        Node start = new Node(new Point(rand.nextInt(sX-1), rand.nextInt(sY-1)), 0.0f, MapCode.START);

        nodeList.add(start);

        while(!nodeList.isEmpty()) {
            ArrayList<Node> temp = new ArrayList<>();

            Node curNode = nodeList.remove(nodeList.size() - 1);

            int x = curNode.getPos().x, y = curNode.getPos().y;

            boolean tu = false, td = false, tl = false, tr = false;


            // Add nodes to temp if they are NOT space, expanded or start
            if (getItem(x + 1, y) != null && getItem(x + 1, y).getTileType() != MapCode.SPACE && getItem(x + 1, y).getTileType() != MapCode.EXP && getItem(x + 1, y).getTileType() != MapCode.START) {
                temp.add(getItem(x + 1, y));
                //System.out.printf("%d : %d added\n", x+1, y);
            } else {
                tr = true;
            }

            if (getItem(x, y + 1) != null && getItem(x, y + 1).getTileType() != MapCode.SPACE && getItem(x, y + 1).getTileType() != MapCode.EXP && getItem(x, y + 1).getTileType() != MapCode.START) {
                temp.add(getItem(x, y + 1));
                //System.out.printf("%d : %d added\n", x, y+1);
            } else {
                tu = true;
            }

            if (getItem(x - 1, y) != null && getItem(x - 1, y).getTileType() != MapCode.SPACE && getItem(x - 1, y).getTileType() != MapCode.EXP && getItem(x - 1, y).getTileType() != MapCode.START) {
                temp.add(getItem(x - 1, y));
                //System.out.printf("%d : %d added\n", x-1, y);
            } else {
                tl = true;
            }

            if (getItem(x, y - 1) != null && getItem(x, y - 1).getTileType() != MapCode.SPACE && getItem(x, y - 1).getTileType() != MapCode.EXP && getItem(x, y - 1).getTileType() != MapCode.START) {
                temp.add(getItem(x, y - 1));
                //System.out.printf("%d : %d added\n", x, y-1);
            } else {
                td = true;
            }

            int val = 0;
            if (tu)
                val++;
            if (td)
                val++;
            if (tl)
                val++;
            if (tr)
                val++;

            // Essential part! This ensures the map does not have unreachable areas
            if (val >= 2)
                temp.clear();
            else
                curNode.setTileType(MapCode.SPACE);

            // Shuffle the node ordering
            Collections.shuffle(temp, rand);

            if (!temp.isEmpty()) {
                nodeList.addAll(temp);
            }
            temp.clear();
        }

        // Adds breaks in the wall
        for(int y = 2; y < sY-2; y++) {
            for(int x = 2; x < sX-2; x++) {
                boolean assignSpace = false;
                // Check for runs of n blocks DO NOT ALLOW IF SURROUNDED BY BLOCKS!
                if(map[x][y].getTileType() == MapCode.WALL) {
                    if(map[x-1][y].getTileType() == MapCode.WALL && map[x+1][y].getTileType() == MapCode.WALL) {
                        // Checks for rows of x boxes
                        assignSpace = !assignSpace;
                    }
                    if(map[x][y-1].getTileType() == MapCode.WALL && map[x][y+1].getTileType() == MapCode.WALL) {
                        // Checks for columns of y boxes
                        assignSpace = !assignSpace;
                    }

                    if(assignSpace)
                        map[x][y] = new Node(new Point(x,y), 0.0f, MapCode.SPACE);
                }
            }
        }

        // Add random spaces
        for(int y = 2; y < sY-2; y++) {
            for(int x = 2; x < sX-2; x++) {
                if((map[x][y].getTileType() == MapCode.WALL) && rand.nextInt(3) == 2){
                    map[x][y] = new Node(new Point(x,y), 0.0f, MapCode.SPACE);
                }
            }
        }

        // Set the boxes
        int numBoxes = Math.round(sX*sY*0.5f);

        while(numBoxes > 0) {
            int randX = rand.nextInt(sX);
            int randY = rand.nextInt(sY);

            // Ensure the tile is a space, allowing us to place an end node
            if(getItem(randX,randY).getTileType() == MapCode.SPACE && !checkRadius(3,randX, randY, MapCode.SPAWN)) {
                map[randX][randY] = new Node(new Point(randX,randY), 0.0f, MapCode.BOX);
                numBoxes--;
            }

        }

        // Set the corners of the map to be spawns and set the spawn area
        // Top Left
        map[1][1] = new Node(new Point(1,1), 0.0f, MapCode.SPAWN);
        map[1][2] = new Node(new Point(1,2), 0.0f, MapCode.SPACE);
        map[2][1] = new Node(new Point(2,1), 0.0f, MapCode.SPACE);

        // Bottom Left
        map[1][sY-2] = new Node(new Point(1,sY-2), 0.0f, MapCode.SPAWN);
        map[1][sY-3] = new Node(new Point(1,sY-3), 0.0f, MapCode.SPACE);
        map[2][sY-2] = new Node(new Point(2,sY-2), 0.0f, MapCode.SPACE);

        // Top Right
        map[sX-2][1] = new Node(new Point(sX-2,1), 0.0f, MapCode.SPAWN);
        map[sX-2][2] = new Node(new Point(sX-2,2), 0.0f, MapCode.SPACE);
        map[sX-3][1] = new Node(new Point(sX-3,1), 0.0f, MapCode.SPACE);

        // Bottom Right
        map[sX-2][sY-2] = new Node(new Point(sX-2,sY-2), 0.0f, MapCode.SPAWN);
        map[sX-2][sY-3] = new Node(new Point(sX-2,sY-3), 0.0f, MapCode.SPACE);
        map[sX-3][sY-2] = new Node(new Point(sX-3,sY-2), 0.0f, MapCode.SPACE);

        spawns.add(map[1][1]);
        spawns.add(map[1][sY-2]);
        spawns.add(map[sX-2][1]);
        spawns.add(map[sX-2][sY-2]);

        // Add the outside walls
        for(int x = 0; x < sX; x++) {
            map[x][0] = new Node(new Point(x,0), 10.0f, MapCode.WALL);
            map[x][sY-1] = new Node(new Point(x,sY-1), 10.0f, MapCode.WALL);
        }

        for (int y = 0; y < sY; y++){
            map[0][y] = new Node(new Point(0,y), 10.0f, MapCode.WALL);
            map[sX-1][y] = new Node(new Point(sX-1,y), 10.0f, MapCode.WALL);
        }

//        // Save and draw the map
//        System.out.println("Drawing GenMap");
//        drawMap();
//        //SaveMap.writeMap(sX, sY, map, "GenMap.png");
    }
    
    
    /**
     * Converts the int colour value to a map tile
     * 
     * @param rgb	The integer rgb value
     * @return		The MapCode enum type
     */
    private MapCode tileToMapCode(int rgb) {

        if(MapCode.BOX.getVal() == rgb)
            return MapCode.BOX;
        if(MapCode.SPACE.getVal() == rgb)
            return MapCode.SPACE;
        if(MapCode.SPAWN.getVal() == rgb)
            return MapCode.SPAWN;

        return MapCode.WALL;
    }


    
    /**
     * Gets the specified node from the map
     * 
     * @param x	The x value of the tile
     * @param y	The y value of the tile
     * @return	The Node of the specified x and y value
     */
    public Node getItem(int x, int y){
        if(x >= sX || y >= sY || x < 0 || y < 0){
            return null;
        }
        return map[x][y];
    }

    
    /**
     * Resets the map by setting all tiles as Space
     * 
     */
    public void clearMap() {
        for(int y = 0; y < sY; y++) {
            for(int x = 0; x < sX; x++) {
                if(map[x][y].getTileType() == MapCode.EXP || map[x][y].getTileType() == MapCode.PATH || map[x][y].getTileType() == MapCode.OPEN)
                    map[x][y] = new Node(new Point(x,y), 0.0f, MapCode.SPACE);
            }
        }
    }

    
    /**
     * Sets a node with the specified node value
     * 
     * @param n	Sets the node with the new node value of n
     */
    private void setItem(Node n) {
        int x = n.getPos().x;
        int y = n.getPos().y;

        if(x <= sX && y <= sY && x >= 0 && y >= 0)
            if(map[x][y].getTileType() == MapCode.WALL || map[x][y].getTileType() == MapCode.SPACE)
                map[x][y] = n;
            else
                map[x][y] = n;
    }

    
    /**
     * Sets the tile specified by x and y with a new MapCode
     * 
     * @param x		The tile x value
     * @param y		The tile y value
     * @param type	The MapCode tile type
     */
    private void setTileType(int x, int y, MapCode type) {
        if(map[x][y].getTileType() == MapCode.WALL)
            map[x][y] = new Node(new Point(x,y), Float.MAX_VALUE,type);
    }

    
    /**
     * Draws the map 
     * 
     */
    private void drawMap() {
        for(int y = 0; y < sY; y++) {
            System.out.println();

            for(int x = 0; x < sX; x++) {
                drawTile(map[x][y]);
            }
        }
        System.out.println();
    }

    
    /**
     * Draws the Node of the map
     * 
     * @param n	The node that must be drawn
     */
    private void drawTile(Node n) {
        MapCode type = n.getTileType();

        switch (type){
            case SPACE:
                System.out.print(" ");
                break;
            case WALL:
                System.out.print("\u2588");
                break;
            case START:
                System.out.print(" S ");
                break;
            case GOAL:
                System.out.print(" G ");
                break;
            case PATH:
                System.out.print(" x ");
                break;
            case EXP:
                System.out.print(" o ");
                break;
            case SPAWN:
                System.out.print("\u229d");
                break;
            case BOX:
                System.out.print("\u229e");
                break;
            default:
                System.out.print("?");
        }
    }

    
    /**
     * Initialise the map with walls set at every tile
     * 
     */
    private void initMap() {
        for(int y = 0; y < sY; y++) {
            for(int x = 0; x < sX; x++) {
                map[x][y] = new Node(new Point(x,y), 0.0f, MapCode.WALL);
            }
        }
    }

    
    /**
     * Check the radius around a given node for a type of tile
     * 
     * @param rad				The radius of tiles to check
     * @param x					The x value of the centre tile
     * @param y					The y value of the centre tile
     * @param protectedType		The MapCode enum of the protected type
     * @return					The boolean value of if the protected tile is within the radius of the centre tile
     */
    private boolean checkRadius(int rad, int x, int y, MapCode protectedType) {
        for(int i = 0; i < rad; i++) {
            for(int j = 0; j < rad; j++) {
                if(x+j < sX && y+i < sY && x-j > 0 && y-i > 0)
                    if(getItem(x+j,y+i).getTileType() == protectedType || getItem(x-j,y-i).getTileType() == protectedType)
                        return true;
            }
        }
        return false;
    }

    private boolean checkSurround(int rad, int x, int y, MapCode protectedType) {
        boolean surround = false;
        for(int i = 0; i < rad; i++) {
            for(int j = 0; j < rad; j++) {
                if(x+j < sX && y+i < sY && x-j > 0 && y-i > 0)
                    if(getItem(x+j,y+i).getTileType() == protectedType && getItem(x-j,y-i).getTileType() == protectedType)
                        surround = true;
                    else
                        return false;
            }
        }
        return surround;
    }

    
    /**
     * Gets all the nodes from the map
     * 
     * @return	A 2d array of nodes representing the graph
     */
    public Node[][] getNodes(){
        return map;
    }

    
    /**
     * Get the width of the map
     * 
     * @return	An int value of the width of the graph
     */
    public int getWidth(){
        return sX;
    }

    /**
     * Get the height of the map
     * 
     * @return	An int value of the height of the graph
     */
    public int getHeight(){
        return sY;
    }

    
    /**
     * Gets the spawn points from the map
     * 
     * @return	An ArrayList of nodes which contains the game spawn points
     */
    public ArrayList<Node> getSpawns() {return spawns;}
}
