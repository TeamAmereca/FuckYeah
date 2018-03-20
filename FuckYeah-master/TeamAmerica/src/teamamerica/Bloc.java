/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teamamerica;

/**
 *
 * @author gmarino
 */
public class Bloc extends ElementsCarte{
    private boolean cassable;
    
    public Bloc(int x,int y, boolean cassable){
        super(x,y);
        this.cassable = cassable;
    }
    
    public boolean getCassable(){
        return this.cassable;
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
