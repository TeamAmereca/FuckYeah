package Display;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Morris
 */
public class HealthBarDisplay extends JPanel {
    //This panel displays a timer and players' health bar
    private final int HEALTH_BAR_LENGTH = 200;
    private final int HEALTH_BAR_HEIGHT = 20;
    private final int HEIGHT = 20;//height between the label and the hp abr
    private final int MARGIN = 5;
    private int healthPoint;
    
    public HealthBarDisplay(String pseudo){
        this.healthPoint = 100;
        JLabel labelPseudo = new JLabel(pseudo);
        labelPseudo.setFont(new Font("Serif", Font.ROMAN_BASELINE, 25));
        labelPseudo.setBounds(MARGIN,0, HEALTH_BAR_LENGTH, HEALTH_BAR_HEIGHT);
        add(labelPseudo);
        setSize(HEALTH_BAR_LENGTH+2*MARGIN,HEALTH_BAR_HEIGHT+HEIGHT+MARGIN+MARGIN);
        setLayout(null);
        setVisible(true);
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.drawRoundRect(MARGIN, MARGIN+HEIGHT, HEALTH_BAR_LENGTH, HEALTH_BAR_HEIGHT,5,5); //draws healthbar outline
        g.setColor(Color.red);
        g.fillRect(MARGIN, MARGIN+HEIGHT, (int)healthPoint*HEALTH_BAR_LENGTH/100, HEALTH_BAR_HEIGHT); //draws health       
    }
    
    public void setHealthPoint(int healthPoint){
        //Repaint the hp bar every time it is modified
        this.healthPoint = healthPoint;
        repaint();
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setBounds(0, 0, 1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setVisible(true);
        HealthBarDisplay ss = new HealthBarDisplay("ss");
        ss.setLocation(0, 0);
        frame.add(ss);
        HealthBarDisplay sss = new HealthBarDisplay("sss");
        sss.setLocation(0,50);
        frame.add(sss);
    }
}
