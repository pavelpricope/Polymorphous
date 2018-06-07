package com.polymorphous.ai.agents;

import java.awt.Point;
import java.util.LinkedList;

import com.polymorphous.ai.util.Internals;
import com.polymorphous.ai.util.PathFinder;
import com.polymorphous.main.Game;
import com.polymorphous.main.Objects.Bomb;
import com.polymorphous.main.Objects.Box;
import com.polymorphous.main.Objects.Explosion;
import com.polymorphous.main.Objects.ID;
import com.polymorphous.util.mapGen.MapCode;
import com.polymorphous.util.mapGen.Node;
import com.polymorphous.util.mapGen.UniversalMap;
import com.polymorphous.util.physics.Collision;

import javafx.scene.paint.Color;

/**
 * @author dxf209
 *
 */
public class BoxAgent extends Agent {

	private Point botPos;
	private LinkedList<Box>boxes;
	private float voteMultiplier = 0.5f;
	private int bombTime;
	private final Bot bot;
	private boolean debug;
	private LinkedList<Node> pathToBox;
	
	
	/**
	 * Creates an agent that evaluates the boxes on the map
	 * 
	 * @param id		The agent ID
	 * @param boxes		A linked list of boxes 
	 * @param botPos	The position of the bot
	 * @param bombTime	The bomb cool down time
	 * @param bot		The bot
	 * @param debug		debug boolean that activate or deactivates the debug mode
	 */
	public BoxAgent(int id, LinkedList<Box>boxes, Point botPos, int bombTime, Bot bot, boolean debug) {
		super(id);
		this.botPos = botPos;
		this.boxes = boxes;
		this.bombTime = bombTime;
		this.bot = bot;
		this.debug = debug;
		pathToBox = new LinkedList<>();
		
	}
	
	@Override
	protected ActionVote calculateVote() {
		// Calculates vote based on heuristic dependent on boxes
		return new ActionVote(decideAction(), voteMultiplier);
	}
	
	
	/**
	 * Returns the decided action by evaluating the box positions on the map
	 * 
	 * @return	The evaluated action
	 */
	protected Action decideAction() {
		
		if(boxes.isEmpty() || bombTime > 0) {
			voteMultiplier = 0.0f;
			return Action.STAY;
		} else {
			Box closestBox = boxes.getLast();

			Point closestBoxPos = new Point(closestBox.getX(), closestBox.getY());
			Point closestLocalisedBox = UniversalMap.convertScreenToLogic(closestBoxPos);
			
			if(debug) {
				Game.getHandler().addObject(new Internals(closestBoxPos.x, closestBoxPos.y, ID.AIInternals, Color.CADETBLUE));
			}

			Point localisedBotPos = UniversalMap.convertScreenToLogic(botPos);

			for(Box b : boxes) {
				if(bombTime <= 0 && distToBox(b) <= 50.0d && isInBombRange(b)){
//				if(bombTime <= 0 && isInBombRange(b)){
					if(debug) { System.out.println("Bombs away!"); }
					voteMultiplier = 50.0f; // FIXME: Was 15.0f
					return Action.DROP_BOMB;
				}
			}
			
			pathToBox = PathFinder.pathFind(Game.gen_map.getItem(localisedBotPos.x, localisedBotPos.y), Game.gen_map.getItem(closestLocalisedBox.x, closestLocalisedBox.y), Game.gen_map);
			Node nxtNode = NavigatorAgent.getNextNode(localisedBotPos, pathToBox);
			if(nxtNode.getTileType() != MapCode.WALL){
				Point moveTo = new Point (nxtNode.getPos().x-localisedBotPos.x, nxtNode.getPos().y-localisedBotPos.y);
				
				voteMultiplier = 10.0f;
				
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
		voteMultiplier = 0.0f;
		return Action.STAY;
	}
	
	
	/**
	 * Updates the box positions 
	 * 
	 * @param boxes	A linked list of boxes
	 */
	public void updateBoxPosition(LinkedList<Box> boxes) {
		this.boxes = boxes;
	}
	
	
	/**
	 * Updates the bomb cool down variable
	 * 
	 * @param bombTime	The bomb time passed to the agent
	 */
	public void updateBombTime(int bombTime) {
		this.bombTime = bombTime;
	}
	
	
	/**
	 * Determines if a box is in the range of the bomb
	 * 
	 * @param closestBox	The closest box to the player
	 * @return				A boolean telling us if the closest box is in range of the player
	 */
	private boolean isInBombRange(Box closestBox) {		
		Bomb b = new Bomb((int)bot.getX(), (int)bot.getY(), null, null, bot, 0, bot.getBombSize(),true);
		
		Explosion[] exp = b.getExplosion();
		
		for(int i = 0; i < exp.length; i++) {
			if(Collision.checkPolygonsCollision(closestBox.getBounds(), exp[i].getBounds())){
				return true;
			}
		}
		return false;
	}
	
	private double distToBox(Box b) {
		return Math.abs((bot.getX()-b.getX()) + (bot.getY()-b.getY()));
	}
	
	
	/**
	 * Update the position of the bot
	 * 
	 * @param pos	The new position of the bot
	 */
	public void updateBotPos(Point pos) {
		this.botPos = pos;
	}

}
