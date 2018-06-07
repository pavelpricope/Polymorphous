package com.paradroid.util.AchievementHandler;

public class AchievementObj {

    /**
     * @author jxv603
     */
    public String name;
    public int score, total;

    AchievementObj(String name, int score, int total) {
        this.name = name;
        this.score = score;
        this.total = total;
    }

    public void increaseScore() {
        score++;
    }

}
