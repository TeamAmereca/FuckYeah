package Database;

import Joueur.Joueur;
import Map.Map;
import Map.Bloc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
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
    
    private int blockLength;
    private int blockXNumber;
    private int blockYNumber;
    
    
    public Write_Read(int blockLength, int blockXNumber, int blockYNumber) {
        this.blockLength = blockLength;
        this.blockXNumber = blockXNumber;
        this.blockYNumber = blockYNumber;
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
       refreshMap();
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
    public void refreshMap(){
        try {
            PreparedStatement requete = connection.prepareStatement("SELECT * FROM blocs");
            ResultSet result = requete.executeQuery();
            while(result.next()){
                this.map.addBloc(new Bloc(result.getInt("positionX"), result.getInt("positionY"), result.getBoolean("cassable")));
            }
            requete.close();
        } catch (SQLException ex) {
            Logger.getLogger(Write_Read.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(this.map);
    }
    public Map getMap(){
        return this.map;
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
 
    public void getWaitingPlayers(DefaultTableModel model) {
        try {
            model.setRowCount(0);
            PreparedStatement requete = connection.prepareStatement("SELECT pseudo,nation,pv FROM joueur WHERE status=0");
            ResultSet resultat = requete.executeQuery();

            while (resultat.next()) {
                String pseudo = resultat.getString("pseudo");
                String nation = resultat.getString("nation");
                int pv = resultat.getInt("pv");
                model.addRow(new Object[]{pseudo,nation,pv});
            }       
            requete.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public boolean enoughPlayers(Timer timer, DefaultTableModel model,int numberOfPlayers) {
        boolean firstWaitingPlayer = false;
        boolean modelContainsMainPlayer = false;
        try {            
            PreparedStatement requete = connection.prepareStatement("SELECT pseudo,nation FROM joueur WHERE status = 0 LIMIT ?");
            requete.setInt(1,numberOfPlayers);
            ResultSet resultat = requete.executeQuery();
            model.setRowCount(0);  
            while(resultat.next()) {
                String pseudo = resultat.getString("pseudo");
                String nation = resultat.getString("nation");
                model.addRow(new Object[]{pseudo,nation,0});
                if(this.mainPlayer.getPseudo().equals(pseudo)){
                    modelContainsMainPlayer = true;
                    if(resultat.getRow() ==1){
                        firstWaitingPlayer = true;
                    }             
                }
            }
            requete.close();
        } catch (SQLException ex) {
            Logger.getLogger(Write_Read.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(model.getRowCount() == numberOfPlayers && modelContainsMainPlayer){
            if(firstWaitingPlayer){
                //there is enought players in the waiting room
                //the first player in the waiting room creates the game
                initializePlayers(model,blockXNumber,blockYNumber);//initialize the players positions
                this.map = new Map(0, connection);//create a new map
                map.nouvelleMap();//clear the database and send the actual one to the database
                boolean everyoneReady = false;
                
                while(!everyoneReady){
                    getWaitingPlayers(model);
                    for(int i=0;i<numberOfPlayers;i++){
                        if((int)model.getValueAt(i,2) == 100){
                            everyoneReady=true;
                        }else{
                            if(!((String)model.getValueAt(i,0)).equals(this.mainPlayer.getPseudo())){
                                everyoneReady=false;
                                break;
                            }
                        }
                    }
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Write_Read.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                modifyPlayersStatus();//modify all players' status to true
                this.mainPlayer.modifierPv(100);
            }else{
                //the player is not in the waiting room
                //it means that this player is going to join the game
                retrievePlayers(model);//retrieve information about every single players from the database
                this.map = new Map(0, connection);//create a new map
                map.miseAJourMap();//download the lastest map from the database                
            }
            return true;
        }else{
            return false;
        }
    }
    
    public void modifyPlayersStatus() {
         //change the status of every player
        for(int i=0; i<players.size();i++) {
            players.get(i).modifierStatus();
        }
        this.mainPlayer.modifierStatus();
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

    public void setMainPlayer(Joueur mainPlayer) {
        this.mainPlayer = mainPlayer;
    }

    public void setPlayers(ArrayList<Joueur> players) {
        this.players = players;
    }
    
    public void initializePlayers(DefaultTableModel model, int blockXNumber, int blockYNumber) {
        //initialize the variable players
        //assign each player to a different place
        this.players = new ArrayList();
        int variableX, variableY;
        for(int i=0;i<model.getRowCount();i++) {
            String pseudo = (String) model.getValueAt(i, 0);
            try {
                //write in data
                //Set the initial position this player
                PreparedStatement requeteWrite = connection.prepareStatement("UPDATE joueur SET x = ?, y = ? WHERE pseudo = ?");
                if(i==0 || i==3){
                    variableX = 0;
                } else {
                    variableX = 1;
                }
                if(i==0 || i==2){
                    variableY = 0;
                } else {
                    variableY = 1;
                }
                requeteWrite.setInt(1,(blockXNumber-1)*variableX);
                requeteWrite.setInt(2,(blockYNumber-1)*variableY);
                requeteWrite.setString(3, pseudo);
                requeteWrite.executeUpdate();
                requeteWrite.close();   

                //get the data of this player from the database
                PreparedStatement requete = connection.prepareStatement("SELECT * FROM joueur WHERE pseudo = ?");
                requete.setString(1, pseudo);
                ResultSet resultat = requete.executeQuery();
                resultat.next();//we are sure that this 'resultat' exists
                int positionX = resultat.getInt("x");
                int positionY = resultat.getInt("y");
                int pv = resultat.getInt("pv");
                String nation = resultat.getString("nation");
                String orientation = resultat.getString("orientation");
                
                if(!this.mainPlayer.getPseudo().equals(pseudo)){
                    //Add all other players to the array list players
                    this.players.add(new Joueur(pseudo, positionX, positionY, pv, nation, orientation, this.connection));              
                }
                requete.close();
                
            } catch (SQLException ex) {
                Logger.getLogger(Write_Read.class.getName()).log(Level.SEVERE, null, ex);
            }
        }       
    }

    public void retrievePlayers(DefaultTableModel model) {
        //retrieve players information from the database
        this.players = new ArrayList();
        for(int i=0;i<model.getRowCount();i++) {
            String pseudo = (String) model.getValueAt(i, 0);
            try {                
                //get the data of this player from the database
                PreparedStatement requete = connection.prepareStatement("SELECT * FROM joueur WHERE pseudo = ?");
                requete.setString(1, pseudo);
                ResultSet resultat = requete.executeQuery();
                resultat.next();//we are sure that this 'resultat' exists
                int positionX = resultat.getInt("x");
                int positionY = resultat.getInt("y");
                int pv = resultat.getInt("pv");
                String nation = resultat.getString("nation");
                String orientation = resultat.getString("orientation");
                
                if(!this.mainPlayer.getPseudo().equals(pseudo)){
                    //Add all other players to the array list players
                    this.players.add(new Joueur(pseudo, positionX, positionY, pv, nation, orientation, this.connection));              
                }else{
                    //this player's pv is set to 100
                    //meaning that this player gots all the necessary information to create a party
                    this.mainPlayer.modifierPv(100);
                }
                requete.close();
                
            } catch (SQLException ex) {
                Logger.getLogger(Write_Read.class.getName()).log(Level.SEVERE, null, ex);
            }
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
