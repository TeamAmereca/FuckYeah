//Written by Clément
package teamamerica;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Joueur {
    
    private int positionX;
    private int positionY;
    private String pseudo;
    private String nation;
    private String orientation;
    private String arme;
    private int pv;
    private int vitesse;
    private int connectePartie;
    private Connection connexion;
    
    public Joueur(String pseudo, Connection connexion){
        positionX = 0;
        positionY = 0;
        this.pseudo = pseudo;
        nation = "America";
        orientation = "bas";
        pv = 100;
        vitesse = 1; //joueur se déplace case par case par défaut
        connectePartie = 0;
        this.connexion = connexion;
    }
    
    public void creerJoueurSQL(){ //crée le jouer dans la BDD SQL
        try {

            PreparedStatement requete = connexion.prepareStatement("INSERT INTO joueur VALUES (?,?,?,?,?,?)");
            requete.setString(1, pseudo);
            requete.setInt(2, positionX);
            requete.setInt(3, positionY);
            requete.setInt(4, pv);
            requete.setString(5, nation);
            requete.setString(6, orientation);
            requete.executeUpdate();

            requete.close();
            connexion.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void modifierPseudo(String nouveauPseudo){
        String ancienPseudo = this.pseudo;
        this.pseudo = nouveauPseudo;
        try {

            PreparedStatement requete = connexion.prepareStatement("UPDATE joueur SET pseudo = ? WHERE pseudo = ?");
            requete.setString(1, this.pseudo);
            requete.setString(2, ancienPseudo);
            requete.executeUpdate();

            requete.close();
            connexion.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void modifierNation(String nouvelleNation){
        this.nation = nouvelleNation;
        try {

            PreparedStatement requete = connexion.prepareStatement("UPDATE joueur SET nation = ? WHERE pseudo = ?");
            requete.setString(1, this.nation);
            requete.setString(2, pseudo);
            requete.executeUpdate();

            requete.close();
            connexion.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void modifierPv(int pv){
        this.pv = pv;
        try {

            PreparedStatement requete = connexion.prepareStatement("UPDATE joueur SET pv = ? WHERE pseudo = ?");
            requete.setInt(1, this.pv);
            requete.setString(2, pseudo);
            requete.executeUpdate();

            requete.close();
            connexion.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void modifierOrientation(String orientation){
        this.orientation = orientation;
        try {

            PreparedStatement requete = connexion.prepareStatement("UPDATE joueur SET orientation = ? WHERE pseudo = ?");
            requete.setString(1, this.orientation);
            requete.setString(2, pseudo);
            requete.executeUpdate();

            requete.close();
            connexion.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void tir(){
        int positionBalleX = positionX;
        int positionBalleY = positionY;
        if(orientation == "gauche"){
            positionBalleX -= 1;
        }else if(orientation == "droite"){
            positionBalleX += 1;
        }else if(orientation == "haut"){
            positionBalleY += 1;
        }else if(orientation == "bas"){
            positionBalleY -= 1;
        }
        //Balle balle = new Balle(connexion, 0, arme, positionBalleX, positionBalleY);
        //balle.deplacer;
    }
    
}
