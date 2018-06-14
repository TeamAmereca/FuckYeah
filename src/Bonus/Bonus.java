package Bonus;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;

public class Bonus {
    private int type;
    private int positionX;
    private int positionY;
    private Connection connexion;
    
    public Bonus(int positionX, int positionY, int type, Connection connexion) throws SQLException{
        this.positionX = positionX;
        this.positionY = positionY;
        this.type = type;
        this.connexion = connexion;
    }
    
    public void creerBonusSQL() throws SQLException { //crée le bonus dans la BDD SQL
            PreparedStatement requete = connexion.prepareStatement("INSERT INTO bonus VALUES (?,?,?)");
            requete.setInt(1, positionX);
            requete.setInt(2, positionY);
            requete.setInt(3, type);
            requete.executeUpdate();

            requete.close();
    }
    
    public void deleteBonus(){
        try {
            PreparedStatement requete = connexion.prepareStatement("DELETE FROM bonus WHERE positionX = ? AND positionY = ?");
            requete.setInt(1, this.positionX);
            requete.setInt(2, this.positionY);
            requete.executeUpdate();
            requete.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int getType() {
        return type;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }
    
}
