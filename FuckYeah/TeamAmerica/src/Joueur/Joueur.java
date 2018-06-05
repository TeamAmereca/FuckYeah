package Joueur;

import Balle.Balle;

import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    private Boolean connectePartie;
    private Connection connexion; //contient le lien de connexion au serveur
    
    public Joueur(String pseudo, String nation, Connection connexion){
        positionX = 0;
        positionY = 0;
        this.pseudo = pseudo;
        this.nation = nation;
        orientation = "Droite";
        arme = "M16";
        pv = 0;
        vitesse = 1; //joueur se déplace case par case par défaut
        connectePartie = false;
        this.connexion = connexion;
    }
    
    public Joueur(String pseudo, int positionX, int positionY, int pv,String nation, String orientation, Connection connexion){
        this.positionX = positionX;
        this.positionY = positionY;
        this.pseudo = pseudo;
        this.nation = nation;
        this.orientation = orientation;
        arme = "M16";
        this.pv = pv;
        vitesse = 1; //joueur se déplace case par case par défaut
        connectePartie = true;
        this.connexion = connexion;
    }
    
    public void creerJoueurSQL() throws SQLException { //crée le jouer dans la BDD SQL
            PreparedStatement requete = connexion.prepareStatement("INSERT INTO joueur VALUES (?,?,?,?,?,?,?)");
            requete.setString(1, pseudo);
            requete.setInt(2, positionX);
            requete.setInt(3, positionY);
            requete.setInt(4, pv);
            requete.setString(5, nation);
            requete.setString(6, orientation);
            requete.setBoolean(7, connectePartie);
            requete.executeUpdate();

            requete.close();
    }
    
    public void deleteJoueur(){
        //Added by Morris
        try {
            PreparedStatement requete = connexion.prepareStatement("DELETE FROM joueur WHERE pseudo = ?");
            requete.setString(1, this.pseudo);
            requete.executeUpdate();
            requete.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void modifierStatus(){
        try {
            PreparedStatement requete = connexion.prepareStatement("UPDATE joueur SET status = 1 WHERE pseudo = ?");
            requete.setString(1, this.pseudo);
            requete.executeUpdate();
            requete.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void modifierArme(String nouvelleArme){
        arme = nouvelleArme;
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

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void modifierNation(String nouvelleNation){
        this.nation = nouvelleNation;
        if (nouvelleNation=="America"){
            arme = "M16";
        } else if (nouvelleNation=="Terrorist"){
            arme = "AK47";
        }
        try {

            PreparedStatement requete = connexion.prepareStatement("UPDATE joueur SET nation = ? WHERE pseudo = ?");
            requete.setString(1, this.nation);
            requete.setString(2, pseudo);
            requete.executeUpdate();

            requete.close();

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

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void tir(){
        int positionBalleX = positionX;
        int positionBalleY = positionY;
        if(orientation == "Gauche"){
            positionBalleX -= 1;
        }else if(orientation == "Droite"){
            positionBalleX += 1;
        }else if(orientation == "Haut"){
            positionBalleY += 1;
        }else if(orientation == "Bas"){
            positionBalleY -= 1;
        }
        Balle balle = new Balle(connexion, orientation, arme, positionBalleX, positionBalleY);
        System.out.println(orientation + ' ' + arme + ' ' + positionBalleX + ' ' + positionBalleY);
        //ajouter temps attente avant prochain tir possible (dans DisplayWindow)
    }
    
    public void modifierConnection(){
        if (connectePartie){
            connectePartie = false;
        } else {
            connectePartie = true;
        }
        try {

            PreparedStatement requete = connexion.prepareStatement("UPDATE joueur SET status = ? WHERE pseudo = ?");
            requete.setBoolean(1, this.connectePartie);
            requete.setString(2, pseudo);
            requete.executeUpdate();

            requete.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void deplacer(KeyEvent e){ //déplace le joueur automatiquement en fonction de sa vitesse et de son orientation
        try {
            Connection laconnexion = DriverManager.getConnection("jdbc:mysql://nemrod.ens2m.fr:3306/20172018_s2_vs2_fuckyeah?serverTimezone=UTC", "fuckyeah", "america");
            
            System.out.println("Position x avant appui = "+this.positionX);
            System.out.println("Position y avant appui = "+this.positionY);
            System.out.println("Orientation avant appui = "+this.orientation);
            System.out.println("------------------------------------------");
        
            switch (e.getKeyCode()){
                case (KeyEvent.VK_RIGHT):
                    System.out.println("Touche de droite appuyée");
                    if (this.orientation == "droite"){ 
                        PreparedStatement requeteJoueur = laconnexion.prepareStatement("SELECT * FROM joueur WHERE x = ? AND y = ?");
                        requeteJoueur.setInt(1, this.positionX+1);
                        requeteJoueur.setInt(2, this.positionY);
                        ResultSet resultatJoueur = requeteJoueur.executeQuery();                        
                        
                        PreparedStatement requeteBloc = laconnexion.prepareStatement("SELECT * FROM blocs WHERE positionX = ? AND positionY = ?");
                        requeteBloc.setInt(1, this.positionX+1);
                        requeteBloc.setInt(2, this.positionY);
                        ResultSet resultatBloc = requeteBloc.executeQuery();
                        
                        System.out.println("Vérification de la disponibilité de la case : x = "+(this.positionX+1)+", y = "+this.positionY);
                        
                        if ((resultatJoueur.next()==false) && (resultatBloc.next()==false))  {
                            System.out.println("Aucun joueur et aucun bloc n'est sur la case : x = "+(this.positionX+1)+", y = "+this.positionY);
                            PreparedStatement requeteNouvellesCoordonnees = laconnexion.prepareStatement("UPDATE joueur SET x = ? WHERE pseudo = ?");            
                            requeteNouvellesCoordonnees.setInt(1, this.positionX+1);
                            requeteNouvellesCoordonnees.setString(2, this.pseudo);
                            requeteNouvellesCoordonnees.executeUpdate();
                            requeteNouvellesCoordonnees.close();
                            this.positionX += 1;                // définit la nouvelle position du joueur en local
                            System.out.println("Nouvelles coordonnées de votre joueur : x = "+this.positionX+", y = "+this.positionY);
                        }
                        else{System.out.println("Un joueur ou un bloc est déjà sur la case !");}
                        requeteJoueur.close();
                        requeteBloc.close();
                        System.out.println("Fin du tour");
                        System.out.println("------------------------------------------");
                        System.out.println("------------------------------------------");
                    }
                    else{
                        PreparedStatement requeteNouvelleOrientation = laconnexion.prepareStatement("UPDATE joueur SET orientation = ? WHERE pseudo = ?");            
                        requeteNouvelleOrientation.setString(1, "droite");
                        requeteNouvelleOrientation.setString(2, this.pseudo);
                        requeteNouvelleOrientation.executeUpdate();
                        requeteNouvelleOrientation.close();
                        this.orientation = "droite";
                        System.out.println("Votre joueur est réorienté vers la droite");
                        System.out.println("Fin du tour");
                        System.out.println("------------------------------------------");
                        System.out.println("------------------------------------------");
                    }
                    break;
                    
                case (KeyEvent.VK_LEFT):
                    System.out.println("Touche de gauche appuyée");
                    if (this.orientation == "gauche"){ 
                        PreparedStatement requeteJoueur = laconnexion.prepareStatement("SELECT * FROM joueur WHERE x = ? AND y = ?");
                        requeteJoueur.setInt(1, this.positionX-1);
                        requeteJoueur.setInt(2, this.positionY);
                        ResultSet resultatJoueur = requeteJoueur.executeQuery();                        
                        
                        PreparedStatement requeteBloc = laconnexion.prepareStatement("SELECT * FROM blocs WHERE positionX = ? AND positionY = ?");
                        requeteBloc.setInt(1, this.positionX-1);
                        requeteBloc.setInt(2, this.positionY);
                        ResultSet resultatBloc = requeteBloc.executeQuery();
                        
                        System.out.println("Vérification de la disponibilité de la case : x = "+(this.positionX-1)+", y = "+this.positionY);
                        
                        if ((resultatJoueur.next()==false) && (resultatBloc.next()==false))  {
                            System.out.println("Aucun joueur et aucun bloc n'est sur la case : x = "+(this.positionX-1)+", y = "+this.positionY);
                            PreparedStatement requeteNouvellesCoordonnees = laconnexion.prepareStatement("UPDATE joueur SET x = ? WHERE pseudo = ?");            
                            requeteNouvellesCoordonnees.setInt(1, this.positionX-1);
                            requeteNouvellesCoordonnees.setString(2, this.pseudo);
                            requeteNouvellesCoordonnees.executeUpdate();
                            requeteNouvellesCoordonnees.close();
                            this.positionX -= 1;                // définit la nouvelle position du joueur en local
                            System.out.println("Nouvelles coordonnées de votre joueur : x = "+this.positionX+", y = "+this.positionY);
                        }
                        else{System.out.println("Un joueur ou un bloc est déjà sur la case !");}
                        requeteJoueur.close();
                        requeteBloc.close();
                        System.out.println("Fin du tour");
                        System.out.println("------------------------------------------");
                        System.out.println("------------------------------------------");
                    }
                    else{
                        PreparedStatement requeteNouvelleOrientation = laconnexion.prepareStatement("UPDATE joueur SET orientation = ? WHERE pseudo = ?");            
                        requeteNouvelleOrientation.setString(1, "gauche");
                        requeteNouvelleOrientation.setString(2, this.pseudo);
                        requeteNouvelleOrientation.executeUpdate();
                        requeteNouvelleOrientation.close();
                        this.orientation = "gauche";
                        System.out.println("Votre joueur est réorienté vers la gauche");
                        System.out.println("Fin du tour");
                        System.out.println("------------------------------------------");
                        System.out.println("------------------------------------------");
                    }
                    break;    
        
                case (KeyEvent.VK_DOWN):
                    System.out.println("Touche du bas appuyée");
                    if (this.orientation == "bas"){ 
                        PreparedStatement requeteJoueur = laconnexion.prepareStatement("SELECT * FROM joueur WHERE x = ? AND y = ?");
                        requeteJoueur.setInt(1, this.positionX);
                        requeteJoueur.setInt(2, this.positionY-1);
                        ResultSet resultatJoueur = requeteJoueur.executeQuery();                        
                        
                        PreparedStatement requeteBloc = laconnexion.prepareStatement("SELECT * FROM blocs WHERE positionX = ? AND positionY = ?");
                        requeteBloc.setInt(1, this.positionX);
                        requeteBloc.setInt(2, this.positionY-1);
                        ResultSet resultatBloc = requeteBloc.executeQuery();
                        
                        System.out.println("Vérification de la disponibilité de la case : x = "+this.positionX+", y = "+(this.positionY-1));
                        
                        if ((resultatJoueur.next()==false) && (resultatBloc.next()==false))  {
                            System.out.println("Aucun joueur et aucun bloc n'est sur la case : x = "+this.positionX+", y = "+(this.positionY-1));
                            PreparedStatement requeteNouvellesCoordonnees = laconnexion.prepareStatement("UPDATE joueur SET y = ? WHERE pseudo = ?");            
                            requeteNouvellesCoordonnees.setInt(1, this.positionY-1);
                            requeteNouvellesCoordonnees.setString(2, this.pseudo);
                            requeteNouvellesCoordonnees.executeUpdate();
                            requeteNouvellesCoordonnees.close();
                            this.positionY -= 1;                // définit la nouvelle position du joueur en local
                            System.out.println("Nouvelles coordonnées de votre joueur : x = "+this.positionX+", y = "+this.positionY);
                        }
                        else{System.out.println("Un joueur ou un bloc est déjà sur la case !");}
                        requeteJoueur.close();
                        requeteBloc.close();
                        System.out.println("Fin du tour");
                        System.out.println("------------------------------------------");
                        System.out.println("------------------------------------------");
                    }
                    else{
                        PreparedStatement requeteNouvelleOrientation = laconnexion.prepareStatement("UPDATE joueur SET orientation = ? WHERE pseudo = ?");            
                        requeteNouvelleOrientation.setString(1, "bas");
                        requeteNouvelleOrientation.setString(2, this.pseudo);
                        requeteNouvelleOrientation.executeUpdate();
                        requeteNouvelleOrientation.close();
                        this.orientation = "bas";
                        System.out.println("Votre joueur est réorienté vers le bas");
                        System.out.println("Fin du tour");
                        System.out.println("------------------------------------------");
                        System.out.println("------------------------------------------");
                    }
                    break;

                case (KeyEvent.VK_UP):
                    System.out.println("Touche du haut appuyée");
                    if (this.orientation == "haut"){ 
                        PreparedStatement requeteJoueur = laconnexion.prepareStatement("SELECT * FROM joueur WHERE x = ? AND y = ?");
                        requeteJoueur.setInt(1, this.positionX);
                        requeteJoueur.setInt(2, this.positionY+1);
                        ResultSet resultatJoueur = requeteJoueur.executeQuery();                        
                        
                        PreparedStatement requeteBloc = laconnexion.prepareStatement("SELECT * FROM blocs WHERE positionX = ? AND positionY = ?");
                        requeteBloc.setInt(1, this.positionX);
                        requeteBloc.setInt(2, this.positionY+1);
                        ResultSet resultatBloc = requeteBloc.executeQuery();
                        
                        System.out.println("Vérification de la disponibilité de la case : x = "+this.positionX+", y = "+(this.positionY+1));
                        
                        if ((resultatJoueur.next()==false) && (resultatBloc.next()==false))  {
                            System.out.println("Aucun joueur et aucun bloc n'est sur la case : x = "+this.positionX+", y = "+(this.positionY+1));
                            PreparedStatement requeteNouvellesCoordonnees = laconnexion.prepareStatement("UPDATE joueur SET y = ? WHERE pseudo = ?");            
                            requeteNouvellesCoordonnees.setInt(1, this.positionY+1);
                            requeteNouvellesCoordonnees.setString(2, this.pseudo);
                            requeteNouvellesCoordonnees.executeUpdate();
                            requeteNouvellesCoordonnees.close();
                            this.positionY += 1;                // définit la nouvelle position du joueur en local
                            System.out.println("Nouvelles coordonnées de votre joueur : x = "+this.positionX+", y = "+this.positionY);
                        }
                        else{System.out.println("Un joueur ou un bloc est déjà sur la case !");}
                        requeteJoueur.close();
                        requeteBloc.close();
                        System.out.println("Fin du tour");
                        System.out.println("------------------------------------------");
                        System.out.println("------------------------------------------");
                    }
                    else{
                        PreparedStatement requeteNouvelleOrientation = laconnexion.prepareStatement("UPDATE joueur SET orientation = ? WHERE pseudo = ?");            
                        requeteNouvelleOrientation.setString(1, "haut");
                        requeteNouvelleOrientation.setString(2, this.pseudo);
                        requeteNouvelleOrientation.executeUpdate();
                        requeteNouvelleOrientation.close();
                        this.orientation = "haut";
                        System.out.println("Votre joueur est réorienté vers le haut");
                        System.out.println("Fin du tour");
                        System.out.println("------------------------------------------");
                        System.out.println("------------------------------------------");
                    }
                    break;        
            }
            laconnexion.close();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        } 
//        System.out.println("position en y : "+this.positionY);
//        System.out.println("position en x : "+this.positionX);
//        System.out.println("orientation : "+this.orientation);
        
        
//        
//        
//        
//        // a faire
//                try {
//
//            Connection connexion = DriverManager.getConnection("jdbc:mysql://nemrod.ens2m.fr:3306/20172018_s2_vs2_fuckyeah=UTC", "fuckyeah", "america");
//            PreparedStatement requete = connexion.prepareStatement("UPDATE joueur SET x = ?, y = ? WHERE pseudo = G.W. Bush");
//            requete.setString(1, "0");
//            requete.setString(2, "Dracaufeu Caché ;)");
//            requete.setDouble(3, 0.2);
//            requete.setDouble(4, 0.1);
//            requete.setBoolean(5, false);  
//            requete.setString(6, "Personne");
//            System.out.println(requete);
//            requete.executeUpdate();
//
//            requete.close();
//            connexion.close();
//
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        } 
    }

    public String getPseudo() {
        return pseudo;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }
    
    
}
