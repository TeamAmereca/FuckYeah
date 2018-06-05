package sound;

import java.io.File;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 *
 * @author Morris
 */
public class SoundEffect{
    private MediaPlayer mediaPlayer;
    public SoundEffect(){
        JFXPanel fxPanel = new JFXPanel();
    }
    
    public void play(String name){
        //Play sound effect
        stop();
        mediaPlayer = new MediaPlayer(new Media(new File("./build/classes/sound/"+name+".mp3").toURI().toString()));
        mediaPlayer.play();
    }
    
    public void stop(){
        //Stop sound effect
        try{
            mediaPlayer.stop();            
        }catch(java.lang.NullPointerException e){
            System.out.println(e);
        }
    }
}
