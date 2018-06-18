package sound;

import java.io.File;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 *
 * @author Morris
 * There are two kinds of sounds:
 *      -the background music that is played in loop during the game,
 *      -the sound effect
 */
public class SoundEffect{
    private final String BACKGROUND_MUSIC = "Im So Ronery";
    private final MediaPlayer mediaPlayerBackGroundMusic;
    private MediaPlayer mediaPlayerSoundEffect;
    public SoundEffect(){        
        JFXPanel fxPanel = new JFXPanel();
        
        //Set up background music
        mediaPlayerBackGroundMusic = new MediaPlayer(new Media(new File("./build/classes/sound/"+BACKGROUND_MUSIC+".mp3").toURI().toString()));
        mediaPlayerBackGroundMusic.setVolume(0.8);
        mediaPlayerBackGroundMusic.play();
        mediaPlayerBackGroundMusic.setAutoPlay(true);    
        
    }
    
    public void play(String name){
        //Play sound effect
        mediaPlayerBackGroundMusic.setVolume(0.2);
        
        stopSoundEffect();
        mediaPlayerSoundEffect = new MediaPlayer(new Media(new File("./build/classes/sound/"+name+".mp3").toURI().toString()));
        mediaPlayerSoundEffect.play();
        mediaPlayerSoundEffect.setOnEndOfMedia(new Runnable() {
            //Play the background music on end of sound effect
            @Override public void run() {
                playBackgroundMusic();
            }
        });
        mediaPlayerSoundEffect.setOnStopped(new Runnable() {
            //Play the background music on end of sound effect
            @Override public void run() {
                playBackgroundMusic();
            }
        }); 
    }
    
    public void playBackgroundMusic() {
        //Play the background music
        mediaPlayerBackGroundMusic.setVolume(0.8);
        mediaPlayerBackGroundMusic.play();
    }
    
    public void stopSoundEffect() {
        try{
            //Stop previous sound effect if it is still playing
            mediaPlayerSoundEffect.stop();
        }catch(java.lang.NullPointerException e){
            System.out.println("No track is playing: ");
        }
    }
    
    public void stop() {
        try{
            //Stop every track
            mediaPlayerBackGroundMusic.stop();
            mediaPlayerSoundEffect.stop();            
        }catch(java.lang.NullPointerException e){
            System.out.println("No track is playing: ");
        }
    }
    
}
