//written by Guillaume
package teamamerica;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentListener;

/*
 * @author Guillaume
 */
public class JoueurByGuillaume {
    private int positionX;
    private int positionY;
    private String pseudo;
    private String nation;
    private String orientation;
    private String arme;
    private int pv;
    private int vitesse;
    private int connectePartie;

    public JoueurByGuillaume(int positionX, int positionY, String pseudo, String nation, String orientation, String arme, int pv, int vitesse, int connectePartie) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.pseudo = pseudo;
        this.nation = nation;
        this.orientation = orientation;
        this.arme = arme;
        this.pv = pv;
        this.vitesse = vitesse;
        this.connectePartie = connectePartie;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getNation() {
        return nation;
    }

    public String getOrientation() {
        return orientation;
    }

    public String getArme() {
        return arme;
    }

    public int getPv() {
        return pv;
    }

    public int getVitesse() {
        return vitesse;
    }

    public int getConnectePartie() {
        return connectePartie;
    }
    
    public void deplacer(KeyEvent e){ //déplace le joueur automatiquement en fonction de sa vitesse et de son orientation
        switch (e.getKeyCode()){
            case (KeyEvent.VK_RIGHT):
                if (this.orientation == "droite")
                {
                    this.positionX += 1;
                }
                else 
                {
                    this.orientation = "droite";
                }
                break;
            case (KeyEvent.VK_LEFT):
                if (this.orientation == "gauche")
                {
                    this.positionX -= 1;
                } 
                else 
                {
                    this.orientation = "gauche";
                }
                break;
            case (KeyEvent.VK_DOWN):
                if (this.orientation == "bas")
                {
                    this.positionY -= 1;
                } 
                else 
                {
                    this.orientation = "bas";
                }
                break;
            case (KeyEvent.VK_UP):
                if (this.orientation == "haut")
                {
                    this.positionY += 1;
                } 
                else 
                {
                    this.orientation = "haut";
                }
                break;
        }
        System.out.println("position en y : "+this.positionY);
        System.out.println("position en x : "+this.positionX);
        System.out.println("orientation : "+this.orientation);
        System.out.println("CA MARCHEEEEEEEEEEEEEEE !!!!!!!!!!!!!!!!!!!");
        
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

            
}
