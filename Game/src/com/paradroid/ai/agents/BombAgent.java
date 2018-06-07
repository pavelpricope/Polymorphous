package com.paradroid.ai.agents;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Random;

import com.paradroid.ai.util.Internals;
import com.paradroid.ai.util.PathFinder;
import com.paradroid.main.Game;
import com.paradroid.main.Objects.Bomb;
import com.paradroid.main.Objects.Explosion;
import com.paradroid.main.Objects.GameObject;
import com.paradroid.main.Objects.ID;
import com.paradroid.util.mapGen.GenMap;
import com.paradroid.util.mapGen.MapCode;
import com.paradroid.util.mapGen.Node;
import com.paradroid.util.mapGen.UniversalMap;
import com.paradroid.util.physics.Collision;

import javafx.scene.paint.Color;

/**
 * @author dxf209
 *
 */
public class BombAgent extends Agent {

	private LinkedList<Bomb> bombs;
	private Point botPos;
	private float voteMultiplier = 0.5f;
	private final Bot bot;
	private boolean debug;
	private final static int DELAY = 20;
	private int del = 0;
	private Action prevMove;
	private LinkedList<Node> avoidancePath;
	private GenMap logicBoxMap;
	
	/**
	 * Creates a bomb agent that is used to evaluate the bomb positions on the map
	 * 
	 * @param id			The agent ID		
	 * @param bombs			Linked List of Bombs 
	 * @param botPos		The position of the bot
	 * @param bot			The bot
	 * @param debug			Boolean flag for turning on debug mode (true) or off (false)
	 * @param logicBoxMap	Logical map containing box positions
	 */
	public BombAgent(int id, LinkedList<Bomb> bombs, Point botPos, Bot bot, boolean debug, GenMap logicBoxMap) {
		super(id);
		this.botPos = botPos;
		this.bombs = bombs;
		this.bot = bot;
		this.debug = debug;
		avoidancePath = new LinkedList<>();
		this.logicBoxMap = logicBoxMap;
	}
	
