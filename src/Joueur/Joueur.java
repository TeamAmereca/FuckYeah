package Joueur;

import Balle.Balle;
import Bonus.Bonus;

import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

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
    private Boolean peutTirer;
    private boolean peutSeDeplacer;
    
    public Joueur(String pseudo, String nation, Connection connexion){
        positionX = 0;
        positionY = 0;
        this.pseudo = pseudo;
        this.nation = nation;
        orientation = "Bas";
        if ("America".equals(nation)){
            arme = "M16";
        } else if ("Terrorist".equals(nation)){
            arme = "Gun";
        }
        pv = 0;
        vitesse = 1; //joueur se déplace case par case par défaut
        connectePartie = false;
        this.connexion = connexion;
        peutTirer = true;
        peutSeDeplacer = true;
    }
    
    public Joueur(String pseudo, int positionX, int positionY, int pv, String nation, String orientation, Connection connexion, Boolean peutSeDeplacer){
        this.positionX = positionX;
        this.positionY = positionY;
        this.pseudo = pseudo;
        this.nation = nation;
        this.orientation = orientation;
        if ("America".equals(nation)){
            arme = "M16";
        } else if ("Terrorist".equals(nation)){
            arme = "Gun";
        }
        this.pv = pv;
        vitesse = 1; //joueur se déplace case par case par défaut
        connectePartie = true;
        this.connexion = connexion;
        peutTirer = true;
        this.peutSeDeplacer = peutSeDeplacer;
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
        if ("America".equals(nouvelleNation)){
            arme = "M16";
        } else if ("Terrorist".equals(nouvelleNation)){
            arme = "Gun";
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
        System.out.println("vérification avant de tirer : peutTirer = " + peutTirer);
        if (peutTirer){
            Balle balle = new Balle(connexion, orientation, arme, positionX, positionY, this.pseudo);
            System.out.println("Balle : " + orientation + ' ' + arme + ' ' + positionX + ' ' + positionY);
            peutTirer = false;
            //temps attente avant prochain tir possible
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    peutTirer=true;
                }
            };
            Timer timer = new Timer();
            timer.schedule(timerTask,800); //empêche de tirer pendant 2s
            //timerTask.cancel();
            //timer.cancel();
            //peutTirer = true;
            System.out.println("tir effectué et temps d'attente terminé : peutTirer = " + peutTirer);
        } else if (peutTirer==false){ //pour tests, à supprimer ensuite
            System.out.println("temps d'attente non écoulé");
        }
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
        //System.out.println("deplacement : "+peutSeDeplacer);
        if (this.peutSeDeplacer){
            try {
                //Connection connexion = DriverManager.getConnection("jdbc:mysql://nemrod.ens2m.fr:3306/20172018_s2_vs2_fuckyeah?serverTimezone=UTC", "fuckyeah", "america");
                System.out.println("Position x avant appui = "+this.positionX);
                System.out.println("Position y avant appui = "+this.positionY);
                System.out.println("Orientation avant appui = "+this.orientation);
                System.out.println("------------------------------------------");
                
                switch (e.getKeyCode()){
                    case (KeyEvent.VK_RIGHT):
                        System.out.println("Touche de Droite appuyée");
                        if (this.orientation.equals("Droite")){ 
                            PreparedStatement requeteBloc = connexion.prepareStatement("SELECT * FROM blocs WHERE positionX = ? AND positionY = ?");
                            requeteBloc.setInt(1, this.positionX+1);
                            requeteBloc.setInt(2, this.positionY);
                            ResultSet resultatBloc = requeteBloc.executeQuery();

                            PreparedStatement requeteJoueur = connexion.prepareStatement("SELECT * FROM joueur WHERE x = ? AND y = ?");
                            requeteJoueur.setInt(1, this.positionX+1);
                            requeteJoueur.setInt(2, this.positionY);
                            ResultSet resultatJoueur = requeteJoueur.executeQuery(); 
                            
                            System.out.println("Vérification de la disponibilité de la case : x = "+(this.positionX+1)+", y = "+this.positionY);
                            
                            if ((resultatJoueur.next()==false) && (resultatBloc.next()==false))  {
                                System.out.println("Aucun joueur et aucun bloc n'est sur la case : x = "+(this.positionX+1)+", y = "+this.positionY);
                                PreparedStatement requeteNouvellesCoordonnees = connexion.prepareStatement("UPDATE joueur SET x = ? WHERE pseudo = ?");            
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
                           
                            // Gestion du temps de pause après déplacement
                            peutSeDeplacer = false; 
                            
                            // Bonus
                            PreparedStatement requeteBonus = connexion.prepareStatement("SELECT * FROM bonus WHERE positionX = ? AND positionY = ?");
                            requeteBonus.setInt(1, this.positionX);
                            requeteBonus.setInt(2, this.positionY);
                            ResultSet resultatBonus = requeteBonus.executeQuery();
                            if(resultatBonus.next()==true){
                                PreparedStatement requeteType = connexion.prepareStatement("SELECT type FROM bonus WHERE positionX = ? AND positionY = ?");
                                ResultSet resultat = requeteType.executeQuery();
                                int type = resultat.getInt("type");
                                requeteType.close();
                                Bonus bonus = new Bonus(positionX,positionY,type,connexion);
                                bonus.deleteBonus();
                                this.bonus(type);
                            }
                            
                            TimerTask timerTask = new TimerTask() {
                                @Override
                                public void run() {
                                    peutSeDeplacer = true;
                                }             
                            };             
                            Timer timer = new Timer();
                            timer.schedule(timerTask,500); //empêche de se déplacer pendant 2s
                            
                            System.out.println("Fin du tour");
                            System.out.println("------------------------------------------");
                            System.out.println("------------------------------------------");
                        }
                        else{
                            PreparedStatement requeteNouvelleOrientation = connexion.prepareStatement("UPDATE joueur SET orientation = ? WHERE pseudo = ?");            
                            requeteNouvelleOrientation.setString(1, "Droite");
                            requeteNouvelleOrientation.setString(2, this.pseudo);
                            requeteNouvelleOrientation.executeUpdate();
                            requeteNouvelleOrientation.close();
                            this.orientation = "Droite";
                            
                            // Gestion du temps de pause après orientation
                            peutSeDeplacer = false; 
                            TimerTask timerTask = new TimerTask() {
                                @Override
                                public void run() {
                                    peutSeDeplacer = true;                 
                                }             
                            };             
                            Timer timer = new Timer();
                            timer.schedule(timerTask,100); //empêche de se déplacer pendant 1s
                            
                            System.out.println("Votre joueur est réorienté vers la Droite");
                            System.out.println("Fin du tour");
                            System.out.println("------------------------------------------");
                            System.out.println("------------------------------------------");
                        }
                        break;

                    case (KeyEvent.VK_LEFT):
                        System.out.println("Touche de Gauche appuyée");
                        if (this.orientation.equals("Gauche")){ 
                            PreparedStatement requeteJoueur = connexion.prepareStatement("SELECT * FROM joueur WHERE x = ? AND y = ?");
                            requeteJoueur.setInt(1, this.positionX-1);
                            requeteJoueur.setInt(2, this.positionY);
                            ResultSet resultatJoueur = requeteJoueur.executeQuery();                        

                            PreparedStatement requeteBloc = connexion.prepareStatement("SELECT * FROM blocs WHERE positionX = ? AND positionY = ?");
                            requeteBloc.setInt(1, this.positionX-1);
                            requeteBloc.setInt(2, this.positionY);
                            ResultSet resultatBloc = requeteBloc.executeQuery();

                            System.out.println("Vérification de la disponibilité de la case : x = "+(this.positionX-1)+", y = "+this.positionY);

                            if ((resultatJoueur.next()==false) && (resultatBloc.next()==false))  {
                                System.out.println("Aucun joueur et aucun bloc n'est sur la case : x = "+(this.positionX-1)+", y = "+this.positionY);
                                PreparedStatement requeteNouvellesCoordonnees = connexion.prepareStatement("UPDATE joueur SET x = ? WHERE pseudo = ?");            
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
                            
                            // Gestion du temps de pause après déplacement
                            peutSeDeplacer = false; 
                            
                            // Bonus
                            PreparedStatement requeteBonus = connexion.prepareStatement("SELECT * FROM bonus WHERE positionX = ? AND positionY = ?");
                            requeteBonus.setInt(1, this.positionX);
                            requeteBonus.setInt(2, this.positionY);
                            ResultSet resultatBonus = requeteBonus.executeQuery();
                            if(resultatBonus.next()==true){
                                PreparedStatement requeteType = connexion.prepareStatement("SELECT type FROM bonus WHERE positionX = ? AND positionY = ?");
                                ResultSet resultat = requeteType.executeQuery();
                                int type = resultat.getInt("type");
                                requeteType.close();
                                Bonus bonus = new Bonus(positionX,positionY,type,connexion);
                                bonus.deleteBonus();
                                this.bonus(type);
                            }
                            
                           
                            TimerTask timerTask = new TimerTask() {
                                @Override
                                public void run() {
                                    peutSeDeplacer = true;                 
                                }             
                            };             
                            Timer timer = new Timer();
                            timer.schedule(timerTask,250); //empêche de se déplacer pendant 2s
                            
                            System.out.println("Fin du tour");
                            System.out.println("------------------------------------------");
                            System.out.println("------------------------------------------");
                        }
                        else{
                            PreparedStatement requeteNouvelleOrientation = connexion.prepareStatement("UPDATE joueur SET orientation = ? WHERE pseudo = ?");            
                            requeteNouvelleOrientation.setString(1, "Gauche");
                            requeteNouvelleOrientation.setString(2, this.pseudo);
                            requeteNouvelleOrientation.executeUpdate();
                            requeteNouvelleOrientation.close();
                            this.orientation = "Gauche";
                            
                            // Gestion du temps de pause après orientation
                            peutSeDeplacer = false; 
                            TimerTask timerTask = new TimerTask() {
                                @Override
                                public void run() {
                                    peutSeDeplacer = true;                 
                                }             
                            };             
                            Timer timer = new Timer();
                            timer.schedule(timerTask,100); //empêche de se déplacer pendant 1s
                            
                            System.out.println("Votre joueur est réorienté vers la Gauche");
                            System.out.println("Fin du tour");
                            System.out.println("------------------------------------------");
                            System.out.println("------------------------------------------");
                        }
                        break;    

                    case (KeyEvent.VK_UP):
                        System.out.println("Touche du Haut appuyée");
                        if (this.orientation.equals("Haut")){ 
                            PreparedStatement requeteJoueur = connexion.prepareStatement("SELECT * FROM joueur WHERE x = ? AND y = ?");
                            requeteJoueur.setInt(1, this.positionX);
                            requeteJoueur.setInt(2, this.positionY-1);
                            ResultSet resultatJoueur = requeteJoueur.executeQuery();                        

                            PreparedStatement requeteBloc = connexion.prepareStatement("SELECT * FROM blocs WHERE positionX = ? AND positionY = ?");
                            requeteBloc.setInt(1, this.positionX);
                            requeteBloc.setInt(2, this.positionY-1);
                            ResultSet resultatBloc = requeteBloc.executeQuery();

                            System.out.println("Vérification de la disponibilité de la case : x = "+this.positionX+", y = "+(this.positionY-1));

                            if ((resultatJoueur.next()==false) && (resultatBloc.next()==false))  {
                                System.out.println("Aucun joueur et aucun bloc n'est sur la case : x = "+this.positionX+", y = "+(this.positionY-1));
                                PreparedStatement requeteNouvellesCoordonnees = connexion.prepareStatement("UPDATE joueur SET y = ? WHERE pseudo = ?");            
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
                            
                            // Gestion du temps de pause après déplacement
                            peutSeDeplacer = false; 
                            
                            // Bonus
                            PreparedStatement requeteBonus = connexion.prepareStatement("SELECT * FROM bonus WHERE positionX = ? AND positionY = ?");
                            requeteBonus.setInt(1, this.positionX);
                            requeteBonus.setInt(2, this.positionY);
                            ResultSet resultatBonus = requeteBonus.executeQuery();
                            if(resultatBonus.next()==true){
                                PreparedStatement requeteType = connexion.prepareStatement("SELECT type FROM bonus WHERE positionX = ? AND positionY = ?");
                                ResultSet resultat = requeteType.executeQuery();
                                int type = resultat.getInt("type");
                                requeteType.close();
                                Bonus bonus = new Bonus(positionX,positionY,type,connexion);
                                bonus.deleteBonus();
                                this.bonus(type);
                            }
                            
                            
                            TimerTask timerTask = new TimerTask() {
                                @Override
                                public void run() {
                                    peutSeDeplacer = true;                 
                                }             
                            };             
                            Timer timer = new Timer();
                            timer.schedule(timerTask,250); //empêche de se déplacer pendant 2s
                            
                            System.out.println("Fin du tour");
                            System.out.println("------------------------------------------");
                            System.out.println("------------------------------------------");
                        }
                        else{
                            PreparedStatement requeteNouvelleOrientation = connexion.prepareStatement("UPDATE joueur SET orientation = ? WHERE pseudo = ?");            
                            requeteNouvelleOrientation.setString(1, "Haut");
                            requeteNouvelleOrientation.setString(2, this.pseudo);
                            requeteNouvelleOrientation.executeUpdate();
                            requeteNouvelleOrientation.close();
                            this.orientation = "Haut";
                            
                            // Gestion du temps de pause après orientation
                            peutSeDeplacer = false; 
                            TimerTask timerTask = new TimerTask() {
                                @Override
                                public void run() {
                                    peutSeDeplacer = true;                 
                                }             
                            };             
                            Timer timer = new Timer();
                            timer.schedule(timerTask,100); //empêche de se déplacer pendant 1s
                            
                            System.out.println("Votre joueur est réorienté vers le Bas");
                            System.out.println("Fin du tour");
                            System.out.println("------------------------------------------");
                            System.out.println("------------------------------------------");
                        }
                        break;

                    case (KeyEvent.VK_DOWN):
                        System.out.println("Touche du Bas appuyée");
                        if (this.orientation.equals("Bas")){ 
                            PreparedStatement requeteJoueur = connexion.prepareStatement("SELECT * FROM joueur WHERE x = ? AND y = ?");
                            requeteJoueur.setInt(1, this.positionX);
                            requeteJoueur.setInt(2, this.positionY+1);
                            ResultSet resultatJoueur = requeteJoueur.executeQuery();                        

                            PreparedStatement requeteBloc = connexion.prepareStatement("SELECT * FROM blocs WHERE positionX = ? AND positionY = ?");
                            requeteBloc.setInt(1, this.positionX);
                            requeteBloc.setInt(2, this.positionY+1);
                            ResultSet resultatBloc = requeteBloc.executeQuery();

                            System.out.println("Vérification de la disponibilité de la case : x = "+this.positionX+", y = "+(this.positionY+1));

                            if ((resultatJoueur.next()==false) && (resultatBloc.next()==false))  {
                                System.out.println("Aucun joueur et aucun bloc n'est sur la case : x = "+this.positionX+", y = "+(this.positionY+1));
                                PreparedStatement requeteNouvellesCoordonnees = connexion.prepareStatement("UPDATE joueur SET y = ? WHERE pseudo = ?");            
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
                            
                            // Gestion du temps de pause après déplacement
                            peutSeDeplacer = false; 
                            
                            // Bonus
                            PreparedStatement requeteBonus = connexion.prepareStatement("SELECT * FROM bonus WHERE positionX = ? AND positionY = ?");
                            requeteBonus.setInt(1, this.positionX);
                            requeteBonus.setInt(2, this.positionY);
                            ResultSet resultatBonus = requeteBonus.executeQuery();
                            if(resultatBonus.next()==true){
                                PreparedStatement requeteType = connexion.prepareStatement("SELECT type FROM bonus WHERE positionX = ? AND positionY = ?");
                                ResultSet resultat = requeteType.executeQuery();
                                int type = resultat.getInt("type");
                                requeteType.close();
                                Bonus bonus = new Bonus(positionX,positionY,type,connexion);
                                bonus.deleteBonus();
                                this.bonus(type);
                            }
                            
                            
                            TimerTask timerTask = new TimerTask() {
                                @Override
                                public void run() {
                                    peutSeDeplacer = true;                 
                                }             
                            };             
                            Timer timer = new Timer();
                            timer.schedule(timerTask,250); //empêche de se déplacer pendant 2s
                            
                            System.out.println("Fin du tour");
                            System.out.println("------------------------------------------");
                            System.out.println("------------------------------------------");
                        }
                        else{
                            PreparedStatement requeteNouvelleOrientation = connexion.prepareStatement("UPDATE joueur SET orientation = ? WHERE pseudo = ?");            
                            requeteNouvelleOrientation.setString(1, "Bas");
                            requeteNouvelleOrientation.setString(2, this.pseudo);
                            requeteNouvelleOrientation.executeUpdate();
                            requeteNouvelleOrientation.close();
                            this.orientation = "Bas";
                            
                            // Gestion du temps de pause après orientation
                            peutSeDeplacer = false; 
                            TimerTask timerTask = new TimerTask() {
                                @Override
                                public void run() {
                                    peutSeDeplacer = true;                 
                                }             
                            };             
                            Timer timer = new Timer();
                            timer.schedule(timerTask,100); //empêche de se déplacer pendant 1s
                            
                            System.out.println("Votre joueur est réorienté vers le Haut");
                            System.out.println("Fin du tour");
                            System.out.println("------------------------------------------");
                            System.out.println("------------------------------------------");
                        }
                        break;        
                }
            
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
        
    public String getPseudo() {
        return pseudo;
    }

    public String getNation() {
        return nation;
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

    public int getPv() {
        return pv;
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
    
    public void bonus(int i){
        if(i==1){
            this.bonusVitesse();
        }else if(i==2){
            this.bonusTeteChercheuse();
        }
    }
    
    public void bonusVitesse(){
        vitesse+=0.2;
    }
    
    public void bonusTeteChercheuse(){
        arme = "Gun";
    }
    
    public void bonusVie(){
        pv+=30;
    }
    
}
