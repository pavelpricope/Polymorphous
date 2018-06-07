package com.polymorphous.util.audioHandler;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


public class AudioManager  {

    /**
     * @author jxv603
     */

    private MediaPlayer musicPlayer;
    //The two classes that contains the sound effects and music
    public SFXAssets sfx = new SFXAssets();
    public SongAssets music = new SongAssets();
    private float sfxVolume = (float) 1.0;
    private float musicVolumeInClass= (float) 100.0;


    public AudioManager (){
        updateVolume(100,100);
    }

    /**
     * Method to update the volume from the user's settings
     * @param musicVolume
     * @param soundEffectVolume
     */
    public void updateVolume(float musicVolume, float soundEffectVolume) {
        sfxVolume = soundEffectVolume / (float) 100.0;
        musicVolumeInClass = musicVolume / (float) 100.0;
        if (musicPlayer != null) {musicPlayer.setVolume(musicVolumeInClass);}
    }

    public void updateVolumeMusic(float musicVolume) {
        musicVolumeInClass = musicVolume / (float) 100.0;
        if (musicPlayer != null) {musicPlayer.setVolume(musicVolume);}
    }
    public void updateVolumeFx(float soundEffectVolume) {
        sfxVolume = soundEffectVolume / (float) 100.0;
    }

    /**
     * Method to start playing a continuous chosen music resource
     * @param media
     * @param musicVolume
     */
    public void playMusic(Media media, float musicVolume) {
        musicPlayer = new MediaPlayer(media);
        musicPlayer.setVolume(musicVolume);
        musicPlayer.setOnEndOfMedia(() -> playMusic(media, musicVolumeInClass)); //////////////////////////////////////////////////
        musicPlayer.play();
    }

    /**
     * Method to stop playing any currently playing music
     */
    public void stopMusic() {
        if (musicPlayer != null) {musicPlayer.stop();}
    }

    /**
     * PLay sound effects
     * @param media
     */
    public void playSFX(AudioClip media) {
        media.setVolume(sfxVolume * sfxVolume);
        media.play();
    }





}

