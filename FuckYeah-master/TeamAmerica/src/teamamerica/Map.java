/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teamamerica;

import java.util.*;

/**
 *
 * @author gmarino
 */
public class Map {
    private int nbCase_X;
    private int nbCase_Y;
    private ArrayList<ElementsCarte> listeObjet;
    
    public Map(int X, int Y){
        this.nbCase_X = X;
        this.nbCase_Y = Y;
        this.listeObjet = new ArrayList<ElementsCarte>();
        for (int x=0; x<X; x++){
            for (int y=0; y<Y; y++){
                Random randomno = new Random();
                boolean value = randomno.nextBoolean();
                this.listeObjet.add(new Bloc(x, y, false));
            }
        }
    }
    public ArrayList<ElementsCarte> getListeObjet(){
        return this.listeObjet;
    }
    public String toString(){
        String [][] tabInt = new String[this.nbCase_X][this.nbCase_Y];
        for (int i=0; i<this.listeObjet.size(); i++){
            int x = this.listeObjet.get(i).getX();
            int y = this.listeObjet.get(i).getY();
            if (this.listeObjet.get(i) instanceof teamamerica.Bloc){
                tabInt[x][y]="b";
            }
            else{
                tabInt[x][y]="o";
            }
            
        }
        String s = "";
        for (int x=0; x<this.nbCase_X; x++){
            for (int y=0; y<this.nbCase_Y; y++){
                s+=tabInt[x][y]+" ";
            }
            s+="\n";
        }
        return s;
    }
}
