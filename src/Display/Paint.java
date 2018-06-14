package Display;

import Database.Write_Read;
import Joueur.Joueur;
import Map.Bloc;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
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
    
    public Paint(WelcomeWindow welcomeWindow, Write_Read database, Timer timer, int blockLength, int blockXNumber, int blockYNumber) {
        // Initialisation
        this.welcomeWindow = welcomeWindow;
        this.database = database;
        this.timer = timer;
        this.blockLength = blockLength;
        this.blockXNumber = blockXNumber;
        this.blockYNumber = blockYNumber;   
        paintTimer();
    }

    public void paintComponent(Graphics g) {
        //Here where we paint the players and the map
        //It is refreshed to every changes
        super.paintComponent(g);
        
        //Paint main player
        paintMap(g);
        paintPlayers(g);
        paintBalles(g);
    }
    public void paintMap(Graphics g) {
        try{
            Image sand = ImageIO.read(new File("./src/Images/sable.png"));
            for(int i = 0; i<this.database.getMap().getX(); i++){
                for(int j=0; j<this.database.getMap().getY();j++){
                    g.drawImage(sand, i*50, j*50, this);
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        for(int i=0; i<database.getMap().nombreBlocs(); i++){
            paintBloc(g, database.getMap().getBloc(i));
        }
    }
    public void paintBloc(Graphics g, Bloc b){
        try{
            Image solidBloc = ImageIO.read(new File("./src/Images/bloc.jpg"));
            Image breakable = ImageIO.read(new File("./src/Images/test.png"));
            if(b.getCassable()){
                g.drawImage(breakable, b.getX()*50, b.getY()*50, this);
            }
            else{
                g.drawImage(solidBloc, b.getX()*50, b.getY()*50, this);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public void paintPlayers(Graphics g) {
        //Paint every players
        ArrayList<Joueur> players = database.getPlayers();
        for(int i=0;i<players.size();i++){System.out.println("number of players"+players.size());
            paintPlayer(g, players.get(i));
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
        g.setColor(Color.YELLOW);
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
