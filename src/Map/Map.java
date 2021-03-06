package Map;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author gmarino
 */
public class Map {
    private int nbCase_X = 20;
    private int nbCase_Y = 13;
    private ArrayList<Bloc> listeBloc;
    private ArrayList<Bonus> listeBonus;
    private Connection connexion;
    
//Création de cartes
    
    
    public Map(int forme, Connection connexion){
        this.connexion = connexion;
    	this.listeBloc = new ArrayList<>();
        this.listeBonus = new ArrayList<>();
        if (forme==0 || forme ==1){//Contour incassable pour toutes les cartes
            for(int x=0; x<this.nbCase_X; x++) {
                this.listeBloc.add(new Bloc(x, 0, false));
                this.listeBloc.add(new Bloc(x, this.nbCase_Y - 1, false));
            }
            for(int y=1; y<this.nbCase_Y-1; y++) {
                this.listeBloc.add(new Bloc(0, y, false));
                this.listeBloc.add(new Bloc(this.nbCase_X-1, y, false));
            }
        }
        if (forme==1){//Carte du product owner
            for(int i = 2; i<17; i=i+2){
                for(int j = 1; j<12; j=j+2){
                this.listeBloc.add(new Bloc(i, j, false));
                }
            }
            for(int i = 1; i<12; i++){
                this.listeBloc.add(new Bloc(18, i, false));
            }
            // création bloc cassables et incassables
            this.listeBloc.add(new Bloc(10, 2, false));
            this.listeBloc.add(new Bloc(14, 2, false));
            this.listeBloc.add(new Bloc(5, 3, false));
            this.listeBloc.add(new Bloc(17, 3, false));
            this.listeBloc.add(new Bloc(4, 6, false));
            this.listeBloc.add(new Bloc(5, 7, false));
            this.listeBloc.add(new Bloc(9, 7, false));
            this.listeBloc.add(new Bloc(13, 7, false));
            this.listeBloc.add(new Bloc(2, 8, false));
            this.listeBloc.add(new Bloc(4, 10, false));
            this.listeBloc.add(new Bloc(8, 10, false));
            this.listeBloc.add(new Bloc(2, 2, true));
            this.listeBloc.add(new Bloc(6, 2, true));
            this.listeBloc.add(new Bloc(8, 2, true));
            this.listeBloc.add(new Bloc(13, 2, true));
            this.listeBloc.add(new Bloc(16, 2, true));
            this.listeBloc.add(new Bloc(9, 3, true));
            this.listeBloc.add(new Bloc(3, 4, true));
            this.listeBloc.add(new Bloc(8, 4, true));
            this.listeBloc.add(new Bloc(10, 4, true));
            this.listeBloc.add(new Bloc(12, 4, true));
            this.listeBloc.add(new Bloc(15, 4, true));
            this.listeBloc.add(new Bloc(8, 6, true));
            this.listeBloc.add(new Bloc(10, 6, true));
            this.listeBloc.add(new Bloc(12, 6, true));
            this.listeBloc.add(new Bloc(1, 7, true));
            this.listeBloc.add(new Bloc(11, 7, true));
            this.listeBloc.add(new Bloc(6, 8, true));
            this.listeBloc.add(new Bloc(15, 8, true));
            this.listeBloc.add(new Bloc(3, 9, true));
            this.listeBloc.add(new Bloc(7, 9, true));
            this.listeBloc.add(new Bloc(9, 9, true));
            this.listeBloc.add(new Bloc(10, 10, true));
            this.listeBloc.add(new Bloc(12, 10, true));
            this.listeBloc.add(new Bloc(15, 10, true));
            
            //création bonus
            //this.listeBonus.add(new Bonus(1,1,1,connexion));
            new Bonus(9,6,2,connexion).creerBonusSQL();
            new Bonus(9,2,1,connexion).creerBonusSQL();
            new Bonus(9,11,1,connexion).creerBonusSQL();

            
    	}
    }
    
// écriture base de donnée
    
    public void envoyerMap() {
    	try {
            for(int i=0; i<this.listeBloc.size(); i++) {
                PreparedStatement requete = connexion.prepareStatement("INSERT INTO blocs VALUES (?,?,?)");
                requete.setInt(1,  this.listeBloc.get(i).getX());
                requete.setInt(2,  this.listeBloc.get(i).getY());
                requete.setBoolean(3,  this.listeBloc.get(i).getCassable());
                requete.executeUpdate();
                requete.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }   
    }
    
    public void supprimerMap() { //supprime la carte de la base de donnée
        try {
            PreparedStatement suppression = connexion.prepareStatement("DELETE FROM blocs");
            suppression.executeUpdate();
            suppression.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void nouvelleMap(){
        supprimerMap();
        envoyerMap();
    }
    
// Lecture base de donnée
    
    public void miseAJourMap(){
        try {
            this.listeBloc.clear();
            
            PreparedStatement requeteNomColonne = connexion.prepareStatement("SELECT * FROM blocs");
            ResultSet result = requeteNomColonne.executeQuery();
            while (result.next()) {
                this.listeBloc.add(new Bloc(result.getInt("positionX"), result.getInt("positionY"), result.getBoolean("cassable")));
            }
            requeteNomColonne.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public ArrayList<Bloc> getlisteBloc(){
        return this.listeBloc;
    }
    
    @Override
    public String toString(){
        String [][] carte = new String[this.nbCase_Y][this.nbCase_X]; // on créer la map vide
        for (int x=0; x<this.nbCase_X; x++) {
        	for (int y=0; y<this.nbCase_Y; y++) {
        		carte[y][x] = "-";
        	}
        }
        for (int i=0; i<this.listeBloc.size(); i++){ // on parcourt la liste
            int x = this.listeBloc.get(i).getX(); // on récupère les coordonnées de chaque bloc
            int y = this.listeBloc.get(i).getY();
            if (this.listeBloc.get(i).getCassable()){
                carte[y][x]="o";//si bloc cassable
            }
            else{
                carte[y][x]="x";//si bloc non cassable
            }
        }
        String s = "";//on stock le tout dans une chaine de caractère
        for (int x=0; x<this.nbCase_X; x++){
            for (int y=0; y<this.nbCase_Y; y++){
                s+=carte[y][y]+" ";
            }
            s+="\n";
        }
        return s;
    }
 
    
    public void deleteCassable(){
        for(int i=0; i<this.listeBloc.size(); i++){
            if(this.listeBloc.get(i).getCassable()){
                this.listeBloc.remove(i);
                i--;
            }
        }
    }
    public void addBloc(Bloc b) {
        this.listeBloc.add(b);
    }
    
    public int nombreBlocs(){
        return this.listeBloc.size();
    }
    
    public Bloc getBloc(int i){
        return this.listeBloc.get(i);
    }
    public int getX(){
        return this.nbCase_X;
    }
    public int getY(){
        return this.nbCase_Y;
    }

    public ArrayList<Bonus> getListeBonus() {
        return listeBonus;
    }
}

