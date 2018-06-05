/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Map;

/**
 *
 * @author gmarino
 */

// Les blocs sont des éléments présents sur la carte cassable ou non cassable
public class Bloc {
    private boolean cassable;
    private int x;
    private int y;
    
    public Bloc(int x,int y, boolean cassable){
        this.x = x;
        this.y = y;
        this.cassable = cassable;
    }
    
    public boolean getCassable(){
        return this.cassable;
    }
    
    public int getX() {
    	return this.x;
    }
    
    public int getY() {
    	return this.y;
    }
    
    public String toString(){
        if (cassable == true){
            return ("x");
        }
        else{
            return ("o");
        }
    }
}
