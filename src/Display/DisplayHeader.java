package Display;

import Joueur.Joueur;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
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
    private ArrayList<HealthBarDisplay> healthBarDisplay = new ArrayList<>();
    
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
            int variableX, variableY;
            if(i==0){
                variableX = 20;
                variableY = 20;
            }else if(i==1){
                variableX = panelWidth-250;
                variableY = 20;
            }else if(i==2){
                variableX = 20;
                variableY = 70;
            }else{
                variableX = panelWidth-250;
                variableY = 70;
            }
            display.setLocation(variableX,variableY);
            add(display);
            healthBarDisplay.add(display);
            this.revalidate();
            repaint();
        }
    }
    
    public void setHPbar(ArrayList<Joueur> players){
        for (int i=0;i<players.size();i++) {
            healthBarDisplay.get(i).setHealthPoint(players.get(i).getPv());
            repaint();
        }
    }
}
