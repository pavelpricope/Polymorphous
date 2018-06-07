package com.polymorphous.ai.agents;

import com.polymorphous.ai.util.Internals;
import com.polymorphous.ai.util.PathFinder;
import com.polymorphous.main.Game;
import com.polymorphous.main.Handlers.PlayersHandler;
import com.polymorphous.main.Objects.Bomb;
import com.polymorphous.main.Objects.Explosion;
import com.polymorphous.main.Objects.ID;
import com.polymorphous.main.Player.Player;
import com.polymorphous.util.mapGen.MapCode;
import com.polymorphous.util.mapGen.Node;
import com.polymorphous.util.mapGen.UniversalMap;
import com.polymorphous.util.physics.Collision;

import javafx.scene.paint.Color;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;


/**
 * @author dxf209
 *
 */
public class PlayerAgent extends Agent {

	private int DELAY = 10;
	private int delay = 0;
	private LinkedList<Node> pathToPlayer = new LinkedList<>();
	private PlayersHandler pHandler;
	private Point botPos;
	private boolean debug;
	private float voteMultiplier = 1.0f;
	private Action prevAct;
	private final Bot bot;

	/**
	 * Creates a player agent that evaluates the player positions in the game to determine actions
	 * 
	 * @param id		The ID of the player agent
	 * @param pHandler	The player handler object that contains all the players
	 * @param botPos	The position of the bot
	 * @param bot		The bot
	 * @param debug		A debug flag that turn on/off debug mode
	 */
	public PlayerAgent(int id, PlayersHandler pHandler, Point botPos, Bot bot, boolean debug) {
		super(id);
		this.pHandler = pHandler;
		this.botPos = botPos;
		this.bot = bot;
		this.debug = debug;
	}
	
	@ Override
	protected ActionVote calculateVote() {
		// Calculates vote based on heuristic dependent on player position
		return new ActionVote(decideAction(), voteMultiplier);
	}

	/**
	 * Decide the action based on player positions
	 * 
	 * @return	The decided action based on player evaluation
	 */
	protected Action decideAction() {
		Player player = pHandler.players.peek();
		Point playerPos = new Point((int)pHandler.players.peek().getX(), (int)pHandler.players.peek().getY());
		
		Point localisedPlayerPos = UniversalMap.convertScreenToLogic(playerPos);
		Point localisedBotPos = UniversalMap.convertScreenToLogic(new Point((int)bot.getX(), (int)bot.getY()));

		if(debug) {
			if(localisedPlayerPos == null) {
				System.out.println("Local Player Pos is NULL");
			}
		}
		
		delay = 0;
		
		pathToPlayer = PathFinder.pathFind(Game.map.getMap().getItem(localisedBotPos.x, localisedBotPos.y), Game.map.getMap().getItem(localisedPlayerPos.x, localisedPlayerPos.y), Game.map.getMap());
		
		if(debug && (pathToPlayer == null || pathToPlayer.size() <= 1)) { System.out.println("No path"); }
		
		voteMultiplier = 40.0f;
				
		if (pathToPlayer.size() > 25 || pathToPlayer.size() <= 1 || pathToPlayer == null) {
			voteMultiplier = 0.0f;
			return Action.STAY;
		}

		try {
			
			if(debug) { 
				for (Node n : pathToPlayer) {
					System.out.printf("(%d,%d)->", n.getPos().x, n.getPos().y);
					Point convPoint = UniversalMap.convertLogicToScreen(n.getPos());
					Game.getHandler().addObject(new Internals(botPos.x, botPos.y, ID.AIInternals, Color.ALICEBLUE));
					Game.getHandler().addObject(new Internals(convPoint.x, convPoint.y, ID.AIInternals, Color.BLUEVIOLET));
				}
				System.out.printf("\n");
			}
			
			Node nxtNode = NavigatorAgent.getNextNode(localisedBotPos, pathToPlayer);
			if(nxtNode.getTileType() != MapCode.WALL){
				Point moveTo = new Point (nxtNode.getPos().x-localisedBotPos.x, nxtNode.getPos().y-localisedBotPos.y);
				Point moveScreen = UniversalMap.convertLogicToScreen(nxtNode.getPos());
				Point internalMapBotPos = UniversalMap.convertLogicToScreen(localisedBotPos);
				if(debug) {
					Game.getHandler().addObject(new Internals(internalMapBotPos.x-=20, internalMapBotPos.y-=20, ID.AIInternals, Color.DEEPPINK)); // BLUE
					Game.getHandler().addObject(new Internals(moveScreen.x, moveScreen.y, ID.AIInternals, Color.BLUE)); // BLUE
					Game.getHandler().addObject(new Internals(playerPos.x-=20, playerPos.y-=20, ID.AIInternals, Color.BROWN)); // BLUE
				}
				
				if(isInBombRange(player)) {
					return Action.DROP_BOMB;
				}
				
				if(moveTo.x < 0) {
					prevAct = Action.LEFT;
					return Action.LEFT;
				}else if(moveTo.x > 0) {
					prevAct = Action.RIGHT;
					return Action.RIGHT;
				}else if(moveTo.y < 0) {
					prevAct = Action.UP;
					return Action.UP;
				}else if(moveTo.y > 0) {
					prevAct = Action.DOWN;
					return Action.DOWN;
				}else {
					prevAct = Action.STAY;
					return Action.STAY;
				}
			} else {
				if(debug) {
					System.out.println("Wall in the way!");
				}
				
				Random r = new Random();
				int move = r.nextInt(4);
				
				switch(move){
				case 0:
					prevAct = Action.UP;
					return Action.UP;
				case 1:
					prevAct = Action.DOWN;
					return Action.DOWN;
				case 2:
					prevAct = Action.LEFT;
					return Action.LEFT;
				case 3:
					prevAct = Action.RIGHT;
					return Action.RIGHT;
				default:
					prevAct = Action.UP;
					return Action.UP;
				}
			}	
        } catch (Exception e) {
            System.err.println("Peek Exception!");
            e.printStackTrace();
            return Action.STAY;
        }
    }

    
	/**
	 * Update the bot position
	 * 
	 * @param pos	The new position of the bot
	 */
	public void updateBotPos(Point pos) {
        this.botPos = pos;
    }
    
    
	/**
	 * Determine if a player is in bomb range
	 * 
	 * @param player	The closest player object
	 * @return			Boolean telling us if the player is in range of a bomb
	 */
	private boolean isInBombRange(Player player) {					
		Bomb b = new Bomb((int)bot.getX(), (int)bot.getY(), null, null, bot, 0, bot.getBombSize(), true);
		
		Explosion[] exp = b.getExplosion();
		
		for(int i = 0; i < exp.length; i++) {
			if(Collision.checkPolygonsCollision(player.getBounds(), exp[i].getBounds())){
				return true;
			}
		}
		return false;
    }

}
