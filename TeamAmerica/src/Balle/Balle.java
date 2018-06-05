package Balle;

import java.awt.event.*;
import java.io.IOException;
import javax.swing.Timer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author ranger
 */
public class Balle implements ActionListener {
    private int numeroBalle;
    private String typeBalle;
    private int vitesse;
    private int positionx;
    private int positiony;
    private int degats;
    private int portee;
    private String orientationBalle;
    private Connection connexion;
    private Timer timer;
    
    public Balle (Connection connexion, String orientationBalle, String typeBalle, int positionx, int positiony){
        try {
            this.typeBalle = typeBalle;
            this.positionx = positionx;
            this.positiony = positiony;
            this.orientationBalle = orientationBalle;
            String[] returnId = { "BATCHID" };
            PreparedStatement requete = connexion.prepareStatement("INSERT INTO balle VALUES (0,?,?,?,?)",returnId);
            requete.setString(4, this.typeBalle);
            requete.setInt(1, this.positionx);
            requete.setInt(2, this.positiony);
            requete.setString(3, this.orientationBalle);
            requete.executeUpdate();
            ResultSet resultat = requete.getGeneratedKeys();
            if (resultat.next()){
                this.numeroBalle=resultat.getInt(1);
            }
            requete.close();
            if (this.typeBalle == "AK47"){
                this.degats=50;
                this.portee=0;
                this.vitesse=10;}
            else if (this.typeBalle == "M16"){
                this.degats=50;
                this.portee=0;
                this.vitesse=10;}
            else if (this.typeBalle == "Gun"){
                this.degats=20;
                this.portee=0;
                this.vitesse=5;}
            else {System.out.println("Balle non référencée");}
            this.timer = new Timer( 1000 , this);
            timer.start();
        } catch (SQLException ex) {
            Logger.getLogger(Balle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void deplacer(){
        
        if (this.orientationBalle == "Haut"){
            deplacement(this.positionx, this.positiony+1);}
        if (this.orientationBalle == "Bas"){
            deplacement(this.positionx, this.positiony-1);}
        if (this.orientationBalle == "Droite"){
            deplacement(this.positionx+1, this.positiony);}
        if (this.orientationBalle == "Gauche"){
            deplacement(this.positionx-1, this.positiony);
        }
    }

    public  void deplacement(int posx, int posy){
        try {
            Connection connexion = DriverManager.getConnection("jdbc:mysql://nemrod.ens2m.fr:3306/20172018_s2_vs2_fuckyeah?serverTimezone=UTC", "fuckyeah", "america");
            PreparedStatement requete = connexion.prepareStatement("SELECT * FROM joueur WHERE x = ? AND y = ?");
            requete.setInt(1, posx);
            requete.setInt(2, posy);
            ResultSet resultat = requete.executeQuery();
            if (resultat.next()==true) {
                System.out.println("joueur détecté");
                String Nom = resultat.getString("Nom");
                int x = resultat.getInt("x");
                int y = resultat.getInt("y");
                int pv = resultat.getInt("pv");
                String nation = resultat.getString("nation");
                String orientation = resultat.getString("orientation");
                String status = resultat.getString("status");
                requete.close();
                PreparedStatement requete2 = connexion.prepareStatement("UPDATE joueur SET pv = ? WHERE pseudo = ?");            
                requete2.setInt(1, pv-this.degats);
                requete2.setString(2, Nom);
                requete2.close();
                connexion.close();
            }
            PreparedStatement requete3 = connexion.prepareStatement("SELECT * FROM blocs WHERE positionX = ? AND positionY = ?");
            requete3.setInt(1, posx);
            requete3.setInt(2, posy);
            requete.close();
            ResultSet resultat3 = requete3.executeQuery();
            if (resultat3.next()==true){
                System.out.println("bloc détecté");
                int positionX = resultat3.getInt("positionX");
                int positionY = resultat3.getInt("positionY");
                boolean cassable = resultat3.getBoolean("cassable");
                requete3.close();
                if (cassable==true){
                    System.out.println("cassable, destruction bloc");
                    PreparedStatement requete4 = connexion.prepareStatement("DELETE FROM blocs WHERE positionX = ? AND positionY = ?"); 
                    requete4.setInt(1, posx);
                    requete4.setInt(2, posy);
                    requete4.executeUpdate();
                    requete4.close();
                }
                System.out.println("destruction balle");
                PreparedStatement requete5 = connexion.prepareStatement("DELETE FROM balle WHERE positionx = ? AND positiony = ? "); 
                System.out.println(this.positionx);
                System.out.println(this.positiony);
                requete5.setInt(1, this.positionx);
                requete5.setInt(2, this.positiony);
                requete5.executeUpdate();
                requete5.close();
                timer.stop();
                connexion.close();
            }else {
                this.positionx=posx;
                this.positiony=posy;
                System.out.println("Deplacement");
                System.out.println("posy=" + posy);
                System.out.println("posx=" + posx);
                PreparedStatement requete6 = connexion.prepareStatement("UPDATE balle SET positionx = ?, positiony = ? WHERE numero = ? "); 
                requete6.setInt(1, posx);
                requete6.setInt(2, posy);
                requete6.setInt(3, this.numeroBalle);
                requete6.executeUpdate();
                
                requete6.close();
                connexion.close();
            }
            
    } catch (SQLException ex) {
        System.out.println(ex);
        }
    }
    public void actionPerformed(ActionEvent evt){
        this.deplacer();
    }


    
    
}