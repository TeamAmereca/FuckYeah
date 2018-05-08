package Database;

import Balle.Balle;
import Joueur.Joueur;
import Map.Map;
import java.awt.List;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Morris
 */
public class Write_Read {
    private Connection connection;
    private Joueur mainPlayer;
    private ArrayList<Joueur> players;
    private ArrayList<Integer> ballesX;
    private ArrayList<Integer> ballesY;
    private ArrayList<String> ballesO;
    private Map map;
    
    public Write_Read() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://nemrod.ens2m.fr:3306/20172018_s2_vs2_fuckyeah?serverTimezone=UTC", "fuckyeah", "america");
        } catch (SQLException ex) {
            Logger.getLogger(Write_Read.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void refreshDataBase() {
       refreshPlayer(this.mainPlayer);
       for(int i=0;i<players.size();i++){
            refreshPlayer(players.get(i));
        }
       refreshBalle();
    }
    public void refreshPlayer(Joueur joueur) {
        try {
            PreparedStatement requete = connection.prepareStatement("SELECT x, y, pv, orientation FROM joueur WHERE pseudo = ?");
            requete.setString(1,joueur.getPseudo());
            ResultSet resultat = requete.executeQuery();
            resultat.next();
            int positionX = resultat.getInt("x");
            int positionY = resultat.getInt("y");
            int pv = resultat.getInt("pv");System.out.println("refresh "+joueur.getPseudo()+":"+positionX+";"+positionY+";"+pv);
            String orientation = resultat.getString("orientation");
            joueur.setPositionX(positionX);
            joueur.setPositionY(positionY);
            joueur.setPv(pv);
            joueur.setOrientation(orientation);
            requete.close();
        } catch (SQLException ex) {
            Logger.getLogger(Write_Read.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void refreshBalle() {
        try {
            this.ballesX = new ArrayList<Integer>();
            this.ballesY = new ArrayList<Integer>();
            this.ballesO = new ArrayList<String>();
            PreparedStatement requete = connection.prepareStatement("SELECT * FROM balle");
            ResultSet resultat = requete.executeQuery();
            while (resultat.next()) {
                String orientation = resultat.getString("orientation");
                String type = resultat.getString("type");
                int positionx = resultat.getInt("positionx");
                int numero = resultat.getInt("numero");
                int positiony = resultat.getInt("positiony");
                
                this.ballesX.add(positionx);
                this.ballesY.add(positiony);
                this.ballesO.add(orientation);
            }       
            requete.close();            
        } catch (SQLException ex) {
            Logger.getLogger(Write_Read.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void createMainPlayer(String pseudo, String nation) throws SQLException{
        //create an instance of joueur in the database
        mainPlayer = new Joueur(pseudo, nation, connection);
        mainPlayer.creerJoueurSQL();
    }
    
    public void createNewMap() {
        this.map = new Map(0, connection);//create a new map
    }
    
    public void getWaitingPlayers( DefaultTableModel model) {
        try {
            model.setRowCount(0);
            PreparedStatement requete = connection.prepareStatement("SELECT pseudo,nation FROM joueur WHERE status=0");
            ResultSet resultat = requete.executeQuery();

            while (resultat.next()) {
                String pseudo = resultat.getString("pseudo");
                String nation = resultat.getString("nation");
                model.addRow(new Object[]{pseudo,nation});
            }       
            requete.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void enoughPlayers(DefaultTableModel model,int number) {
        try {
            model.setRowCount(0);
            PreparedStatement requete = connection.prepareStatement("SELECT pseudo,nation FROM joueur WHERE status = 0");
            ResultSet resultat = requete.executeQuery();
            while (resultat.next()) {
                String pseudo = resultat.getString("pseudo");
                String nation = resultat.getString("nation");
                model.addRow(new Object[]{pseudo,nation});
            }
            requete.close();
        } catch (SQLException ex) {
            Logger.getLogger(Write_Read.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void modifyPlayersStatus() {
         //change the status of every player
        for(int i=0; i<players.size();i++) {
            players.get(i).modifierStatus();
        }
        this.mainPlayer.modifierStatus();
    }
    
    public boolean isMainPayerStatusChanged() {
        boolean  isStatusChanged = false;
        try {
            PreparedStatement requete = connection.prepareStatement("SELECT status FROM joueur WHERE pseudo = ?");
            requete.setString(1,this.mainPlayer.getPseudo());
            ResultSet resultat = requete.executeQuery();
            resultat.next();
            isStatusChanged = resultat.getBoolean("status");
            requete.close();
        } catch (SQLException ex) {
            Logger.getLogger(Write_Read.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isStatusChanged;
    }
    
    public Joueur getMainPlayer() {
        return mainPlayer;
    }

    public ArrayList<Joueur> getPlayers() {
        return players;
    }

    public Connection getConnection() {
        return connection;
    }

    public Map getMap() {
        return map;
    }

    public void setMainPlayer(Joueur mainPlayer) {
        this.mainPlayer = mainPlayer;
    }

    public void setPlayers(ArrayList<Joueur> players) {
        this.players = players;
    }
    
    public void initializePlayers(DefaultTableModel model) {
        //initialize the variable players
        //assign each player to a different place
        this.players = new ArrayList();
        for(int i=0;i<model.getRowCount();i++) {
            String pseudo = (String)model.getValueAt(i, 0);
            try {
                if(!pseudo.equals(this.mainPlayer.getPseudo())) {                
                    //write in data
                    //Set the initial position for other players
                    PreparedStatement requeteWrite = connection.prepareStatement("UPDATE joueur SET x = ?, y = ? WHERE pseudo = ?");
                    requeteWrite.setInt(1,50*20);//improve
                    requeteWrite.setInt(2,50*13);//improve
                    requeteWrite.setString(3, pseudo);
                    requeteWrite.executeUpdate();
                    requeteWrite.close();
                    
                    //get the data
                    PreparedStatement requete = connection.prepareStatement("SELECT * FROM joueur WHERE pseudo = ?");
                    requete.setString(1, pseudo);
                    ResultSet resultat = requete.executeQuery();
                    resultat.next();//we are sure that this 'resultat' exists
                    int positionX = resultat.getInt("x");
                    int positionY = resultat.getInt("y");
                    int pv = resultat.getInt("pv");
                    String nation = resultat.getString("nation");
                    String orientation = resultat.getString("orientation");
                    this.players.add(new Joueur(pseudo, positionX, positionY, pv, nation, orientation, this.connection));              
                    requete.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Write_Read.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //get the data of the main player
        try {
            PreparedStatement requete = connection.prepareStatement("SELECT x, y, orientation FROM joueur WHERE pseudo = ?");
            requete.setString(1, this.mainPlayer.getPseudo());
            ResultSet resultat = requete.executeQuery();
            resultat.next();//we are sure that this 'resultat' exists
            int positionX = resultat.getInt("x");
            int positionY = resultat.getInt("y");
            String orientation = resultat.getString("orientation");                   
            if(this.mainPlayer.getPositionX() != positionX && this.mainPlayer.getPositionY() != positionY) {
                //Update the main player info if not the latest
                this.mainPlayer.setPositionX(positionX);
                this.mainPlayer.setPositionY(positionY);
                this.mainPlayer.setOrientation(orientation);                        
            }
            requete.close();
        } catch (SQLException ex) {
            Logger.getLogger(Write_Read.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public ArrayList<Integer> getBallesX() {
        return ballesX;
    }

    public ArrayList<Integer> getBallesY() {
        return ballesY;
    }

    public ArrayList<String> getBallesO() {
        return ballesO;
    }

}
