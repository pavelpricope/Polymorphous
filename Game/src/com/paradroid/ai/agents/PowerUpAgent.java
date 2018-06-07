package com.paradroid.ai.agents;

import java.awt.Point;
import java.util.LinkedList;

import com.paradroid.ai.util.PathFinder;
import com.paradroid.main.Game;
import com.paradroid.main.Objects.Box;
import com.paradroid.main.Objects.GameObject;
import com.paradroid.main.PowerUp.PowerUpLoseLife;
import com.paradroid.util.mapGen.MapCode;
import com.paradroid.util.mapGen.Node;
import com.paradroid.util.mapGen.UniversalMap;

/**
 * @author dxf209
 *
 */
public class PowerUpAgent extends Agent {
	
	private LinkedList<GameObject> powerupList = new LinkedList<>();
	private final Bot bot;
	private float voteMultiplier;
	private boolean debug;
	private LinkedList<Node> pathToPowerUp;

	/**
	 * Creates a power up agent that determines the action of the agent after evaluating the power up locations
	 * 
	 * @param id			The ID of the power up agent
	 * @param powerUpList	A list of power ups
	 * @param bot			The bot
	 * @param debug			Debug flag that enables/disables the debug mode
	 */
	public PowerUpAgent(int id, LinkedList<GameObject> powerUpList, Bot bot, boolean debug) {
		super(id);
		this.powerupList = powerupList;
		this.bot = bot;
		this.debug = debug;
		pathToPowerUp = new LinkedList<>();
	}
	
	@Override
	protected ActionVote calculateVote() {
		// Calculates vote based on heuristic dependent on boxes
		return new ActionVote(decideAction(), voteMultiplier);
	}
	
	
	/**
	 * Determine the action to be taken based on the evaluation of power ups in the game
	 * 
	 * @return	The determined action based on evaluation
	 */
	protected Action decideAction() {
		
		if(powerupList.isEmpty()) {
			voteMultiplier = 0.0f;
			return Action.STAY;
		}
		
		GameObject closestPowerup = powerupList.getFirst();
		
		if(closestPowerup.getClass().equals(PowerUpLoseLife.class)) {
			voteMultiplier = 0.0f;
			return Action.STAY;
		} else {
			Point powerUpLoc = UniversalMap.convertScreenToLogic(new Point(closestPowerup.getX(), closestPowerup.getY()));
			Point botLoc = UniversalMap.convertScreenToLogic(new Point((int)bot.getX(), (int)bot.getY()));
			
			pathToPowerUp = PathFinder.pathFind(Game.gen_map.getItem(botLoc.x, botLoc.y), Game.gen_map.getItem(powerUpLoc.x, powerUpLoc.y), Game.gen_map);
			
			if(pathToPowerUp.size() < 5) {
				voteMultiplier = 30.0f/pathToPowerUp.size();
				
				Node nxtNode = NavigatorAgent.getNextNode(botLoc, pathToPowerUp);
				if(nxtNode.getTileType() != MapCode.WALL){
					Point moveTo = new Point (nxtNode.getPos().x-botLoc.x, nxtNode.getPos().y-botLoc.y);
					Point moveScreen = UniversalMap.convertLogicToScreen(nxtNode.getPos());
					
					Point internalMapBotPos = UniversalMap.convertLogicToScreen(botLoc);
					
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
		voteMultiplier = 0.0f;
		return Action.STAY;
	}
	
	
	/**
	 * Updates the positions of the power ups
	 * 
	 * @param powerupList	A linked list of new power up positions
	 */
	public void updatePowerUpPosition(LinkedList<GameObject> powerupList) {
		this.powerupList = powerupList;
	}

}
