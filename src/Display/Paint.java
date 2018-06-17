package Display;

import Map.Bonus;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
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
    private Image tetechercheusefront;
    private Image tetechercheuseside;
    private Image sideamerica;
    private Image frontamerica;
    private Image bonus;
    private Image terroriste;
    

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
            this.sideamerica = ImageIO.read(new File("./src/Images/sideamerica.png"));
            this.frontamerica = ImageIO.read(new File("./src/Images/frontamerica.png"));
            this.tetechercheusefront = ImageIO.read(new File("./src/Images/tetechercheusefront.png"));
            this.tetechercheuseside = ImageIO.read(new File("./src/Images/tetechercheuseside.png"));
            this.bonus = ImageIO.read(new File("./src/Images/bonus.png"));
            this.terroriste = ImageIO.read(new File ("./src/Images/terroriste.png"));
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
            Bonus b = database.getMap().getListeBonus().get(i);
            g.drawImage(bonus, b.getPositionX()*blockLength, b.getPositionY()*blockLength, this);
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
        //System.out.println("Player is at: ("+joueur.getOrientation()+","+joueur.getPositionX()+","+joueur.getPositionY()+")");
        g.setColor(Color.RED);
        if(joueur.getOrientation().equals("Droite")) {
                if(joueur.getNation() == "America"){
                    g.drawImage(sideamerica, joueur.getPositionX()*blockLength, joueur.getPositionY()*blockLength, 50, 50, this);
                }
                else if(joueur.getNation() == "Terrorist"){
                    g.drawImage(terroriste, joueur.getPositionX()*blockLength, joueur.getPositionY()*blockLength, 50, 50, this);
                }
            g.drawRect(joueur.getPositionX()*blockLength+40, joueur.getPositionY()*blockLength+20, 3, 3);
            g.fillRect(joueur.getPositionX()*blockLength+40, joueur.getPositionY()*blockLength+20, 3, 3);
        } else if(joueur.getOrientation().equals("Gauche")) {
                if(joueur.getNation() == "America"){
                    g.drawImage(sideamerica, joueur.getPositionX()*blockLength +50, joueur.getPositionY()*blockLength, -50, 50, this);
                }
                else if(joueur.getNation() == "Terrorist"){
                    g.drawImage(terroriste, joueur.getPositionX()*blockLength +50, joueur.getPositionY()*blockLength, -50, 50, this);
                }
            g.drawRect(joueur.getPositionX()*blockLength, joueur.getPositionY()*blockLength+20, 3, 3);
            g.fillRect(joueur.getPositionX()*blockLength, joueur.getPositionY()*blockLength+20, 3, 3);
        } else if(joueur.getOrientation().equals("Haut")) {
                if(joueur.getNation() == "America"){
                    g.drawImage(frontamerica, joueur.getPositionX()*blockLength, joueur.getPositionY()*blockLength, 50, 50, this);
                }
                else if(joueur.getNation() == "Terrorist"){
                    g.drawImage(terroriste, joueur.getPositionX()*blockLength, joueur.getPositionY()*blockLength, 50, 50, this);
                }
            g.drawRect(joueur.getPositionX()*blockLength+20, joueur.getPositionY()*blockLength, 3, 3);
            g.fillRect(joueur.getPositionX()*blockLength+20, joueur.getPositionY()*blockLength, 3, 3);
        } else if(joueur.getOrientation().equals("Bas")) {
                if(joueur.getNation() == "America"){
                    g.drawImage(frontamerica, joueur.getPositionX()*blockLength+50, joueur.getPositionY()*blockLength, -50, 50, this);
                }
                else if(joueur.getNation() == "Terrorist"){
                    g.drawImage(terroriste, joueur.getPositionX()*blockLength+50, joueur.getPositionY()*blockLength, -50, 50, this);
                }
            g.drawRect(joueur.getPositionX()*blockLength+20, joueur.getPositionY()*blockLength+40, 3, 3);
            g.fillRect(joueur.getPositionX()*blockLength+20, joueur.getPositionY()*blockLength+40, 3, 3);
        }
    }
    public void paintBalles(Graphics g) {
        // Paint every balles
        ArrayList<Integer> ballesX = database.getBallesX();
        ArrayList<Integer> ballesY = database.getBallesY();
        ArrayList<String> ballesO =database.getBallesO();
        ArrayList<String> ballesT = database.getBallesT();
        for(int i=0;i<ballesX.size();i++){
            paintBalle(g, ballesX.get(i), ballesY.get(i), ballesO.get(i), ballesT.get(i));
        }
    }
    public void paintBalle(Graphics g, int x, int y, String o, String t) {
        if(o.equals("Droite")) {
            if(t.equals("Gun")){
                g.drawImage(tetechercheuseside, x*50 + 50, y*50, -50, 50, this);
            }
            else if(t.equals("AK47")){
                g.drawImage(balle, x*50 + 50, y*50, -50, 50, this);
            }
            else if(t.equals("M16")){
                g.drawImage(balle, x*50 + 50, y*50, -50, 50, this);
            }
        } else if(o.equals("Gauche")) {
            if(t.equals("Gun")){
                g.drawImage(tetechercheuseside, x*50, y*50, this);
            }
            else if(t.equals("AK47")){
                g.drawImage(balle, x*50, y*50, this);
            }
            else if(t.equals("M16")){
                g.drawImage(balle, x*50, y*50, this);
            }
        } else if(o.equals("Haut")) {
            if(t.equals("Gun")){
                g.drawImage(tetechercheusefront, x*50, y*50, 50, 50, this);
            }
            else if(t.equals("AK47")){
                g.drawImage(balle2, x*50, y*50, 50, 50, this);
            }
            else if(t.equals("M16")){
                g.drawImage(balle2, x*50, y*50, 50, 50, this);
            }
        } else if(o.equals("Bas")) {
            if(t.equals("Gun")){
                g.drawImage(tetechercheusefront, x*50, y*50+50, 50, -50,this);
            }
            else if(t.equals("AK47")){
                g.drawImage(balle2, x*50, y*50+50, 50, -50,this);
            }
            else if(t.equals("M16")){
                g.drawImage(balle2, x*50, y*50+50, 50, -50,this);
            }
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