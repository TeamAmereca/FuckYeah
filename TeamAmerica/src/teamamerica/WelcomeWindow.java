package teamamerica;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

public class WelcomeWindow extends JFrame implements KeyListener, ActionListener {
    
    public static final String GAME_NAME = "Team America";
    public static final int GIF_HEIGHT = 300;
    public static final int GIF_WIDTH = 300;
    public static final int GO_BUTTON_HEIGHT = 100;
    public static final int GO_BUTTON_WIDTH = 400;
    private TextField text_pseudo;
    private JComboBox box_nations;
    private JLabel label = new JLabel();
    private JButton goButton = new JButton();
    //////////////////////////////////
    //  get the size of the screen  //
    //////////////////////////////////
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private final double screenWidth = screenSize.getWidth();
    private final double screenHeight = screenSize.getHeight();
        
    @Override
    public void keyPressed(KeyEvent e) {
        
        // Click on esc key to close the game
        if(e.getKeyCode() == 27) {
            dispose();
            //System.exit(0);
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        if(button.getText() == "Play!") {
            signUp();
        } else if(button.getText() == "Connect To The Server!") {
            String pseudo = text_pseudo.getText();
            String nation = (String) box_nations.getSelectedItem();
        }        
    }
    ActionListener comboBoxActionListener = new ActionListener() {
        // ActionListener for combo box. Display gif according to the selected item in the combo box
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedNation = (String) box_nations.getSelectedItem();//get the selected item
            ImageIcon icon = new ImageIcon(getClass().getResource("/gif/"+selectedNation+".gif"));
            label.setIcon(icon);                
        }
    };
    public void signUp() {
        //Create input fields in order to sign in as a player
        int textFieldHeight = 20,
            textFieldWidth = 200;
        JLabel label_pseudo = new JLabel("Pseudo");
        JLabel label_nation = new JLabel("Nation");        
        text_pseudo = new TextField(20);
        String[] nations = { "America", "Terrorist" };
        box_nations = new JComboBox(nations);
        box_nations.addActionListener(comboBoxActionListener);
        box_nations.setSelectedIndex(0);
        add(label_pseudo);
        add(text_pseudo);
        add(label_nation);
        add(box_nations);
        label_pseudo.setBounds((int)(screenWidth/2-textFieldWidth/2),(int)screenHeight*5/24,textFieldWidth,textFieldHeight);
        label_nation.setBounds((int)(screenWidth/2-textFieldWidth/2),(int)screenHeight*8/24,textFieldWidth,textFieldHeight);
        text_pseudo.setBounds((int)(screenWidth/2-textFieldWidth/2),(int)screenHeight*3/12,textFieldWidth,textFieldHeight);
        box_nations.setBounds((int)(screenWidth/2-textFieldWidth/2),(int)screenHeight*9/24,textFieldWidth,textFieldHeight);
        goButton.setText("Connect To The Server!");
    }
    WelcomeWindow() {
        
        setTitle(GAME_NAME);

        //////////////////////////////////////
        //  adding connect button on frame  //
        //////////////////////////////////////
        goButton.setText("Play!");
        goButton.setBounds((int)(screenWidth/2-GO_BUTTON_WIDTH/2),(int)screenHeight*10/12,GO_BUTTON_WIDTH,GO_BUTTON_HEIGHT);
        goButton.setFont(new Font("Arial", Font.PLAIN, 30));
        goButton.setFocusable(false);
        add(goButton);
        goButton.addActionListener(this);
        
        JLabel gameNameLabel = new JLabel(GAME_NAME);
        gameNameLabel.setBounds((int)(screenWidth/2)-150,0, 1000,100);
        gameNameLabel.setForeground(Color.RED);
        gameNameLabel.setFont(new Font("Serif", Font.PLAIN, 50));
        add(gameNameLabel);       

        // add label for gif        
        label.setBounds((int)(screenWidth/2-GIF_WIDTH/2),(int)screenHeight*10/24,GIF_WIDTH,GIF_HEIGHT);
        add(label);
        
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
    
    public static void main(String[] args) {
        new WelcomeWindow();
    }
}