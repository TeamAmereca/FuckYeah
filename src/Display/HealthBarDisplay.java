package Display;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

/**
 *This panel displays players' health bar
 *The panel's height is MARGIN+COMPONENT_HEIGHT+GAP_HEIGHT+COMPONENT_HEIGHT+MARGIN = 55
 *The panel's length is MARGIN+COMPONENT_LENGTH+MARGIN = 210
 * @author Morris
 */
public class HealthBarDisplay extends JPanel {
    private final int COMPONENT_LENGTH = 200;
    private final int COMPONENT_HEIGHT = 20;
    private final int GAP_HEIGHT = 5;//height between the label and the hp bar
    private final int MARGIN = 5;
    
    private int healthPoint;
    private final String pseudo;
    
    public HealthBarDisplay(String pseudo){
        this.healthPoint = 100;//set the initial pv to 100
        this.pseudo = pseudo;
        
        //Add a label for displaying pseudo
        JLabel labelPseudo = new JLabel(pseudo);
        labelPseudo.setFont(new Font("Serif", Font.ROMAN_BASELINE, 25));
        labelPseudo.setBounds(MARGIN,0, COMPONENT_LENGTH, COMPONENT_HEIGHT);
        add(labelPseudo);
        
        
        //Configurate this JPanel
        setSize(COMPONENT_LENGTH+2*MARGIN,2*COMPONENT_HEIGHT+GAP_HEIGHT+2*MARGIN);
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        setBackground(new Color(175,167,167));
        setLayout(null);
        setVisible(true);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(72, 74, 69));
        g.drawRoundRect(MARGIN, COMPONENT_HEIGHT+GAP_HEIGHT, COMPONENT_LENGTH, COMPONENT_HEIGHT,5,5); //draws healthbar outline
        g.setColor(new Color(new Float(0.96), new Float(0.27), new Float(0.27), new Float(Math.min(1, healthPoint/100))));
        g.fillRect(MARGIN, COMPONENT_HEIGHT+GAP_HEIGHT, (int)(healthPoint*COMPONENT_LENGTH/Math.max(100, healthPoint)), COMPONENT_HEIGHT); //draws health               
        g.setColor(new Color(173, 255, 47));
        g.drawString(healthPoint+"", MARGIN+3, COMPONENT_HEIGHT+GAP_HEIGHT+15);
    }
    
    public void setHealthPoint(int healthPoint){
        //Repaint the hp bar every time it is modified
        if(healthPoint==0){
            //modify the background color when the playre's pv is null, that is the player is dead
            setBackground(new Color(132,236,204));
        }
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

    public String getPseudo() {
        return pseudo;
    }
    
}
