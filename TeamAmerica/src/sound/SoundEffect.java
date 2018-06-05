package sound;

import java.io.File;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 *
 * @author Morris
 */
public class SoundEffect {
    private MediaPlayer mediaPlayer;
    
    public SoundEffect(){
        JFXPanel fxPanel = new JFXPanel();
    }
    public void countdownSound(){
        //Play countdown sound
        String bip = "build\\classes\\sound\\countdown.mp3";
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(new File(bip).toURI().toString()));
        mediaPlayer.play();
    }
}
