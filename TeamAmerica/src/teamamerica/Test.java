/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teamamerica;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author guillaume.laurent
 */
public class Test {

    public static void main(String[] args) {

        try {

            Connection connexion = DriverManager.getConnection("jdbc:mysql://nemrod.ens2m.fr:3306/20172018_s2_vs2_fuckyeah?serverTimezone=UTC", "fuckyeah", "america");

//            PreparedStatement requete = connexion.prepareStatement("INSERT INTO joueur VALUES (?,?,?,?,?,?)");
//            requete.setString(1, "Team Rock");
//            requete.setString(2, "senvolevers@dautrescieux.tingg");
//            requete.setString(3, OutilsJDBC.MD5("onix"));
//            requete.setDouble(4, +251430.21);
//            requete.setDouble(5, +54155712.1);  
//            requete.setString(6, "2018-03-06 16:19:11");
//            System.out.println(requete);
//            requete.executeUpdate();

            PreparedStatement requete = connexion.prepareStatement("INSERT INTO blocs VALUES (?,?,?)");
            requete.setInt(1, 0);
            requete.setInt(2, 0);
            requete.setBoolean(3, false);
            System.out.println(requete);
            requete.executeUpdate();

            requete.close();
            connexion.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

}
