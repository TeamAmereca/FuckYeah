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
    private static String countdown = "countdown";
    private static String playerAmerica = "player America";
    private static String playerTerrorist = "player Terrorist";
    private static String rife1 = "rife1";
    private static String rife2 = "rife2";
    
    public SoundEffect(){
        JFXPanel fxPanel = new JFXPanel();
    }
    
    public void play(String name){
        //Play sound effect
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(new File("./build/classes/sound/"+name+".mp3").toURI().toString()));
        mediaPlayer.play();
    }       
}
