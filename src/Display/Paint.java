package Display;

import Bonus.Bonus;
import Database.Write_Read;
import Joueur.Joueur;
import Map.Bloc;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import teamamerica.WelcomeWindow;

public class Paint extends JComponent {
    
    private WelcomeWindow welcomeWindow;
    private int increment = 10;
    private Write_Read database;
    private int blockLength;
    private int blockXNumber;
    private int blockYNumber;
    private Timer timer;
    private Image solidBloc;
    private Image breakable;
    private Image sand;
    private Image balle;
    

    public Paint(WelcomeWindow welcomeWindow, Write_Read database, Timer timer, int blockLength, int blockXNumber, int blockYNumber){
        try {
            // Initialisation
            this.welcomeWindow = welcomeWindow;
            this.database = database;
            this.timer = timer;
            this.blockLength = blockLength;
            this.blockXNumber = blockXNumber;
            this.blockYNumber = blockYNumber;
            
            this.solidBloc = ImageIO.read(new File("./src/Images/bloc.jpg"));
            this.breakable = ImageIO.read(new File("./src/Images/cassable2.png"));
            this.sand = ImageIO.read(new File("./src/Images/sable.png"));
            this.balle = ImageIO.read(new File("./src/Images/balle.png"));
            paintTimer();
        } catch (IOException ex) {
            Logger.getLogger(Paint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void paintComponent(Graphics2D g) {
        //Here where we paint the players and the map
        //It is refreshed to every changes
        super.paintComponent(g);
        
        //Paint main player
        paintMap(g);
        paintPlayers(g);
        paintBalles(g);
    }
    public void paintMap(Graphics2D g) {          
        for(int i = 0; i<this.database.getMap().getX(); i++){
            for(int j=0; j<this.database.getMap().getY();j++){
                g.drawImage(sand, i*50, j*50, this);
            }
        }
        for(int i=0; i<database.getMap().nombreBlocs(); i++){
            paintBloc(g, database.getMap().getBloc(i));
        }
        for(int i=0; i<database.getMap().getListeBonus().size(); i++){
            Bonus bonus = database.getMap().getListeBonus().get(i);
            g.fillRect(bonus.getPositionX()*blockLength, bonus.getPositionY()*blockLength, 50, 50);
        }
    }
    public void paintBloc(Graphics2D g, Bloc b){           
        if(b.getCassable()){
            g.drawImage(breakable, b.getX()*50, b.getY()*50, this);
        }
        else{
            g.drawImage(solidBloc, b.getX()*50, b.getY()*50, this);
        }
    }
    public void paintPlayers(Graphics2D g) {
        //Paint every players
        ArrayList<Joueur> players = database.getPlayers();
        for(int i=0;i<players.size();i++){
            paintPlayer(g, players.get(i));
        }
    }
    public void paintPlayer(Graphics2D g, Joueur joueur) {
        //Paint the player
        g.setColor(Color.BLACK);
        //System.out.println("Player is at: ("+joueur.getOrientation()+","+joueur.getPositionX()+","+joueur.getPositionY()+")");
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
    public void paintBalles(Graphics2D g) {
        // Paint every balles
        ArrayList<Integer> ballesX = database.getBallesX();
        ArrayList<Integer>ballesY = database.getBallesY();
        ArrayList<String> ballesO =database.getBallesO();
        for(int i=0;i<ballesX.size();i++){
            paintBalle(g, ballesX.get(i), ballesY.get(i), ballesO.get(i));
        }
    }
    public void paintBalle(Graphics2D g, int x, int y, String o) {
        //Paint the balle
        //System.out.println("Balle est à ("+x+","+y+") orientée vers "+o);
//        g.setColor(Color.YELLOW);
//        g.drawRect(x*blockLength, y*blockLength, 50, 50);
//        g.fillRect(x*blockLength, y*blockLength, 50, 50);
//        g.setColor(Color.RED);
        AffineTransform r = new AffineTransform();
        if(o.equals("Droite")) {
            g.drawImage(balle, x*50 + 50, y*50, -50, 50, this);
        } else if(o.equals("Gauche")) {
            g.drawImage(balle, x*50, y*50, this);
        } else if(o.equals("Haut")) {
            AffineTransform old = g.getTransform();
            g.rotate(Math.toRadians(90));
            g.drawImage(balle, x*50, y*50, this);
            g.setTransform(old);
        } else if(o.equals("Bas")) {
            g.drawImage(balle, x*50, y*50, this);
        }
    }
    
    public void paintHealthPoint(){
        welcomeWindow.getDisplayHeader().setHPbar(database.getPlayers());
    }
    public void paintTimer() {
        //Here we set a timer to get a copy of the database every 0.25 sec
        TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    database.refreshDataBase();  
                    paintHealthPoint();
                    repaint();
                }
            };
        timer.scheduleAtFixedRate(timerTask,0, 100);
    }  
}
