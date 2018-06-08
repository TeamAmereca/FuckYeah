package teamamerica;

import Display.*;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import javax.swing.*;
import sound.SoundEffect;

public class WelcomeWindow extends JFrame{
    
    private final String GAME_NAME = "Team America";
    private DisplayBody centralPanel;
    private DisplayHeader displayHeader;  
    private FrameKeyListener frameKeyListener = new FrameKeyListener();
    private SoundEffect soundEffect = new SoundEffect();

    WelcomeWindow() {
        setTitle(GAME_NAME);
        
        //////////////////////////////////
        //  get the size of the screen  //
        //////////////////////////////////
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        
        ///////////////////////////////
        //  ddding DisplayHeader on frame  //
        //////////////////////////////
        displayHeader = new DisplayHeader(screenWidth,screenHeight);
        displayHeader.addHeader(GAME_NAME);
        add(displayHeader);
        //Initialize centralPanel
        centralPanel = new DisplayBody(this,screenWidth, screenHeight);
        add(centralPanel);
        //
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        addKeyListener0();//add keylistener
        
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        setUndecorated(true);
        //setBackground(new Color(220,220,220,250));
        setLayout(null);
        setVisible(true);
    }
    
    public void addKeyListener0(){
        this.addKeyListener(frameKeyListener);
    }
    
    public void removeKeyListener0(){
        this.removeKeyListener(frameKeyListener);
    }

    @Override
    public void dispose() {
        super.dispose(); //To change body of generated methods, choose Tools | Templates.
        centralPanel.close();
    }
    
    class FrameKeyListener implements KeyListener{
        @Override
        public void keyPressed(KeyEvent e) {
            /** Handle the key-pressed event */            
            switch (e.getKeyCode()) {
                case 27:
                    // Click on esc key to close the game
                    dispose();
                    System.exit(0);
                    break;
                case KeyEvent.VK_RIGHT:
                    centralPanel.getDatabase().getPlayers().get(0).deplacer(e);
                    break;
                case KeyEvent.VK_LEFT:
                    centralPanel.getDatabase().getPlayers().get(0).deplacer(e);
                    break;
                case KeyEvent.VK_UP:
                    centralPanel.getDatabase().getPlayers().get(0).deplacer(e);
                    break;
                case KeyEvent.VK_DOWN:
                    centralPanel.getDatabase().getPlayers().get(0).deplacer(e);
                    break;
                case KeyEvent.VK_SPACE:
                    centralPanel.getDatabase().getPlayers().get(0).tir();
                    soundEffect.play("rifle "+centralPanel.getDatabase().getPlayers().get(0).getNation());
                    break;
                default:
                    break;
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
    }
    
    public static void main(String[] args) throws SQLException {
        new WelcomeWindow();
    }

    public SoundEffect getSoundEffect() {
        return soundEffect;
    }

    public DisplayBody getCentralPanel() {
        return centralPanel;
    }

    public DisplayHeader getDisplayHeader() {
        return displayHeader;
    }
}