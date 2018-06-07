package com.paradroid.util.AchievementHandler;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import javax.print.DocFlavor;

public class TestReader {

    private static final String PATH = "src/com/paradroid/util/AchievementHandler/achievements.txt";

    @Test
    public void restorePositive() {
        Reader.restoreAchievements(PATH);
        assertSame(Reader.listOfAchievements.size(), 15);
    }
    @Test
    public void restoreNegative() {
        Reader.restoreAchievements(PATH);
        assertSame(Reader.listOfAchievements.size()==5,false);
    }
    @Test
    public void backupPositive() {
        Reader.restoreAchievements(PATH);
        int previousValue = Reader.listOfAchievements.get(0).score;
        Reader.increaseBombAchievement();
        Reader.backupAchievements(PATH);
        Reader.restoreAchievements(PATH);
        int lastValue = Reader.listOfAchievements.get(0).score;
        assertSame(previousValue!=lastValue, true);
    }
    @Test
    public void backupNegative() {
        Reader.restoreAchievements(PATH);
        int previousValue = Reader.listOfAchievements.get(0).score;
        Reader.backupAchievements(PATH);
        Reader.restoreAchievements(PATH);
        int lastValue = Reader.listOfAchievements.get(0).score;
        assertSame(previousValue==lastValue, true);
    }
    @Test
    public void increaseBombTestFail() {
        Reader.restoreAchievements(PATH);
        int previousValue = Reader.listOfAchievements.get(0).score;
        Reader.increaseBombAchievement();
        int lastValue = Reader.listOfAchievements.get(0).score;
        assertSame(previousValue!=lastValue,true);
    }
    @Test
    public void increaseBombTestPositive() {
        Reader.restoreAchievements(PATH);
        int previousValue = Reader.listOfAchievements.get(0).score;
        Reader.increaseBombAchievement();
        int lastValue = Reader.listOfAchievements.get(0).score;
        assertSame(previousValue==lastValue,false);
    }

    @Test
    public void increaseKillsTestFail() {
        Reader.restoreAchievements(PATH);
        int previousValue = Reader.listOfAchievements.get(4).score;
        Reader.increaseKillsAchievement();
        int lastValue = Reader.listOfAchievements.get(4).score;
        assertSame(previousValue!=lastValue,true);
    }
    @Test
    public void increaseKillsTestPositive() {
        Reader.restoreAchievements(PATH);
        int previousValue = Reader.listOfAchievements.get(4).score;
        Reader.increaseKillsAchievement();
        int lastValue = Reader.listOfAchievements.get(4).score;
        assertSame(previousValue==lastValue,false);
    }
    @Test
    public void increaseDeadCounterTestFail() {
        Reader.restoreAchievements(PATH);
        int previousValue = Reader.listOfAchievements.get(6).score;
        Reader.increaseDeadCounterAchivement();
        int lastValue = Reader.listOfAchievements.get(6).score;
        assertSame(previousValue!=lastValue,true);
    }
}
