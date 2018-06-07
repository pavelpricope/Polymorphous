package com.paradroid.main.Objects;


import com.paradroid.ai.util.PathFinder;
import com.paradroid.main.Game;
import com.paradroid.main.State;
import com.paradroid.util.mapGen.GenMap;
import com.paradroid.util.mapGen.MapCode;
import com.paradroid.util.mapGen.Node;
import com.paradroid.util.mapGen.UniversalMap;
import com.paradroid.util.physics.GeometryUtil;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author pxp660
 */
public class Map extends GameObject {


    private static LinkedList<Space> spaces = new LinkedList<>();
    private static LinkedList<Point2D> spawnPos = new LinkedList<>();
    private static LinkedList<GameObject> allTiles = new LinkedList<>();

    private GenMap map;
    private int width;
    private int height;

    /**
     * @param x  The width of the map
     * @param y  The height of the map
     * @param id The id of rhe map
     */
    public Map(int x, int y, ID id) {
        super(x, y, id);
        width = x;
        height = y;

        map = new GenMap(19, 19);

        UniversalMap.populateUniversalMap(map, x, y);

        boolean traversable = true;
        ArrayList<Node> spawns = map.getSpawns();
        for (int i = 0; i < spawns.size() - 1; i++) {
            if (PathFinder.pathFind(spawns.get(i), spawns.get(i + 1), map).isEmpty()) {
                traversable = false;
                break;
            }
        }
        if (!traversable)
            map = new GenMap(width / Game.BLOCK_SIZE, height / Game.BLOCK_SIZE);

        if (Game.state == State.Multiplayer) {
            map = Game.gen_map;
        }

        createWalls();
    }

    @Override
    public void update() {
    }


    @Override
    public void render(GraphicsContext g) {
    }

    @Override
    public Polygon getBounds() {
        return null;
    }

    public void createWalls() {

        int yVal = Game.BLOCK_SIZE;
        int xVal = width / 2 - Game.BLOCK_SIZE / 2;
        int xTemp = xVal;
        int yTemp = yVal;

        for (int i = 0; i < height / Game.BLOCK_SIZE; i++) {
            for (int j = 0; j < width / Game.BLOCK_SIZE; j++) {

                if (map.getItem(j, i).getTileType() == MapCode.WALL) {

                    allTiles.add(new Wall(xVal, yVal, ID.Wall, Game.BLOCK_SIZE, Game.BLOCK_SIZE));
                }
                if (map.getItem(j, i).getTileType() == MapCode.BOX) {
                    allTiles.add(new Box(xVal, yVal, ID.Box));
                }
                if (map.getItem(j, i).getTileType() == MapCode.SPACE) {
                    spaces.add(new Space(xVal, yVal, ID.Space));
                    allTiles.add(new Space(xVal, yVal, ID.Space));
                }
                if (map.getItem(j, i).getTileType() == MapCode.SPAWN) {
                    spawnPos.add(new Point2D(xVal, yVal));
                    spaces.add(new Space(xVal, yVal, ID.Space));
                    allTiles.add(new Space(xVal, yVal, ID.Space));
                }

                xVal += Game.BLOCK_SIZE / 2;
                yVal += Game.BLOCK_SIZE / 4;
            }
            xTemp -= Game.BLOCK_SIZE / 2;
            yTemp += Game.BLOCK_SIZE / 4;
            xVal = xTemp;
            yVal = yTemp;
        }
    }

    public LinkedList<Space> getSpaces() {
        return spaces;
    }

    public LinkedList<Point2D> getSpawnPos() {
        return spawnPos;
    }

    ;

    public LinkedList<GameObject> getObstacles() {
        return allTiles;
    }

    public GenMap getMap() {
        return map;
    }

    /**
     * @param x
     * @param y
     * @return the space at position x and y
     */
    public Space getSpaceAt(int x, int y) {
        Point2D point = new Point2D(x, y);

        for (Space tempSpace : spaces) {
            Point2D p1 = new Point2D(tempSpace.getBounds().getPoints().get(0), tempSpace.getBounds().getPoints().get(1));
            Point2D p2 = new Point2D(tempSpace.getBounds().getPoints().get(2), tempSpace.getBounds().getPoints().get(3));
            Point2D p3 = new Point2D(tempSpace.getBounds().getPoints().get(4), tempSpace.getBounds().getPoints().get(5));
            Point2D p4 = new Point2D(tempSpace.getBounds().getPoints().get(6), tempSpace.getBounds().getPoints().get(7));
            Point2D[] polygon = {p1, p2, p3, p4};
            if (GeometryUtil.isInsidePoligon(point, polygon)) {
                return tempSpace;
            }

        }
        return null;
    }

    /**
     * @param x,y
     * @return the tile at position x and y
     */
    public GameObject getTileAt(int x, int y) {
        Point2D point = new Point2D(x, y);

        for (GameObject tempTile : getObstacles()) {
            if (tempTile instanceof Box || tempTile instanceof Wall) {
                Point2D p1 = new Point2D(tempTile.getBounds().getPoints().get(0), tempTile.getBounds().getPoints().get(1));
                Point2D p2 = new Point2D(tempTile.getBounds().getPoints().get(2), tempTile.getBounds().getPoints().get(3));
                Point2D p3 = new Point2D(tempTile.getBounds().getPoints().get(4), tempTile.getBounds().getPoints().get(5));
                Point2D p4 = new Point2D(tempTile.getBounds().getPoints().get(6), tempTile.getBounds().getPoints().get(7));
                Point2D[] polygon = {p1, p2, p3, p4};
                if (GeometryUtil.isInsidePoligon(point, polygon)) {
                    return tempTile;
                }
            }

        }
        return null;
    }
}
