package Display;

import java.awt.Color;
import java.awt.Font;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JLabel;
import javax.swing.JPanel;
import teamamerica.WelcomeWindow;

/**
 *
 * @author Morris
 */
public class CountdownDisplay extends JPanel{
    private WelcomeWindow frame;
    private int countdown;
    private JLabel countdownLabel;
    
    public CountdownDisplay(WelcomeWindow frame, int countdown){
        super();
        this.frame = frame;
        frame.removeKeyListener0();//remove key listener while in countdown to prevent player from moving
        this.countdown = countdown;
        this.countdownLabel = new JLabel();
        countdownLabel.setFont(new Font("SansSerif", Font.BOLD, 320));
        countdownLabel.setBackground(new Color(255,255,255,255));
        this.add(countdownLabel);
        countdownNumber();
        frame.getSoundEffect().play("countdown");
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
                    frame.addKeyListener0();//add keylistener back to the frame since the countdown is finished
                }else{
                    countdownLabel.setText(countdown+"");
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask,0, 1000);
    }
}
