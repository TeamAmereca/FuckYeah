package teamamerica;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;

public class Paint extends JComponent {
    
    // Initialisation
    protected int positionX = 50;
    protected int positionY = 50;
    private int increment = 5;

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw a rectangle
        g.drawRect(positionX, positionY, 50, 50);
        g.fillRect(positionX, positionY, 50, 50);
        g.setColor(Color.BLACK);
    }

    public void moveRight() {
        positionX = positionX + increment;
        repaint();
    }

    public void moveLeft() {
        positionX = positionX - increment;
        repaint();
    }

    public void moveDown() {
        positionY = positionY + increment;
        repaint();
    }

    public void moveUp() {
        positionY = positionY - increment;
        repaint();
    }
}
