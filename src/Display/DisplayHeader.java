package Display;

import Joueur.Joueur;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Morris
 * It creates a JPanel on top on of screen in which we can find the game title, the players' hp
 */
public class DisplayHeader extends JPanel{
    private final int panelHeight;
    private final int panelWidth;
    private final ArrayList<HealthBarDisplay> healthBarDisplay = new ArrayList<>();
    
    public DisplayHeader(int screenWidth, int screenHeight) {
        //Set the height, the width, the position of this panel
        panelHeight = (int) screenHeight/10;
        panelWidth = screenWidth;
        this.setSize(panelWidth,panelHeight);
        this.setLocation(0,0);
        this.setLayout(null);
    }
    
    public void addHeader(String text) {
        //Add a title
        JLabel headerLabel = new JLabel(text);
        headerLabel.setBounds((int)(panelWidth/2)-150,0, 1000,100);
        headerLabel.setForeground(Color.RED);
        headerLabel.setFont(new Font("Serif", Font.PLAIN, 50));
        add(headerLabel);
    }
    
    public void addHPbar(ArrayList<Joueur> players){        
        for (int i=0;i<players.size();i++) {
            HealthBarDisplay display = new HealthBarDisplay(players.get(i).getPseudo());
            int variableX;
            int MARGIN = 20,
                DISTANCE_BAR = 90;
            if(i==0){
                //First hp bar
                variableX = MARGIN;
            }else if(i==1){
                //Fourth hp bar
                variableX = panelWidth-(display.getWidth()+MARGIN);
            }else if(i==2){
                //Second hp bar
                variableX = MARGIN+display.getWidth()+DISTANCE_BAR;
            }else{
                //Third hp bar
                variableX = panelWidth-(DISTANCE_BAR+2*display.getWidth()+MARGIN);
            }
            display.setLocation(variableX,20);
            add(display);//add to JPanel
            healthBarDisplay.add(display);//add to healthBarDisplay
        }
    }
    
    public void setHPbar(ArrayList<Joueur> players){
        //modify each player's pv bar accordingly
        for (int i=0;i<players.size();i++) {
            for(int j=0;j<healthBarDisplay.size();j++) {
                if(healthBarDisplay.get(j).getPseudo().equals(players.get(i).getPseudo())){
                    healthBarDisplay.get(j).setHealthPoint(players.get(i).getPv());
                    break;
                }
            }
        }
        repaint();
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setBounds(0, 0, 1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setVisible(true);
        DisplayHeader display = new DisplayHeader(1000,600);
        frame.add(display);
//        HealthBarDisplay ss = new HealthBarDisplay("ss");
//        ss.setLocation(0, 0);
//        frame.add(ss);
//        HealthBarDisplay sss = new HealthBarDisplay("sss");
//        sss.setLocation(0,50);
//        frame.add(sss);
    }
}
