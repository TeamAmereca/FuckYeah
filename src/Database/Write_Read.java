package Database;

import Bonus.Bonus;
import Joueur.Joueur;
import Map.Bloc;
import Map.Map;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
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
    private ArrayList<Joueur> players = new ArrayList();;
    private ArrayList<Integer> ballesX;
    private ArrayList<Integer> ballesY;
    private ArrayList<String> ballesO;
    private Map map;
    
    private int blockLength;
    private int blockXNumber;
    private int blockYNumber;
    
    private int delay = 10;
    
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
            int pv = resultat.getInt("pv");
            String orientation = resultat.getString("orientation");
            joueur.setPositionX(positionX);
            joueur.setPositionY(positionY);
            joueur.setPv(pv);
            joueur.setOrientation(orientation);
            requete.close();
            if(joueur.getPv()<=0){
                //If PV is negative, then the player is erased from the database
                this.players.remove(joueur);
                joueur.deleteJoueur();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Write_Read.class.getName()).log(Level.SEVERE, null, ex);
            joueur.deleteJoueur();
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
    public void refreshMap(){
        try {
            this.map.deleteCassable();
            PreparedStatement requete = connection.prepareStatement("SELECT * FROM blocs");
            ResultSet result = requete.executeQuery();
            while(result.next()){
                if(result.getBoolean("cassable")){
                    this.map.addBloc(new Bloc(result.getInt("positionX"), result.getInt("positionY"), result.getBoolean("cassable")));
                }
            }
            requete.close();
            
            requete = connection.prepareStatement("SELECT * FROM bonus");
            result = requete.executeQuery();
            while(result.next()){
                int positionX = result.getInt("positionX");
                int positionY = result.getInt("positionY");
                int type = result.getInt("type");
                Bonus bonus = new Bonus(positionX, positionY, type, connection);
                if(!this.map.getListeBonus().contains(bonus)){
                    this.map.getListeBonus().remove(bonus);
                }
            }
            requete.close();
        } catch (SQLException ex) {
            Logger.getLogger(Write_Read.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public Map getMap(){
        return this.map;
    }
    public void createMainPlayer(String pseudo, String nation) throws SQLException{
        //create an instance of joueur in the database
        players.add(new Joueur(pseudo, nation, connection));
        players.get(0).creerJoueurSQL();
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
        boolean lastWaitingPlayer = false;
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
                if(this.players.get(0).getPseudo().equals(pseudo)){
                    modelContainsMainPlayer = true;
                    if(resultat.getRow() ==numberOfPlayers){
                        lastWaitingPlayer = true;
                    }             
                }
            }
            requete.close();
        } catch (SQLException ex) {
            Logger.getLogger(Write_Read.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(model.getRowCount() == numberOfPlayers && modelContainsMainPlayer){
            if(lastWaitingPlayer){
                //there are enough players in the waiting room
                //the last player in the waiting room creates the game
                initializePlayers(model,blockXNumber,blockYNumber);//initialize every player's positions
                this.map = new Map(1, connection);//create a new map
                map.nouvelleMap();//clear the database and send the actual one to the database
                this.players.get(0).modifierPv(50);//the last player sets his pv to 100, meaning that the map and player's positions have been set
                boolean everyoneReady = false;              
                while(!everyoneReady){
                    //if everyone is ready, meaning that all other player has set his pv to 100
                    //else the main player waits so that they can fetch all data of all players
                    getWaitingPlayers(model);
                    for(int i=0;i<numberOfPlayers;i++){
                        if((int)model.getValueAt(i,2) == 100){
                            everyoneReady=true;
                        }else{
                            if(!((String)model.getValueAt(i,0)).equals(this.players.get(0).getPseudo())){
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
                setTimeDatabase();//set the beginning time
                this.players.get(0).modifierPv(100);
                modifyPlayersStatus();//modify all players' status to true                
            }else{
                //the player is not in the waiting room
                //it means that this player is going to join the game
                getLastPlayerPV(model, numberOfPlayers, 50);//true if map is ready
                retrievePlayers(model);//retrieve information about every single players from the database
                this.map = new Map(1, connection);//create a new map
                map.miseAJourMap();//download the lastest map from the database
                getLastPlayerPV(model, numberOfPlayers, 100);//true if the game is going to start
                getTimeDatabase();
            }
            return true;
        }else{
            return false;
        }
    }
    
    public void getLastPlayerPV(DefaultTableModel model, int numberOfPlayers, int pv){
        boolean equal = false; 
        try {
            while(!equal){
                //we wait until the map is created, that is when the last entered player's pv is 100
                getWaitingPlayers(model);
                if(model.getRowCount()==0){
                    break;
                }else{
                    equal = (int)model.getValueAt(numberOfPlayers-1,2) == pv;
                    TimeUnit.SECONDS.sleep(1);    
                }
                    
            }                
        } catch (InterruptedException ex) {
            Logger.getLogger(Write_Read.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void modifyPlayersStatus() {
         //change the status of every player
        for(int i=0; i<players.size();i++) {
            players.get(i).modifierStatus();
        }
    }

    public ArrayList<Joueur> getPlayers() {
        return players;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setPlayers(ArrayList<Joueur> players) {
        this.players = players;
    }
    
    public void initializePlayers(DefaultTableModel model, int blockXNumber, int blockYNumber) {
        //initialize the variable players
        //assign each player to a different place
        int variableX, variableY;
        String orientation;
        for(int i=0;i<model.getRowCount();i++) {
            String pseudo = (String) model.getValueAt(i, 0);
            try {
                //write in data
                //Set the initial position this player
                PreparedStatement requeteWrite = connection.prepareStatement("UPDATE joueur SET x = ?, y = ?,orientation=? WHERE pseudo = ?");
                switch (i) {
                    case 0:
                        variableX = 1;
                        variableY = 1;
                        orientation="Droite";
                        break;
                    case 1:
                        variableX = blockXNumber-2;
                        variableY = blockYNumber-2;
                        orientation="Gauche";                        
                        break;
                    case 2:
                        variableX = blockXNumber-2;
                        variableY = 1;
                        orientation="Bas";
                        break;
                    default:
                        variableX = 1;
                        variableY = blockYNumber-2;
                        orientation="Haut";
                        break;
                }
                requeteWrite.setInt(1,variableX);
                requeteWrite.setInt(2,variableY);
                requeteWrite.setString(3, orientation);
                requeteWrite.setString(4, pseudo);
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
                orientation = resultat.getString("orientation");
                
                if(!this.players.get(0).getPseudo().equals(pseudo)){
                    //Add all other players to the array list players
                    this.players.add(new Joueur(pseudo, positionX, positionY, pv, nation, orientation, this.connection, true));
                }
                requete.close();
                
            } catch (SQLException ex) {
                Logger.getLogger(Write_Read.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void retrievePlayers(DefaultTableModel model) {
        //retrieve players information from the database
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
                
                if(!this.players.get(0).getPseudo().equals(pseudo)){
                    //Add all other players to the array list players
                    this.players.add(new Joueur(pseudo, positionX, positionY, pv, nation, orientation, this.connection, true));              
                }else{
                    //this player's pv is set to 100
                    //meaning that this player gots all the necessary information to create a party
                    this.players.get(0).modifierPv(100);
                }
                requete.close();
                
            } catch (SQLException ex) {
                Logger.getLogger(Write_Read.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void setTimeDatabase() {
        //set game beginning time
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(timestamp.getTime());
            cal.add(Calendar.SECOND, delay);
            Timestamp later = new Timestamp(cal.getTime().getTime());
            PreparedStatement requete = connection.prepareStatement("UPDATE time SET date = ? WHERE number = 0");
            requete.setTimestamp(1, later);
            requete.executeUpdate();
            requete.close();
            Timestamp now = new Timestamp(System.currentTimeMillis());
            waitMilliSeconds(later.getTime()-now.getTime());
        } catch (SQLException ex) {
            Logger.getLogger(Write_Read.class.getName()).log(Level.SEVERE, null, ex);
        }      
    }
    
    public void getTimeDatabase() {
        //get game beginning time
        try {
            PreparedStatement requete = connection.prepareStatement("SELECT date FROM time WHERE number = 0");
            ResultSet resultat = requete.executeQuery();
            resultat.next();//we are sure that this 'resultat' exists
            Timestamp later = resultat.getTimestamp("date");
            Timestamp now = new Timestamp(System.currentTimeMillis());
            requete.close();
            waitMilliSeconds(later.getTime()-now.getTime());//have to convert GMT to UTC+2 local time
        } catch (SQLException ex) {
            Logger.getLogger(Write_Read.class.getName()).log(Level.SEVERE, null, ex);
        }      
    }

    public void waitMilliSeconds(long x){
        try {
            if(x<0){
                x=0;
            }else{
                x = Math.min(Math.abs(x),delay*1000);
            }
            TimeUnit.MILLISECONDS.sleep(x);
        } catch (InterruptedException ex) {
            Logger.getLogger(Write_Read.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    
    
    public static void main(String[] args){
    int delay = 5;
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://nemrod.ens2m.fr:3306/20172018_s2_vs2_fuckyeah?serverTimezone=UTC", "fuckyeah", "america");
            
            
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(timestamp.getTime());
            cal.add(Calendar.SECOND, delay);
            Timestamp later = new Timestamp(cal.getTime().getTime());
            PreparedStatement requete = connection.prepareStatement("UPDATE time SET date = ? WHERE number = 0");
            requete.setTimestamp(1, later);
            requete.executeUpdate();
            requete.close();
            Timestamp now = new Timestamp(System.currentTimeMillis());
            //waitMilliSeconds(later.getTime()-now.getTime());
            
            System.exit(0);
        connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(Write_Read.class.getName()).log(Level.SEVERE, null, ex);
        }      
}
}

 