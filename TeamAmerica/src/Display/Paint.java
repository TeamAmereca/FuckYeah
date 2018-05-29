package Display;

import Database.Write_Read;
import Joueur.Joueur;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JComponent;

public class Paint extends JComponent {
    
    private int increment = 10;
    private Write_Read database;
    private int blockLength = 50;
    private int blockXNumber = 20;
    private int blockYNumber = 13;
    private Timer timer;
    
    private int panelHeight;
    private int panelWidth;
    
    public Paint(Write_Read database, Timer timer) {
        // Initialisation
        this.database = database;
        this.timer = timer;
        this.panelHeight = blockYNumber*blockLength;
        this.panelWidth = blockXNumber*blockLength;
        paintTimer();
    }

    public void paintComponent(Graphics g) {
        //Here where we paint the players and the map
        //It is refreshed to every changes
        super.paintComponent(g);
        
        //Paint main player
        paintPlayers(g);
        paintMap(g);
        paintBalles(g);
    }
    
    public void paintMap(Graphics g) {
        //Paint the map
        g.setColor(Color.BLACK);
        for(int i=0;i<=blockYNumber;i++) {
            //Horizontal lines
            g.drawLine(0, i*blockLength, blockXNumber*blockLength, i*blockLength);
        }
        
        for(int i=0;i<=blockXNumber;i++) {
            //Vertical lines
            g.drawLine(i*blockLength, 0, i*blockLength, blockYNumber*blockLength);
        }
    }
    
    public void paintPlayers(Graphics g) {
        //Paint every players
        paintPlayer(g,database.getMainPlayer());
        ArrayList<Joueur> players = database.getPlayers();
        for(int i=0;i<players.size();i++){
            paintPlayer(g, players.get(i));
        }
    }
    public void paintPlayer(Graphics g, Joueur joueur) {
        //Paint the player
        g.setColor(Color.BLACK);
        System.out.println("Player is at: ("+joueur.getOrientation()+","+joueur.getPositionX()+","+joueur.getPositionY()+")");
        g.drawRect(joueur.getPositionX()*blockLength, joueur.getPositionY()*blockLength, 50, 50);
        g.fillRect(joueur.getPositionX()*blockLength, joueur.getPositionY()*blockLength, 50, 50);
        g.setColor(Color.RED);
        if(joueur.getOrientation().equals("Droite")) {
            g.drawRect(joueur.getPositionX()*blockLength+40, joueur.getPositionY()*blockLength+20, 10, 10);
            g.fillRect(joueur.getPositionX()*blockLength+40, joueur.getPositionY()*blockLength+20, 10, 10);
        } else if(joueur.getOrientation().equals("Gauche")) {
            g.drawRect(joueur.getPositionX()*blockLength, joueur.getPositionY()*blockLength+20, 10, 10);
            g.fillRect(joueur.getPositionX()*blockLength, joueur.getPositionY()*blockLength+20, 10, 10);
        } else if(joueur.getOrientation().equals("Haut")) {
            g.drawRect(joueur.getPositionX()*blockLength+20, joueur.getPositionY()*blockLength, 10, 10);
            g.fillRect(joueur.getPositionX()*blockLength+20, joueur.getPositionY()*blockLength, 10, 10);
        } else if(joueur.getOrientation().equals("Bas")) {
            g.drawRect(joueur.getPositionX()*blockLength+20, joueur.getPositionY()*blockLength+40, 10, 10);
            g.fillRect(joueur.getPositionX()*blockLength+20, joueur.getPositionY()*blockLength+40, 10, 10);
        }
    }
    public void paintBalles(Graphics g) {
        // Paint every balles
        ArrayList<Integer> ballesX = database.getBallesX();
        ArrayList<Integer>ballesY = database.getBallesY();
        ArrayList<String> ballesO =database.getBallesO();
        for(int i=0;i<ballesX.size();i++){
            paintBalle(g, ballesX.get(i), ballesY.get(i), ballesO.get(i));
        }
    }
    public void paintBalle(Graphics g, int x, int y, String o) {
        //Paint the balle
        System.out.println("Balle est à ("+x+","+y+") orientée vers "+o);
        g.setColor(Color.BLACK);
        System.out.println("Balle is at: ("+o+","+x+","+y+")");
        g.drawRect(x*blockLength, y*blockLength, 50, 50);
        g.fillRect(x*blockLength, y*blockLength, 50, 50);
        g.setColor(Color.RED);
        if(o.equals("Droite")) {
            g.drawRect(x*blockLength+40, y*blockLength+20, 10, 10);
            g.fillRect(x*blockLength+40, y*blockLength+20, 10, 10);
        } else if(o.equals("Gauche")) {
            g.drawRect(x*blockLength, y*blockLength+20, 10, 10);
            g.fillRect(x*blockLength, y*blockLength+20, 10, 10);
        } else if(o.equals("Haut")) {
            g.drawRect(x*blockLength+20, y*blockLength, 10, 10);
            g.fillRect(x*blockLength+20, y*blockLength, 10, 10);
        } else if(o.equals("Bas")) {
            g.drawRect(x*blockLength+20, y*blockLength+40, 10, 10);
            g.fillRect(x*blockLength+20, y*blockLength+40, 10, 10);
        }
    }
    public void paintTimer() {
        //Here we set a timer a get a copy of the database every 1 sec
        TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {System.out.println("timer for database");
                    database.refreshDataBase();
                    repaint();
                }
            };
        timer.scheduleAtFixedRate(timerTask,0, 250);
    }

    public void moveRight() {
        database.getMainPlayer().setPositionX(database.getMainPlayer().getPositionX() + increment);
        repaint();
    }

    public void moveLeft() {
        database.getMainPlayer().setPositionX(database.getMainPlayer().getPositionX() - increment);
        repaint();
    }

    public void moveDown() {
        database.getMainPlayer().setPositionY(database.getMainPlayer().getPositionY() + increment);
        repaint();
    }

    public void moveUp() {
        database.getMainPlayer().setPositionY(database.getMainPlayer().getPositionY() - increment);
        repaint();
    }

    public int getPanelHeight() {
        return panelHeight;
    }

    public int getPanelWidth() {
        return panelWidth;
    }
    
}
