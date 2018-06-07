package com.paradroid.ai.agents;

import com.paradroid.main.Game;
import com.paradroid.main.Handlers.Handler;
import com.paradroid.main.Handlers.PlayersHandler;
import com.paradroid.main.Objects.Bomb;
import com.paradroid.main.Objects.Box;
import com.paradroid.main.Objects.GameObject;
import com.paradroid.main.Objects.ID;
import com.paradroid.main.Player.Player;
import com.paradroid.util.mapGen.GenMap;
import com.paradroid.util.mapGen.MapCode;
import com.paradroid.util.mapGen.UniversalMap;

import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.TreeMap;

/**
 * @author dxf209
 *
 */
public class Arbiter {

	private final int id;
	private LinkedList<Bomb> bombPositions = new LinkedList<>();
	private LinkedList<Box> boxPositions = new LinkedList<>();
	private LinkedList<GameObject> powerupList = new LinkedList<>();
	private Handler handler = Game.getHandler();
	private PlayersHandler pHandler = Game.getPlayersHandler();
	
	private Point pos = null;
	private int bombTime;

	private final PlayerAgent playerAgent;
	private final BombAgent bombAgent;
	private final BoxAgent boxAgent;
	private final RandomAgent randAgent;
	private final PowerUpAgent powerUpAgent;
	private final Bot bot;
	private boolean debug;
	private GenMap playMap;

	/**
	 * Creates an arbiter that handles all bot agents and decides on the actions of the AI
	 * 
	 * @param id			The integer ID of the bot
	 * @param pos			The Position of the bot
	 * @param bomb_time		The bomb cooldown of the bot
	 * @param bot			The Bot itself
	 * @param debug			Boolean flag true is debug mode
	 * @param actionList	The List of actions the bot has taken
	 */
	public Arbiter(int id, Point pos, int bombTime, Bot bot, boolean debug, LinkedList<Action> actionList) {
		this.id = id;
		this.bombTime = bombTime;
		this.pos = pos;
		this.bot = bot;
		this.debug = debug;
		playMap = Game.gen_map;
		
		// Initial Box population
		handler.object.forEach((GameObject g)-> {
			if (g.getId()==ID.Box) boxPositions.add((Box)g);
			else if (g.getId()==ID.Bomb) bombPositions.add((Bomb)g); 
			else if (g.getId()==ID.PowerUp) powerupList.add(g);});
	
		boxPositions.sort((b1,b2)->comparePos(new Point(b1.getX(), b1.getY()),new Point (b2.getX(), b2.getY())));
		
		// Initial Bomb population
//		handler.object.forEach((GameObject g)-> {if (g.getId()==ID.Bomb) {bombPositions.add((Bomb)g);}});
		bombPositions.sort((b1,b2)->distanceToBot(new Point(b1.getX(), b1.getY()),new Point(b2.getX(), b2.getY())));
		
		// Power-up positions
//		handler.object.forEach((GameObject g)-> {if (g.getId()==ID.PowerUp) {powerupList.add(g);}});
		powerupList.sort((b1,b2)->distanceToBot(new Point(b1.getX(), b1.getY()),new Point(b2.getX(), b2.getY())));
		
		bombAgent = new BombAgent(id, bombPositions, pos, bot, debug, playMap);
		boxAgent = new BoxAgent(id, boxPositions, pos, bombTime, bot, debug);
		playerAgent = new PlayerAgent(id, pHandler, pos, bot, debug);
		randAgent = new RandomAgent(id, debug);
		powerUpAgent = new PowerUpAgent(id, powerupList, bot, debug);

	}

	
	/**
	 * Populates the lists of positions of the game object that exist in the game
	 */
	private void populatePositions() {
		
		boxPositions = new LinkedList<>();
		bombPositions = new LinkedList<>();
		powerupList = new LinkedList<>();
		
		handler.object.forEach((GameObject g)-> {
			if (g.getId()==ID.Box) boxPositions.add((Box)g);
			else if (g.getId()==ID.Bomb) bombPositions.add((Bomb)g); 
			else if (g.getId()==ID.PowerUp) powerupList.add(g);});

		
//		bombPositions = new LinkedList<>();
//		handler.object.forEach((GameObject g)-> {if (g.getId()==ID.Bomb) {bombPositions.add((Bomb)g);}});
		bombPositions.sort((b1,b2)->distanceToBot(new Point(b1.getX(), b1.getY()),new Point(b2.getX(), b2.getY())));
		
//		boxPositions = new LinkedList<>();
//		handler.object.forEach((GameObject g)-> {if (g.getId()==ID.Box) {boxPositions.add((Box)g);}});
		boxPositions.sort((b1,b2)->comparePos(new Point(b1.getX(), b1.getY()),new Point (b2.getX(), b2.getY())));
		
		for(int y = 0; y < playMap.getHeight(); y++) {
			for(int x = 0; x < playMap.getWidth(); x++) {
				if(playMap.getItem(x, y).getTileType() == MapCode.BOX) {
					playMap.getItem(x, y).setTileType(MapCode.SPACE);
				}
			}
		}
		
		for(Box b : boxPositions) {
			Point p = UniversalMap.convertScreenToLogic(new Point (b.getX(), b.getY()));
			playMap.getItem(p.x, p.y).setTileType(MapCode.BOX);
		}
		
//		powerupList = new LinkedList<>();
//		handler.object.forEach((GameObject g)-> {if (g.getId()==ID.PowerUp) {powerupList.add(g);}});
		powerupList.sort((b1,b2)->distanceToBot(new Point(b1.getX(), b1.getY()),new Point(b2.getX(), b2.getY())));
	}

	
	/**
	 * Updates positions and gets the highest reward action from the list
	 * 
	 * @param pos			The position of the bot
	 * @param bomb_time		The bomb cooldown time
	 * @return				The Action decided by the agent
	 */
	public Action action(Point pos, int bomb_time) {
		// Removes debug tiles from the previous frame (seems to break things if removed!)
		if(debug) {
			Iterator<GameObject> goIter = handler.object.iterator();
			while(goIter.hasNext()) {
				GameObject nextGO = goIter.next(); 
				if(nextGO.getId() == ID.AIInternals) {
					goIter.remove();
				}
			}
		}
		// Carries out actions: movement, placing bombs etc.
		// This will later be based on the recommendations of the individual agents
		populatePositions();
		
		pos = new Point(pos.x-20, pos.y-20);
		
		bombAgent.updateBombPositions(bombPositions);
		bombAgent.updateBotPosition(pos);
		
		boxAgent.updateBoxPosition(boxPositions);
		boxAgent.updateBotPos(pos);
		boxAgent.updateBombTime(bomb_time);
		
		randAgent.updateDelay();

		playerAgent.updateBotPos(pos);
		
		ActionVote playerVote = playerAgent.calculateVote();
		ActionVote bombVote = bombAgent.calculateVote();
		ActionVote boxVote = boxAgent.calculateVote();
		ActionVote randVote = randAgent.calculateVote();
		ActionVote powerUpVote = powerUpAgent.calculateVote();
		if(debug) {
			System.out.printf("PV: %f, BV: %f, BxV: %f RV: %f PU: %f\n", playerVote.getVote(), bombVote.getVote(), boxVote.getVote(), randVote.getVote(), powerUpVote.getVote());
		}
		
		// Have sorting system for votes then take the head of the priority queue
		PriorityQueue<ActionVote> actionQueue = new PriorityQueue<>((v1,v2)->v1.compareVotes(v2));
		actionQueue.add(playerVote);
		actionQueue.add(bombVote);
		actionQueue.add(boxVote);
		actionQueue.add(randVote);
		actionQueue.add(powerUpVote);
		
		// gets the top rated Action vote from the sorted list
		ActionVote nextAct = actionQueue.poll();
		return nextAct.getAction();
	}

	
	
