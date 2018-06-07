package paradroid.packets.mapGen;

import com.polymorphous.main.Game;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;


/**
 * @author dxf209
 *
 */
public class UniversalMap {

	private static HashMap<Polygon, Point> screenToLogic = new HashMap<>();
	private static HashMap<Point, Polygon> logicToScreen = new HashMap<>();
	
	/**
	 * Maps screen coordinates to a logical map
	 * 
	 * @param node			The Logical map node
	 * @param screenPoint	The Screen pixel position
	 */
	public static void addToUniMap(Point node, Point screenPoint) {
//		int[] xPoints = {screenPoint.x, screenPoint.x+Game.BLOCK_SIZE/2, screenPoint.x-Game.BLOCK_SIZE/2, screenPoint.x};
//		int[] yPoints = {screenPoint.y, screenPoint.y + Game.BLOCK_SIZE/2, screenPoint.y + Game.BLOCK_SIZE/2, screenPoint.y + Game.BLOCK_SIZE};
//		Polygon poly = new Polygon(xPoints, yPoints, 4); // int[] x, int[] y, int number of points
		int x = screenPoint.x-Game.BLOCK_SIZE/2;
		int y = screenPoint.y-Game.BLOCK_SIZE/2;
		
		int width = Game.BLOCK_SIZE;
		int height = Game.BLOCK_SIZE;
		
		//up
        double sx = x + width / 2;
        double sy = y + Game.BLOCK_SIZE / 3 - 3;
        //right
        double sw = x + width;
        double sh = y + height / 2 + 3;
        //down
        double dx = x + width / 2;
        double dy = y + height - Game.BLOCK_SIZE / 5;
        //left
        double dw = x;
        double dh = y + height / 2 + 3;
        
        int[] xPoints = {(int)sx,(int)sw,(int)dx,(int)dw};
        int[] yPoints = {(int)sy,(int)sh,(int)dy,(int)dh};

        Polygon poly = new Polygon(xPoints, yPoints, 4);

		logicToScreen.put(node, poly);
		screenToLogic.put(poly, node);
		
//		Random r = new Random();
//		int col = r.nextInt(5);
//		
//		if(col == 0) 
//			Game.getHandler().addObject(new Internals(x,y, ID.AIInternals, Color.ALICEBLUE));
//		else if(col == 1)
//			Game.getHandler().addObject(new Internals(x,y, ID.AIInternals, Color.AQUA));
//		else if(col == 2)
//			Game.getHandler().addObject(new Internals(x,y, ID.AIInternals, Color.RED));
//		else if(col == 3)
//			Game.getHandler().addObject(new Internals(x,y, ID.AIInternals, Color.GREEN));
//		else if(col == 4)
//			Game.getHandler().addObject(new Internals(x,y, ID.AIInternals, Color.YELLOW));
		
		
		
//		debugPolys(xPoints, yPoints);
	}
	
	public static void debugPolys(int[] xPoints, int[] yPoints) {
		//Internals i = new Internals(0,0,ID.AIInternals,0);
		double[] xp = new double[xPoints.length];
		double[] yp = new double[yPoints.length];
		for(int j = 0; j < xPoints.length; j++) {
			xp[j] = (double)xPoints[j];
		}
		for(int j = 0; j < yPoints.length; j++) {
			yp[j] = (double)yPoints[j];
		}
		
//		i.renderDebug(g, xp, yp);
	}
	
	/**
	 * Converts the screen pixel position to the logical map
	 * 
	 * @param p	The screen pixel coordinate
	 * @return	The logical map coordinate
	 */
	public static Point convertScreenToLogic(Point p) {
		LinkedList<Point> tiles = new LinkedList<>();
		HashMap<Polygon,Point> temp = screenToLogic;
		Iterator iter = temp.entrySet().iterator();
		while(iter.hasNext()) {
			Map.Entry pair = (Map.Entry)iter.next();
			if(((Polygon)pair.getKey()).contains(p.x, p.y)) { // || ((Polygon)pair.getKey()).intersects(p.x, p.y, 1, 1)) {
				return((Point) pair.getValue());
			}
		}
		return null;
	}

	/**
	 * Converts the logical map coordinate to the corresponding screen position
	 * 
	 * @param node	The logical map coordinate
	 * @return		The screen coordinate
	 */
	public static Point convertLogicToScreen(Point node) {
		if(logicToScreen.containsKey(node)) {
			Polygon p = logicToScreen.get(node);
			return (new Point(p.xpoints[0], p.ypoints[0]));
		}
		return new Point(0,0);
	}
	
	
	/**
	 * Takes the generated map and creates the corresponding screen polygon coordinates
	 *  
	 * @param map	The generated map for the game
	 * @param x		The width of the map
	 * @param y		The height of the map
	 */
	public static void populateUniversalMap(GenMap map, int x, int y) {
		int yVal = Game.BLOCK_SIZE;
        int xVal = (x / 2 - Game.BLOCK_SIZE / 2);
        int xTemp = xVal;
        int yTemp = yVal;
        
        screenToLogic = new HashMap<>();
        logicToScreen = new HashMap<>();
        

        for (int i = 0; i < y / Game.BLOCK_SIZE; i++) {
            for (int j = 0; j < x / Game.BLOCK_SIZE; j++) {
            	//System.out.printf("UNIMAP: (%d,%d)->(%d,%d)\n", j,i,xVal,yVal);
            	addToUniMap(new Point(j,i), new Point(xVal,yVal));

                xVal += Game.BLOCK_SIZE / 2;
                yVal += Game.BLOCK_SIZE/4;
            }
            xTemp -= Game.BLOCK_SIZE / 2;
            yTemp += Game.BLOCK_SIZE/4;
            xVal = xTemp;
            yVal = yTemp;
        }
	}
	
	
	/**
	 * Creates a string which shows the logical mapping to the generated polygons
	 * 
	 * @return	A string containing the logical map to generated polygon points
	 */
	public static String printMap() {
		String res = "";
		HashMap<Polygon, Point> temp = screenToLogic;
		Iterator iter = temp.entrySet().iterator();
		while(iter.hasNext()) {
			Map.Entry pair = (Map.Entry)iter.next();
			res += "Logic: (" + ((Point)pair.getValue()).x +", "+ ((Point)pair.getValue()).y + ") "
					+ "-> ScreenPoly: ([" + ((Polygon)pair.getKey()).xpoints[0] +", " + ((Polygon)pair.getKey()).ypoints[0] + "], "
							+ "[" + ((Polygon)pair.getKey()).xpoints[1] +", " + ((Polygon)pair.getKey()).ypoints[1] + "], "
									+ "[" + ((Polygon)pair.getKey()).xpoints[2] +", " + ((Polygon)pair.getKey()).ypoints[2] + "], "
											+ "[" + ((Polygon)pair.getKey()).xpoints[3] +", " + ((Polygon)pair.getKey()).ypoints[3] + "], ";
			res += "\n";
			//iter.remove();
		}
		return res;
	}
}
