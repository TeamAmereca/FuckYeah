package Display;

import java.awt.Color;
import java.awt.Font;
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
}