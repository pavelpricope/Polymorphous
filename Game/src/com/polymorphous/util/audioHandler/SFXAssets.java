package com.polymorphous.util.audioHandler;

import javafx.scene.media.AudioClip;
import java.io.File;
import java.util.Random;

public class SFXAssets {
    /**
     * @author jxv603
     */

    private String pathIngame="Game//src//com//polymorphous//resources//audio//game-audio//ingame//";
    private String pathMenu="Game//src//com//polymorphous//resources//audio//game-audio//menu//";
    public AudioClip explosion = new AudioClip(new File(pathIngame+"exp1.wav").toURI().toString());
    public AudioClip explosion2 = new AudioClip(new File(pathIngame+"exp2.wav").toURI().toString());



    public AudioClip life = new AudioClip(new File(pathIngame+"life.wav").toURI().toString());
    public AudioClip bombSize = new AudioClip(new File(pathIngame+"bombSize.wav").toURI().toString());
    public AudioClip run = new AudioClip(new File(pathIngame+"run2.mp3").toURI().toString());
    public AudioClip freeze = new AudioClip(new File(pathIngame+"freeze.wav").toURI().toString());
    public AudioClip randomic = new AudioClip(new File(pathIngame+"random.wav").toURI().toString());


    public AudioClip laugh = new AudioClip(new File(pathIngame+"shortLaugh.wav").toURI().toString());
    public AudioClip death = new AudioClip(new File(pathIngame+"endLaugh.wav").toURI().toString());


    //Menu
    public AudioClip click = new AudioClip(new File(pathMenu+"click.wav").toURI().toString());

    //If we want to have different tone for the explosion we can have a explosion array
    public AudioClip[] explosionList = {explosion,explosion2};


    /**
     * Get a random explosion effect from the array above
     * @return
     */
    public AudioClip getRandomExplosionSound() {
        Random r = new Random();
        int i = r.nextInt(explosionList.length);
        return explosionList[i];
    }
}