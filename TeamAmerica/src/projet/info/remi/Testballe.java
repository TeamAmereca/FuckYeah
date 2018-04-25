/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.info.remi;

import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.Timer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ranger
 */
public class Testballe {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        try {
            Connection connexion = DriverManager.getConnection("jdbc:mysql://nemrod.ens2m.fr:3306/20172018_s2_vs2_fuckyeah?serverTimezone=UTC", "fuckyeah", "america");
            Balle balle1=new Balle(connexion, "Droite", "AK47", 14, 9);
            System.out.println("Presser la touch" + "e ENTREE pour terminer le programme.");
        System.in.read();
        System.exit(0);
        connexion.close();
            
            // TODO code application logic here
        } catch (SQLException ex) {
            Logger.getLogger(Testballe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
