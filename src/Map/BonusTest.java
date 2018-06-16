package Map;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BonusTest {

    public static void main(String[] args) throws SQLException {
        Connection connexion = DriverManager.getConnection("jdbc:mysql://nemrod.ens2m.fr:3306/20172018_s2_vs2_fuckyeah?serverTimezone=UTC", "fuckyeah", "america");
        Bonus bonus = new Bonus(8,10,1,connexion);
        //bonus.creerBonusSQL();
        bonus.deleteBonus();
        connexion.close();
    }
    
}