	@Override
	protected ActionVote calculateVote() {
		// Vote calculated based on bomb location heuristic
		/* Essentially, if a bomb is close enough to the agent, to harm, then
		 * the vote count will be high, the bomb proximity will alter the weight given to
		 * the vote and passed back to the arbiter. */
		return new ActionVote(decideAction(), voteMultiplier);
	}
	
	
	/**
	 * Evaluates the Bombs on the map to generate the most appropriate action
	 * 
	 * @return	The evaluated action  
	 */
	protected Action decideAction() {
		
		// If there are no bombs on the map, the agent has no action to vote for
		if(bombs.isEmpty()) {
			voteMultiplier = 0.0f;
			return Action.STAY;
		} else {
			
			Bomb closestBomb = bombs.peek();
			Point closestBombPos = new Point(closestBomb.getX(), closestBomb.getY());
			
			Point localisedClosestBomb = UniversalMap.convertScreenToLogic(closestBombPos);
			Point localisedBotPos = UniversalMap.convertScreenToLogic(botPos);
			
			if(debug) {
				Game.getHandler().addObject(new Internals(closestBombPos.x, closestBombPos.y, ID.AIInternals, Color.RED));
			}
			
			double sld = Double.MAX_VALUE;
			
			if(localisedClosestBomb != null){
				sld = Math.sqrt(Math.pow((localisedBotPos.x-localisedClosestBomb.x), 2.0d)+Math.pow((localisedBotPos.y-localisedClosestBomb.y), 2.0d));
			}
			
			if(sld > 5.0f) {
				voteMultiplier = 0.0f;
				return Action.STAY;
			}
			
			Explosion[] exp = closestBomb.getExplosion();
			
			boolean danger = false;
			
			for(int i = 0; i < exp.length; i++) {
				if(Collision.checkPolygonsCollision(bot.getBounds(), exp[i].getBounds())){
					danger = true;
					break;
				}
			}
			
			if(!danger) {
				voteMultiplier = 80.0f;
				return Action.STAY;
			}
			
			if (debug) { 
				System.out.println("SLD: " + sld);
			}
			
			if(sld < 5.0d) {
				if(debug){
					System.out.println("Bomb is close!");
				}
				voteMultiplier = 80.0f; // Was 20.0f
				Node safeZone = null;

				if(checkTraversable(new Point(localisedClosestBomb.x, localisedClosestBomb.y), new Point (localisedClosestBomb.x+1, localisedClosestBomb.y+1))) {
					safeZone = logicBoxMap.getItem(localisedClosestBomb.x+1, localisedClosestBomb.y+1); 
				} else if (checkTraversable(new Point(localisedClosestBomb.x, localisedClosestBomb.y), new Point (localisedClosestBomb.x+1, localisedClosestBomb.y-1))) {
					safeZone = logicBoxMap.getItem(localisedClosestBomb.x+1, localisedClosestBomb.y-1);
				} else if (checkTraversable(new Point(localisedClosestBomb.x, localisedClosestBomb.y), new Point (localisedClosestBomb.x-1, localisedClosestBomb.y+1))) {
					safeZone = logicBoxMap.getItem(localisedClosestBomb.x-1, localisedClosestBomb.y+1);
				} else if (checkTraversable(new Point(localisedClosestBomb.x, localisedClosestBomb.y), new Point (localisedClosestBomb.x-1, localisedClosestBomb.y-1))) {
					safeZone = logicBoxMap.getItem(localisedClosestBomb.x-1, localisedClosestBomb.y-1);
				} else {
					for(int y = 5; y < 10; y++) {
						if(localisedClosestBomb.y+y > Game.gen_map.getHeight() || localisedClosestBomb.y-y < 0) {
							break;
						}
//						System.out.println("Fall through"); // REM
						for(int x = 5; x < 10; x++) {
							if(localisedClosestBomb.x+x > Game.gen_map.getWidth() || localisedClosestBomb.x-x < 0) {
								break;
							}
							if(checkTraversable(new Point(localisedClosestBomb.x, localisedClosestBomb.y), new Point (localisedClosestBomb.x+x, localisedClosestBomb.y))) {
								safeZone = Game.map.getMap().getItem(localisedClosestBomb.x+x, localisedClosestBomb.y);
								break;
							} else if (checkTraversable(new Point(localisedClosestBomb.x, localisedClosestBomb.y), new Point (localisedClosestBomb.x, localisedClosestBomb.y+y))) {
								safeZone = Game.map.getMap().getItem(localisedClosestBomb.x, localisedClosestBomb.y+y);
								break;
							}  else if (checkTraversable(new Point(localisedClosestBomb.x, localisedClosestBomb.y), new Point (localisedClosestBomb.x-x, localisedClosestBomb.y))) {
								safeZone = Game.map.getMap().getItem(localisedClosestBomb.x-x, localisedClosestBomb.y);
								break;
							}  else if (checkTraversable(new Point(localisedClosestBomb.x, localisedClosestBomb.y), new Point (localisedClosestBomb.x, localisedClosestBomb.y-y))) {
								safeZone = Game.map.getMap().getItem(localisedClosestBomb.x, localisedClosestBomb.y-y);
								break;
							} else {
//								System.out.println("Fall further");
								Point refuge = UniversalMap.convertScreenToLogic(new Point ((int)Game.map.getSpawnPos().get(3).getX(), (int)Game.map.getSpawnPos().get(3).getY()));
								safeZone = Game.map.getMap().getItem(refuge.x, refuge.y);
							}
						}
					}
					
					if(safeZone == null) {
						Point refuge = UniversalMap.convertScreenToLogic(new Point ((int)Game.map.getSpawnPos().get(3).getX(), (int)Game.map.getSpawnPos().get(3).getY()));
						safeZone = Game.map.getMap().getItem(refuge.x, refuge.y);
					}
////					if(debug) { System.out.println("Pathing to spawn!"); }
//					
//					System.out.printf("Refuge: %d, %d\n", refuge.x, refuge.y);
				}
					
//				if(Game.map.getMap().getItem(localisedBotPos.x, localisedBotPos.y) == null) {// REM
//					System.out.println("NULL local bot pos");
//				}
//				if(safeZone == null) {// REM
//					System.out.println("NULL safe zone");
//				}
				avoidancePath = PathFinder.pathFind(Game.map.getMap().getItem(localisedBotPos.x, localisedBotPos.y), safeZone, logicBoxMap);
//				System.out.printf("Avoidance path length: %d\n", avoidancePath.size());
				
				for(Node n : avoidancePath) {
					if(n.getTileType() == MapCode.BOX) {
						voteMultiplier = 80.0f;
						Random r = new Random();
						int val = r.nextInt(4);
						switch(val) {
						case 0:
							return Action.UP;
						case 1:
							return Action.DOWN;
						case 2:
							return Action.LEFT;
						case 3:
							return Action.RIGHT;
						}
					}
				}
				
				Node nxtNode = NavigatorAgent.getNextNode(localisedBotPos, avoidancePath);
				if(nxtNode.getTileType() != MapCode.WALL){
					Point moveTo = new Point (nxtNode.getPos().x-localisedBotPos.x, nxtNode.getPos().y-localisedBotPos.y);
					
					if(moveTo.x < 0) {
						return Action.LEFT;
					}else if(moveTo.x > 0) {
						return Action.RIGHT;
					}else if(moveTo.y < 0) {
						return Action.UP;
					}else if(moveTo.y > 0) {
						return Action.DOWN;
					}else {
						return Action.STAY;
					}
				}
			}
		}
		return null;
	}
		
	
	/**
	 * Updates the bot position
	 * 
	 * @param botPos	The bot position
	 */
	public void updateBotPosition(Point botPos) {
		this.botPos = botPos;
	}
	
	
	/**
	 * Updates the bomb list
	 * 
	 * @param bombs		A linked list of currently active bombs
	 */
	public void updateBombPositions(LinkedList<Bomb> bombs) {
		this.bombs = bombs;
	}
	
	
	/**
	 * Updates the logical map with boxes
	 * 
	 * @param logicBoxMap	The GenMap to update to
	 */
	public void updateBoxMap(GenMap logicBoxMap) {
		this.logicBoxMap = logicBoxMap;
	}
	
	
	/**
	 * Checks if a path from point s to point e is traversable (does not contain walls or boxes)
	 * 
	 * @param s		The start node
	 * @param e		The end node
	 * @return		Boolean that states if a path is traversable or not
	 */
	private boolean checkTraversable(Point s, Point e) {
		Node start = logicBoxMap.getItem(s.x, s.y);
		Node end = logicBoxMap.getItem(e.x, e.y);
		if(end == null || end.getTileType() == MapCode.WALL || start.getTileType() == MapCode.BOX) {return false;}
		LinkedList<Node> path = PathFinder.pathFind(start, end, logicBoxMap);
		
		for(Node n : path) {
			if(n.getTileType() == MapCode.BOX) {
				return false;
			}
		}
		return true;
	}

}
