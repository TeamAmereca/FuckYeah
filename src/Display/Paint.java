package Display;

import Bonus.Bonus;
import Database.Write_Read;
import Joueur.Joueur;
import Map.Bloc;

import java.awt.Color;
import java.awt.Graphics;
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
    private Write_Read database;
    private int blockLength;
    private Timer timer;
    private Image solidBloc;
    private Image breakable;
    private Image sand;
    private Image balle;
    private Image balle2;
    

    public Paint(WelcomeWindow welcomeWindow, Write_Read database, Timer timer, int blockLength){
        try {
            // Initialisation
            this.welcomeWindow = welcomeWindow;
            this.database = database;
            this.timer = timer;
            this.blockLength = blockLength;
            
            this.solidBloc = ImageIO.read(new File("./src/Images/bloc.jpg"));
            this.breakable = ImageIO.read(new File("./src/Images/cassable2.png"));
            this.sand = ImageIO.read(new File("./src/Images/sable.png"));
            this.balle = ImageIO.read(new File("./src/Images/balle.png"));
            this.balle2 = ImageIO.read(new File("./src/Images/balle2.png"));
            paintTimer();
        } catch (IOException ex) {
            Logger.getLogger(Paint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        this.removeAll();
        //Here where we paint the players and the map
        //It is refreshed to every changes
        super.paintComponent(g);
        
        //Paint main player
        paintMap(g);
        paintPlayers(g);
        paintBalles(g);
    }
    public void paintMap(Graphics g) {   
        for(int i = 0; i<this.database.getMap().getX(); i++){
            for(int j=0; j<this.database.getMap().getY();j++){
                g.drawImage(sand, i*50, j*50, this);
            }
        }
        for(int i=0; i<database.getMap().nombreBlocs(); i++){
            paintBloc(g, database.getMap().getBloc(i));
        }
        for(int i=0; i<database.getMap().getListeBonus().size(); i++){          
            //Draw bonus
            Bonus bonus = database.getMap().getListeBonus().get(i);
            g.setColor(Color.YELLOW);
            g.fillRect(bonus.getPositionX()*blockLength, bonus.getPositionY()*blockLength, 50, 50);
        }
    }
    public void paintBloc(Graphics g, Bloc b){           
        if(b.getCassable()){
            g.drawImage(breakable, b.getX()*50, b.getY()*50, this);
        }
        else{
            g.drawImage(solidBloc, b.getX()*50, b.getY()*50, this);
        }
    }
    public void paintPlayers(Graphics g) {
        //Paint every players
        ArrayList<Joueur> players = database.getPlayers();
        for(int i=0;i<players.size();i++){       
            if(players.get(i).getPv()>0){
                //the player is still alive, so we repaint the player
                paintPlayer(g, players.get(i));
            }else{
                //the player is dead, we don't repaint the player
                if(i==0){
                    this.welcomeWindow.setMovePlayer(false);//remove the player's ability to move
                }
            }
        }
    }
    public void paintPlayer(Graphics g, Joueur joueur) {
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
//            AffineTransform old = g.getTransform();
//            g.rotate(Math.toRadians(45));
            g.drawImage(balle2, x*50, y*50, 50, 50, this);
//            g.setTransform(old);
        } else if(o.equals("Bas")) {
            g.drawImage(balle2, x*50, y*50+50, 50, -50,this);
        } 
    }
    
    public void paintTimer() {
        //Here we set a timer to get a copy of the database every 0.25 sec
        TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    database.refreshDataBase();  
                    welcomeWindow.getDisplayHeader().setHPbar(database.getPlayers());
                    repaint();
                }
            };
        timer.scheduleAtFixedRate(timerTask,0, 100);
    }  
}