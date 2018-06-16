package Joueur;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JoueurTest {

    public static void main(String[] args) throws SQLException {
        Connection connexion = DriverManager.getConnection("jdbc:mysql://nemrod.ens2m.fr:3306/20172018_s2_vs2_fuckyeah?serverTimezone=UTC", "fuckyeah", "america");
        //Joueur joueur = new Joueur("G.W. Bush",connexion);
        Joueur joueur = new Joueur("joueurTest", 2 ,6 , 10, "America", "Droite", connexion, true);
        joueur.creerJoueurSQL();
        //joueur.modifierOrientation("Gauche");
        //joueur.modifierPv(10);
        //joueur.modifierNation("America");
        //joueur.tir();
        //joueur.tir();
        
        connexion.close();
    }
    
}
