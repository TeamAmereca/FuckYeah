package teamamerica;

import Display.*;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import javax.swing.*;

public class WelcomeWindow extends JFrame implements KeyListener {
    
    public static final String GAME_NAME = "Team America";
    private DisplayBody centralPanel;
                
    @Override
    public void keyPressed(KeyEvent e) {
        /** Handle the key-pressed event */
        System.out.println(centralPanel.getKeyListener());
        // Click on esc key to close the game
        if(e.getKeyCode() == 27) {
            dispose();
            System.exit(0);
        }else if(centralPanel.getKeyListener()){
            if(e.getKeyCode()== KeyEvent.VK_RIGHT)
            centralPanel.getDatabase().getMainPlayer().deplacer(e);
        else if(e.getKeyCode()== KeyEvent.VK_LEFT)
            centralPanel.getDatabase().getMainPlayer().deplacer(e);
        else if(e.getKeyCode()== KeyEvent.VK_UP)
            centralPanel.getDatabase().getMainPlayer().deplacer(e);
        else if(e.getKeyCode()== KeyEvent.VK_DOWN)
            centralPanel.getDatabase().getMainPlayer().deplacer(e);
        else if(e.getKeyCode()== KeyEvent.VK_SPACE)
            centralPanel.getDatabase().getMainPlayer().tir();            
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        /** Handle the key-released event */
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
         /** Handle the key typed event */
    }
    
    WelcomeWindow() {
        setTitle(GAME_NAME);
        
        //////////////////////////////////
        //  get the size of the screen  //
        //////////////////////////////////
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        
        ///////////////////////////////
        //  adding header on frame  //
        //////////////////////////////
        DisplayHeader header = new DisplayHeader(screenWidth,screenHeight);
        header.addHeader(GAME_NAME);
        add(header);
        //Initialize centralPanel
        centralPanel = new DisplayBody(screenWidth, screenHeight);
        add(centralPanel);
        //
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        setUndecorated(true);
        //setBackground(new Color(220,220,220,250));
        setLayout(null);
        setVisible(true);
    }

    @Override
    public void dispose() {
        super.dispose(); //To change body of generated methods, choose Tools | Templates.
        centralPanel.close();
    }
    
    public static void main(String[] args) throws SQLException {
        new WelcomeWindow();
    }
}