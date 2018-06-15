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
 *
 * @author Morris
 */
public class HealthBarDisplay extends JPanel {
    //This panel displays players' health bar
    //The panel's height is MARGIN+COMPONENT_HEIGHT+GAP_HEIGHT+COMPONENT_HEIGHT+MARGIN = 55
    //The panel's length is MARGIN+COMPONENT_LENGTH+MARGIN = 210
    private final int COMPONENT_LENGTH = 200;
    private final int COMPONENT_HEIGHT = 20;
    private final int GAP_HEIGHT = 5;//height between the label and the hp bar
    private final int MARGIN = 5;
    private int healthPoint;
    private final String pseudo;
    
    public HealthBarDisplay(String pseudo){
        this.healthPoint = 100;
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
        g.setColor(Color.BLACK);
        g.drawRoundRect(MARGIN, COMPONENT_HEIGHT+GAP_HEIGHT, COMPONENT_LENGTH, COMPONENT_HEIGHT,5,5); //draws healthbar outline
        g.setColor(Color.red);
        g.fillRect(MARGIN, COMPONENT_HEIGHT+GAP_HEIGHT, (int)(healthPoint*COMPONENT_LENGTH/100), COMPONENT_HEIGHT); //draws health       
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
