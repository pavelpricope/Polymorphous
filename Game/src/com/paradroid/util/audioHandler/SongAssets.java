package com.paradroid.util.audioHandler;

import javafx.scene.media.Media;
import java.io.File;
import java.util.Random;

public class SongAssets {
    /**
     * @author jxv603
     */

    private String path="Game//src//com//paradroid//resources//audio//game-audio//songs//";
    public Media gotTrack1 = new Media(new File(path+"GoTIntroGameofThrones8bit.wav").toURI().toString());
    public Media gotTrack2 = new Media(new File(path+"GotRainsOfCastamere8Bit.wav").toURI().toString());
    public Media general1 = new Media(new File(path + "Terraria_Soundtrack-Boss8bit.wav").toURI().toString());
    public Media general2 = new Media(new File(path + "Terraria-Underground8bit.wav").toURI().toString());
    public Media western1 = new Media(new File(path + "Western8bit.wav").toURI().toString());
    public Media western2 = new Media(new File(path + "WesternFastestGuninthe_8bit.wav").toURI().toString());

    //Possible tracks to play
    public Media gotTracks[] = {gotTrack1,gotTrack2};
    public Media generalTracks[] = {general1,general2};
    public Media westerTracks[] = {western1,western2};
    public Media allTracks[] = {gotTrack1,gotTrack2,general1,general2,western1,western2};

    /**
     * Get some random track to play
     * @return
     */
    public Media getRandomTrack() {
        Random r = new Random();
        int i = r.nextInt(allTracks.length);
        return allTracks[i];
    }

    public Media getRandomGeneralTrack(){
        Random r = new Random();
        int i = r.nextInt(generalTracks.length);
        return generalTracks[i];
    }
    public Media getRandomWesternTrack(){
        Random r = new Random();
        int i = r.nextInt(westerTracks.length);
        return westerTracks[i];
    }
    public Media getRandomGoTTrack(){
        Random r = new Random();
        int i = r.nextInt(gotTracks.length);
        return gotTracks[i];
    }

}