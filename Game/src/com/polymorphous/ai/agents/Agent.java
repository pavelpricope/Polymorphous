package com.polymorphous.ai.agents;

/**
 * @author dxf209
 *
 */
abstract class Agent {
    private final int id;
    private float weight;
    private float vote;

    /**
     * Constructor that takes the integer id and sets it as a parameter
     * @param id	The integer id
     */
    public Agent(int id) {
        this.id = id;
    }

    
    /**
     * Calculates the action vote, which is overridden in sub-classes for specific behaviour 
     * @return	The ActionVote object calculated in the method
     */
    protected ActionVote calculateVote() {
		return null;
	}


    /**
     * Set the vote weight of the Agent
     * @param weight	The new weight value to be set
     */
    public void setWeight(float weight) {
        this.weight = weight;
    }

    /**
     * Gets the vote of the agent
     * @return	the agent vote
     */
    public float getVote() {
        return vote;
    }

}
