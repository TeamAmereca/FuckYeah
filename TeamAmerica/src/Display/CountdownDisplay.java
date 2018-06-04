package Display;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Morris
 */
public class CountdownDisplay extends JPanel{
    private int countdown;
    private JLabel countdownLabel;
    
    public CountdownDisplay(int countdown){
        this.countdown = countdown;
        this.countdownLabel = new JLabel();
        countdownLabel.setFont(new Font("SansSerif", Font.BOLD, 320));
        countdownLabel.setBackground(new Color(255,255,255,255));
        this.add(countdownLabel);
        countdownNumber();
        countdownSound();
    }
    
    public void countdownNumber(){
        //Display the coundown numbers
        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if(countdown--==0){
                    timer.cancel();
                    countdownLabel.setVisible(false);
                }else{
                    countdownLabel.setText(countdown+"");
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask,0, 1000);
    }
    
    public void countdownSound(){
        //Play countdown sound
        JFXPanel fxPanel = new JFXPanel();
        String bip = "build\\classes\\sound\\countdown.mp3";
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(new File(bip).toURI().toString()));
        mediaPlayer.play();
    }
}
