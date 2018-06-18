package Display;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import teamamerica.WelcomeWindow;

/**
 *This panel displays a countdown timer
 * @author Morris
 */
public class CountdownDisplay extends JComponent{
    private WelcomeWindow frame;
    private int countdown;
    
    public CountdownDisplay(WelcomeWindow frame, int countdown){
        super();        
        frame.setMovePlayer(false);//disable player moving
        this.frame = frame;
        this.countdown = countdown;
        countdownTimer();
        frame.getSoundEffect().play("countdown");
    }
    
    public CountdownDisplay( int countdown){
        super();
        this.countdown = countdown;
        this.setBounds(0, 0,1000,1000);
        countdownTimer();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        g.setFont( new Font("TimesRoman", Font.PLAIN, 490) );
        g.drawString(countdown+"", 0, 400);
    }
    public void countdownTimer(){
        //Display the coundown numbers
        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if(countdown--==0){
                    setVisible(false);
                    timer.cancel();
                    frame.setMovePlayer(true);//enable player moving
                }else{
                    repaint();
                }
                
            }
        };
        timer.scheduleAtFixedRate(timerTask,0, 1000);
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setBounds(0, 0, 1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setVisible(true);        
        CountdownDisplay ss = new CountdownDisplay(3);      
        CountdownDisplay sss = new CountdownDisplay(4);  
        frame.add(ss,0);
        frame.add(sss,1);
    }
}