package com.polymorphous.util.AchievementHandler;

import com.polymorphous.main.Game;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Reader {


    /**
     * @author jxv603
     */

    public static ArrayList<AchievementObj> listOfAchievements = new ArrayList<>();

    public Reader() {
    }

    public static void increaseBombAchievement() {
        listOfAchievements.get(0).increaseScore();
        listOfAchievements.get(1).increaseScore();
        listOfAchievements.get(2).increaseScore();
    }

    public static void increaseKillsAchievement() {
        listOfAchievements.get(3).increaseScore();
        listOfAchievements.get(4).increaseScore();
        listOfAchievements.get(5).increaseScore();
    }

    public static void increaseDeadCounterAchivement() {
        listOfAchievements.get(6).increaseScore();
        listOfAchievements.get(7).increaseScore();
    }

    public static void increasePowerUpAchivement() {
        listOfAchievements.get(8).increaseScore();
        listOfAchievements.get(9).increaseScore();
        listOfAchievements.get(10).increaseScore();
    }

    public static void increaseMultiPlayerAchivement() {
        listOfAchievements.get(11).increaseScore();
        listOfAchievements.get(12).increaseScore();
        listOfAchievements.get(13).increaseScore();
    }

    public static void increaseGameWonAchivement() {
        listOfAchievements.get(14).increaseScore();
        listOfAchievements.get(15).increaseScore();
    }

    /**
     * Read and populate an arrayList with all the achievements in the file
     */
    public static void restoreAchievements(String path) {
        listOfAchievements.clear();
        File text = new File(path);
        //Creating Scanner instnace to read File in Java
        Scanner scnr = null;
        try {
            scnr = new Scanner(text);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //Reading each line of file using Scanner class
        while (scnr.hasNextLine()) {
            String line = scnr.nextLine();
            String[] result = line.split(":");
            String[] secondPart = result[1].split("/");
            AchievementObj tmp = new AchievementObj(result[0], Integer.parseInt(secondPart[0]), Integer.parseInt(secondPart[1]));
            listOfAchievements.add(tmp);
        }
    }

    public static void backupAchievements(String path) {
        BufferedWriter output = null;
        try {
            File file = new File(path);
            output = new BufferedWriter(new FileWriter(file));
            for (AchievementObj achievementObj : listOfAchievements) {
                output.write(achievementObj.name + ":" + achievementObj.score + "/" + achievementObj.total + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
