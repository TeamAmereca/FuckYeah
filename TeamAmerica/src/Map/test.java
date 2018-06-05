/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Map;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gmarino
 */
public class test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Connection connexion = DriverManager.getConnection("jdbc:mysql://nemrod.ens2m.fr:3306/20172018_s2_vs2_fuckyeah?serverTimezone=UTC", "fuckyeah", "america");   
            //Map yo = new Map(0,connexion);
            //yo.nouvelleMap();
            //yo.miseAJourMap();
            Map yo = new Map(1,connexion);
            System.out.println(yo);
        } catch (SQLException ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