	/**
	 * Calculates the distance from two points to the bot for comparison between P1 and P2
	 * 
	 * @param p1	The first point
	 * @param p2	The second point
	 * @return		1 if the distance between p1 and the bot is larger than p2 and the bot, if the distance between the bot and p2 is larger -1 is returned otherwise 0
	 */
	public int distanceToBot(Point p1, Point p2) {
		Point botPos = this.pos;
		double d1 = Math.sqrt(Math.pow(botPos.x-p1.x, 2)+Math.pow(botPos.y-p1.y, 2));
		double d2 = Math.sqrt(Math.pow(botPos.x-p2.x, 2)+Math.pow(botPos.y-p2.y, 2));
		
		if(d1 > d2) return 1;
		if(d1 < d2) return -1;
		return 0;
	}
	
	
	/**
	 * Compare the distance between the pos variable and points P1 and P2
	 * 
	 * @param p1	The first comparison point
	 * @param p2	The second comparison point
	 * @return		1 if p1Dist >  p2Dist, -1 if p1Dist < p2Dist, 0 otherwise
	 */
	public int comparePos(Point p1, Point p2) {
		double p1Dist = Math.sqrt(Math.pow(p1.x-pos.x, 2)+Math.pow(p1.y-pos.y, 2));
		double p2Dist = Math.sqrt(Math.pow(p2.x-pos.x, 2)+Math.pow(p2.y-pos.y, 2));
		
		if(p1Dist < p2Dist) {
			return 1;
		}else if(p1Dist > p2Dist) {
			return -1;
		}
		return 0;
	}
	
	

	/**
	 * Updates the position of the bot
	 * 
	 * @param point 	The updated position
	 */
	public void updateBotPos(Point point) {
		pos = point;
	}


	/**
	 * Updates the bombTime variable
	 * 
	 * @param bombTime	The time until another bomb can be dropped
	 */
	public void updateBombTime(int bombTime) {
		this.bombTime = bombTime;
	}
}
