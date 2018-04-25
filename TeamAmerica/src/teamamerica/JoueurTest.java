//Written by Clément
package teamamerica;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JoueurTest {

    public static void main(String[] args) throws SQLException {
        Connection connexion = DriverManager.getConnection("jdbc:mysql://nemrod.ens2m.fr:3306/20172018_s2_vs2_fuckyeah?serverTimezone=UTC", "fuckyeah", "america");
        Joueur joueur = new Joueur("joueur1",connexion);
        //joueur.creerJoueurSQL();
        //joueur.modifierOrientation("gauche");
        //joueur.modifierPv(10);
        //joueur.modifierNation("terroriste");
        joueur.modifierPseudo("G.W. Bush");
        
    }
    
}
