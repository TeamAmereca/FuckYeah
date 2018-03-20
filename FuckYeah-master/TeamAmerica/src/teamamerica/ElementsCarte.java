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
public class ElementsCarte {
    protected int x;
    protected int y;
    
    public ElementsCarte(int x, int y){
        this.x = x;
        this.y = y;
    }
    
     public int getX(){
        return this.x;
    }
    
    public int getY(){
        return this.y;
    }
}
