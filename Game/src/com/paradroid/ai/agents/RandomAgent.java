package com.paradroid.ai.agents;

import java.util.Random;

/**
 * @author dxf209
 *
 */
public class RandomAgent extends Agent{
	
	private Action prevAct = Action.STAY;
	private static int DELAY_DUR = 10;
	private int delay = 0;
	private boolean debug;
	
	private float voteMultiplier = 5.0f;
	
	/**
	 * Creates an agent that generates random moves
	 * 
	 * @param id		The ID of random agent
	 * @param debug		Debug flag that enables/disables the debug mode
	 */
	public RandomAgent(int id, boolean debug) {
		super(id);
		this.debug = debug;
	}
	
	@Override
	protected ActionVote calculateVote() {
		// Calculates vote based on heuristic
		return new ActionVote(decideAction(), voteMultiplier);
	}
	
	
	/**
	 * Decides the action the agent should take after random movement generation
	 * 
	 * @return	The randomly chosen action 
	 */
	protected Action decideAction() {
		
		if(delay > 0) {
			voteMultiplier = 5.0f;
			return prevAct;
		}
		
		Random rand = new Random();
		rand.setSeed(System.currentTimeMillis());
		
		int rval = rand.nextInt(5);
		voteMultiplier = rand.nextInt(60);
		
		switch(rval) {
		case 0:
			delay = DELAY_DUR;
			prevAct = Action.UP;
			if(debug) { System.out.println("GO UP"); }
			return Action.UP;
		case 1:
			delay = DELAY_DUR;
			prevAct = Action.DOWN;
			if(debug) { System.out.println("GO DOWN"); }
			return Action.DOWN;
		case 2:
			delay = DELAY_DUR;
			prevAct = Action.LEFT;
			if(debug) { System.out.println("GO LEFT"); }
			return Action.LEFT;
		case 3:
			delay = DELAY_DUR;
			prevAct = Action.RIGHT;
			if(debug) { System.out.println("GO RIGHT"); }
			return Action.RIGHT;
		case 4:
			delay = DELAY_DUR;
			prevAct = Action.STAY;
			if(debug) { System.out.println("STAY"); }
			return Action.STAY;
		default:
			delay = DELAY_DUR;
			prevAct = Action.STAY;
			if(debug) { System.out.println("STAY DF"); }
			return Action.STAY;
		}
	}
	
	
	/**
	 * Updates the agent delay
	 * 
	 */
	public void updateDelay() {
		delay --;
	}
	
	
	/**
	 * Sets the agent delay
	 * 
	 * @param delay		The delay value
	 */
	public void setDelay(int delay) {
		this.delay = delay;
	}
}
