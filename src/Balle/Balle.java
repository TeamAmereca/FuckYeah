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
    private String tireur;
    
    public Balle (Connection connexion, String orientationBalle, String typeBalle, int positionx, int positiony, String tireur){
        try {
            this.typeBalle = typeBalle;
            this.positionx = positionx;
            this.positiony = positiony;
            this.orientationBalle = orientationBalle;
            this.tireur = tireur;
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
                this.vitesse=1;}
            else if (this.typeBalle == "M16"){
                this.degats=50;
                this.portee=0;
                this.vitesse=1;}
            else if (this.typeBalle == "Gun"){
                this.degats=20;
                this.portee=0;
                this.vitesse=1;}
            else {System.out.println("Balle non référencée");}
            this.timer = new Timer(150*this.vitesse, this);
            this.connexion = connexion;
            timer.start();
        } catch (SQLException ex) {
            Logger.getLogger(Balle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void deplacer(){
        
        if (this.orientationBalle.equals("Haut")){
            deplacement(this.positionx, this.positiony-1);}
        if (this.orientationBalle.equals("Bas")){
            deplacement(this.positionx, this.positiony+1);}
        if (this.orientationBalle.equals("Droite")){
            deplacement(this.positionx+1, this.positiony);}
        if (this.orientationBalle.equals("Gauche")){
            deplacement(this.positionx-1, this.positiony);
        }
    }

    public  void deplacement(int posx, int posy){
        try {
            System.out.println(this.typeBalle);
            //part en couille
            if (this.typeBalle=="Gun"){
                System.out.println("balle chercheuses");
                for(int i = 4; i<40; i++){
                    if (i%4==0){
                        PreparedStatement requete12 = connexion.prepareStatement("SELECT * FROM joueur WHERE x = ? AND y = ? AND NOT pseudo = ?");
                        requete12.setInt(1, posx+(i/4));
                        requete12.setInt(2, posy);
                        requete12.setString(3, this.tireur);
                        ResultSet resultat2 = requete12.executeQuery();
                            if (resultat2.next()==true) {
                                System.out.println("joueur trouvé");
                                this.orientationBalle="Droite";
                                PreparedStatement requete17 = connexion.prepareStatement("UPDATE balle SET orientation = ? WHERE numero = ? "); 
                                requete17.setString(1, "Droite");
                                requete17.setInt(2, this.numeroBalle);
                                requete17.executeUpdate();
                                requete17.close();
                                i=50;
                                }}
                    else if (i%4==1){
                        PreparedStatement requete13 = connexion.prepareStatement("SELECT * FROM joueur WHERE x = ? AND y = ? AND NOT pseudo = ?");
                        requete13.setInt(1, posx);
                        requete13.setInt(2, posy+(i/4));
                        requete13.setString(3, this.tireur);
                        ResultSet resultat3 = requete13.executeQuery();
                            if (resultat3.next()==true) {
                                System.out.println("joueur trouvé");
                                this.orientationBalle="Bas";
                                PreparedStatement requete18 = connexion.prepareStatement("UPDATE balle SET orientation = ? WHERE numero = ? "); 
                                requete18.setString(1, "Bas");
                                requete18.setInt(2, this.numeroBalle);
                                requete18.executeUpdate();
                                requete18.close();
                                i=50;
                                }
                         }
                    else if (i%4==2){
                        PreparedStatement requete13 = connexion.prepareStatement("SELECT * FROM joueur WHERE x = ? AND y = ? AND NOT pseudo = ?");
                        requete13.setInt(1, posx);
                        requete13.setInt(2, posy-(i/4));
                        requete13.setString(3, this.tireur);
                        ResultSet resultat3 = requete13.executeQuery();
                            if (resultat3.next()==true) {
                                System.out.println("joueur trouvé");
                                this.orientationBalle="Haut";
                                PreparedStatement requete19 = connexion.prepareStatement("UPDATE balle SET orientation = ? WHERE numero = ? "); 
                                requete19.setString(1, "Haut");
                                requete19.setInt(2, this.numeroBalle);
                                requete19.executeUpdate();
                                requete19.close();
                                i=50;
                                }
                         }
                    else if (i%4==3){
                        PreparedStatement requete13 = connexion.prepareStatement("SELECT * FROM joueur WHERE x = ? AND y = ? AND NOT pseudo = ?");
                        requete13.setInt(1, posx-(i/4));
                        requete13.setInt(2, posy);
                        requete13.setString(3, this.tireur);
                        ResultSet resultat3 = requete13.executeQuery();
                            if (resultat3.next()==true) {
                                System.out.println("joueur trouvé");
                                this.orientationBalle="Gauche";
                                PreparedStatement requete20 = connexion.prepareStatement("UPDATE balle SET orientation = ? WHERE numero = ? "); 
                                requete20.setString(1, "Gauche");
                                requete20.setInt(2, this.numeroBalle);
                                requete20.executeUpdate();
                                requete20.close();
                                i=50;
                                }
                         }
                
                }

                }
            //Connection connexion = DriverManager.getConnection("jdbc:mysql://nemrod.ens2m.fr:3306/20172018_s2_vs2_fuckyeah?serverTimezone=UTC", "fuckyeah", "america");
            PreparedStatement requete = connexion.prepareStatement("SELECT * FROM joueur WHERE x = ? AND y = ? AND NOT pseudo = ?");
            requete.setInt(1, posx);
            requete.setInt(2, posy);
            requete.setString(3, this.tireur);
            ResultSet resultat = requete.executeQuery();
            while(resultat.next()) {
                System.out.println("joueur détecté");
                String pseudo = resultat.getString("pseudo");
                System.out.println(pseudo);
                System.out.println(this.degats);
                int x = resultat.getInt("x");
                int y = resultat.getInt("y");
                int pv = resultat.getInt("pv");
                String nation = resultat.getString("nation");
                String orientation = resultat.getString("orientation");
                String status = resultat.getString("status");
                requete.close();
                PreparedStatement requete2 = connexion.prepareStatement("UPDATE joueur SET pv = ? WHERE pseudo = ?");            
                requete2.setInt(1, pv-this.degats);
                requete2.setString(2, pseudo);
                requete2.executeUpdate();
                requete2.close();
                System.out.println("destruction balle");
                PreparedStatement requete7 = connexion.prepareStatement("DELETE FROM balle WHERE positionx = ? AND positiony = ? "); 
                System.out.println(this.positionx);
                System.out.println(this.positiony);
                requete7.setInt(1, this.positionx);
                requete7.setInt(2, this.positiony);
                requete7.executeUpdate();
                requete7.close();
                timer.stop();
                //connexion.close();
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
                //connexion.close();
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
                //connexion.close();
            }
            
    } catch (SQLException ex) {
        System.out.println(ex);
        }
    }
    public void actionPerformed(ActionEvent evt){
        this.deplacer();
    }


    
    
}
