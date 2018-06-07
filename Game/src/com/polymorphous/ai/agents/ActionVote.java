package com.polymorphous.ai.agents;

/**
 * @author dxf209
 *
 */
public class ActionVote {

	private Action action;
	private float vote;
	
	/**
	 * A class that stores the Bot action and vote value, so it
	 * can be passed between classes.
	 * 
	 * @param action	The action the bot will take
	 * @param vote		The vote value associated with the action 
	 */
	public ActionVote(Action action, float vote) {
		this.action = action;
		this.vote = vote;
	}
	
	/**
	 * @return	returns the action from the class
	 */
	public Action getAction() {
		return action;
	}
	
	/**
	 * @return	returns the vote value from the class
	 */
	public float getVote() {
		return vote;
	}
	
	
	/**
	 * Compares this vote with a passed parameter ActionVote (used for lambda expression)
	 * @param v2	The ActionVote to compare against
	 * @return		An integer value determining which ActionVote has a larger vote value
	 */
	public int compareVotes(ActionVote v2) {
		if(vote > v2.getVote())
			return -1;
		else if (vote < v2.getVote())
			return 1;
		return 0;
	}
	
}
